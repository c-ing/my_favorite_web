package com.favorite.service;

import com.favorite.domain.Config;

/**
 * Created by cdc on 2018/8/15.
 */
public interface ConfigService {

    public Config saveConfig(Long userId, String favoritesId);

    public void updateConfig(long id, String type, String defaultFavorites);
}
