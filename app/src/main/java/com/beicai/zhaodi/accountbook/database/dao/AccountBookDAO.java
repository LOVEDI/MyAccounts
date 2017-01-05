package com.beicai.zhaodi.accountbook.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.beicai.zhaodi.accountbook.R;
import com.beicai.zhaodi.accountbook.database.SQLiteDaoBase;
import com.beicai.zhaodi.accountbook.entity.AccountBook;
import com.beicai.zhaodi.accountbook.utils.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/12/14 0014.
 */
public class AccountBookDAO extends SQLiteDaoBase{
    public AccountBookDAO(Context context) {
        super(context);
    }

    @Override
    protected String[] getTableNameAndPK() {
        return new String[]{"accountBook","accountBookId"};
    }

    @Override
    protected Object findModel(Cursor cursor) {
        AccountBook accountBook = new AccountBook();
        accountBook.setAccountBookId(cursor.getInt(cursor.getColumnIndex("accountBookId")));
        accountBook.setAccountBookName(cursor.getString(cursor.getColumnIndex("accountBookName")));
        Date createDate = DateUtil.getDate(cursor.getString(cursor.getColumnIndex("createDate")),
                "yyyy-MM-dd HH:mm:ss");
        accountBook.setCreateDate(createDate);
        accountBook.setIsDefault(cursor.getInt(cursor.getColumnIndex("isDefault")));
        Log.e("TAG","在AccountBookDAO中 isDefault=="+accountBook.getIsDefault());
        accountBook.setState(cursor.getInt(cursor.getColumnIndex("state")));
        return  accountBook;
    }
//创建数据库 表
    @Override
    public void onCreate(SQLiteDatabase database) {
        StringBuilder sql = new StringBuilder();
        Log.e("TAG", "这是创建数据库表的方法");
        sql.append("");
        sql.append("  Create TABLE [accountBook](");
        sql.append("  [accountBookId] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
        sql.append("  ,[accountBookName] varchar(20) NOT NULL");
        sql.append(" ,[createDate] datatime NOT NULL");
        sql.append("  ,[state] int NOT NULL");
        sql.append("  ,[isDefault] int NOT NULL");
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
    public boolean insertAccountBook(AccountBook accountBook){
        ContentValues contentValues = createParms(accountBook);
        //返回插入条数的id
        long newid = getDatabase().insert(getTableNameAndPK()[0], null, contentValues);
        return newid>0;
    }
    public ContentValues createParms(AccountBook info){
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountBookName",info.getAccountBookName());
        //日期转换的工具类
        contentValues.put("createDate", DateUtil.getFormatDateTime(info.getCreateDate(),"yyyy-mm-dd HH:mm:ss"));
        contentValues.put("state",info.getState());
        contentValues.put("isDefault",info.getIsDefault());
        Log.e("TAG","AccountBook类中 - info.getDefault="+info.getIsDefault());
        return contentValues;
    }
    //删除
    public boolean deleteAccountBook(String  condition){
        return delete(getTableNameAndPK()[0],condition);
    }
    //修改
    public boolean updateAccountBook(String condition,AccountBook accountBook){
        ContentValues contentValues = createParms(accountBook);
        Log.e("TAG","contentValuses"+contentValues.toString());
        return updateAccountBook(condition, contentValues);
    }
    //这个也是修改,修改单个
    public boolean updateAccountBook(String condition,ContentValues contentValues){
        Log.e("TAG","contentvalues"+contentValues+"3333333333333condition"+condition);
        // 2、要修改的数据 ， 3、条件    4、向里面填的值
        return getDatabase().update(getTableNameAndPK()[0],
                contentValues,condition,null)>0;
    }
    //查看
    public List<AccountBook> getAccountBook(String condition){
        String sql = "select * from  accountBook where 1=1 "+condition;
        return getList(sql);
    }
    //初始化插入的数据
    private void initDefaultData( SQLiteDatabase database){
        AccountBook accountBook = new AccountBook();
        String[] accountBookName = getContext().getResources().
                getStringArray(R.array.InitDefaultAccountBookName);
            accountBook.setAccountBookName(accountBookName[0]);
            accountBook.setIsDefault(1);
            ContentValues contentValues = createParms(accountBook);
            database.insert(getTableNameAndPK()[0],null,contentValues);
        }

}
