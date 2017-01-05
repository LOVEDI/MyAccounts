package com.beicai.zhaodi.accountbook.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.beicai.zhaodi.accountbook.R;
import com.beicai.zhaodi.accountbook.activity.base.FrameActivity;
import com.beicai.zhaodi.accountbook.adapter.AccountBookSelectAdapter;
import com.beicai.zhaodi.accountbook.adapter.CategoryAdapter;
import com.beicai.zhaodi.accountbook.adapter.PayoutAdapter;
import com.beicai.zhaodi.accountbook.adapter.UserAdapter;
import com.beicai.zhaodi.accountbook.business.AccountBookBusiness;
import com.beicai.zhaodi.accountbook.business.CategoryBusiness;
import com.beicai.zhaodi.accountbook.business.PayoutBsiness;
import com.beicai.zhaodi.accountbook.business.UserBusiness;
import com.beicai.zhaodi.accountbook.business.base.DialogCountBassness;
import com.beicai.zhaodi.accountbook.entity.AccountBook;
import com.beicai.zhaodi.accountbook.entity.Category;
import com.beicai.zhaodi.accountbook.entity.Payout;
import com.beicai.zhaodi.accountbook.entity.Users;
import com.beicai.zhaodi.accountbook.utils.DateUtil;
import com.beicai.zhaodi.accountbook.utils.RegexTools;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Administrator on 2016/12/22 0022.
 */
