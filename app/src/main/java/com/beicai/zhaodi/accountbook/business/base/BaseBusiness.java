package com.beicai.zhaodi.accountbook.business.base;

import android.content.Context;

/**
 * Created by Administrator on 2016/12/14 0014.
 */
public class BaseBusiness {
    public Context context;
    public BaseBusiness(Context context) {
        this.context = context;
    }
    public String getNotHideRootCategory1(){
        String condition = " and state = 1 and parentId = 0";
        return condition;
    }
    public String getNotHideCountByParentId1(int id ){
        String condition = " and state = 1 and parentId ="+id;
        return condition;
    }

    public String getNotHideCategoryListByParentId1(int  id ){
        String condition = " and state = 1 and  parentId ="+id;
        return condition;
    }
    public String getAllNotHideCategory(){
        String condition = " and state = 1";
        return condition;
    }


}
