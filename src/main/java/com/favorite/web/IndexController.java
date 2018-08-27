package com.favorite.web;

import com.favorite.comm.Const;
import com.favorite.comm.aop.LoggerManager;
import com.favorite.domain.Config;
import com.favorite.domain.Favorites;
import com.favorite.domain.Follow;
import com.favorite.domain.User;
import com.favorite.domain.enums.IsDelete;
import com.favorite.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cdc on 2018/6/21.
 */
@Controller
@RequestMapping("/")
public class IndexController extends BaseController {

    @Autowired
    private FavoritesRepository favoritesRepository;

    @Autowired
    private CollectRepository collectRepository;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private FollowRepository followRepository;

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    @LoggerManager(description = "首页")
    public String index(Model model) {
        model.addAttribute("collector", "");
        User user = super.getUser();
        if (null != user) {
            model.addAttribute("user", user);
        }
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @LoggerManager(description = "登陆页面")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    @LoggerManager(description = "注册页面")
    public String register() {
        return "register";
    }

    @RequestMapping(value="/",method=RequestMethod.GET)
    @LoggerManager(description="登陆后首页")
    public String home(Model model) {
        System.out.println("home");
        if (null == getUser()) {
            return "redirect:/login";
        }
        // TODO: 2018/8/16  方法权限增加，已登录用户
        long size= collectRepository.countByUserIdAndIsDelete(getUserId(), IsDelete.NO);
        Config config = configRepository.findByUserId(getUserId());
        Favorites favorites = favoritesRepository.findById(Long.parseLong(config.getDefaultFavorties()));
        List<String> followList = followRepository.findByUserId(getUserId());
        model.addAttribute("config",config);
        model.addAttribute("favorites",favorites);
        model.addAttribute("size",size);
        model.addAttribute("followList",followList);
        model.addAttribute("user",getUser());
        model.addAttribute("newAtMeCount",noticeRepository.countByUserIdAndTypeAndReaded(getUserId(), "at", "unread"));
        model.addAttribute("newCommentMeCount",noticeRepository.countByUserIdAndTypeAndReaded(getUserId(), "comment", "unread"));
        model.addAttribute("newPraiseMeCount",noticeRepository.countPraiseByUserIdAndReaded(getUserId(), "unread"));
        logger.info("collect size="+size+" userID="+getUserId());
        return "home";//git提交测试
    }

    @RequestMapping(value="/import")
    @LoggerManager(description="收藏夹导入页面")
    public String importm() {
        return "favorites/import";
    }

    @RequestMapping(value = "newFavorites")
    @LoggerManager(description = "新建收藏夹")
    public String newFavorites() {
        return "favorites/newfavorites";
    }

    @RequestMapping(value = "/tool")
    @LoggerManager(description = "工具页面")
    public String tools(Model model) {
        String path="javascript:(function()%7Bvar%20description;var%20desString=%22%22;var%20metas=document.getElementsByTagName('meta');for(var%20x=0,y=metas.length;x%3Cy;x++)%7Bif(metas%5Bx%5D.name.toLowerCase()==%22description%22)%7Bdescription=metas%5Bx%5D;%7D%7Dif(description)%7BdesString=%22&amp;description=%22+encodeURIComponent(description.content);%7Dvar%20win=window.open(%22"
                + Const.BASE_PATH
                +"collect?from=webtool&url=%22+encodeURIComponent(document.URL)+desString+%22&title=%22+encodeURIComponent(document.title)+%22&charset=%22+document.charset,'_blank');win.focus();%7D)();";
        model.addAttribute("path",path);
        return "tool";
    }
}
