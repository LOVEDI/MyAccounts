package com.beicai.zhaodi.accountbook.database;

import android.content.Context;
import android.util.Log;

import com.beicai.zhaodi.accountbook.R;

import java.util.ArrayList;

/**
 *  数据库具体创建的类
 */
public class SQLiteDateBaseConfig {
    //数据库名
    public static final String DATABASE_NAME = "readily.db";
    //版本号
    private  static final int VERSION = 1;
    private static SQLiteDateBaseConfig INSTANCE;
    private  static Context CONTEXT;

    private SQLiteDateBaseConfig(){

    }
    public synchronized static SQLiteDateBaseConfig getInstance(Context context){
        if(INSTANCE==null) {
            INSTANCE = new SQLiteDateBaseConfig();
            CONTEXT = context;
        }
        return  INSTANCE;
    }

    public String getDatabaseName(){
        return DATABASE_NAME;
    }

    public int getVersion(){
        return VERSION;
    }

        //通过反射调用 创建数据库表的操作
    public ArrayList<String> getTables(){
        ArrayList<String> list = new ArrayList<String>();
        String[] sqliteDaoClassName = CONTEXT.getResources().getStringArray
                (R.array.SQLiteDAOClassName);//类名
        String packagePath = CONTEXT.getPackageName();//清单文件中的包名
        packagePath = packagePath+".database.dao.";//得到全类名
        for (int i = 0 ; i< sqliteDaoClassName.length;i++){
            list.add(packagePath+sqliteDaoClassName[i]);//这才是真正的全类名
            Log.e("TAG", "反射类的路径是：" + list.get(i));
        }

        return list;
    }
}
