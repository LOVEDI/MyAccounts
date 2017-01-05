package com.beicai.zhaodi.accountbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beicai.zhaodi.accountbook.R;
import com.beicai.zhaodi.accountbook.adapter.base.SimpleBaseAdapter;
import com.beicai.zhaodi.accountbook.business.UserBusiness;
import com.beicai.zhaodi.accountbook.entity.Users;

import java.util.List;

/**
 * Created by Administrator on 2016/12/12 0012.
 */
public class UserAdapter extends SimpleBaseAdapter{
        int position ;
   private UserBusiness userBusiness;
    public UserAdapter(Context context) {
        super(context, null);
        userBusiness = new UserBusiness(context);
        setListFromBusiness();
    }
    public void updateList(){
        setListFromBusiness();
        updateDisplay();
    }
    private void setListFromBusiness(){
        List<Users> list = userBusiness .getNotHideUser();
        setList(list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        position = position;
        Holder holder;
        if(convertView==null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.users_list_item, null);
             holder = new Holder();
             holder.user_item_icon_iv = (ImageView) convertView.findViewById(R.id.user_item_icon_iv);
             holder.user_item_name_iv = (TextView) convertView.findViewById(R.id.user_item_name_tv);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        Users user  = (Users) dats.get(position);
        holder.user_item_icon_iv.setImageResource(R.drawable.grid_user);
        holder.user_item_name_iv.setText(user.getUserName());
        return convertView;
    }
    private class  Holder{
        ImageView user_item_icon_iv;
        TextView user_item_name_iv;
    }
    public Object getList(int position){
        return  getItem(position);
    }
}
