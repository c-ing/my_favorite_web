package com.favorite.comm.filter;

import com.favorite.comm.Const;
import com.favorite.domain.User;
import com.favorite.repository.UserRepository;
import com.favorite.utils.Des3EncryptionUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by cdc on 2018/8/21.
 */
public class SecurityFilter implements Filter {

    protected Logger logger = LoggerFactory.getLogger(SecurityFilter.class);
    private static Set<String> GreenUrlSet = new HashSet<>();

    @Autowired
    private UserRepository userRepository;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        GreenUrlSet.add("/login");
        GreenUrlSet.add("/register");
        GreenUrlSet.add("/index");
        GreenUrlSet.add("/forgotPassword");
        GreenUrlSet.add("/newPassword");
        GreenUrlSet.add("/tool");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        String uri = httpServletRequest.getRequestURI();
        if (httpServletRequest.getSession().getAttribute(Const.LOGIN_SESSION_KEY) == null) {
            Cookie[] cookies = httpServletRequest.getCookies();
            if (containsSuffix(uri) || GreenUrlSet.contains(uri) || containsKey(uri)) {
                logger.debug("don't check url" + uri);
                filterChain.doFilter(request, response);
                return;
            } else if (cookies!=null) {
                boolean flag = true;
                for (int i = 0; i < cookies.length; i++) {
                    Cookie cookie = cookies[i];
                    if (cookie.getName().equals(Const.LOGIN_SESSION_KEY)) {
                        if(StringUtils.isNotBlank(cookie.getValue())){
                            flag = false;
                        }else{
                            break;
                        }
                        String value = getUserId(cookie.getValue());
                        Long userId = 0l;
                        if (userRepository == null) {
                            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                            userRepository = (UserRepository) factory.getBean("userRepository");
                        }
                        if(StringUtils.isNotBlank(value)){
                            userId = Long.parseLong(value);
                        }
                        User user = userRepository.findById((long)userId);
                        String html = "";
                        if(null == user){
                            html = "<script type=\"text/javascript\">window.location.href=\"_BP_login\"</script>";
                        }else{
                            logger.info("userId :" + user.getId());
                            httpServletRequest.getSession().setAttribute(Const.LOGIN_SESSION_KEY, user);
                            String referer = this.getRef(httpServletRequest);
                            if(referer.indexOf("/collect?") >= 0 || referer.indexOf("/lookAround/standard/") >= 0
                                    || referer.indexOf("/lookAround/simple/") >= 0){
                                filterChain.doFilter(request, response);
                                return;
                            }else{
                                html = "<script type=\"text/javascript\">window.location.href=\"_BP_\"</script>";
                            }
                        }
                        html = html.replace("_BP_", Const.BASE_PATH);
                        response.getWriter().write(html);
                        /**
                         * HttpServletResponse response = (HttpServletResponse) sresponse;
                         response.sendRedirect("/");
                         */
                    }
                }
                if(flag){
                    //跳转到登陆页面
                    String referer = this.getRef(httpServletRequest);
                    logger.debug("security filter, deney, " + httpServletRequest.getRequestURI());
                    String html = "";
                    if(referer.contains("/collect?") || referer.contains("/lookAround/standard/")
                            || referer.contains("/lookAround/simple/")){
                        html = "<script type=\"text/javascript\">window.location.href=\"_BP_login\"</script>";
                    }else{
                        html = "<script type=\"text/javascript\">window.location.href=\"_BP_index\"</script>";
                    }
                    html = html.replace("_BP_", Const.BASE_PATH);
                    response.getWriter().write(html);
                }
            }else{
                //跳转到登陆页面
                String referer = this.getRef(httpServletRequest);
                logger.debug("security filter, deney, " + httpServletRequest.getRequestURI());
                String html = "";
                if(referer.contains("/collect?") || referer.contains("/lookAround/standard/")
                        || referer.contains("/lookAround/simple/")){
                    html = "<script type=\"text/javascript\">window.location.href=\"_BP_login\"</script>";
                }else{
                    html = "<script type=\"text/javascript\">window.location.href=\"_BP_index\"</script>";
                }
                html = html.replace("_BP_", Const.BASE_PATH);
                response.getWriter().write(html);
                //	HttpServletResponse response = (HttpServletResponse) sresponse;
                //response.sendRedirect("/");

            }
        }else{
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }

    public  String codeToString(String str) {
        String strString = str;
        try {
            byte tempB[] = strString.getBytes("ISO-8859-1");
            strString = new String(tempB);
            return strString;
        } catch (Exception e) {
            return strString;
        }
    }

    public String getRef(HttpServletRequest request){
        String referer = "";
        String param = this.codeToString(request.getQueryString());
        if(StringUtils.isNotBlank(request.getContextPath())){
            referer = referer + request.getContextPath();
        }
        if(StringUtils.isNotBlank(request.getServletPath())){
            referer = referer + request.getServletPath();
        }
        if(StringUtils.isNotBlank(param)){
            referer = referer + "?" + param;
        }
        request.getSession().setAttribute(Const.LAST_REFERER, referer);
        return referer;
    }

    private boolean containsSuffix(String url) {
            if (url.endsWith(".js")
                    || url.endsWith(".css")
                    || url.endsWith(".jpg")
                    || url.endsWith(".gif")
                    || url.endsWith(".png")
                    || url.endsWith(".html")
                    || url.endsWith(".eot")
                    || url.endsWith(".svg")
                    || url.endsWith(".ttf")
                    || url.endsWith(".woff")
                    || url.endsWith(".ico")
                    || url.endsWith(".woff2")) {
                return true;
            } else {
                return false;
            }
    }

    private boolean containsKey(String url) {

        if (url.contains("/media/")
                || url.contains("/login")||url.contains("/user/login")
                || url.contains("/register")||url.contains("/user/regist")||url.contains("/index")
                || url.contains("/forgotPassword")||url.contains("/user/sendForgotPasswordEmail")
                || url.contains("/newPassword")||url.contains("/user/setNewPassword")
                || (url.contains("/collector") && !url.contains("/collect/detail/"))
                || url.contains("/collect/standard/")||url.contains("/collect/simple/")
                || url.contains("/user")||url.contains("/favorites")||url.contains("/comment")
                || url.startsWith("/lookAround/standard/")
                || url.startsWith("/lookAround/simple/")
                || url.startsWith("/user/")
                || url.startsWith("/feedback")
                || url.startsWith("/standard/")
                || url.startsWith("/collect/standard/lookAround/")
                || url.startsWith("/collect/simple/lookAround/")) {
            return true;
        } else {
            return false;
        }
    }

    public String getUserId(String value) {
        try {
            String userId = Des3EncryptionUtil.decode(Const.DES3_KEY, value);
            userId = userId.substring(0, userId.indexOf(Const.PASSWORD_KEY));
            return userId;
        } catch (Exception e) {
            logger.error("解析cookie异常：",e);
        }
        return null;
    }

}
