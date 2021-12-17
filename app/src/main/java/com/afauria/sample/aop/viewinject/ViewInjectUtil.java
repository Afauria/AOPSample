package com.afauria.sample.aop.viewinject;

import android.app.Activity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Afauria on 12/8/21.
 */
class ViewInjectUtil {
    static void injectViews(Activity activity) {
        Class<? extends Activity> activityCls = activity.getClass(); // 获取activity的Class
        Field[] fields = activityCls.getDeclaredFields(); // 通过Class获取activity的所有字段
        for (Field field : fields) { // 遍历所有字段
            // 获取字段的注解，如果没有ViewInject注解，则返回null
            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            if (viewInject == null) {
                continue;
            }
            int viewId = viewInject.value(); // 获取字段注解的参数，这就是我们传进去控件Id
            if (viewId == -1) {
                continue;
            }
            try {
                field.setAccessible(true);
                // 执行findViewById方法，返回View实例
                // 给字段赋值
                field.set(activity, activity.findViewById(viewId));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
