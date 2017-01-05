package com.beicai.zhaodi.accountbook.utils;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by Administrator on 2016/12/30 0030.
 */
public class MyUtil {
    //判断哪个activity正在运行
    public static String getRunningActivityName(Context c){

        ActivityManager activityManager=(ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);

        String runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getClassName();

        return runningActivity;

    }
}
