package com.beicai.zhaodi.accountbook.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.beicai.zhaodi.accountbook.database.SQLiteDaoBase;
import com.beicai.zhaodi.accountbook.entity.Payout;
import com.beicai.zhaodi.accountbook.utils.DateUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/12/14 0014.
 */
public class PayoutDAO extends SQLiteDaoBase{
    String payoutDate;
    public PayoutDAO(Context context) {
        super(context);
    }

    @Override
    protected String[] getTableNameAndPK() {
        return new String[]{"payout","payoutId"};
    }

    @Override
    protected Object findModel(Cursor cursor) {
        Payout payout = new Payout();
        payout.setPayoutId(cursor.getInt(cursor.getColumnIndex("payoutId")));
        payout.setAccountBookId(cursor.getInt(cursor.getColumnIndex("accountBookId")));
        payout.setComment(cursor.getString(cursor.getColumnIndex("comment")));
        payout.setCategoryId(cursor.getInt(cursor.getColumnIndex("categoryId")));
        payout.setPayoutDate(cursor.getString(cursor.getColumnIndex("payoutDate")));
        payout.setPayoutType(cursor.getString(cursor.getColumnIndex("payoutType")));
        Log.e("TAG", "在payoutdao中取出来的type是：：：" + payout.getPayoutType());
        payout.setPayoutUserId(cursor.getString(cursor.getColumnIndex("payoutUserId")));
        Date createDate = DateUtil.getDate(cursor.getString(cursor.getColumnIndex("createDate")),
                "yyyy-MM-dd HH:mm:ss");
        payout.setCreateDate(createDate);
        payout.setAmount(new BigDecimal(cursor.getString(cursor.getColumnIndex("amount"))));
        payout.setState(cursor.getInt(cursor.getColumnIndex("state")));
        return  payout;
    }
//创建数据库 表
    @Override
    public void onCreate(SQLiteDatabase database) {
        StringBuilder sql = new StringBuilder();
        Log.e("TAG", "这是创建payoutDao数据库表的方法");
        sql.append("  Create TABLE [payout](");
        sql.append("  [payoutId] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
        sql.append("  ,[accountBookId] int NOT NULL");
        sql.append("  ,[categoryId] int NOT NULL");
        sql.append("  ,[amount] decimal NOT NULL");
        sql.append(" ,[payoutDate] varchar NOT NULL");
        sql.append("  ,[payoutUserId] text NOT NULL");
        sql.append(",[comment] varchar(20) NOT NULL");
        sql.append("  ,[payoutType] varchar(20) NOT NULL");
        sql.append(" ,[createDate] datatime NOT NULL");
        sql.append("  ,[state] int NOT NULL");
        sql.append(" )");
        database.execSQL(sql.toString());
    }
//版本升级
    @Override
    public void onUpgrade(SQLiteDatabase database) {

    }

    //增加
    public boolean insertPayout(Payout payout){
        ContentValues contentValues = createParms(payout);
        //返回插入条数的id
        long newid = getDatabase().insert(getTableNameAndPK()[0], null, contentValues);
        payout.setPayoutId((int) newid);
        return newid>0;
    }
    public ContentValues createParms(Payout info){
        Log.e("TAG","在PayoutDao中的comment是：==="+info.getComment());
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountBookId",info.getAccountBookId());
        contentValues.put("categoryId",info.getCategoryId());
        contentValues.put("amount",info.getAmount().toString());
        contentValues.put("comment",info.getComment().toString());
        //日期转换的工具类
        contentValues.put("payoutDate", info.getPayoutDate());
        contentValues.put("createDate", DateUtil.getFormatDateTime(info.getCreateDate(), "yyyy-mm-dd HH:mm:ss"));
        contentValues.put("state",info.getState());
        contentValues.put("payoutType",info.getPayoutType());
        contentValues.put("payoutUserId",info.getPayoutUserId());
        Log.e("TAG", "在payoutDao类中出现的数据" + contentValues.toString());
        return contentValues;
    }
    //删除
    public boolean deletePayout(String condition) {
        return delete(getTableNameAndPK()[0], condition);
    }
    //修改
    public boolean updatePayout(String condition,Payout payout){
        ContentValues contentValues = createParms(payout);
        Log.e("TAG","contentValuses"+contentValues.toString());
        return updatePayout(condition, contentValues);
    }
    //这个也是修改,修改单个
    public boolean updatePayout(String condition,ContentValues contentValues){
        Log.e("TAG","contentvalues"+contentValues+"3333333333333condition"+condition);
        // 2、要修改的数据 ， 3、条件    4、向里面填的值
        return getDatabase().update(getTableNameAndPK()[0],
                contentValues,condition,null)>0;
    }
    //查看
    public List<Payout> getPayout(String condition){
        String sql = "select * from  payout where 1=1 "+condition;
        return getList(sql);
    }
    //获取数量
    public int getChildCount(String condition){
        List<Payout> payout = getPayout(condition);
        return payout.size();
    }
    //模糊查询  数量和总和
    public  List getCountAndSum(String payoutDate, int accountBookId) {
        String sql = "select sum(amount),count(payoutId) from payout where 1=1 and state = 1 and accountBookId = "+accountBookId +" and payoutDate = '"+payoutDate+"'";
        this.payoutDate = payoutDate;
        Log.e("TAG", "在PayouttDAO中 sql语句是：" + sql);
        Cursor cursor = getDatabase().rawQuery(sql, null);
        List list = new ArrayList();
        Log.e("TAG", "在PayouttDAO中 list(0)：" + cursor.getCount());
        while (cursor.moveToNext()) {
            list.add(cursor.getString(0));
            list.add(cursor.getString(1));
        }
        Log.e("TAG", "list.get====" + list.get(1));
        return list;
    }
    //模糊查询  数量和总和
    public  List getCountAndSum( int accountBookId) {
        String sql = "select sum(amount),count(payoutId) from payout where 1=1 and state = 1 and accountBookId = "+accountBookId;
        Log.e("TAG", "在PayouttDAO中 sql语句是：" + sql);
        Cursor cursor = getDatabase().rawQuery(sql, null);
        List list = new ArrayList();
        Log.e("TAG", "在PayouttDAO中 list(0)：" + cursor.getCount());
        while (cursor.moveToNext()) {
            list.add(cursor.getString(0));
            list.add(cursor.getString(1));
        }
        Log.e("TAG", "list.get====" + list.get(1));
        return list;
    }
}