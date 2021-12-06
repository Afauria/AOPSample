package com.afauria.sample.aop;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Created by Afauria on 12/3/21.
 */
// 校验方法参数是否为空，为空则不执行
@Aspect
public class CheckArgsAspect {
    private static final String TAG = "CheckArgsAspect";

    //匹配有一个参数的方法，可以使用..匹配多参数
    //Advice方法中参数名称需要和args表达式中名称一样
    @Around("execution(* *(String)) && args(arg)")
    public void checkArgs(ProceedingJoinPoint joinPoint, String arg) {
        Log.e(TAG, "checkArgs: " + arg + " [" + joinPoint.getSourceLocation() + "] " + joinPoint.getSignature().getName());
        if (arg != null && !arg.isEmpty()) {
            try {
                joinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else {
            Toast.makeText(((Context) joinPoint.getThis()), "参数为空", Toast.LENGTH_SHORT).show();
        }
    }
}
