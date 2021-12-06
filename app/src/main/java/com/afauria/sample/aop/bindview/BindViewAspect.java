package com.afauria.sample.aop.bindview;

import android.app.Activity;
import android.view.View;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Created by Afauria on 12/2/21.
 */
@Aspect
public class BindViewAspect {
    @Around("get(@BindView * *) && @annotation(annotation)")
    public View bindViewById(ProceedingJoinPoint joinPoint, BindView annotation) {
        return ((Activity) joinPoint.getTarget()).findViewById(annotation.value());
    }
}
