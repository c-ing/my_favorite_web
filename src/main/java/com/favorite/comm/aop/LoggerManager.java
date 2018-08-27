package com.favorite.comm.aop;

import java.lang.annotation.*;

/**
 * Created by cdc on 2018/6/21.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoggerManager {

    public String description();
}
