package com.beicai.zhaodi.accountbook.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.beicai.zhaodi.accountbook.R;
import com.beicai.zhaodi.accountbook.activity.base.FrameActivity;
import com.beicai.zhaodi.accountbook.adapter.AccountBookSelectAdapter;
import com.beicai.zhaodi.accountbook.business.AccountBookBusiness;
import com.beicai.zhaodi.accountbook.business.StaisticsBusiness;
import com.beicai.zhaodi.accountbook.entity.AccountBook;
import com.beicai.zhaodi.accountbook.view.SlideMenuItem;

import static com.beicai.zhaodi.accountbook.view.SlideMenuView.OnSlideMenuListener;

/**
 * Created by Administrator on 2016/12/12 0012.
 */
public class StatisticsActivity extends FrameActivity implements OnSlideMenuListener {
    public TextView statistics_result_tv;
    public StaisticsBusiness staisticsBusiness;
    private AccountBookBusiness accountbookBusiness;
    private AccountBook accountBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(R.layout.statistics);
        initVariable();
        initView();
        initLinsteners();
        initData();
        setTitle();
        //调创建菜单的方法
        createSlideMenu(R.array.SlideMenuStatistics);
    }

    //初始化变量
    private void initVariable() {
        staisticsBusiness = new StaisticsBusiness(this);
        accountbookBusiness = new AccountBookBusiness(this);
        accountBook = accountbookBusiness.getDefaultAccountBook();

    }

    //初始化数据
    public void initData() {
        showProgressDdialog(R.string.dialog_title_statistics_progress, R.string.dialog_waiting_statics_progress);
        new BinDateThread().start();
    }

    @Override
    public void onslideMenuItemClick(SlideMenuItem item) {
        //关闭菜单显示对话框
        slideMenuToggle();
        if (item.getItemId() == 0) {//切换账本
            showAccountBookSelectDialog();
        }
        if (item.getItemId() == 1) {//导出表格
            exportData();
        }
    }

    private void exportData() {
        String result = "";
        try {
            result = staisticsBusiness.exportStatistics(accountBook.getAccountBookId());
        } catch (Exception e) {
            e.printStackTrace();
            //导出失败
            result = getString(R.string.export_data_fail);
        }
        //提示用户
        showMsg(result);
    }
    private void showAccountBookSelectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getInflater().inflate(R.layout.dialog_list, null);
        ListView select_lv = (ListView) view.findViewById(R.id.select_lv);
        AccountBookSelectAdapter accountBookSelectAdapter = new AccountBookSelectAdapter(this);
        select_lv.setAdapter(accountBookSelectAdapter);

        builder.setTitle(R.string.button_text_select_account_book)
                .setNegativeButton(R.string.button_text_back, null)
                .setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        select_lv.setOnItemClickListener(new OnAccountBookItemClickListener(dialog));
    }
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    String result = (String) msg.obj;
                    statistics_result_tv.setText(result);
                    dismissProgressDialog();
                    break;

            }
        }
    };
    private class BinDateThread extends Thread {
        public void run() {
            String result = staisticsBusiness.getPayoutUserIdByAccountBookId(accountBook.getAccountBookId());
            Message msg = handler.obtainMessage();
            msg.obj = result;
            msg.what = 1;
            handler.sendMessage(msg);
        }
    }

    private void setTitle(){
       setTopBarTitle(getString(R.string.title_account_book,new Object[]{accountBook.getAccountBookName()}));
    }

    private void initView() {
        statistics_result_tv = (TextView) findViewById(R.id.statistics_result_tv);
    }
    private void initLinsteners() {

    }

    private class OnAccountBookItemClickListener implements AdapterView.OnItemClickListener {
        private AlertDialog dialog;

        public OnAccountBookItemClickListener(AlertDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            accountBook = (AccountBook) adapterView.getAdapter().getItem(i);
            initData();
            dialog.dismiss();
        }
    }

}
