package com.beicai.zhaodi.accountbook.entity;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/12/28 0028.
 */
public class Statistics {

    public String payerUserId;//付款人
    public String consumerUserId;
    public String payoutType;
    public BigDecimal cast;

    @Override
    public String toString() {
        return "Statistics{" +
                "payerUserId='" + payerUserId + '\'' +
                ", consumerUserId='" + consumerUserId + '\'' +
                ", payoutType='" + payoutType + '\'' +
                ", cast=" + cast +
                '}';
    }
}
