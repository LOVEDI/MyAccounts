package com.beicai.zhaodi.accountbook.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beicai.zhaodi.accountbook.R;
import com.beicai.zhaodi.accountbook.adapter.base.SimpleBaseAdapter;
import com.beicai.zhaodi.accountbook.business.PayoutBsiness;
import com.beicai.zhaodi.accountbook.business.UserBusiness;
import com.beicai.zhaodi.accountbook.entity.Payout;

import java.util.List;

/**
 * Created by Administrator on 2016/12/12 0012.
 */
public class PayoutAdapter extends SimpleBaseAdapter{
    private PayoutBsiness payoutBsiness;
    private UserBusiness userBusiness;
    private int accountBookId;
    public PayoutAdapter(Context context,int accountBookId) {
        super(context, null);
        payoutBsiness = new PayoutBsiness(context);
        userBusiness = new UserBusiness(context);
        this.accountBookId = accountBookId;
        setListFromBusiness();
    }
    public void updateList(){
        setListFromBusiness();
        updateDisplay();
    }
    private void setListFromBusiness(){
        //账本的id查询消费记录，注意排序  相同日期放在一起  order by
        List<Payout> list = payoutBsiness .getPayoutListByAccountBookId(accountBookId);
        //Log.e("TAG", "PayoutAdapter中 的list ==" + list.get(0).getAmount());
        setList(list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        position = position;
        Holder holder;
        if(convertView==null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.post_list_item, null);
            holder = new Holder();
            holder.payout_item_icon_iv = (ImageView) convertView.findViewById(R.id.payout_item_icon_iv);
            holder.payout_item_name_tv = (TextView) convertView.findViewById(R.id.payout_item_name_tv);
            holder.payout_item_amount_tv = (TextView) convertView.findViewById(R.id.payout_item_amount_tv);
            holder.payout_item_user_and_type_tv = (TextView) convertView.findViewById(R.id.payout_item_user_and_type_tv);
            holder.payout_item_date_rl = convertView.findViewById(R.id.payout_item_date_rl);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        Payout payout = (Payout) getItem(position);
        String payoutDate = payout.getPayoutDate();
        boolean isShow = false;
        if(position>0) {
            //获取上一个实体
            Payout payoutLast = (Payout) getItem(position-1);
            //获取上一个实体的日期
         //   String payoutDateLast = DateUtil.getFormatDateTime(payoutLast.getPayoutDate(),"yyyy-MM-dd");
            String payoutDateLast = payoutLast.getPayoutDate();
            //如果当前日期与上一个日期不等就显示出来
            Log.e("TAG", "各个时间是多少？？？"+payoutLast.getPayoutDate());
            isShow = !payoutDate.equals(payoutDateLast);
        }
        //一条语局出两个结果  模糊查询 sum count
        List tltle = payoutBsiness.getPayoutTotalMessage(payoutDate, accountBookId);
        //设置蓝条上的文字
        ((TextView)holder.payout_item_date_rl.findViewById(R.id.payout_item_date_tv))
                .setText(payoutDate);
        ((TextView)holder.payout_item_date_rl.findViewById(R.id.payout_item_total_tv))
                .setText("共"+tltle.get(1)+"笔，一共消费"+tltle.get(0)+"元");
        holder.payout_item_icon_iv.setImageResource(R.drawable.grid_payout);

        holder.payout_item_amount_tv.setText(context.getString(R.string.textview_text_payout_amount,
                new Object[]{payout.getAmount().toString()}));
        String userName = userBusiness.getUserNameByUserId(payout.getPayoutUserId());
        holder.payout_item_name_tv.setText(userName);
        holder.payout_item_user_and_type_tv.setText(payout.getPayoutType());
        if(isShow||position==0) {
            holder.payout_item_date_rl.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
    private class  Holder{
        ImageView payout_item_icon_iv;
        TextView payout_item_name_tv,payout_item_amount_tv,payout_item_user_and_type_tv;
        View payout_item_date_rl;
    }
    public Object getList(int position){
        return  getItem(position);
    }
}
