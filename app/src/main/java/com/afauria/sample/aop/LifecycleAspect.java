package com.afauria.sample.aop;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by Afauria on 12/2/21.
 */
@Aspect
public class LifecycleAspect {
    private static final String TAG = "LifecycleAspect";

    //匹配Activity中on开头的方法
    @Pointcut("execution(* *..*Activity.on*(..))")
    public void onLifecycle() {
    }

    //在切点之前执行
    @Before("onLifecycle()")
    public void beforLifecycle(JoinPoint joinPoint) {
        Log.e(TAG, "[" + joinPoint.getSourceLocation() + "] " + joinPoint.getSignature().getName());
    }
}
