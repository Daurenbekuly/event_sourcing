package com.example.demo.castom_bean_config;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Service
public @interface Step {

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name();

    String receiverName() default "";

    String exceptionHandler() default "direct:def";

    int exceptionMaxRedeliveries() default 5;

    int exceptionBackOffMultiplier() default 2;
}