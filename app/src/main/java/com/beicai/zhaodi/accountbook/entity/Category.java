package com.beicai.zhaodi.accountbook.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *用户
 */
public class Category implements Serializable{

    //主键嗯
    private int categoryId;
    //用户名
    private String categoryName;
    //服类型的id
    private int parentId;
    //路径
    private String path;
    //添加日期
    private Date createDate = new Date();

    public Category() {
    }

    public Category(int categoryId, String categoryName) {

        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    //状态 0 ;失效 1 启用 默认启用
    private int state = 1;
    public void setPath(String path) {
        this.path = path;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getParentId() {
        return parentId;
    }

    public String getPath() {
        return path;
    }


    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setState(int state) {
        this.state = state;
    }



    public int getCategoryId() {
        return categoryId;
    }

    @Override
    public String toString() {
        return categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public int getState() {
        return state;
    }
}
