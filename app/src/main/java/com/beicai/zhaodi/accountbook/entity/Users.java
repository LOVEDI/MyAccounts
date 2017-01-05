package com.beicai.zhaodi.accountbook.entity;

import java.util.Date;

/**
 *用户
 */
public class Users {

    //主键嗯
    private int userId;
    //用户名
    private String userName;
    //添加日期
    private Date createDate = new Date();
    //状态 0 ;失效 1 启用 默认启用
    private int state = 1;
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setState(int state) {
        this.state = state;
    }



    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public int getState() {
        return state;
    }



}
