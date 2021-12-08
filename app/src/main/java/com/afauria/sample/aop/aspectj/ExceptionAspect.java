package com.afauria.sample.aop.aspectj;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by Afauria on 12/2/21.
 */
@Aspect
public class ExceptionAspect {
    private static final String TAG = "ExceptionAspect";

    //原理：匹配代码中的try-catch块，且catch的类型要一样
    //或者使用通配符
    @Pointcut("handler(java.lang.*Exception) && args(e)")
    public void onException(Exception e) {
    }

    @Before(value = "onException(e)", argNames = "e")
    public void handleExceptionBefore(JoinPoint joinPoint, Exception e) {
        Log.e(TAG, "handleExceptionBefore: " + "[" + joinPoint.getSourceLocation() + "] " + joinPoint.getSignature().getName() + " " + e.getClass().getName());
    }

    //原理：在目标位置加try-catch，catch之后先执行方法，再throw抛异常。并不会阻止程序崩溃
//    @AfterThrowing(value = "execution(* *(..))", throwing = "e")
//    public void afterThrowing(JoinPoint joinPoint, Exception e) {
//        Log.e(TAG, "afterThrowing: " + joinPoint.getSignature().getName());
//    }
}
