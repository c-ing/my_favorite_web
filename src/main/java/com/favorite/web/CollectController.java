package com.favorite.web;

import com.favorite.comm.aop.LoggerManager;
import com.favorite.domain.Favorites;
import com.favorite.domain.enums.IsDelete;
import com.favorite.domain.result.Response;
import com.favorite.repository.FavoritesRepository;
import com.favorite.service.CollectService;
import com.favorite.service.FavoritesService;
import com.favorite.utils.HtmlUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Created by cdc on 2018/8/17.
 */
@RestController
@RequestMapping("/collect")
public class CollectController extends BaseController {

    @Autowired
    private FavoritesRepository favoritesRepository;

    @Autowired
    private CollectService collectService;

    @Autowired
    private FavoritesService favoritesService;

    @RequestMapping("/import")
    @LoggerManager(description = "导入收藏夹操作")
    public void collectImport(@RequestParam("htmlFile")MultipartFile htmlFile,String structure,String type) {
        try {
            if (StringUtils.isNotBlank(structure) && IsDelete.YES.equals(structure)) {
                Map<String, Map<String, String>> map = HtmlUtil.parseHtmlTwo(htmlFile.getInputStream());
                if(null == map || map.isEmpty()){
                    logger.info("未获取到url连接");
                    return ;
                }
                for (Map.Entry<String, Map<String,String>> entry : map.entrySet()) {
                    String favoritesName = entry.getKey();
                    Favorites favorites = favoritesRepository.findByUserIdAndName(getUserId(), favoritesName);
                    if (null == favorites) {
                        favoritesRepository.save(favorites);
                    }
                   collectService.importHtml(entry.getValue(),favorites.getId(),getUserId(),type);
                }
            } else {
                Map<String, String> map = HtmlUtil.parseHtmlOne(htmlFile.getInputStream());
                if(null == map || map.isEmpty()){
                    logger.info("未获取到url连接");
                    return ;
                }
                // 全部导入到<导入自浏览器>收藏夹
                Favorites favorites = favoritesRepository.findByUserIdAndName(getUserId(), "导入自浏览器");
                if(null == favorites){
                    favorites = favoritesService.saveFavorites(getUserId(),"导入自浏览器");
                }
                collectService.importHtml(map, favorites.getId(), getUserId(),type);
            }
        } catch (Exception e) {
            logger.info("导入html异常",e);
        }
        logger.info("导入html成功");
        return ;
    }

    @RequestMapping(value = "like/{id}",method = RequestMethod.POST)
    @LoggerManager(description = "文章点赞或者取消点赞")
    public Response like(@PathVariable("id") long id) {
        logger.info("文章点赞或者取消点赞");
        try {
            collectService.like(getUserId(), id);
        } catch (Exception e) {
            logger.error("文章点赞或者取消点赞异常",e);
        }
        return result();
    }

}
