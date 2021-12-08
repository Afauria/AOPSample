package com.afauria.sample.aop.aspectj.trace;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Created by Afauria on 12/2/21.
 */
@Aspect
public class DebugTraceAspect {
    private static final String TAG = "DebugTraceAspect";

    //Around和Before、After的区别：Around入参为ProceedingJoinPoint，可以调用proceed方法执行目标方法。
    //Around和Before、After匹配到同一个目标的时候，Around切点需要放到前面
    @Around("execution(@DebugTrace * *(..))")
    public void trace(ProceedingJoinPoint joinPoint) {
        //统计方法耗时
        long time = System.currentTimeMillis();
        try {
            joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        long duration = System.currentTimeMillis() - time;
        Log.e(TAG, "[" + joinPoint.getSourceLocation() + "] " + joinPoint.getSignature().getName() + "方法耗时: " + duration);
    }

//    @Before("execution(* **(..))")
//    public void beginTrace(JoinPoint joinPoint) {
//        Log.e(TAG, "beginTrace: " + "[" + joinPoint.getSourceLocation() + "] " + joinPoint.getSignature().getName());
//        Trace.beginSection(joinPoint.getSignature().toString());
//    }
//
//    @After("execution(* **(..))")
//    public void endTrace(JoinPoint joinPoint) {
//        Log.e(TAG, "endTrace: " + "[" + joinPoint.getSourceLocation() + "] " + joinPoint.getSignature().getName());
//        Trace.endSection();
//    }
}
