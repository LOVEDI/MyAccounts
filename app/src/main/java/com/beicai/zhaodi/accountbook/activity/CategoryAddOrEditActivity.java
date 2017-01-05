package com.beicai.zhaodi.accountbook.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.beicai.zhaodi.accountbook.R;
import com.beicai.zhaodi.accountbook.activity.base.FrameActivity;
import com.beicai.zhaodi.accountbook.business.CategoryBusiness;
import com.beicai.zhaodi.accountbook.entity.Category;
import com.beicai.zhaodi.accountbook.utils.RegexTools;
import com.beicai.zhaodi.accountbook.view.SlideMenuItem;
import com.beicai.zhaodi.accountbook.view.SlideMenuView;

/**
 * Created by Administrator on 2016/12/21 0021.
 */
public class CategoryAddOrEditActivity extends FrameActivity implements View.OnClickListener ,SlideMenuView.OnSlideMenuListener{
    private Button category_save_btn;
    private Button category_cancle_btn;
    private   EditText category_name_et;
    private Spinner category_parentid_sp;
    private CategoryBusiness categoryBusiness;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(R.layout.category_add_or_edit);
        //移除菜单
        removeBottomBox();
        initVariable();
        initView();
        initLinsteners();
        initData();
        setTitle();
    }
    private void setTitle(){
        String title ;
        if(category==null ) {
            title = getString(R.string.title_category_add_or_edit,new Object[]{getString(R.string.title_add )});
        }else {
            title = getString(R.string.title_category_add_or_edit,new Object[]{getString(R.string.title_edit )});
            //在修改的时候把实体穿进去
            bindData(category);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.category_save_button:
                addOrEditCategory();
                break;
            case R.id.category_cancle_button:
                finish();
                break;

        }
    }
    //初始化数据
    private void initData() {
        categoryBusiness = new CategoryBusiness(this);
        ArrayAdapter<Category> arrayAdapter = categoryBusiness.getRootCategoryArrayAdapter();
        Log.e("TAG", "category_parentid_sp 的id是什么？？？"+category_parentid_sp+"arrayAdapter"+arrayAdapter);
        category_parentid_sp.setAdapter(arrayAdapter);

    }
    private void bindData(Category category){
        //TODO 这里嵌套有一些问题
        category_name_et.setText(category.getCategoryName());
        ArrayAdapter arrayAdapter  = (ArrayAdapter) category_parentid_sp.getAdapter();
        if(category.getParentId()!=0) {
            int position = 0 ;
            for(int i = 0; i < arrayAdapter.getCount(); i++) {
             Category categoryItem = (Category) arrayAdapter.getItem(i);
                        if(categoryItem.getCategoryId()==category.getParentId()) {
                            //说明找到啦
                            position = arrayAdapter.getPosition(categoryItem);
                            break;
                        }
            }
            //做一个定位
            category_parentid_sp.setSelection(position);
        }else {
            int count = categoryBusiness.getNotHideCountByParentId(category.getCategoryId());
            if(count!=0) {
                category_parentid_sp.setEnabled(false);
            }
        }
            
    }

    private void initLinsteners() {
        category_save_btn.setOnClickListener(this);
        category_cancle_btn.setOnClickListener(this);
    }
    //初始化控件
    private void initView() {
        category_save_btn = (Button) findViewById(R.id.category_save_button);
        category_cancle_btn = (Button) findViewById(R.id.category_cancle_button);
        category_name_et = (EditText) findViewById(R.id.category_name_et);
        category_parentid_sp = (Spinner) findViewById(R.id.category_parentid_sp);


    }
    //初始化变量
    private void initVariable() {
        categoryBusiness = new CategoryBusiness(this);
        //获取一个实体
        category = (Category) getIntent().getSerializableExtra("category");
    }

    public void onslideMenuItemClick(SlideMenuItem item) {

        showMsg(item.getTitle());
    }
    public void addOrEditCategory() {
        //// TODO: 这里有问题
        String categoryName = category_name_et.getText().toString().trim();
        boolean checkResult = RegexTools.RegexName(categoryName);
        if (!checkResult) {
            showMsg(getString(R.string.check_text_chinese_english_name,
                    new Object[]{getString(R.string.textview_text_category_name)}));
            return;
        }
        if (category == null) {
            category = new Category();
            category.setPath("");
        }
        //类别名称
        category.setCategoryName(categoryName);
        //如果当前选择的那一项不是请选择   说明有父类
        Log.e("TAG", "这里的选择项是？？+" + category_parentid_sp.getSelectedItem());
        if (!getString(R.string.spinner_places_choose).equals(category_parentid_sp.getSelectedItem().toString())) {
            Category parentCategory = (Category) category_parentid_sp.getSelectedItem();
            if (parentCategory != null) {
                Log.e("TAG", "看看父类是哪个、？+" + parentCategory + "再看看父类的id是多少？" + parentCategory.getCategoryId());
                category.setParentId(parentCategory.getCategoryId());
            }
        }else {
            category.setParentId(0);
        }
            boolean result = false;
            if (category.getCategoryId() == 0) {
                Log.e("TAG","看一下在activity中父类的id是多少？？？"+category.getParentId());
                result = categoryBusiness.insertCategory(category);
            } else {
                result=categoryBusiness.updateCategory(category);
            }
            if (result) {
                showMsg(getString(R.string.tips_add_success));
                finish();
            } else {
                //添加失败
                showMsg(getString(R.string.tips_add_fail));
            }
        }


}
