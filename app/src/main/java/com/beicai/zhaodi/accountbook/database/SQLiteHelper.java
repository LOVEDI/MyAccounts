package com.beicai.zhaodi.accountbook.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.beicai.zhaodi.accountbook.utils.Reflection;

import java.util.List;

/**
 * Created by Administrator on 2016/12/13 0013.
 */
public class SQLiteHelper extends SQLiteOpenHelper{
        private static SQLiteDateBaseConfig CONFIG;
        private static SQLiteHelper INSTANCE;
        private Reflection reflection;
        Context context;
    //定义一个 接口  进行创建和版本升级
        public interface SQLiteDataTable{
            public void onCreate(SQLiteDatabase database);
            public void onUpgrade(SQLiteDatabase database);
        }
    public synchronized static SQLiteHelper getINSTANCE(Context context){
        if(INSTANCE==null) {
            CONFIG = SQLiteDateBaseConfig.getInstance(context);
            INSTANCE = new SQLiteHelper(context);
        }
        return INSTANCE;
    }
    //构造方法】
    private SQLiteHelper(Context context){
        super(context, CONFIG.getDatabaseName(), null, CONFIG.getVersion());
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {

            List<String> list = CONFIG.getTables();
            //给工具类创建实例
            reflection = new Reflection();
            Log.e("TAG","list**************"+list.get(0)+"************"+list.size());
            for (int i = 0; i < list.size(); i++) {
                /**
                 * 全类名    构造方法参数     构造方法参数的类型
                 */
              SQLiteDataTable sqliteDataTable = (SQLiteDataTable) reflection.newInstance(list.get(i),
                      new Object[]{context},new Class[]{Context.class});
                //创建数据库表
                Log.e("TAG", "sqliteDateTABLE 中的信息是：" + sqliteDataTable.toString());
                sqliteDataTable.onCreate(db);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
