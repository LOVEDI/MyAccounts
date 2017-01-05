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
import com.beicai.zhaodi.accountbook.business.AccountBookBusiness;
import com.beicai.zhaodi.accountbook.business.PayoutBsiness;
import com.beicai.zhaodi.accountbook.entity.AccountBook;

import java.util.List;

/**
 * Created by Administrator on 2016/12/12 0012.
 */
public class AccountBookSelectAdapter extends SimpleBaseAdapter{
    private PayoutBsiness payoutBsiness;
   private AccountBookBusiness account_BookBusiness;
    public AccountBookSelectAdapter(Context context) {
        super(context, null);
        payoutBsiness = new PayoutBsiness(context);
        account_BookBusiness = new AccountBookBusiness(context);
        setListFromBusiness();
    }
    public void updateList(){
        setListFromBusiness();
        updateDisplay();
    }
    private void setListFromBusiness(){
        List<AccountBook> list = account_BookBusiness .getNotHideAccountBook();
        setList(list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        position = position;
        Holder holder;
        if(convertView==null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.account_book_select_list_item, null);
             holder = new Holder();
             holder.account_Book_item_icon_iv = (ImageView) convertView.findViewById(R.id.account_book_item_icon_iv);
             holder.account_Book_item_name_tv = (TextView) convertView.findViewById(R.id.account_book_item_name_tv);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        AccountBook account_Book  = (AccountBook) dats.get(position);
        Log.e("TAG", "在adapter类中 isDefault=="+account_Book.getIsDefault());
        if(account_Book.getIsDefault()==1) {
            holder.account_Book_item_icon_iv.setImageResource(R.drawable.account_book_default);
        }else {
            holder.account_Book_item_icon_iv.setImageResource(R.drawable.account_book_icon);
        }
        holder.account_Book_item_name_tv.setText(account_Book.getAccountBookName());
        return convertView;
    }
    private class  Holder{
        ImageView account_Book_item_icon_iv;
        TextView account_Book_item_name_tv;
    }
    public Object getList(int position){
        return  getItem(position);
    }
}
