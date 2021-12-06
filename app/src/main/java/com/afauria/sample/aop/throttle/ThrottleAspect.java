package com.afauria.sample.aop.throttle;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Created by Afauria on 12/3/21.
 */
@Aspect
public class ThrottleAspect {
    private long lastTime = 0L;

    @Around("execution(@ThrottleClick * *(..)) && @annotation(annotation)")
    public void handleThrottle(ProceedingJoinPoint joinPoint, ThrottleClick annotation) {
        int duration = annotation.duration();
        if (System.currentTimeMillis() - lastTime > duration) {
            try {
                joinPoint.proceed();
                lastTime = System.currentTimeMillis();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}
