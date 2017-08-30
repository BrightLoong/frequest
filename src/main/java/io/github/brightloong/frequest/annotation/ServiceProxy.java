package io.github.brightloong.frequest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于service代理的注解，方法具有此注解时候会执行service代理
 * Created by BrightLoong on 2017/8/14.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface ServiceProxy {
}