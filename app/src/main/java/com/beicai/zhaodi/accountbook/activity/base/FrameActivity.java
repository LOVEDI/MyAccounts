package com.beicai.zhaodi.accountbook.activity.base;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beicai.zhaodi.accountbook.R;
import com.beicai.zhaodi.accountbook.view.SlideMenuItem;
import com.beicai.zhaodi.accountbook.view.SlideMenuView;

/**
 * Created by Administrator on 2016/12/12 0012.
 * 跟业务有关的
 */
public class FrameActivity extends BaseActivity{
        private SlideMenuView slideMenuView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        View view = findViewById(R.id.app_back);
        view.setOnClickListener(new OnBackListener());
    }
    private class OnBackListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            finish();
        }
    }


    //主页面隐藏按钮
    protected void hideTitleBackBtn(){
        findViewById(R.id.app_back).setVisibility(View.GONE);
    }
    //动态添加布局
    protected void appendMainBody(int resId){
        View view = LayoutInflater.from(this).inflate(resId,null);
        appendMainBody(view);

    }

    //创建滑动菜单
    protected void createSlideMenu(int resId){
         slideMenuView = new SlideMenuView(this);
        String[] munuItemArray = getResources().getStringArray(resId);
        for (int i = 0 ; i<munuItemArray.length;i++){
            SlideMenuItem item = new SlideMenuItem(i,munuItemArray[i]);
            slideMenuView.add(item);
        }
        //绑定数据
            slideMenuView.bindList();;
    }
    //切换菜单开闭
    protected void slideMenuToggle(){
        slideMenuView.toggle();
    }
//创建上下文菜单
    protected void createContextMenu(Menu menu){
            menu.add(0,1,0, R.string.menu_text_edit);
            menu.add(0, 2, 0, R.string.menu_text_delete);

    }
    protected void removeBottomBox(){
        slideMenuView = new SlideMenuView(this);
        Log.e("TAG", " 菜单创建成共");
        slideMenuView.removeBottomBox();
    }
    protected void setTopBarTitle(String title){
        TextView top_title_tv = (TextView) findViewById(R.id.top_title_tv);
        top_title_tv.setText(title);
    }
    //动态添加布局(重载)
    protected void appendMainBody(View view){
        //找到中间显示GridView的布局
        LinearLayout mainBody = (LinearLayout) findViewById(R.id.main_body_ll);
        //设置宽和高填充父控件
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        //给main_body_ll设置填充View
        mainBody.addView(view, layoutParams);
    }

}
