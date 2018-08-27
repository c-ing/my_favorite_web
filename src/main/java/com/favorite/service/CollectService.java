package com.favorite.service;

import com.favorite.domain.view.CollectSummary;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by cdc on 2018/8/16.
 */
public interface CollectService {

    List<CollectSummary> getCollects(String type, Long userId, Pageable pageable, Long favoritesId, Long specUserId);

    List<CollectSummary> searchMy(Long userId,String key, Pageable pageable);

    List<CollectSummary> searchOther(Long userId, String key, Pageable pageable);

    public void importHtml(Map<String, String> map, Long favoritesId, Long userId, String type);

    public void like(Long userId,long id);
}
