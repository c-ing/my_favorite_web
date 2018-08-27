package com.favorite.service.impl;

import com.favorite.domain.Notice;
import com.favorite.domain.view.CollectSummary;
import com.favorite.repository.NoticeRepository;
import com.favorite.service.NoticeService;
import com.favorite.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by cdc on 2018/8/20.
 */
@Service("noticeService")
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    /**
     * 保存消息通知
     * @param collectId
     * @param type
     * @param userId
     * @param operId
     */
    @Override
    public void saveNotice(String collectId, String type, Long userId, String operId) {
        Notice notice = new Notice();
        if(StringUtils.isNotBlank(collectId)){
            notice.setCollectId(collectId);
        }
        notice.setReaded("unread");
        notice.setType(type);
        if(StringUtils.isNotBlank(operId)){
            notice.setOperId(operId);
        }
        notice.setUserId(userId);
        notice.setCreateTime(DateUtils.getCurrentTime());
        noticeRepository.save(notice);
    }

    @Override
    public List<CollectSummary> getNoticeCollects(String type, Long userId, Pageable pageable) {
        return null;
    }
}
