package com.afauria.sample.aop.aspectj.checklogin;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.afauria.sample.aop.aspectj.Tools;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Created by Afauria on 12/3/21.
 */
@Aspect
public class CheckLoginAspect {
    private static final String TAG = "CheckLoginAspect";

    @Around("execution(@CheckLogin * *(..))")
    public void checkLogin(ProceedingJoinPoint joinPoint) {
        if (Tools.checkLogin()) {
            Log.e(TAG, "checkLogin: 已登录");
            try {
                joinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else {
            //或者直接跳转登录页面，跳转逻辑可以封装到外部
            Log.e(TAG, "checkLogin: 未登录");
            Toast.makeText(((Context) joinPoint.getThis()), "请登录...", Toast.LENGTH_SHORT).show();
        }
    }
}
