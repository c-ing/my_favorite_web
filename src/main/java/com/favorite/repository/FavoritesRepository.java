package com.favorite.repository;

import com.favorite.domain.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by cdc on 2018/8/15.
 */
public interface FavoritesRepository extends JpaRepository<Favorites,Long> {
    Favorites findById(long id);

    List<Favorites> findByUserId(Long userId);

    List<Favorites> findByUserIdOrderByIdDesc(Long userId);

    Favorites findByUserIdAndName(Long userId,String name);

}
