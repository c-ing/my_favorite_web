package com.favorite.repository;

import com.favorite.domain.Config;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by cdc on 2018/8/15.
 */
public interface ConfigRepository extends JpaRepository<Config,Long> {

    public Config findByUserId(Long userId);
}
