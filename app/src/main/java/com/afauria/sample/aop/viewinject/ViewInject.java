package com.afauria.sample.aop.viewinject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Afauria on 12/8/21.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface ViewInject {
    int value();
}
