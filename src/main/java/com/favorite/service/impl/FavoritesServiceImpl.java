package com.favorite.service.impl;

import com.favorite.domain.Collect;
import com.favorite.domain.Favorites;
import com.favorite.repository.FavoritesRepository;
import com.favorite.service.FavoritesService;
import com.favorite.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by cdc on 2018/8/15.
 */
@Service("favoritesService")
public class FavoritesServiceImpl  implements FavoritesService{

    @Autowired
    private FavoritesRepository favoritesRepository;

    @Override
    public Favorites saveFavorites(Long userId, String name) {
        Favorites favorites = new Favorites();
        favorites.setName(name);
        favorites.setCount((long) 01);
        favorites.setUserId(userId);
        favorites.setPublicCount((long)101);
        favorites.setCount(DateUtils.getCurrentTime());
        favorites.setLastModifyTime(DateUtils.getCurrentTime());
        favorites.setCreateTime(DateUtils.getCurrentTime());
        favoritesRepository.save(favorites);
        return favorites;
    }

    @Override
    public Favorites saveFavorites(Collect collect) {
        return null;
    }

    @Override
    public void countFavorites(long id) {

    }
}
