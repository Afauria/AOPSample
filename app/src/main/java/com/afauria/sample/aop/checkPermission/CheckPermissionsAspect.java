package com.afauria.sample.aop.checkPermission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Afauria on 12/3/21.
 */
@Aspect
public class CheckPermissionsAspect {
    private static final String TAG = "CheckPermissionAspect";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Around("execution(@CheckPermissions * *(..)) && @annotation(annotation)")
    public void checkPermission(ProceedingJoinPoint joinPoint, CheckPermissions annotation) {
        Log.e(TAG, "checkPermission: " + "[" + joinPoint.getSourceLocation() + "] " + joinPoint.getSignature().getName() + annotation);
        String[] permissions = annotation.value();
        if (permissions.length > 0) {
            Activity activity = (Activity) joinPoint.getTarget();
            List<String> deniedPermission = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                if (activity.checkSelfPermission(permissions[i]) == PackageManager.PERMISSION_DENIED) {
                    deniedPermission.add(permissions[i]);
                }
            }
            if (!deniedPermission.isEmpty()) {
                //动态申请权限，需要在onRequestPermissionsResult中检查是否授权成功。也可以通过切入的方式检查
                activity.requestPermissions(permissions, 0x1);
            } else {
                try {
                    joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
    }
}
