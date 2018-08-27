package com.favorite.service.impl;

import com.favorite.domain.Config;
import com.favorite.repository.ConfigRepository;
import com.favorite.service.ConfigService;
import com.favorite.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by cdc on 2018/8/15.
 */
@Service("configService")
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigRepository configRepository;

    @Override
    public Config saveConfig(Long userId, String favoritesId) {
        Config config = new Config();
        config.setUserId(userId);
        config.setDefaultModel("simple");
        config.setDefaultFavorties(favoritesId);
        config.setDefaultCollectType("public");
        config.setCreateTime(DateUtils.getCurrentTime());
        config.setLastModifyTime(DateUtils.getCurrentTime());
        configRepository.save(config);
        return config;
    }

    @Override
    public void updateConfig(long id, String type, String defaultFavorites) {

    }
}
