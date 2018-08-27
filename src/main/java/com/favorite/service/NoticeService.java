package com.favorite.service;

import com.favorite.domain.view.CollectSummary;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by cdc on 2018/8/20.
 */
public interface NoticeService {

    public void saveNotice(String collectId,String type,Long userId,String operId);

    public List<CollectSummary> getNoticeCollects(String type, Long userId, Pageable pageable);

}
