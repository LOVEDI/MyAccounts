package com.beicai.zhaodi.accountbook.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.beicai.zhaodi.accountbook.R;
import com.beicai.zhaodi.accountbook.activity.base.FrameActivity;
import com.beicai.zhaodi.accountbook.adapter.AccountBookAdapter;
import com.beicai.zhaodi.accountbook.business.AccountBookBusiness;
import com.beicai.zhaodi.accountbook.entity.AccountBook;
import com.beicai.zhaodi.accountbook.utils.RegexTools;
import com.beicai.zhaodi.accountbook.view.SlideMenuItem;
import com.beicai.zhaodi.accountbook.view.SlideMenuView;

/**
 * Created by Administrator on 2016/12/12 0012.
 */
public class AccountBookActivity extends FrameActivity implements SlideMenuView.OnSlideMenuListener{
    private ListView accountBook_list_lv;
    private AccountBookAdapter accountBookAdapter;
    private AccountBookBusiness accountBookBusiness;
    AccountBook accountBook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(R.layout.account_book_list   );
        initVariable();
        initView();
        initLinsteners();
        initData();
        //调创建菜单的方法
        createSlideMenu(R.array.SlideMenuAccountBook);
    }
   //初始化数据
    private void initData() {
        if(accountBookAdapter==null) {
            accountBookAdapter = new AccountBookAdapter(this);
            accountBook_list_lv.setAdapter(accountBookAdapter);
        }else {
            accountBookAdapter.clear();
            accountBookAdapter.updateList();
        }
        setTitle();

    }
    private void setTitle(){
            setTopBarTitle(getString(R.string.title_accountBook,new Object[]{accountBookAdapter.getCount()}));
    }

    private void initLinsteners() {
        //注册上下文带单
        registerForContextMenu(accountBook_list_lv);

        //这个时点击事件   自己瞎写的
        accountBook_list_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AccountBook accountBook = (AccountBook) accountBookAdapter.getList(position);
                showMsg("姓名：" + accountBook.getAccountBookName() + "      记账时间：" + accountBook.getCreateDate());
            }
        });
    }
