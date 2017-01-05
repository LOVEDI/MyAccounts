package com.beicai.zhaodi.accountbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.beicai.zhaodi.accountbook.R;
import com.beicai.zhaodi.accountbook.activity.base.FrameActivity;
import com.beicai.zhaodi.accountbook.adapter.AppGridAdapter;
import com.beicai.zhaodi.accountbook.business.DataBackupBusiness;
import com.beicai.zhaodi.accountbook.service.ServiceDatabaseBackup;
import com.beicai.zhaodi.accountbook.view.SlideMenuItem;
import com.beicai.zhaodi.accountbook.view.SlideMenuView;

import java.util.Date;

/**
 * Created by Administrator on 2016/12/12 0012.
 */
public class MainActivity extends FrameActivity implements SlideMenuView.OnSlideMenuListener{
    private GridView main_body_gv;
    private AppGridAdapter gridAdapter;
    private DataBackupBusiness dataBackupBusiness;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appendMainBody(R.layout.main_boday);
        //隐藏按钮
        hideTitleBackBtn();
        initVariable();
        initView();
        initLinsteners();
        initData();
        //调创建菜单的方法
        createSlideMenu(R.array.SlideMenuActivityMain);
        //开区服务
        startMayService();
    }

    private void startMayService() {
        Intent intent = new Intent(this,ServiceDatabaseBackup.class);
        startService(intent);

    }

    //初始化数据
    private void initData() {
        main_body_gv.setAdapter(gridAdapter);
    }

    private void initLinsteners() {
        main_body_gv.setOnItemClickListener(new OnGridItemClickListener());
    }
   //初始化控件
    private void initView() {
        main_body_gv = (GridView)findViewById(R.id.main_body_gv);
    }
    //初始化变量
    private void initVariable() {
        dataBackupBusiness = new DataBackupBusiness(this);
        gridAdapter = new AppGridAdapter(this);

    }

    @Override
    public void onslideMenuItemClick(SlideMenuItem item) {
        if(item.getItemId() ==0) {
            //数据备份
            databaseBackup();
        }if(item.getItemId()==1) {
            //数据还原
            databaseRestore();
        }
    }

    private void databaseRestore() {
        if(dataBackupBusiness.databaseRestore()) {
            showMsg(getString(R.string.dialog_message_restore_sesscess));
        }else{
            showMsg(getString(R.string.dialog_message_restore_file));
        }
    }

    private void databaseBackup() {
        if(dataBackupBusiness.databaseBackup(new Date())) {
            showMsg(getString(R.string.dialog_message_backup_sesscess));
        }else{
             showMsg(getString(R.string.dialog_message_backup_false));
        }

    }

    private class OnGridItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String menuName = (String) parent.getAdapter().getItem(position);
            if(menuName.equals(getString(R.string.grid_payout_people))) {
                openActivity(UserActivity.class);
                return;
            }if(menuName.equals(getString(R.string.grid_account_manage))) {
                openActivity(AccountBookActivity.class);
                return;
            }if(menuName.equals(getString(R.string.grid_payout_type))) {
                openActivity(CategoryActivity.class);
                return;
            }if(menuName.equals(getString(R.string.grid_payout_add))) {
                openActivity(PayoutAddOrEditActivity.class);
                return;
            }
            if(menuName.equals(getString(R.string.grid_payout_manage))) {
                openActivity(PayoutActivity.class);
                return;
            }if(menuName.equals(getString(R.string.grid_payout_sum))) {
                openActivity(StatisticsActivity.class);
                return;
            }

        }
    }
}