public class PayoutAddOrEditActivity extends FrameActivity implements View.OnClickListener {
    View view;
    Button payout_enter_amount_bt,payout_select_account_bt,payout_select_category_bt,payout_select_date_bt,payout_calculate_type_bt,
            payout_select_people_bt,payout_save_button,payout_cancle_button;
    AutoCompleteTextView payout_select_category_ectv;
     Payout payout;
    private AccountBook accountBook;
    private PayoutBsiness payoutBsiness;
    private AccountBookBusiness accountBookBusiness;
    private CategoryBusiness categoryBusiness;
    private UserBusiness userBusiness;
    private Integer accountBookId;
    private Integer categoryId;
    private String payoutUserId;//1,2,3,
    private String payoutTypeArray[];
    private List<LinearLayout> itemColor;
    private List<Users> userSelectedList;
    PayoutActivity payoutActivity;
    private EditText payout_select_account_et,payout_enter_amount_et,
            payout_select_date_et,payout_calculate_type_et,payout_select_people_et,payout_comment_et;
    List<Category> notHintCategory;
    List<String > listName;
    PayoutAdapter payoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(R.layout.payout_add_or_edit);
        removeBottomBox();
        initView();
        initVariable();
        initData();
        initListener();
    }
    private void initView() {
        payout_enter_amount_bt = (Button) findViewById(R.id.payout_enter_amount_bt);
        payout_select_account_bt = (Button)findViewById(R.id.payout_select_account_bt);
        payout_select_category_bt = (Button)findViewById(R.id.payout_select_category_bt);
        payout_select_date_bt = (Button)findViewById(R.id.payout_select_date_bt);
        payout_calculate_type_bt = (Button)findViewById(R.id.payout_calculate_type_bt);
        payout_select_people_bt = (Button)findViewById(R.id.payout_select_people_bt);
        payout_save_button = (Button)findViewById(R.id.payout_save_button);
        payout_cancle_button = (Button)findViewById(R.id.payout_cancle_button);
        payout_select_account_et = (EditText)findViewById(R.id.payout_select_account_et);
        payout_enter_amount_et = (EditText)findViewById(R.id.payout_enter_amount_et);
        payout_select_date_et = (EditText)findViewById(R.id.payout_select_date_et);
        payout_calculate_type_et = (EditText)findViewById(R.id.payout_calculate_type_et);
        payout_select_people_et = (EditText)findViewById(R.id.payout_select_people_et);
        payout_comment_et = (EditText)findViewById(R.id.payout_comment_et);
        payout_select_category_ectv = (AutoCompleteTextView)findViewById(R.id.payout_select_category_ectv);
    }
    private void initListener() {
        payout_enter_amount_bt.setOnClickListener(this);
        payout_select_account_bt.setOnClickListener(this);
        payout_select_category_bt.setOnClickListener(this);
        payout_select_date_bt.setOnClickListener(this);
        payout_calculate_type_bt.setOnClickListener(this);
        payout_select_people_bt.setOnClickListener(this);
        payout_save_button.setOnClickListener(this);
        payout_cancle_button.setOnClickListener(this);
        //自动显示框
        autoShow(payout_select_category_ectv);//给自动提示框设置数据
        payout_select_category_ectv.setOnItemClickListener(new OnAutoCompleteTextViewIntemClickListener());
    }
    public void initData(){
        accountBookId = accountBook.getAccountBookId();
        //直接显示默认账本
        payout_select_account_et.setText(accountBook.getAccountBookName());
        //设置adapter
        payout_select_category_ectv.setAdapter(categoryBusiness.getAllCategoryArrayAdapter());
        payout_select_date_et.setText(DateUtil.getFormatDateTime(new Date(), "yyyy-MM-dd"));
        payoutTypeArray  = getResources().getStringArray(R.array.payoutType);
        payout_calculate_type_et.setText(payoutTypeArray[0]);
        setTitle();
    }
    private void bindDate(Payout payout){
        payout_select_account_et.setText(payout.getAccountBookName());
        accountBookId = payout.getAccountBookId();
        payout_enter_amount_et.setText(payout.getAmount().toString());
        // TODO: 根据id 获取名字
        String name  = categoryBusiness.getNotHideRootCategoryById(payout.getCategoryId());
        payout_select_category_ectv.setText(name);
        categoryId = payout.getCategoryId();
        payout_select_date_et.setText(payout.getPayoutDate());
        payout_calculate_type_et.setText(payout.getPayoutType());
        String userName = userBusiness.getUserNameByUserId(payout.getPayoutUserId());
        payout_select_people_et.setText(userName);
        payoutUserId = payout.getPayoutUserId();
        payout_comment_et.setText(payout.getComment());
    }
    private void initVariable(){
        payoutBsiness = new PayoutBsiness(this);
        accountBookBusiness = new AccountBookBusiness(this);
        categoryBusiness = new CategoryBusiness(this);
        userBusiness = new UserBusiness(this);
        payoutActivity = new PayoutActivity();
        payout = (Payout) getIntent().getSerializableExtra("payout");
     //   Log.e("TAG","在PayoutAddOrEditActivity中 得到的payout的信息是："+payout.getAmount());
        accountBook = accountBookBusiness.getDefaultAccountBook();//获取默认账本
    }
    private void setTitle(){
        String title;
        if(payout == null) {
                title = getString(R.string.title_payout_add_or_edit ,
                        new Object[]{getString(R.string.title_add)});
        }else {
            title = getString(R.string.title_payout_add_or_edit ,
                    new Object[]{getString(R.string.title_edit)});
            bindDate(payout);
        }
    }


    private void addOrEditPayout() {
        boolean checkResult = checkDate();
        if(!checkResult) {
            return;
        }else {
            if(payout==null) {
                payout = new Payout();
            }
            payout.setAccountBookId(accountBookId);
            payout.setCategoryId(categoryId);
            //设置金额
            payout.setAmount(new BigDecimal(payout_enter_amount_et.getText().toString().trim()));
            payout.setPayoutDate(payout_select_date_et.getText().toString().trim());
            //计算方式
            payout.setPayoutType(payout_calculate_type_et.getText().toString().trim());
            Log.e("TAG","类型到底是什么？？？"+payout_calculate_type_et.getText());
            payout.setPayoutUserId(payoutUserId);
            payout.setComment(payout_comment_et.getText().toString().trim());
            
            boolean result = false;
            int SEETYPE = 0;
            if(payout.getPayoutId()==0) {
                    //说明是新建
                result = payoutBsiness.insertPayout(payout);
            }else {
                SEETYPE = 1;
                //更新
                Log.e("TAG","是更新的吗???????"+payout.getPayoutType());
                result = payoutBsiness.updatePayout(payout);
            }
            if(result) {
                showMsg(getString(R.string.tips_add_success));
                Log.e("TAG","新建的id是00000么？===="+payout.getPayoutId());
                if(SEETYPE!=0) {
                    Intent intent = new Intent(this,PayoutActivity.class);
                    intent.putExtra("accountBookId",payout.getAccountBookId());
                    startActivity(intent);
                }
                finish();
            }else{
                 showMsg(getString(R.string.tips_add_fail));
            }
        }
    }
    private boolean checkDate(){
        //判断金额必须是数字 ，并且不能超过小数点后两位 ，可以是整数 以为或者两位
        boolean checkResult = RegexTools.isMoney(payout_enter_amount_et.getText().toString().trim());
        if(!checkResult) {
            //设置焦点让用户重新填
            payout_enter_amount_et.requestFocus();//内部已经设置好啦
            showMsg(getString(R.string.check_text_money));
            return  false;
        }
        //判断类别是否为空
        checkResult = RegexTools.isNull(categoryId);
        if(checkResult) {
            payout_select_category_ectv.setFocusable(true);//是否能获取焦点
            payout_select_category_ectv.setFocusableInTouchMode(true);//显示键盘
            payout_select_category_ectv.requestFocus();//请求焦点
            showMsg(getString(R.string.payout_type_is_not_null));
            return  false;
        }
        //完成日期的的验证  不许向未开穿越
        String date = payout_select_date_et.getText().toString().trim();
        Log.e("TAG","activity中的data是："+date);
        checkResult = RegexTools.isDate(date);
        Log.e("TAG", "日期的创建是否成功"+checkResult);
        if(checkResult) {
            payout_select_date_bt.setFocusable(true);//是否能获取焦点
            payout_select_date_bt.setFocusableInTouchMode(true);//显示键盘
            payout_select_date_bt.requestFocus();//请求焦点
            showMsg(getString(R.string.check_text_date_is_afterr));
            return  false;
        }
        if(payoutUserId==null) {
            payout_select_people_bt.setFocusable(true);//是否能获取焦点
            payout_select_people_bt.setFocusableInTouchMode(true);//显示键盘
            payout_select_people_bt.requestFocus();//请求焦点
            showMsg(getString(R.string.check_text_payout_user_is_null));
            return  false;
        }
        String payoutType = payout_calculate_type_et.getText().toString();
        if(payoutType.equals(payoutTypeArray[0])||payoutType.equals(payoutTypeArray[1])) {
            if(payoutUserId.split(",").length<=1) {
                payout_select_people_bt.setFocusable(true);//是否能获取焦点
                payout_select_people_bt.setFocusableInTouchMode(true);//显示键盘
                payout_select_people_bt.requestFocus();//请求焦点
                showMsg(getString(R.string.check_text_payout_user));
                return  false;
            }
        }else{
             if("".equals(payoutUserId)) {
                 payout_select_people_bt.setFocusable(true);//是否能获取焦点
                 payout_select_people_bt.setFocusableInTouchMode(true);//显示键盘
                 payout_select_people_bt.requestFocus();//请求焦点
                 showMsg(getString(R.string.check_text_payout_user2));
                 return  false;
             }
        }
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //选择账本
            case R.id.payout_select_account_bt:
                //封装一个方法来实现
                showAccountBookSelectDialog();
                break;
            //输入金额
            case R.id.payout_enter_amount_bt:
                new DialogCountBassness(this, payout_enter_amount_et);
                break;
            //选择类别
            case R.id.payout_select_category_bt:
                showCategorySelectDialog();
                break;
            //选择日期
            case R.id.payout_select_date_bt:
                Calendar calendar = Calendar.getInstance();
                ShowDateSelectDialog(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE));
                break;
            //计算方式
            case R.id.payout_calculate_type_bt:
                showPayoutTypeSelectDialog();
                break;
            //选择消费人
            case R.id.payout_select_people_bt:
                showUserSelectDialog(payout_calculate_type_et.getText().toString());
                break;
            //保存
            case R.id.payout_save_button:
                addOrEditPayout();
                break;
            //取消
            case R.id.payout_cancle_button:
                finish();
                break;
        }
    }

    private void showPayoutTypeSelectDialog() {
            //计算方式   自己写
        View view = LayoutInflater.from(this).inflate(R.layout.payout_type, null);
        ListView listView = (ListView) view.findViewById(R.id.payout_type_lv_lv);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,payoutTypeArray);
        listView.setAdapter(arrayAdapter);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.payout_calculat_text_select)
                .setView(view)
                .setNegativeButton(R.string.button_text_back, null)
                .show();
        listView.setOnItemClickListener(new OnTypeItemListener(dialog));

    }
    //选择消费人
    private void  showUserSelectDialog(String payoutType){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.users,null);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.user_list_ll);
        linearLayout.setBackgroundResource(R.drawable.blue);
        ListView select_lv = (ListView) view.findViewById(R.id.user_list_lv);
        UserAdapter userAdapter = new UserAdapter(this);
        select_lv.setAdapter(userAdapter);
        builder.setIcon(R.drawable.user_small_icon)
                .setTitle(R.string.payout_select_people_text)
                .setNegativeButton(R.string.button_text_back,new OnSelectUserBack())
                .setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        select_lv.setOnItemClickListener(new OnUserIntemClickListener(dialog,payoutType));
    }
    private class OnUserIntemClickListener implements AdapterView.OnItemClickListener{
        private AlertDialog dialog;
        private String payoutType;
        public OnUserIntemClickListener(AlertDialog dialog,String payoutType){
            this.dialog = dialog;
            this.payoutType = payoutType;
        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
         String[] payoutTypeAarray = getResources().getStringArray(R.array.payoutType);
            Users users = (Users) parent.getAdapter().getItem(position);
            if(payoutType.equals(payoutTypeAarray[0])||
                    payoutType.equals(payoutTypeAarray[1])
                    ) {
                LinearLayout lineaerLayout = (LinearLayout) view.findViewById(R.id.user_item_ll);
                if(itemColor==null&&userSelectedList==null) {
                    itemColor = new ArrayList<>();
                    userSelectedList = new ArrayList<>();
                }
                if(itemColor.contains(lineaerLayout)) {
                    lineaerLayout.setBackgroundResource(R.drawable.blue);
                    itemColor.remove(lineaerLayout);
                    userSelectedList.remove(users);
                }else{
                     lineaerLayout.setBackgroundResource(R.drawable.red);
                    itemColor.add(lineaerLayout);
                    userSelectedList.add(users);
                }
                return;
            }
            //个人消费只能选择一个人
            if(payoutType.equals(payoutTypeAarray[2])) {
                String name = users.getUserName()+",";
                payoutUserId = users.getUserId()+",";
                payout_select_people_et.setText(name);
                dialog.dismiss();
            }
        }
    }
    private class OnSelectUserBack implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            payout_select_people_et.setText("");
            String name = "";
            payoutUserId = "";
            if(userSelectedList !=null) {
                for(int i = 0; i < userSelectedList.size(); i++) {
                 name += userSelectedList.get(i).getUserName()+",";
                    payoutUserId += userSelectedList.get(i).getUserId()+",";
                }
                payout_select_people_et.setText(name);
            }
            //清空 方便下次重用
            itemColor = null;
            userSelectedList = null;
        }
    }
    //选择日期
    private void ShowDateSelectDialog(int year, int month, int day) {
        (new DatePickerDialog(this,new onDateSelectListener(),year,month,day)).show();
    }
    private class onDateSelectListener implements DatePickerDialog.OnDateSetListener{

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//            Date data = new Date(year-1900,monthOfYear,dayOfMonth);
            GregorianCalendar gc = new GregorianCalendar(year,monthOfYear,dayOfMonth);
            Date data = gc.getTime();
            String payoutDateLast = DateUtil.getFormatDateTime(data, "yyyy-MM-dd");
            payout_select_date_et.setText(payoutDateLast);
        }
    }
    private void showCategorySelectDialog() {
            //选择类别
        View view = LayoutInflater.from(this).inflate(R.layout.category,null);
        ExpandableListView categoryList = (ExpandableListView)view
                .findViewById(R.id.category_list_lv);
        CategoryAdapter adapter = new CategoryAdapter(this);
        categoryList.setAdapter(adapter);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.payout_select_category_text))
                .setView(view)
                .setNegativeButton(getString(R.string.button_text_back), null)
                .show();
        categoryList.setOnGroupClickListener(new onCategorySelectedListener(
                dialog));
        categoryList.setOnChildClickListener(new onCategorySelectedListener(
                dialog));
    }

    private void showAccountBookSelectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_list,null);
        ListView select_lv  = (ListView) view.findViewById(R.id.select_lv);
        AccountBookSelectAdapter accountBookSelectAdapter = new AccountBookSelectAdapter(this);
        select_lv.setAdapter(accountBookSelectAdapter);
        builder.setTitle(R.string.bottom_text_select_account)
                .setNegativeButton(getString(R.string.button_text_back),null)
                .setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //点击关掉对话框
        select_lv.setOnItemClickListener(new OnAccountBookItemClickListener(alertDialog));

    }
    private class OnAccountBookItemClickListener implements AdapterView.OnItemClickListener{
        private AlertDialog dialog;
        public OnAccountBookItemClickListener(AlertDialog dialog){
            this.dialog = dialog;
        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AccountBook accountBook = (AccountBook) parent.getAdapter().getItem(position);
            payout_select_account_et.setText(accountBook.getAccountBookName());
            accountBookId = accountBook.getAccountBookId();
            //关掉对话框 
            dialog.dismiss();
        }
    }

