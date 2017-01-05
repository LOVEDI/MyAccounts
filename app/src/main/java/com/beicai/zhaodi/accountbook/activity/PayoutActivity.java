package com.beicai.zhaodi.accountbook.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.beicai.zhaodi.accountbook.R;
import com.beicai.zhaodi.accountbook.activity.base.FrameActivity;
import com.beicai.zhaodi.accountbook.adapter.AccountBookAdapter;
import com.beicai.zhaodi.accountbook.adapter.PayoutAdapter;
import com.beicai.zhaodi.accountbook.business.AccountBookBusiness;
import com.beicai.zhaodi.accountbook.business.PayoutBsiness;
import com.beicai.zhaodi.accountbook.entity.AccountBook;
import com.beicai.zhaodi.accountbook.entity.Payout;
import com.beicai.zhaodi.accountbook.view.SlideMenuItem;

import java.util.List;

import static com.beicai.zhaodi.accountbook.view.SlideMenuView.OnSlideMenuListener;

/**
 * Created by Administrator on 2016/12/12 0012.
 */
public class PayoutActivity extends FrameActivity implements OnSlideMenuListener{
    private PayoutBsiness payoutBsiness;
    private Payout payout;
    private AccountBookBusiness accountBookBusiness;
    AccountBook defaultAccountBook;
    PayoutAdapter payoutAdapter;
    ListView payout_list_lv;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(R.layout.payout_list);
        context = this;
        initVariable();
        initView();
        initLinsteners();
        initData(defaultAccountBook.getAccountBookId());
        //调创建菜单的方法
        createSlideMenu(R.array.SlideMenuPayout);
    }
    //初始化变量
    private void initVariable() {
        accountBookBusiness = new AccountBookBusiness(this);
        payoutBsiness = new PayoutBsiness(this);
        payout = new Payout();
        //获取默认账本
         defaultAccountBook = accountBookBusiness.getDefaultAccountBook();
        // TODO: 2016/12/26 0026    //初始化变量，获取默认账本

    }
   //初始化数据
    public void initData(int id ) {
        //根基账本的id 来获得 账本 payout的信息
        if(getIntent()!=null) {
            int  accountBookIdd = getIntent().getIntExtra("accountBookId",id);
            id = accountBookIdd;
        }
        // TODO: 2016/12/26 0026   //绑定Adapter，设置标题
        payoutAdapter = new PayoutAdapter(this,id);
        payout_list_lv.setAdapter(payoutAdapter);
        setTitle(defaultAccountBook);
    }
    private void setTitle(AccountBook accountBook){
        //// TODO: 替换多条  第二个参数是数量
        String stringFormat = "查询消费-%s(%s)";
        setTopBarTitle(String.format(stringFormat, accountBook.getAccountBookName(), payoutAdapter.getCount()));
    }
    private void initView() {
        // TODO: 2016/12/26 0026   //初始化控件
        payout_list_lv = (ListView)findViewById(R.id.payout_list_lv);
    }
    private void initLinsteners() {
            //注册上下文带单
        registerForContextMenu(payout_list_lv);
    }
//上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //得到菜单信息  ,取出消费记录 设置菜单图标和标题 创建菜单
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        ListAdapter listAdapter = payout_list_lv.getAdapter();
         payout = (Payout) listAdapter.getItem(acmi.position);
        //通过id找到账本
        String condition = " and accountBookId = "+payout.getAccountBookId();
        payout.setAccountBookName(accountBookBusiness.getAccountBook(condition).getAccountBookName());
     //   Log.e("TAG","在PayoutActivityz中    大大大大payout==="+payout.getAmount());
        super.onCreateContextMenu(menu, v, menuInfo);
        //通过id找到那么
        //设置标题
        menu.setHeaderTitle(payout.getAccountBookName());
        //设置图片
        menu.setHeaderIcon(R.drawable.payout_small_icon);
        //父类的方法     给menu项设置 id
        createContextMenu(menu);

