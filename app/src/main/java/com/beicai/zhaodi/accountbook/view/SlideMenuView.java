package com.beicai.zhaodi.accountbook.view;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.beicai.zhaodi.accountbook.R;
import com.beicai.zhaodi.accountbook.adapter.SlideMenuAdapter;
import com.beicai.zhaodi.accountbook.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/13 0013.
 */
public class SlideMenuView {
    private Activity activity;
    private List<SlideMenuItem> menuList;
    private boolean isClosed;
    private RelativeLayout bottomBoxLayout;
    //声明监听器的接口
    private  OnSlideMenuListener onSlideMenuListener;

    public SlideMenuView(Activity activity){
        this.activity = activity;
        initView();
        if(activity instanceof OnSlideMenuListener) {
            this.onSlideMenuListener = (OnSlideMenuListener) activity;
        }
        initVariable();
        initListeners();
    }
//初始化变量
    private void initVariable() {
        menuList = new ArrayList<>();
        isClosed = true;
    }

    private void initView() {
        bottomBoxLayout = (RelativeLayout) activity.findViewById(R.id.include_bottom);
    }
//初始化监听
    private void initListeners() {
        bottomBoxLayout.setOnClickListener(new onSlideMenuClick());
        //能够获取焦点
        bottomBoxLayout.setFocusableInTouchMode(true);
        bottomBoxLayout.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode==KeyEvent.KEYCODE_MENU&& keyEvent.getAction()==KeyEvent.ACTION_DOWN){
                    toggle();
                }
                return false;
            }
        });
    }

    //监听的点击事件
    private class onSlideMenuClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //打开关闭菜单的方法
                toggle();
        }
    }
    //打开菜单
    private void open(){
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        //设置在**布局的下边
        layoutParams.addRule(RelativeLayout.BELOW,R.id.include_title);
        bottomBoxLayout.setLayoutParams(layoutParams);
        isClosed = false;
    }
    //关闭菜单
    public void close(){
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT , DisplayUtil.dip2px(activity, 68)
        );
        //设置在**布局的下边 addRule()；天假规则，约束
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        bottomBoxLayout.setLayoutParams(layoutParams);
        isClosed = true;
    }
    //开关方法控制打开关闭
    public   void toggle(){
        if(isClosed) {
            open();
        }else {
            close();
        }

    }
    //添加菜单项
    public void add(SlideMenuItem slideMenuItem){
        menuList.add(slideMenuItem);
    }

    //绑定数据源
    public   void bindList(){
        SlideMenuAdapter adapter = new SlideMenuAdapter(activity,menuList);
        ListView listView = (ListView) activity.findViewById(R.id.slide_list_lv);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnslideMenuItemClick());
    }
    //长按监听 应该是吧
    public class OnslideMenuItemClick implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SlideMenuItem item = (SlideMenuItem) parent.getItemAtPosition(position);
                onSlideMenuListener.onslideMenuItemClick(item);
        }
    }
    //声明一个借口   ,
    public  interface OnSlideMenuListener{
        void onslideMenuItemClick(SlideMenuItem item);
    }
    //删除菜单
    public void removeBottomBox(){
        RelativeLayout maind_rl = (RelativeLayout) activity.findViewById(R.id.maind_rl);
        maind_rl.removeView(bottomBoxLayout);
        bottomBoxLayout = null;
    }


}
