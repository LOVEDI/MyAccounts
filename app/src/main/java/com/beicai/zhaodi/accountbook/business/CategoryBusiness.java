package com.beicai.zhaodi.accountbook.business;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.beicai.zhaodi.accountbook.R;
import com.beicai.zhaodi.accountbook.business.base.BaseBusiness;
import com.beicai.zhaodi.accountbook.database.dao.CategoryDAO;
import com.beicai.zhaodi.accountbook.entity.Category;
import com.beicai.zhaodi.accountbook.entity.CategoryTotal;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务层
 */
public class CategoryBusiness extends BaseBusiness {
    private CategoryDAO categoryDAO;
    List<Category> list;
    public CategoryBusiness(Context context) {
        super(context);
        categoryDAO = new CategoryDAO(context);
    }
    //通过id 找到 类别信息
    public String getNotHideRootCategoryById(int id ){
        String condition = "and categoryId = "+id;
        String name = categoryDAO.getCategoryy(condition);
        return name;
    }
        //获取所有根节点的类别
    public List getNotHideRootCategory() {
        List<Category> categoryDAOCategory = categoryDAO.getCategory(getNotHideRootCategory1());
        return categoryDAOCategory;
    }
    ////获取未隐藏的数量  （包括子类和主类的总数）
    public int getNotHideCount() {
            return getNotHideRootCategory().size();
    }
            //获得所有为隐藏的子类的数量
    public int getNotHideCountByParentId(int categoryId) {
        int childCount = categoryDAO.getChildCount(getNotHideCountByParentId1(categoryId));
        return childCount;
    }
        //通过id 找到  子类的集合
    public List<Category> getNotHideCategoryListByParentId(int categoryId) {
        List<Category> category = categoryDAO.getCategory(getNotHideCategoryListByParentId1(categoryId));
        return category;
    }
    public ArrayAdapter<Category> getRootCategoryArrayAdapter(){
        List<Category>   list = getNotHideRootCategory();
        //设置请选择
        list.add(0, new Category(0, context.getString(R.string.spinner_places_choose)));
        ArrayAdapter<Category> arrayAdapter = new ArrayAdapter<Category>(context,R.layout.simple_spinner_item,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         return  arrayAdapter;
    }
    //插入类别
    public boolean insertCategory(Category category){
        categoryDAO.beginTransaction();
        try {
            boolean result = categoryDAO.insertCategory(category);
            Log.e("TAG", "在CategoryBusiness中的result==="+result);
            boolean result2 = true;
            Category parentCategory = getCategoryByCategoryId(category.getParentId());
            Log.e("TAG","在CategoryBusiness中的parentCategory==="+parentCategory);
            String path;
            if(parentCategory!=null) {
                //福类别的路径—+当前类别的路径
                path = parentCategory.getPath()+category.getCategoryId()+".";
            }else {
                path = category.getCategoryId()+".";
            }
            category.setPath(path);
            result2 = updateCategory(category);
            if(result&&result2) {
                categoryDAO.setTransactionSuccessful();;
                return  true;
            }else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            categoryDAO.endTransaction();
        }
    }
//根据自雷获取父类
    private Category getCategoryByCategoryId(int parentId) {
        List<Category> list = categoryDAO.getCategory(" and parentId=" + parentId + " and state=1");
        return list.get(0);
    }
    public boolean editCategory(Category category) {
        String condition = " categoryId=" + category.getCategoryId();
        boolean result = categoryDAO.updateCategory(condition, category);
        return result;
    }
    public boolean updateCategory(Category category) {
        categoryDAO.beginTransaction();
        try {
            boolean result = editCategory(category);
            boolean result2 = true;
            //根据类别的Id获取类别，父类别
            Category parentCategory = getCategoryByCategoryId(category.getParentId());
            String path;
            if (parentCategory != null) {
                path = parentCategory.getPath() + category.getCategoryId() + ".";
            } else {
                path = category.getCategoryId() + ".";
            }
            category.setPath(path);
            result2 = editCategory(category);
            Log.e("TAG","Category中的CategoryBusiness的 result==="+result2);
            if (result && result2) {
                categoryDAO.setTransactionSuccessful();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.e("TAG","跑的异常是："+e.getMessage());
            e.printStackTrace();
            // : 在这里其实跑啦异常
            return false;
        } finally {
            categoryDAO.endTransaction();
        }
    }
    public boolean hideCategoryByPath(String path){
        String condition = " path like '"+path +"%'";
        ContentValues contentValues = new ContentValues();
        contentValues.put("state", 0);
        boolean result = categoryDAO.updateCategory(condition,contentValues);
        return result;
    }
    public ArrayAdapter<Category> getAllCategoryArrayAdapter() {
        List<Category> list = getNotHideRootCategory();
        ArrayAdapter<Category> arrayAdapter = new ArrayAdapter<Category>(context, R.layout.simple_spinner_item,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return arrayAdapter;
    }
        //逻辑删除
    public boolean hideCategoryByCategoryId(int categoryId) {
        String condition = " categoryId = "+categoryId;
        ContentValues contentValues = new ContentValues();
        contentValues.put("state", 0);
        boolean result = categoryDAO.updateCategory(condition,contentValues);
        return result;
    }
    //获得所有未隐藏的数据
    public List<Category> getNotHintCategory(){
        List<Category> categoryArrays = categoryDAO.getCategory(getAllNotHideCategory());
        return categoryArrays;
    }

    public List<CategoryTotal> getCategoryTotalByParentId(int parentId) {
        String condition=" and parentId="+parentId+" and state=1";
        return getCategoryTotal(condition);
    }
    private List<CategoryTotal> getCategoryTotal(String condition) {
        String sql="select count(payoutId) as count,sum(amount) as sumAmount," +
                "categoryName from v_payout where 1=1 "+condition+" group by categoryId";
        Cursor cursor=categoryDAO.execSql(sql);
        List<CategoryTotal> list=new ArrayList<>();
        while(cursor.moveToNext()) {
            CategoryTotal categoryTotal=new CategoryTotal();
            categoryTotal.count=cursor.getString(cursor.getColumnIndex("count"));
            categoryTotal.sumAmount=cursor.getString(cursor.getColumnIndex("sumAmount"));
            categoryTotal.categoryName=cursor.getString(cursor.getColumnIndex("categoryName"));
            list.add(categoryTotal);
        }
        return list;
    }

    public List<CategoryTotal> categoryTotalByRootCategory() {
        String condition=" and parentId=0 and state=1";
        return getCategoryTotal(condition);
    }
}
