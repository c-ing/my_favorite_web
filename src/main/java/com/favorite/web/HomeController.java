package com.favorite.web;

import com.favorite.comm.aop.LoggerManager;
import com.favorite.domain.Favorites;
import com.favorite.domain.User;
import com.favorite.domain.enums.CollectType;
import com.favorite.domain.enums.FollowStatus;
import com.favorite.domain.enums.IsDelete;
import com.favorite.domain.view.CollectSummary;
import com.favorite.repository.CollectRepository;
import com.favorite.repository.FavoritesRepository;
import com.favorite.repository.FollowRepository;
import com.favorite.repository.UserRepository;
import com.favorite.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by cdc on 2018/8/16.
 */
@RequestMapping("/")
@Controller
public class HomeController extends BaseController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CollectRepository collectRepository;

    @Autowired
    private CollectService collectService;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private FavoritesRepository favoritesRepository;

    @RequestMapping("/user/{userId}/{favoritesId}")
    @LoggerManager(description = "个人首页")
    public String userPageShow(Model model, @PathVariable("userId")long userId, @PathVariable("favoritesId")Long favoritesId,
                               @RequestParam(value = "page",defaultValue = "0") Integer page,
                               @RequestParam(value = "size",defaultValue = "15") Integer size) {
        User user = userRepository.findById(userId);
        Long collectCount = (long)01;
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size,sort);
        List<CollectSummary> collects = null;
        Integer isFollow = 0;
        if(getUserId() == userId){
            model.addAttribute("myself", IsDelete.YES.toString());
            collectCount = collectRepository.countByUserIdAndIsDelete(userId,IsDelete.NO);
            if(0 == favoritesId){
                collects =collectService.getCollects("myself", userId, pageable,null,null);
            }else{
                collects =collectService.getCollects(String.valueOf(favoritesId), userId, pageable,0l,null);
            }
        }else{
            model.addAttribute("myself",IsDelete.NO.toString());
            collectCount = collectRepository.countByUserIdAndTypeAndIsDelete(userId, CollectType.PUBLIC, IsDelete.NO);
            if(favoritesId == 0){
                collects =collectService.getCollects("others", userId, pageable,null,getUserId());
            }else{
                collects = collectService.getCollects("otherpublic", userId, pageable, favoritesId,getUserId());
            }
            isFollow = followRepository.countByUserIdAndFollowIdAndStatus(getUserId(), userId, FollowStatus.FOLLOW);
        }
        Integer follow = followRepository.countByUserIdAndStatus(userId, FollowStatus.FOLLOW);
        Integer followed = followRepository.countByFollowIdAndStatus(userId, FollowStatus.FOLLOW);
        List<Favorites> favoritesList = favoritesRepository.findByUserId(userId);
        List<String> followUser = followRepository.findFollowUserByUserId(userId);
        List<String> followedUser = followRepository.findFollowedUserByFollowId(userId);
        model.addAttribute("collectCount",collectCount);
        model.addAttribute("follow",follow);
        model.addAttribute("followed",followed);
        model.addAttribute("user",user);
        model.addAttribute("collects", collects);
        model.addAttribute("favoritesList",favoritesList);
        model.addAttribute("followUser",followUser);
        model.addAttribute("followedUser",followedUser);
        model.addAttribute("isFollow",isFollow);
        User userTemp = null;
        User currentUser = getUser();
        if(this.getUser()==null){
            userTemp = new User();
            userTemp.setId(0L);
        }
        model.addAttribute("loginUser",currentUser == null ? userTemp : currentUser);
        return "user";
    }

    @RequestMapping("/usercontent/{userId}/{favoritesId}")
    @LoggerManager(description = "个人首页内容替换")
    public String userContentShow(Model model,@PathVariable("userId") long userId,@PathVariable("favoritesId") Long favoritesId,@RequestParam(value = "page", defaultValue = "0") Integer page,
                                  @RequestParam(value = "size", defaultValue = "15") Integer size){
        User user = userRepository.findById(userId);
        Long collectCount = 0l;
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size,sort);
        List<CollectSummary> collects = null;
        if(getUserId() == userId){
            model.addAttribute("myself",IsDelete.YES.toString());
            collectCount = collectRepository.countByUserIdAndIsDelete(userId, IsDelete.NO);
            if(0 == favoritesId){
                collects =collectService.getCollects("myself", userId, pageable,null,null);
            }else{
                collects =collectService.getCollects(String.valueOf(favoritesId), userId, pageable,0l,null);
            }
        }else{
            model.addAttribute("myself",IsDelete.NO.toString());
            collectCount = collectRepository.countByUserIdAndTypeAndIsDelete(userId, CollectType.PUBLIC, IsDelete.NO);
            if(favoritesId == 0){
                collects =collectService.getCollects("others", userId, pageable,null,getUserId());
            }else{
                collects = collectService.getCollects("otherpublic", userId, pageable, favoritesId,getUserId());
            }
        }
        List<Favorites> favoritesList = favoritesRepository.findByUserId(userId);
        model.addAttribute("collectCount",collectCount);
        model.addAttribute("user",user);
        model.addAttribute("collects", collects);
        model.addAttribute("favoritesList",favoritesList);
        model.addAttribute("favoritesId", favoritesId);
        model.addAttribute("loginUser",getUser());
        return "fragments/usercontent";
    }

    @RequestMapping("/search/{key}")
    @LoggerManager(description = "搜索")
    public String search(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size", defaultValue = "20") Integer size,
                         @PathVariable("key") String key) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        List<CollectSummary> myCollects = collectService.searchMy(getUserId(), key, pageable);
        List<CollectSummary> otherCollects = collectService.searchOther(getUserId(), key, pageable);
        model.addAttribute("myCollects", myCollects);
        model.addAttribute("otherCollects", otherCollects);
        model.addAttribute("userId",getUserId());

        model.addAttribute("mysize", myCollects.size());
        model.addAttribute("othersize", otherCollects.size());
        model.addAttribute("key", key);

        logger.info("serach end:" + getUserId());
        return "collect/search";
    }

    @RequestMapping(value = "/standard/{type}/{userId}")
    @LoggerManager(description = "文章列表standa")
    public String standard(Model model,@RequestParam(value = "page",defaultValue = "0")Integer page,
                           @RequestParam(value = "size", defaultValue = "15") Integer size,@PathVariable("type") String type,@PathVariable("userId") long userId) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        model.addAttribute("type", type);
        Favorites favorites = new Favorites();
        if (!"my".equals(type) && !"explore".equals(type) && !"garbage".equals(type)) {
            try{
                favorites = favoritesRepository.findById(Long.parseLong(type));
                favorites.setPublicCount(collectRepository.countByFavoritesIdAndTypeAndIsDelete(favorites.getId(),CollectType.PUBLIC,IsDelete.NO));
            } catch (Exception e) {
                logger.error("获取收藏夹异常：",e);
            }
        }
        List<CollectSummary> collects = null;
        if(0 != userId && 0 != userId && userId != getUserId()){
            User user = userRepository.findById(userId);
            model.addAttribute("otherPeople", user);
            collects =collectService.getCollects("otherpublic",userId, pageable,favorites.getId(),null);
        }else{
            collects =collectService.getCollects(type,getUserId(), pageable,null,null);
        }
        model.addAttribute("collects", collects);
        model.addAttribute("favorites", favorites);
        model.addAttribute("userId", getUserId());
        model.addAttribute("size", collects.size());
        logger.info("standard end :"+ getUserId());
        return "collect/standard";
    }
}
