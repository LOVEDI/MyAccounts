package com.beicai.zhaodi.accountbook.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.beicai.zhaodi.accountbook.R;
import com.beicai.zhaodi.accountbook.activity.base.FrameActivity;
import com.beicai.zhaodi.accountbook.adapter.UserAdapter;
import com.beicai.zhaodi.accountbook.business.UserBusiness;
import com.beicai.zhaodi.accountbook.entity.Users;
import com.beicai.zhaodi.accountbook.utils.RegexTools;
import com.beicai.zhaodi.accountbook.view.SlideMenuItem;
import com.beicai.zhaodi.accountbook.view.SlideMenuView;

/**
 * Created by Administrator on 2016/12/12 0012.
 */
public class UserActivity extends FrameActivity implements SlideMenuView.OnSlideMenuListener{
    private ListView user_list_lv;
    private UserAdapter userAdapter;
    private UserBusiness userBusiness;
    Users user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(R.layout.users);
        initVariable();
        initView();
        initLinsteners();
        initData();
        //调创建菜单的方法
        createSlideMenu(R.array.SlideMenuUser);
    }
   //初始化数据
    private void initData() {
        if(userAdapter==null) {
            userAdapter = new UserAdapter(this);
            user_list_lv.setAdapter(userAdapter);
        }else {
            userAdapter.clear();
            userAdapter.updateList();
        }
        setTitle();

    }
    private void setTitle(){
            setTopBarTitle(getString(R.string.title_user,new Object[]{userAdapter.getCount()}));
    }

    private void initLinsteners() {
        //注册上下文带单
        registerForContextMenu(user_list_lv);

        //这个时点击事件   自己瞎写的
        user_list_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Users users = (Users) userAdapter.getList(position);
                showMsg("姓名：" + users.getUserName() + "      记账时间：" + users.getCreateDate());
            }
        });
    }
//上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        ListAdapter listAdapter = user_list_lv.getAdapter();
        user = (Users) listAdapter.getItem(acmi.position);
        //设置图片
        menu.setHeaderIcon(R.drawable.user_small_icon);
        //设置标题
        menu.setHeaderTitle(user.getUserName());
        //父类的方法     给menu项设置 id
        createContextMenu(menu);

    }
    //菜单项的点击
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1:
                //修改
                showUserAddOrEditDialog(user);
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
                    new Object[]{user.getUserName()});
            showAlertDialog(R.string.dialog_title_delete,msg,new OnDeleteClickListener());

    }

    //初始化控件
    private void initView() {
        user_list_lv = (ListView) findViewById(R.id.user_list_lv);
    }
    //初始化变量
    private void initVariable() {

        userBusiness = new UserBusiness(this);
    }
        //菜单项的点击
    @Override
    public void onslideMenuItemClick(SlideMenuItem item) {
        //点击关闭
        slideMenuToggle();

        if(item.getItemId()==0) {
            showUserAddOrEditDialog(null);
        }
    }

    private  void showUserAddOrEditDialog(Users user){
        View view = getInflater().inflate(R.layout.user_add_or_edit,null);
        EditText user_name_et = (EditText) view.findViewById(R.id.user_name_et);
        String title ;
        if(user==null) {
            //替换 添加
            title = getString(R.string.dialog_title_user,
                    new Object[]{getString(R.string.title_add)});
        }
        else {
            user_name_et.setText(user.getUserName());
//            title = getString(R.string.dialog_title_user,
//                    new Object[]{R.string.title_edit});
            title =getString(R.string.button_text_cancle);
        }

        new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setView(view)
                    .setIcon(R.drawable.grid_user)
                    .setNeutralButton(R.string.button_text_save,new OnAddOrEditUserListener(user,user_name_et,true))
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        })
                    .setNegativeButton(R.string.button_text_cancle, new OnAddOrEditUserListener(null, null, false))
                    .show();

    }


    private class OnAddOrEditUserListener implements DialogInterface.OnClickListener {
        private Users user;
        private EditText userNameET;
        private boolean isSaveButton;
        public OnAddOrEditUserListener(Users user, EditText user_name_et, boolean b) {
            this.user = user;
            this.userNameET = user_name_et;
            this.isSaveButton = b;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(!isSaveButton) {
                setAlertDialogIsClose(dialog,true);
                return;
            }
            if(user==null) {
                user = new Users();
            }

            String username = userNameET.getText().toString().trim();
            //匹配中文英文和数字
            boolean checkResult = RegexTools.RegexName(username);
            if(!checkResult) {
                showMsg(getString(R.string.check_text_chinese_english_name
                        ,new Object[]{userNameET.getHint()}));
                setAlertDialogIsClose(dialog, false);
                return;
            }else {
                //保存成功
                setAlertDialogIsClose(dialog,true);
            }
            //验证用户是否存在
            checkResult = userBusiness.isExistUserByUserName(username,user.getUserId());
            if(checkResult) {
                //这里只能判断用户是已经被删除的状态
                boolean del = userBusiness.isStateDel(username);
                if(del) {
                    showMsg(getString(R.string.check_text_user_exist));
                    Log.e("TAG", "接下来该继续显示对话框啦");
                    setAlertDialogIsClose(dialog, false);
                    return;
                }else {
                   Users users = userBusiness.getUser(" and userName='" + username+"'");
                    Log.e("TAG","这次用户的id是多少？？？"+users.getUserId());
                    this.user = users;
                    setAlertDialogIsClose(dialog, true);
                }
            }else{
                setAlertDialogIsClose(dialog,true);
            }
            user.setUserName(username);
            boolean result = false;
            Log.e("TAG","userName:"+user.getUserName()+"userId："+user.getUserId());
            if(user.getUserId()==0) {
                result = userBusiness.insertUser(user);
            }else {
                result = userBusiness.updateUser(user);
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
            boolean result = userBusiness.hideUserByUserId(user.getUserId());
            if(result) {
                initData();
            }else {
                showMsg(getString(R.string.tips_delete_fail));
            }

        }
    }
}
