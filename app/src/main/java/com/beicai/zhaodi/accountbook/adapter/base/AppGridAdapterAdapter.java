package com.beicai.zhaodi.accountbook.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beicai.zhaodi.accountbook.R;

/**
 * Created by Administrator on 2016/12/12 0012.
 */
public class AppGridAdapterAdapter extends BaseAdapter{
    private Context context;
    private Integer[] imaginteger={R.drawable.grid_payout,R.drawable.grid_bill,R.drawable.grid_report,
    R.drawable.grid_account_book,R.drawable.grid_category,R.drawable.grid_user};
    private String[] imgString = new String[6];
    public AppGridAdapterAdapter(Context context){
        this.context  = context;
        imgString[0] = context.getString(R.string.grid_payout_add);
        imgString[1] = context.getString(R.string.grid_payout_manage);
        imgString[2] = context.getString(R.string.grid_account_manage);
        imgString[3] = context.getString(R.string.grid_payout_sum);
        imgString[4] = context.getString(R.string.grid_payout_type);
        imgString[5] = context.getString(R.string.grid_payout_people);
    }
    @Override
    public int getCount() {
        return imgString.length;
    }

    @Override
    public Object getItem(int position) {
        return imgString[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView==null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.main_boday_item,null);
                    holder = new Holder();
            holder.icon_iv = (ImageView) convertView.findViewById(R.id.main_body_item_icon_iv);
                    holder.name_tv = (TextView) convertView.findViewById(R.id.main_body_item_name_tv);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        convertView.setBackgroundResource(R.drawable.grid_selector);
        holder.icon_iv.setImageResource(imaginteger[position]);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(80,80);
        holder.icon_iv.setLayoutParams(layoutParams);
        holder.icon_iv.setScaleType(ImageView.ScaleType.FIT_XY);
        holder.name_tv.setText(imgString[position]);
        return convertView;
    }
    private class  Holder{
        ImageView icon_iv;
        TextView name_tv;
    }
}
