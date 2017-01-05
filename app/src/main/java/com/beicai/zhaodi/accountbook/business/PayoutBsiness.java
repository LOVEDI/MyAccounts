package com.beicai.zhaodi.accountbook.business;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.beicai.zhaodi.accountbook.business.base.BaseBusiness;
import com.beicai.zhaodi.accountbook.database.dao.PayoutDAO;
import com.beicai.zhaodi.accountbook.entity.AccountBook;
import com.beicai.zhaodi.accountbook.entity.Payout;

import java.util.List;

/**
 * Created by Administrator on 2016/12/22 0022.
 */
public class PayoutBsiness extends BaseBusiness{
    private PayoutDAO payoutDAO;
    AccountBookBusiness accountBookBusiness;
    public PayoutBsiness(Context context) {
        super(context);
        payoutDAO = new PayoutDAO(context);
        accountBookBusiness = new AccountBookBusiness(context);
    }

    public boolean insertPayout(Payout payout) {
        return payoutDAO.insertPayout(payout);
    }

    public boolean updatePayout(Payout payout) {
        return payoutDAO.updatePayout("payoutId=" + payout.getPayoutId(), payout);
    }

    // TODO: 账本的id查询消费记录，注意排序  相同日期放在一起  order by
    public  List<Payout> getPayoutListByAccountBookId(int accountBookId) {
        String condition = " and accountBookId="+accountBookId+" and state = 1  order by payoutDate ";
        return payoutDAO.getPayout(condition);
    }

    // TODO:   一条语局出两个结果  模糊查询 sum count
    public List getPayoutTotalMessage(String payoutDate, int accountBookId) {
        Log.e("TAG", "在payoutAdapter中 getPayoutMessage:"+payoutDAO.getCountAndSum(payoutDate,accountBookId));
        return payoutDAO.getCountAndSum(payoutDate, accountBookId);
    }
    // TODO:   一条语局出两个结果  模糊查询 sum count
    public List getPayoutTotalMessage( int accountBookId) {
        Log.e("TAG", "在payoutAdapter中 getPayoutMessage:"+payoutDAO.getCountAndSum(accountBookId));
        return payoutDAO.getCountAndSum(accountBookId);
    }
        //根据消费记录Id进行逻辑删除
    public boolean deletePayoutByPayoutId(int payoutId) {
        String condition = " payoutId="+payoutId;
        ContentValues contentValues = new ContentValues();
        contentValues.put("state", 0);

        return  payoutDAO.updatePayout(condition, contentValues);
    }
 //获取所有的账本信息
    public List<AccountBook> getNotHintBookCount(){
        String position = "and state = 1";
        List<AccountBook> accountBooks = accountBookBusiness.getAccountBooks(position);
        return accountBooks;
    }

    public List<Payout> getpayoutOrderByPayoutUserId(String condition) {
        condition += " and state = 1 order by payoutUserId ";
        List<Payout> list = payoutDAO.getPayout(condition);
        if(list!=null &&list.size()>0) {
            return list;
        }
        return null;

    }
}
