package com.beicai.zhaodi.accountbook.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/12/13 0013.
 */
public abstract class  SimpleBaseAdapter extends BaseAdapter{
    protected Context context = null;
    protected List dats = null;
    protected LayoutInflater layoutInflater;
    public SimpleBaseAdapter(Context context,List dats){
        this.context  = context;
        this.dats = dats;
        layoutInflater = LayoutInflater.from(context);
    }

    public void clear(){
        dats.clear();
    }
    public void updateDisplay(){
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dats!= null ? dats.size(): 0;
    }

    @Override
    public Object getItem(int position) {
        return dats!= null ? dats.get(position): null
                ;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public abstract View getView(int position, View convertView, ViewGroup parent) ;

    //设置数据源
    public void setList(List list){
        dats = list;
    }
}