// TODO: 2016/12/26 0026  
    }
    //菜单项的点击
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case 1:
                // TODO: 2016/12/26 0026
                //修改或添加
              //以返回结果的方式打开 PayoutAddOrEditActivity,要刷新适配器
                searchPayoutAddOrEditById(payout);
                break;
            case 2:
                //删除
               //调用删除的方法
                delete(payout);
                break;
        }
        return super.onContextItemSelected(item);
    }

    // TODO: 修改和添加
    private void searchPayoutAddOrEditById( Payout payout) {
        if(payout!=null) {
            //说明是修改
            Intent intent = new Intent(this,PayoutAddOrEditActivity.class);
            Log.e("TAG", "传之前；======" + payout.getAmount());
            intent.putExtra("payout", payout);
            startActivity(intent);

        }else{

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        initData();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void delete(Payout payout) {
        // TODO: 2016/12/26 0026   //提示信息：你确定要删除某消费记录么？
        // TODO: 2016/12/26 0026    //显示对话框 并监听
        new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_title_delete)
                .setPositiveButton("确定", new OnDeleteClickListener())
                .setNegativeButton("取消", null)
                .show();
    }
    private class OnDeleteClickListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
              //根据消费记录Id进行逻辑删除
            boolean result = payoutBsiness.deletePayoutByPayoutId(payout.getPayoutId());
            if(result) {
                //提示删除失败刷新适配器
                showMsg(getString(R.string.tips_delete_scesses));
                initData(defaultAccountBook.getAccountBookId());
            }else {
                showMsg(getString(R.string.tips_delete_fail));
            }
        }
    }
        //菜单项的点击
    @Override
    public void onslideMenuItemClick(SlideMenuItem item) {
        // TODO: 2016/12/26 0026
       //关闭滑动菜单，如果点击啦 第0湘菜单，弹出选择账本对话框
        //点击关闭
        AlertDialog show1 = null;
        View view = LayoutInflater.from(this).inflate(R.layout.account_book_list,null);
        ListView account_book_list_lv = (ListView) view.findViewById(R.id.account_book_list_lv);
        //获得所有账本信息
        List<AccountBook> notHintBookCount = payoutBsiness.getNotHintBookCount();
        AccountBookAdapter accountBookAdapter = new AccountBookAdapter(context);
        account_book_list_lv.setAdapter(accountBookAdapter);
        slideMenuToggle();
        if(item.getItemId()==0) {
            show1  = new AlertDialog.Builder(this)
                    .setTitle(R.string.bottom_text_select_account)
                    .setView(view)
                    .setNegativeButton(R.string.button_text_back, null)
                    .show();
        }
        account_book_list_lv.setOnItemClickListener(new setViewOnClickListenerItem(show1, account_book_list_lv, notHintBookCount));
    }

    private  void showAccountBookSelectDialog(AccountBook accountBook){
        // TODO: 2016/12/26 0026
        //使用AlertDialog、加载listview并设置Adapter 设置对话框的标题和放回按钮等，ListView

        //的点击事件

    }

private class onAccountBookItemClickListener implements AdapterView.OnItemClickListener{
    // TODO: 2016/12/26 0026

    private AlertDialog dialog;
    public onAccountBookItemClickListener(AlertDialog dialog){
        this.dialog = dialog;
    }
    //绑定数据
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //拿到条目实体，绑定适配器数据  关闭对话框

    }
}
    private class setViewOnClickListenerItem implements ListView.OnItemClickListener{
        AlertDialog show1;
        ListView account_book_list_lv;
        List<AccountBook> notHintBookCount;
        public setViewOnClickListenerItem(AlertDialog show1, ListView account_book_list_lv,List<AccountBook> notHintBookCount) {
            this.show1 = show1;
            this.account_book_list_lv = account_book_list_lv;
            this.notHintBookCount = notHintBookCount;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            payoutAdapter = new PayoutAdapter(context,notHintBookCount.get(position).getAccountBookId());
            payout_list_lv.setAdapter(payoutAdapter);
            //设置头标题
            setTitle(notHintBookCount.get(position));
            show1.dismiss();
        }
    }
}
