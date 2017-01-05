package com.beicai.zhaodi.accountbook.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.beicai.zhaodi.accountbook.R;
import com.beicai.zhaodi.accountbook.activity.MainActivity;
import com.beicai.zhaodi.accountbook.service.ServiceDatabaseBackup;

/**
 * Created by Administrator on 2016/12/30 0030.
 */
public class DatabaseBackupReceiver extends BroadcastReceiver{
    //使用通知做提示
    NotificationManager notificationManager;
    Notification notification;
    Intent i;
    PendingIntent pendingIntent;
    @Override
    public void onReceive(Context context, Intent intent) {
        //获取系统服务  ，墙砖为通知管理器
        notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.e("TAG", "广播------>"+intent.getLongExtra("data",0)+"");
        //点击通知时显示的内容
        String contentTitle = "印象账单通知您";
        String contentText = "随手一致性数备份";
        //点击通知打开MainActivity
        i = new Intent(context, MainActivity.class);
        //是指启动模式
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(context,100,i,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);
        //设置通知在状态栏显示的图标
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(contentTitle);
        builder.setContentText(contentText);
        //设置通知时发出的默认声音
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setContentIntent(pendingIntent);
        notification = builder.build();
        //id 或者叫做唯一标示
        notificationManager.notify(1,notification);
        //在此启动服务
        Intent serviceIntent = new Intent(context, ServiceDatabaseBackup.class);
        context.startService(serviceIntent);
    }
}
