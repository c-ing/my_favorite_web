package com.favorite.web;

import com.favorite.comm.aop.LoggerManager;
import com.favorite.domain.Favorites;
import com.favorite.domain.result.ExceptionMsg;
import com.favorite.domain.result.Response;
import com.favorite.repository.FavoritesRepository;
import com.favorite.service.FavoritesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by cdc on 2018/8/16.
 */
@RestController
@RequestMapping("/favorites")
public class FavoritesController extends BaseController{

    @Autowired
    private FavoritesRepository favoritesRepository;

    @Autowired
    private FavoritesService favoritesService;

    @RequestMapping(value = "/getFavorites/{userId}", method = RequestMethod.POST)
    @LoggerManager(description="获取收藏夹")
    public List<Favorites> getFavorites(@PathVariable("userId") Long userId) {
        List<Favorites> favorites = null;
        try {
            Long id = getUserId();
            if(null != userId && 0 != userId){
                id = userId;
            }
            favorites = favoritesRepository.findByUserIdOrderByIdDesc(id);
        } catch (Exception e) {
            logger.error("getFavorites failed",e);
        }

        logger.info("getFavorites end favorites ==" );
        return favorites;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @LoggerManager(description = "新建收藏夹")
    public Response addFavorites(String name) {
        if (StringUtils.isNotBlank(name)) {
            Favorites favorites = favoritesRepository.findByUserIdAndName(getUserId(), name);
            if (null != favorites) {
                logger.info("收藏夹名称已经存在");
                return result(ExceptionMsg.FavoritesNameUsed);
            } else {
                try {
                    favoritesService.saveFavorites(getUserId(), name);
                } catch (Exception e) {
                    logger.info("异常",e);
                    return result(ExceptionMsg.FAILED);
                }
            }
        }else {
            logger.info("收藏夹名称为空");
            return result(ExceptionMsg.FavoritesNameIsNull);
        }
        return result();
    }
}
