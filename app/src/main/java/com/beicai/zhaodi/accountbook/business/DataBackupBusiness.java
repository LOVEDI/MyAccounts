package com.beicai.zhaodi.accountbook.business;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.beicai.zhaodi.accountbook.business.base.BaseBusiness;
import com.beicai.zhaodi.accountbook.database.SQLiteDateBaseConfig;
import com.beicai.zhaodi.accountbook.utils.FileUtil;

import java.io.File;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/30 0030.
 */
public class DataBackupBusiness extends BaseBusiness{
    private static final String SDCARD_PARTH = Environment.getExternalStorageDirectory().getPath()
            +"/Readily/DataBaseBak/";
    private String DATA_PATH = Environment.getDataDirectory()+"/data/"+context.getPackageName()
            +"/databases/";
    public DataBackupBusiness(Context context) {
        super(context);
    }

    //用于读取上一次数据备份的日期
    public long loadDatabaseBackupDate(){
        long databaseBackupDate = 0 ;
        //获取指定key的SharedPreferences对象    私有的除啦当前应用 其他应用不可以访问
        SharedPreferences sp = context.getSharedPreferences("databaseBackupDate",Context.MODE_PRIVATE);
        if(sp!=null) {
            databaseBackupDate = sp.getLong("databaseBackupDate",0);
        }
        return databaseBackupDate;
    }
    //数据备份
    public boolean databaseBackup(Date backupDate) {
        boolean result = false;
        try {
            File sourceFile = new File(DATA_PATH + SQLiteDateBaseConfig.DATABASE_NAME);
            //如果数据库文件存在的话才进行备份操作
            if (sourceFile.exists()) {
                //把文件保存到sd卡的指定目录下
                File fileDir = new File(SDCARD_PARTH+SQLiteDateBaseConfig.DATABASE_NAME);
                // 目录不存在就创建
                Log.e("TAG","文件存在么？"+fileDir.exists());
                if (!fileDir.exists()) {
                    //创建一个文件夹
                    fileDir.mkdir();
                }
                //从这个路径下拷贝到另一个文件下，参数1 原路径+文件名
                //参数2 目录路径+文件名
                Log.e("TAG","路径11==="+DATA_PATH + SQLiteDateBaseConfig.DATABASE_NAME);
                Log.e("TAG","路径11==="+SDCARD_PARTH + SQLiteDateBaseConfig.DATABASE_NAME);
                FileUtil.copyFile(DATA_PATH + SQLiteDateBaseConfig.DATABASE_NAME,
                        SDCARD_PARTH+ SQLiteDateBaseConfig.DATABASE_NAME);
                result = true;
            }else{
                File fileDir = new File(DATA_PATH+SQLiteDateBaseConfig.DATABASE_NAME);
                fileDir.mkdir();
                FileUtil.copyFile(DATA_PATH + SQLiteDateBaseConfig.DATABASE_NAME,
                        SDCARD_PARTH);
                result = true;
            }
            // 保存日期共享偏好
            saveDatabaseBackupDate(backupDate.getTime());
        } catch (Exception e) {

            e.printStackTrace();
        }
        return result;
    }

    //保存备份日期
    private void saveDatabaseBackupDate(long time) {
        SharedPreferences sha = context.getSharedPreferences(
                "databaseBackupDate", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sha.edit();
        edit.putLong("databaseBackupDate", time);
        edit.commit();
    }

    //数据还原
    public boolean databaseRestore() {
        boolean result = false;
        File sourceFile = new File(SDCARD_PARTH
                + SQLiteDateBaseConfig.DATABASE_NAME);
        if (sourceFile.exists()) {
            Log.e("TAG","说明文件存在，在数据还原的时候");
            File fileDir = new File(DATA_PATH);
            Log.e("TAG","文件存在么？"+fileDir.exists());
            if (!fileDir.exists()) {
                Log.e("TAG","开始创建数据库文件在刚开始运行的时候呀");
                //创建文件夹
                fileDir.mkdirs();
            }
            Log.e("TAG","还原时候；1==="+SDCARD_PARTH + SQLiteDateBaseConfig.DATABASE_NAME);
            Log.e("TAG","还原时候；2==="+DATA_PATH);
            FileUtil.copyFolder(SDCARD_PARTH + SQLiteDateBaseConfig.DATABASE_NAME,
                    DATA_PATH);
            result = true;
        }
        return result;

    }

}
