package com.beicai.zhaodi.accountbook.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.beicai.zhaodi.accountbook.R;
import com.beicai.zhaodi.accountbook.database.SQLiteDaoBase;
import com.beicai.zhaodi.accountbook.entity.Users;
import com.beicai.zhaodi.accountbook.utils.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/12/14 0014.
 */
public class UserDAO extends SQLiteDaoBase{
    public UserDAO(Context context) {
        super(context);
    }

    @Override
    protected String[] getTableNameAndPK() {
        return new String[]{"users","userId"};
    }

    @Override
    protected Object findModel(Cursor cursor) {
        Users user = new Users();
        user.setUserId(cursor.getInt(cursor.getColumnIndex("userId")));
        user.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
        Date createDate = DateUtil.getDate(cursor.getString(cursor.getColumnIndex("createDate")),
                "yyyy-MM-dd HH:mm:ss");
        user.setCreateDate(createDate);
        user.setCreateDate(createDate);
        user.setState(cursor.getInt(cursor.getColumnIndex("state")));
        return  user;
    }
//创建数据库 表
    @Override
    public void onCreate(SQLiteDatabase database) {
        StringBuilder sql = new StringBuilder();
        Log.e("TAG","这是创建数据库表的方法");
        sql.append("");
        sql.append("  Create TABLE [users](");
        sql.append("  [userId] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
        sql.append("  ,[userName] varchar(20) NOT NULL");
        sql.append(" ,[createDate] datatime NOT NULL");
        sql.append("  ,[state] int NOT NULL");
        sql.append(" )");
        database.execSQL(sql.toString());
        //默认插入三条数据
        initDefaultData(database);
    }
//版本升级
    @Override
    public void onUpgrade(SQLiteDatabase database) {

    }

    //增加
    public boolean insertUser(Users users){
        ContentValues contentValues = createParms(users);
        //返回插入条数的id
        long newid = getDatabase().insert(getTableNameAndPK()[0], null, contentValues);

        return newid>0;
    }
    public ContentValues createParms(Users info){
        ContentValues contentValues = new ContentValues();
        contentValues.put("userName",info.getUserName());
        //日期转换的工具类
        contentValues.put("createDate", DateUtil.getFormatDateTime(info.getCreateDate(),"yyyy-mm-dd HH:mm:ss"));
        contentValues.put("state",info.getState());
        return contentValues;
    }
    //删除
    public boolean deleteUsers(String  condition){
        return delete(getTableNameAndPK()[0],condition);
    }
    //修改
    public boolean updateUsers(String condition,Users user){
        ContentValues contentValues = createParms(user);
        return updateUsers(condition, contentValues);
    }
    //这个也是修改,修改单个
    public boolean updateUsers(String condition,ContentValues contentValues){
        Log.e("TAG","contentvalues"+contentValues+"3333333333333condition"+condition);
        // 2、要修改的数据 ， 3、条件    4、向里面填的值
        return getDatabase().update(getTableNameAndPK()[0],
                contentValues,condition,null)>0;
    }
    //查看
    public List<Users> getUsers(String condition){
        String sql = "select * from  users where 1=1 "+condition;
        return getList(sql);
    }
    //初始化插入的数据
    private void initDefaultData( SQLiteDatabase database){
        Users user = new Users();
        String[] userName = getContext().getResources().
                getStringArray(R.array.InitDefaultUserName);
        for (int i = 0 ; i<userName.length; i++){
            user.setUserName(userName[i]);
        ContentValues contentValues = createParms(user);
            database.insert(getTableNameAndPK()[0],null,contentValues);
        }

    }
}
