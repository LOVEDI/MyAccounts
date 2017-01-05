package com.beicai.zhaodi.accountbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beicai.zhaodi.accountbook.R;
import com.beicai.zhaodi.accountbook.adapter.base.SimpleBaseAdapter;
import com.beicai.zhaodi.accountbook.view.SlideMenuItem;

import java.util.List;

/**
 * Created by Administrator on 2016/12/12 0012.
 */
public class SlideMenuAdapter extends SimpleBaseAdapter{


    public SlideMenuAdapter(Context context, List dats) {
        super(context, dats);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView==null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.slide_menu_list_item, null);
             holder = new Holder();
             holder.slide_menu_list_item_tv = (TextView) convertView.findViewById(R.id.slide_menu_list_item_tv);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        SlideMenuItem item = (SlideMenuItem) dats.get(position);
        holder.slide_menu_list_item_tv.setText(item.getTitle());
        return convertView;
    }
    private class  Holder{
        TextView slide_menu_list_item_tv;
    }
}
