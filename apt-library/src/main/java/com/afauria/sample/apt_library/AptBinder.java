package com.afauria.sample.apt_library;

import android.app.Activity;

/**
 * Created by Afauria on 12/13/21.
 */
public class AptBinder {
    //绑定Activity
    public static void bind(Activity activity) {
        Class<?> cls = activity.getClass();
        //找到类名
        String clsName = activity.getClass().getName() + "_ViewBinding";
        try {
            //反射加载类
            Class<?> clazz = Class.forName(clsName);
            //反射获取构造函数，并实例化。
            //可以缓存构造函数，避免重复反射
            clazz.getConstructor(cls).newInstance(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
