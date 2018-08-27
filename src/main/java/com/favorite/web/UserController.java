package com.favorite.web;

import com.favorite.comm.Const;
import com.favorite.comm.aop.LoggerManager;
import com.favorite.domain.Favorites;
import com.favorite.domain.User;
import com.favorite.domain.result.ExceptionMsg;
import com.favorite.domain.result.Response;
import com.favorite.domain.result.ResponseData;
import com.favorite.repository.UserRepository;
import com.favorite.service.ConfigService;
import com.favorite.service.FavoritesService;
import com.favorite.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by cdc on 2018/6/24.
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FavoritesService favoritesService;

    @Autowired
    private ConfigService configService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @LoggerManager(description = "登录")
    public ResponseData login(User user, HttpServletResponse response) {
        try {
            User loginUser = userRepository.findByUserNameOrEmail(user.getUserName(), user.getEmail());
            if (loginUser == null) {
                return new ResponseData(ExceptionMsg.LoginNameNotExists);
            } else if (!loginUser.getPassWord().equals(getPwd(user.getPassWord()))) {
                return new ResponseData(ExceptionMsg.LoginNameOrPassWordError);
            }
            Cookie cookie = new Cookie(Const.LOGIN_SESSION_KEY, cookieSign(loginUser.getId().toString()));
            cookie.setMaxAge(Const.COOKIE_TIMEOUT);
            cookie.setPath("/");
            response.addCookie(cookie);
            getSession().setAttribute(Const.LOGIN_SESSION_KEY, loginUser);
            String preUrl = "/";
            if (null != getSession().getAttribute(Const.LAST_REFERER)) {
                preUrl = String.valueOf(getSession().getAttribute(Const.LAST_REFERER));
                if (preUrl.indexOf("/collect?") < 0 && preUrl.indexOf("/lookAroud/standard/") < 0
                        && preUrl.indexOf("/lookAroud/simple/") < 0) {
                    preUrl = "/";
                }
            }
            if (preUrl.indexOf("/lookAround/standard/") >= 0) {
                preUrl = "/lookAround/standard/ALL";
            }
            if (preUrl.indexOf("/lookAround/simple/") >= 0) {
                preUrl = "/lookAround/simple/ALL";
            }
            return new ResponseData(ExceptionMsg.SUCCESS, preUrl);
        } catch (Exception e) {
            logger.error("login failed", e);
            return new ResponseData(ExceptionMsg.FAILED);
        }

    }

    @RequestMapping(value = "/regist", method = RequestMethod.POST)
    @Transactional
    @LoggerManager(description = "注册")
    public Response regist(User user) {
        try {
            User registUser = userRepository.findByUserNameOrEmail(user.getUserName(),user.getEmail());
            if (null != registUser && registUser.getEmail().equals(user.getEmail())) {
                return result(ExceptionMsg.EmailUsed);
            }
            if (null != registUser && registUser.getUserName().equals(user.getUserName())) {
                return result(ExceptionMsg.UserNameUsed);
            }
            user.setPassWord(getPwd(user.getPassWord()));
            user.setCreateTime(DateUtils.getCurrentTime());
            user.setLastModifyTime(DateUtils.getCurrentTime());
            user.setProfilePicture("img/favicon.png");
            userRepository.save(user);
            //添加默认收藏夹
            Favorites favorites = favoritesService.saveFavorites(user.getId(), "未读列表");
            //添加默认属性设置
            configService.saveConfig(user.getId(), String.valueOf(favorites.getId()));
            getSession().setAttribute(Const.LOGIN_SESSION_KEY,user);
        } catch (Exception e ) {
            logger.error("create user failed, ", e);
            return result(ExceptionMsg.FAILED);
        }
        return result();
    }

}
