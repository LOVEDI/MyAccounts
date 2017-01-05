package com.beicai.zhaodi.accountbook.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/22 0022.
 */
public class Payout implements Serializable{
    //主键
    private int payoutId;
    //账的id的外键
    private int accountBookId;
    //账本 的名称
    private String accountBookName;
    //类别的id的外键  用于 保存
    private  int categoryId;
    //类别的名称 用于显示

    public void setPayoutId(int payoutId) {
        this.payoutId = payoutId;
    }

    public void setAccountBookId(int accountBookId) {
        this.accountBookId = accountBookId;
    }

    public void setAccountBookName(String accountBookName) {
        this.accountBookName = accountBookName;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setPayoutDate(String payoutDate) {
        this.payoutDate = payoutDate;
    }

    public void setPayoutType(String payoutType) {
        this.payoutType = payoutType;
    }

    public void setPayoutUserId(String payoutUserId) {
        this.payoutUserId = payoutUserId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCategoryName() {

        return categoryName;
    }

    public int getPayoutId() {
        return payoutId;
    }

    public int getState() {
        return state;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getComment() {
        return comment;
    }

    public String getPayoutUserId() {
        return payoutUserId;
    }

    public String getPayoutType() {
        return payoutType;
    }

    public String getPayoutDate() {
        return payoutDate;
    }

    public String getPath() {
        return path;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getAccountBookName() {
        return accountBookName;
    }

    public int getAccountBookId() {
        return accountBookId;
    }

    private String categoryName;
    //类别路径
    private  String path ;
    //消费金额(主要用户商业计算)((BigDecimal)超过十六位运算的数 进行运算)
    private BigDecimal amount;
    //消费日期
    private String payoutDate;
    //计算方式
    private String payoutType;
    //消费人  id 外键
    private String payoutUserId;
    //备注
    private String comment;
    //添加日期
    private Date createDate = new Date();
    //状态 0 失效 1 启用 默认启用
    private  int state =1;

}