//上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        ListAdapter listAdapter = accountBook_list_lv.getAdapter();
         accountBook = (AccountBook) listAdapter.getItem(acmi.position);
        //设置图片
        menu.setHeaderIcon(R.drawable.account_book_small_icon);
        //设置标题
        menu.setHeaderTitle(accountBook.getAccountBookName());
        //父类的方法     给menu项设置 id
        createContextMenu(menu);
    }
    //菜单项的点击
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1:
                //修改
                showAccountBookAddOrEditDialog(accountBook);
                break;
            case 2:
                //删除
                delete();
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void delete() {
            String msg = getString(R.string.dialog_title_massagge_delete,
                    new Object[]{accountBook.getAccountBookName()});
            showAlertDialog(R.string.dialog_message_accountBook_delete,msg,new OnDeleteClickListener());

    }

    //初始化控件
    private void initView() {
        accountBook_list_lv = (ListView) findViewById(R.id.account_book_list_lv);
    }
    //初始化变量
    private void initVariable() {

        accountBookBusiness = new AccountBookBusiness(this);
    }
        //菜单项的点击
    @Override
    public void onslideMenuItemClick(SlideMenuItem item) {
        //点击关闭
        slideMenuToggle();

        if(item.getItemId()==0) {
            showAccountBookAddOrEditDialog(null);
        }
    }

    private  void showAccountBookAddOrEditDialog(AccountBook accountBook){
        View view = getInflater().inflate(R.layout.account_book_add_or_edit,null);
        EditText accountBook_name_et = (EditText) view.findViewById(R.id.account_book_name_et);
        CheckBox account_book_check_default_cb = (CheckBox) view.findViewById(R.id.account_book_check_default_cb);
        String title;
        if(accountBook==null) {
            //替换 添加
            title = getString(R.string.dialog_title_account_book,
                    new Object[]{getString(R.string.title_add)});
        }
        else {
            accountBook_name_et.setText(accountBook.getAccountBookName());
            title = getString(R.string.dialog_title_account_book,
                    new Object[]{getString(R.string.title_edit)});
            if(accountBook.getIsDefault()==1) {
                account_book_check_default_cb.setChecked(true);
            }
         //   title =getString(R.string.button_text_cancle);
        }

        new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setView(view)
                    .setIcon(R.drawable.grid_account_book)
                    .setNeutralButton(R.string.button_text_save,new OnAddOrEditAccountBookListener(accountBook,accountBook_name_et
                            ,account_book_check_default_cb,true))
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        })
                    .setNegativeButton(R.string.button_text_cancle, new OnAddOrEditAccountBookListener(null, null,null, false))
                    .show();

    }


    private class OnAddOrEditAccountBookListener implements DialogInterface.OnClickListener {
        private AccountBook accountBook;
        private EditText accountBookNameET;
        private CheckBox accountBookDefaultCB;
        private boolean isSaveButton;
        public OnAddOrEditAccountBookListener(AccountBook accountBook, EditText account_book_name_et, CheckBox account_book_default_db, boolean b) {
            this.accountBook = accountBook;
            this.accountBookDefaultCB =account_book_default_db;
            this.accountBookNameET = account_book_name_et;
            this.isSaveButton = b;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(!isSaveButton) {
                setAlertDialogIsClose(dialog,true);
                return;
            }
            if(accountBook==null) {
                accountBook = new AccountBook();
            }

            String accountBookname = accountBookNameET.getText().toString().trim();
            //匹配中文英文和数字
            boolean checkResult = RegexTools.RegexName(accountBookname);
            if(!checkResult) {
                showMsg(getString(R.string.check_text_chinese_english_name
                        ,new Object[]{accountBookNameET.getHint()}));
                setAlertDialogIsClose(dialog, false);
                return;
            }else {
                //保存成功
                setAlertDialogIsClose(dialog,true);
            }
            //验证用户是否存在
            checkResult = accountBookBusiness.isExistAccountBookByAccountBookName(accountBookname, accountBook.getAccountBookId());
            if(checkResult) {
                //这里只能判断用户是已经被删除的状态
                boolean del = accountBookBusiness.isStateDel(accountBookname);
                if(del) {
                    showMsg(getString(R.string.check_text_account_book_exist));
                    Log.e("TAG", "接下来该继续显示对话框啦");
                    setAlertDialogIsClose(dialog, false);
                    return;
                }else {
                   AccountBook accountBook = accountBookBusiness.getAccountBook(" and accountBookName='" + accountBookname + "'");
                    Log.e("TAG","这次用户的id是多少？？？"+accountBook.getAccountBookId());
                    this.accountBook = accountBook;
                    setAlertDialogIsClose(dialog, true);
                }
            }else{
                setAlertDialogIsClose(dialog,true);
            }
            accountBook.setAccountBookName(accountBookname);
            Log.e("TAG","在activity中判断到底是否选中的？？？"+accountBook.getIsDefault());

            if(accountBookDefaultCB.isChecked()) {
                accountBook.setIsDefault(1);
            }else{
                if(accountBook.getIsDefault()==1) {
                    showMsg(getString(R.string.account_dialog_show_massage_false));
                    setAlertDialogIsClose(dialog,false);
                    return;
                }else{
                    accountBook.setIsDefault(0);
                }
            }
            boolean result = false;
            Log.e("TAG", "accountBookName:" + accountBook.getAccountBookName() + "accountBookId：" + accountBook.getAccountBookId());
            if (accountBook.getAccountBookId()==0) {
                result = accountBookBusiness.insertAccountBook(accountBook);
                Log.e("TAG","这里是判断的是插入数据时返回的数据？？？+"+result);
            }else {
                result = accountBookBusiness.updateAccountBook(accountBook);
            }
            if(result) {
                initData();
            }else {
                showMsg(getString(R.string.tips_add_fail));
            }
        }
    }
    private class OnDeleteClickListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            boolean result = accountBookBusiness.hideAccountBookByAccountBookId(accountBook.getAccountBookId());
            if(result) {
                initData();
            }else {
                showMsg(getString(R.string.tips_delete_fail));
            }

        }
    }
}
