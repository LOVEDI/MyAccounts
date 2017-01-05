package com.beicai.zhaodi.accountbook.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.beicai.zhaodi.accountbook.R;
import com.beicai.zhaodi.accountbook.database.SQLiteDaoBase;
import com.beicai.zhaodi.accountbook.entity.Category;
import com.beicai.zhaodi.accountbook.utils.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/12/14 0014.
 */
public class CategoryDAO extends SQLiteDaoBase{
    public CategoryDAO(Context context) {
        super(context);
    }

    @Override
    protected String[] getTableNameAndPK() {
        return new String[]{"category","categoryId"};
    }

    @Override
    protected Object findModel(Cursor cursor) {
        Category category = new Category();
        category.setCategoryId(cursor.getInt(cursor.getColumnIndex("categoryId")));
        category.setCategoryName(cursor.getString(cursor.getColumnIndex("categoryName")));
        Date createDate = DateUtil.getDate(cursor.getString(cursor.getColumnIndex("createDate")),
                "yyyy-MM-dd HH:mm:ss");
        category.setCreateDate(createDate);
        category.setParentId(cursor.getInt(cursor.getColumnIndex("parentId")));
        category.setPath(cursor.getString(cursor.getColumnIndex("path")));
        category.setState(cursor.getInt(cursor.getColumnIndex("state")));
        return  category;
    }
//创建数据库 表
    @Override
    public void onCreate(SQLiteDatabase database) {
        StringBuilder sql = new StringBuilder();
        Log.e("TAG", "这是创建数据库表的方法");
        sql.append("  Create TABLE [category](");
        sql.append("  [categoryId] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
        sql.append("  ,[parentId] int NOT NULL");
        sql.append("  ,[categoryName] text NOT NULL");
        sql.append("  ,[path] varchar(20) NOT NULL");
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
    public boolean insertCategory(Category category){
        ContentValues contentValues = createParms(category);
        //返回插入条数的id
        long newid = getDatabase().insert(getTableNameAndPK()[0], null, contentValues);
        category.setCategoryId((int) newid);
        return newid>0;
    }
    public ContentValues createParms(Category info){
        ContentValues contentValues = new ContentValues();
        contentValues.put("categoryName",info.getCategoryName());
        //日期转换的工具类
        contentValues.put("createDate", DateUtil.getFormatDateTime(info.getCreateDate(), "yyyy-mm-dd HH:mm:ss"));
        contentValues.put("state",info.getState());
        contentValues.put("path",info.getPath());
        contentValues.put("parentId",info.getParentId());
        Log.e("TAG", "在categoryDao类中出现的数据" + contentValues.toString());
        return contentValues;
    }
    //删除
    public boolean deleteCategory(String  condition){
        return delete(getTableNameAndPK()[0], condition);
    }
    //修改
    public boolean updateCategory(String condition,Category category){
        ContentValues contentValues = createParms(category);
        Log.e("TAG","contentValuses"+contentValues.toString());
        return updateCategory(condition, contentValues);
    }
    //这个也是修改,修改单个
    public boolean updateCategory(String condition,ContentValues contentValues){
        Log.e("TAG","contentvalues"+contentValues+"3333333333333condition"+condition);
        // 2、要修改的数据 ， 3、条件    4、向里面填的值
        return getDatabase().update(getTableNameAndPK()[0],
                contentValues,condition,null)>0;
    }
    //查看
    public List<Category> getCategory(String condition){
        String sql = "select * from  category where 1=1 "+condition;
        return getList(sql);
    }
    //查看单个实体
    public String getCategoryy(String condition){
        String sql = "select * from  category where 1=1 "+condition;
        Cursor cursor = getDatabase().rawQuery(sql, null);
        String name = null;
        if(cursor!=null) {
            while (cursor.moveToNext()){
                name=  cursor.getString(cursor.getColumnIndex("categoryName"));

            }
        }

        return  name;
    }
    //获取数量
    public int getChildCount(String condition){
        List<Category> category = getCategory(condition);
        return category.size();
    }
    //初始化插入的数据
    private void initDefaultData( SQLiteDatabase database){
        Category category = new Category();
        category.setPath("");
        category.setParentId(0);

        String[] categoryName = getContext().getResources().
                getStringArray(R.array.InitDefaultCategoryName);
        for(int i = 0; i < categoryName.length; i++) {
            category.setCategoryName(categoryName[i]);
            ContentValues contentValues = createParms(category);
            long newid = database.insert(getTableNameAndPK()[0], null, contentValues);
            category.setPath(newid+".");
            contentValues = createParms(category);
            //用占位符 来 防止出现sql注入
            database.update(getTableNameAndPK()[0],contentValues,"categoryId=?",
                    new String[]{newid+""});
        }

        }

}
