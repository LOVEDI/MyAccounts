package com.beicai.zhaodi.accountbook.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/14 0014.
 */
public abstract class SQLiteDaoBase implements SQLiteHelper.SQLiteDataTable{
    private Context context;
    private SQLiteDatabase database;

    public SQLiteDaoBase(Context context) {
        this.context = context;
    }
//返回上下文供别的类使用
    public Context getContext() {
        return context;
    }

    public SQLiteDatabase getDatabase(){
        if(database==null) {
            database = SQLiteHelper.getINSTANCE(context).getWritableDatabase();
        }
        return database;
    }
    //开启事务
    public void beginTransaction(){
        database.beginTransaction();
    }
    //设置事务已经成功
    public void setTransactionSuccessful(){
        database.setTransactionSuccessful();
    }
    //结束事务
    public void endTransaction(){
        database.endTransaction();
    }

    //获取表名和主键
    protected abstract String[] getTableNameAndPK();

    //执行SQL语句的，返回一个游标
    public Cursor execSql(String sql){
        return getDatabase().rawQuery(sql,null);
    }

    //获取总数
    public int getCount(String pk,String tableName,String condition){
            Cursor cursor = execSql("select "+pk+" from "+tableName+" where 1 = 1 "+condition);
        int count = cursor.getCount();
        //可以关闭，不可以回收
        cursor.close();
        return count;
    }

    //获取总数
    public int getCount(String condition){
        String[] str = getTableNameAndPK();
        return getCount(str[1],str[0],condition);
    }

    //删除
    protected boolean delete(String tableName, String condition){
        return getDatabase().delete(tableName," 1=1 "+condition,null)>=0;
    }

    //游标转集合
    protected List cursorToList(Cursor cursor){
        List list = new ArrayList();
        while (cursor.moveToNext()){
            Object object = findModel(cursor);
            list.add(object);
        }
        cursor.close();
        return list;
    }

    //获取集合
    protected List getList(String sql){
        Cursor cursor = execSql(sql);
        return cursorToList(cursor);
    }
    protected abstract Object findModel(Cursor cursor);
}
