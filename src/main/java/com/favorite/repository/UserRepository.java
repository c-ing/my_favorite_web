package com.favorite.repository;

import com.favorite.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by cdc on 2018/6/24.
 */
public interface UserRepository extends JpaRepository<User,Long> {

    User findByUserName(String userName);

    User findByUserNameOrEmail(String userName, String email);

    User findByEmail(String email);

    User findById(long id);

    /*@Modifying(clearAutomatically = true)
    @Transactional
    @Query("update User set outDate=:outDate, validataCode=:validataCode where email=:email")
    int setOutDateAndValidataCode(@Param("outData")String outData, @Param("validate") String validataCode, @Param("email") String email);*/

}
