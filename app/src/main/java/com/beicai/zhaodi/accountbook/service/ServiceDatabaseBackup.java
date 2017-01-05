package com.beicai.zhaodi.accountbook.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.beicai.zhaodi.accountbook.business.DataBackupBusiness;
import com.beicai.zhaodi.accountbook.receiver.DatabaseBackupReceiver;

import java.util.Date;

/**
 * Created by Administrator on 2016/12/30 0030.
 */
public class ServiceDatabaseBackup extends Service {
    //定义自动备份的间隔时间
    private static final long spacingin_terval = 100000000;
    AlarmManager am;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DataBackupBusiness dataBackupBusiness = new DataBackupBusiness(this);
        //读取上一次备份的日期
        long backupMillise = dataBackupBusiness.loadDatabaseBackupDate();
        Date backupDate = new Date();
        if(backupMillise==0) {
            dataBackupBusiness.databaseBackup(backupDate);
            backupMillise = dataBackupBusiness.loadDatabaseBackupDate();
        }else{
             //如果当前日期 - 上次备份的器器》=时间间隔  说明悠悠盖备份啦
            if(backupDate.getTime() - backupMillise >= spacingin_terval) {
                //在执行一次
                dataBackupBusiness.databaseBackup(backupDate);
                //又得到最新的备份时间
                backupMillise = dataBackupBusiness.loadDatabaseBackupDate();

            }
        }
        Log.e("TAG", "备份日期是：=====" + backupMillise);
        Intent  i = new Intent(this,DatabaseBackupReceiver.class);
        if(intent==null) {
            Log.e("TAG","itent==nuoll???"+intent);
        }else{
            //传入备份日期
            intent.putExtra("data",backupMillise);
            //1上下文　２，请求码　，３，想要打开那个类　４　覆盖方式
            PendingIntent pi = PendingIntent.getBroadcast(this,0,i,PendingIntent.FLAG_ONE_SHOT);
            //获取定时器对象
            am = (AlarmManager) getSystemService(ALARM_SERVICE);
            //1，唤起的类型（在系统休眠的状态下 照样备份） 2：触发的时间 3:脑子响应的动作
            am.set(AlarmManager.RTC_WAKEUP,backupMillise+spacingin_terval,
                    pi);

        }
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {

    }
}
