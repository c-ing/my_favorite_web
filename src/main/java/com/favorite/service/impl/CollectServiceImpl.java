package com.favorite.service.impl;

import com.favorite.cache.CacheService;
import com.favorite.domain.Collect;
import com.favorite.domain.Praise;
import com.favorite.domain.enums.CollectType;
import com.favorite.domain.enums.IsDelete;
import com.favorite.domain.view.CollectSummary;
import com.favorite.domain.view.CollectView;
import com.favorite.repository.*;
import com.favorite.service.CollectService;
import com.favorite.service.FavoritesService;
import com.favorite.service.NoticeService;
import com.favorite.utils.DateUtils;
import com.favorite.utils.HtmlUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by cdc on 2018/8/16.
 */
@Service("collectService")
public class CollectServiceImpl extends CacheService implements CollectService {

    protected Logger logger =  LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CollectRepository collectRepository;
    @Autowired
    private FavoritesRepository favoritesRepository;
    @Autowired
    private FavoritesService favoritesService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private UserRepository userRepository;
    /*@Autowired
    private NoticeService noticeService;*/
    @Autowired
    private PraiseRepository praiseRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private FollowRepository followRepository;

    @Override
    public List<CollectSummary> getCollects(String type, Long userId, Pageable pageable, Long favoritesId, Long specUserId) {
        Page<CollectView> views = null;
        if ("my".equals(type)) {
            List<Long> userIds=followRepository.findMyFollowIdByUserId(userId);
            if(userIds==null || userIds.size()==0){
                views = collectRepository.findViewByUserId(userId, pageable);
            }else{
                views = collectRepository.findViewByUserIdAndFollows(userId, userIds, pageable);
            }
        }else if("myself".equals(type)){
            views = collectRepository.findViewByUserId(userId, pageable);
        } else if ("explore".equals(type)) {
            views = collectRepository.findExploreView(userId,pageable);
        } else if("others".equals(type)){
            views = collectRepository.findViewByUserIdAndType(userId, pageable, CollectType.PUBLIC);
            if(null != specUserId){
                userId = specUserId;
            }
        } else if("otherpublic".equals(type)){
            views = collectRepository.findViewByUserIdAndTypeAndFavoritesId(userId, pageable, CollectType.PUBLIC, favoritesId);
            if(null != specUserId){
                userId = specUserId;
            }
        } else if("garbage".equals(type)){
            views = collectRepository.findViewByUserIdAndIsDelete(userId, pageable);
        }else {
            views = collectRepository.findViewByFavoritesId(Long.parseLong(type), pageable);
        }
        return convertCollect(views,userId);
    }

    @Override
    public List<CollectSummary> searchMy(Long userId, String key, Pageable pageable) {
        Page<CollectView> views = collectRepository.searchMyByKey(userId, "%" + key + "%", pageable);
        return convertCollect(views, userId);
    }

    @Override
    public List<CollectSummary> searchOther(Long userId, String key, Pageable pageable) {
        Page<CollectView> views = collectRepository.searchOtherByKey(userId, "%" + key + "%", pageable);
        return convertCollect(views,userId);
    }

    @Override
    public void importHtml(Map<String, String> map, Long favoritesId, Long userId, String type) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                Map<String, String> result = HtmlUtil.getCollectFromUrl(entry.getKey());
                Collect collect = new Collect();
                collect.setCharset(result.get("charset"));
                if(StringUtils.isBlank(result.get("title"))){
                    collect.setTitle(entry.getValue());
                }else{
                    collect.setTitle(result.get("title"));
                }
                if(StringUtils.isBlank(result.get("description"))){
                    collect.setDescription(collect.getTitle());
                }else{
                    collect.setDescription(result.get("description"));
                }
                collect.setRemark(entry.getValue());
                collect.setFavoritesId(favoritesId);
                collect.setIsDelete(IsDelete.NO);
                collect.setLogoUrl(getMap(entry.getKey()));
                if(CollectType.PRIVATE.toString().equals(type)){
                    collect.setType(CollectType.PRIVATE);
                }else{
                    collect.setType(CollectType.PUBLIC);
                }
                collect.setUrl(entry.getKey());
                collect.setUserId(userId);
                collect.setCreateTime(DateUtils.getCurrentTime());
                collect.setLastModifyTime(DateUtils.getCurrentTime());
                List<Collect> list = collectRepository.findByFavoritesIdAndUrlAndUserIdAndIsDelete(favoritesId, entry.getKey(), userId,IsDelete.NO);
                if (!ObjectUtils.isEmpty(list)) {
                    logger.info("收藏夹：" + favoritesId + "中已经存在：" + entry.getKey() + "这个文章，不在进行导入操作");
                    continue;
                }
                collectRepository.save(collect);
                favoritesService.countFavorites(favoritesId);
            } catch (Exception e) {
                logger.info("导入收藏失败",e);
            }
        }
    }

    private List<CollectSummary> convertCollect(Page<CollectView> views, Long userId) {
        List<CollectSummary> summarys=new ArrayList<CollectSummary>();
        for (CollectView view : views) {
            CollectSummary summary=new CollectSummary(view);
            summary.setPraiseCount(praiseRepository.countByCollectId(view.getId()));
            summary.setCommentCount(commentRepository.countByCollectId(view.getId()));
            Praise praise=praiseRepository.findByUserIdAndCollectId(userId, view.getId());
            if(praise!=null){
                summary.setPraise(true);
            }else{
                summary.setPraise(false);
            }
            summary.setCollectTime(DateUtils.getTimeFormatText(view.getCreateTime()));
            summarys.add(summary);
        }
        return summarys;
    }

    @Override
    public void like(Long userId, long id) {
        Praise praise = praiseRepository.findByUserIdAndCollectId(userId, id);
        if (null == praise) {
            Praise newPraise = new Praise();
            newPraise.setCollectId(id);
            newPraise.setUserId(userId);
            newPraise.setCreateTime(DateUtils.getCurrentTime());
            praiseRepository.save(newPraise);
            // 保存消息通知
            Collect collect = collectRepository.findById(id);
            if(null != collect){
                noticeService.saveNotice(String.valueOf(id), "praise", collect.getUserId(), String.valueOf(newPraise.getId()));
            }
        }else if(praise.getUserId().equals(userId)){
            praiseRepository.deleteById(praise.getId());
        }
    }
}
