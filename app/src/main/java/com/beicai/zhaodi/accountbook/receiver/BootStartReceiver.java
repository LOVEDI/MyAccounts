package com.beicai.zhaodi.accountbook.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.beicai.zhaodi.accountbook.service.ServiceDatabaseBackup;

/**
 * Created by Administrator on 2016/12/30 0030.
 */
public class BootStartReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        //启动服务
        Intent i = new Intent(context, ServiceDatabaseBackup.class);
        context.startService(i);
    }
    //开机启动广播接收器
}