//自动提示框
    private class OnAutoCompleteTextViewIntemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           // payout_select_category_ectv.setText(listName.get(position-1));
        }
    }
//计算方式 显示在edittext上
    private class OnTypeItemListener implements AdapterView.OnItemClickListener {
    AlertDialog dialog;
        public OnTypeItemListener(AlertDialog dialog) {
            this.dialog = dialog;
        }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String type = (String) parent.getAdapter().getItem(position);
        if (type.equals(payoutTypeArray[2]) && payoutUserId != null
                && payoutUserId.indexOf(",") <= payoutUserId.length() - 1) {
            payout_select_people_et.setText("");
            payoutUserId = "";
        }
        payout_calculate_type_et.setText(type);
        dialog.dismiss();
    }
}

    private class onCategorySelectedListener implements ExpandableListView.OnGroupClickListener ,ExpandableListView.OnChildClickListener {
        AlertDialog dialog;

        public onCategorySelectedListener(AlertDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            Category categoryGroup = (Category) parent.getAdapter().getItem(
                    groupPosition);
            List<Category> listGroup = categoryBusiness
                    .getNotHideCategoryListByParentId(categoryGroup
                            .getCategoryId());
            if (listGroup.size() < 1) {
                payout_select_category_ectv.setText(categoryGroup.getCategoryName());
                categoryId = categoryGroup.getCategoryId();
                dialog.dismiss();
                return true;
            }
            return false;
        }


        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            Category categoryGroup = (Category) parent.getAdapter().getItem(
                    groupPosition);
            List<Category> listGroup = categoryBusiness
                    .getNotHideCategoryListByParentId(categoryGroup
                            .getCategoryId());
            if (listGroup.size() > 0) {
                Category categoryChild = listGroup.get(childPosition);
                payout_select_category_ectv.setText(categoryChild.getCategoryName());
                categoryId = categoryChild.getCategoryId();
                dialog.dismiss();
                return true;
            }
            return false;
        }
    }
    public void autoShow(AutoCompleteTextView payout_select_category_ectv){
        //在构造方法中进行初始化
        //创建一个字符数组，存放自动填出的数据
         notHintCategory = categoryBusiness.getNotHintCategory();
        listName  = new ArrayList<String>();
        for(int i = 0; i < notHintCategory.size(); i++) {
          listName.add(i,notHintCategory.get(i).getCategoryName().toString().trim());
        }
        //创建一个数组器适配器
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listName);
        //组件绑定适配器
        payout_select_category_ectv.setAdapter(adapter);
    }

}
