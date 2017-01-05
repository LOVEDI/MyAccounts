package com.beicai.zhaodi.accountbook.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.beicai.zhaodi.accountbook.database.SQLiteHelper;

/**
 * Created by Administrator on 2016/12/22 0022.
 */
public class CreateViewDAO implements SQLiteHelper.SQLiteDataTable{

    private Context context;
    public CreateViewDAO (Context context){
        this.context = context;
    }
    //创建数据库 表

    @Override
    public void onCreate(SQLiteDatabase database) {
        StringBuilder sql = new StringBuilder();
        sql.append("Create  VIEW [v_payout] as");
        sql.append(" select p.*, c.parentId, c.categoryName, c.path, a.accountBookName");
        sql.append(" from payout p left join category c on p.categoryId=c.categoryId ");
        sql.append(" left join accountBook a on p.accountBookId=a.accountBookId ");
        //执行sql语句
        database.execSQL(sql.toString());
    }
    //版本升级
    @Override
    public void onUpgrade(SQLiteDatabase database) {

    }


}
