package com.favorite.service;

import com.favorite.domain.Collect;
import com.favorite.domain.Favorites;

/**
 * Created by cdc on 2018/8/15.
 */
public interface FavoritesService {

    public Favorites saveFavorites(Long userId, String name);
    public Favorites saveFavorites(Collect collect);
    public void countFavorites(long id);
}
