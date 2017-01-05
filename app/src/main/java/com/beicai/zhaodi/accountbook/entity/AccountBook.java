package com.beicai.zhaodi.accountbook.entity;

import java.util.Date;

/**
 *用户
 */
public class AccountBook {

    //主键嗯
    private int acountBookId;
    //用户名
    private String acountBookName;
    //添加日期

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public int getIsDefault() {
        return isDefault;
    }

    private Date createDate = new Date();
    //状态 0 ;失效 1 启用 默认启用
    private int state = 1;
    //是否为默认账本
    private int isDefault;
    public void setAccountBookId(int acountBookId) {
        this.acountBookId = acountBookId;
    }

    public void setAccountBookName(String acountBookName) {
        this.acountBookName = acountBookName;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setState(int state) {
        this.state = state;
    }



    public int getAccountBookId() {
        return acountBookId;
    }

    public String getAccountBookName() {
        return acountBookName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public int getState() {
        return state;
    }



}
