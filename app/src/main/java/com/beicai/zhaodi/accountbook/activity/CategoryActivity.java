package com.beicai.zhaodi.accountbook.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.beicai.zhaodi.accountbook.R;
import com.beicai.zhaodi.accountbook.activity.base.FrameActivity;
import com.beicai.zhaodi.accountbook.adapter.CategoryAdapter;
import com.beicai.zhaodi.accountbook.business.CategoryBusiness;
import com.beicai.zhaodi.accountbook.entity.Category;
import com.beicai.zhaodi.accountbook.entity.CategoryTotal;
import com.beicai.zhaodi.accountbook.view.SlideMenuItem;
import com.beicai.zhaodi.accountbook.view.SlideMenuView;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/12 0012.
 */
public class CategoryActivity extends FrameActivity implements SlideMenuView.OnSlideMenuListener{
    private ExpandableListView category_list_elv;
    private CategoryAdapter categoryAdapter;
    private CategoryBusiness categoryBusiness;
    Category category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(R.layout.category);
        initVariable();
        initView();
        initLinsteners();
        initData();
        //调创建菜单的方法
        createSlideMenu(R.array.SlideMenuCategory);
    }
    //关闭本类的方法 到主页面
    public   void toMain(){
        startActivity(new Intent(this,MainActivity.class));
    }
   //初始化数据
    private void initData() {
    //    if(categoryAdapter==null) {
            categoryAdapter = new CategoryAdapter(this);
            category_list_elv.setAdapter(categoryAdapter);
//        }else {
//            categoryAdapter.clear();
//            categoryAdapter.updateList();
//        }
        setTitle();

    }
    private void setTitle(){
        //获取未隐藏的数量  （包括子类和主类的总数）
        int count = categoryBusiness.getNotHideCount();
            setTopBarTitle(getString(R.string.title_category, new Object[]{count}));
    }
    //初始化控件
    private void initView() {
        category_list_elv = (ExpandableListView) findViewById(R.id.category_list_lv);
    }
    private void initLinsteners() {
        //注册上下文带单
        registerForContextMenu(category_list_elv);

        //这个时点击事件   自己瞎写的
        /*category_list_elv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category category = (Category) categoryAdapter.getList(position);
                showMsg("姓名：" + category.getCategoryName() + "      记账时间：" + category.getCreateDate());
            }
        });*/
    }
//上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //得到菜单信息
        ExpandableListView.ExpandableListContextMenuInfo elcm = (ExpandableListView
                        .ExpandableListContextMenuInfo) menuInfo;
        //获取菜单的位置信息
        long position = elcm.packedPosition;
        //判断是父类还是子类（得带类型 ）
        int type  = ExpandableListView.getPackedPositionType(position);
        //通过位置信息得到组位置
        int groupPosition = ExpandableListView.getPackedPositionGroup(position);
        switch (type){
            case ExpandableListView.PACKED_POSITION_TYPE_GROUP://说明他是组
                //根据组的位置取的实体
                category = (Category) categoryAdapter.getGroup(groupPosition);
                break;
            case ExpandableListView.PACKED_POSITION_TYPE_CHILD://是子类
                //获取子位置
                int childPosition = ExpandableListView.getPackedPositionChild(position);
                //获取从组下的某子位置的实体
                category = (Category) categoryAdapter.getChild(groupPosition,childPosition);
                break;

        }
//        ListAdapter listAdapter = category_list_elv.getAdapter();
//         category = (Category) listAdapter.getItem(elcm.position);
        //设置图片
        menu.setHeaderIcon(R.drawable.category_small_icon);
        if(category!=null) {
            menu.setHeaderTitle(category.getCategoryName());
        }
        //设置标题
        menu.setHeaderTitle(category.getCategoryName());
        createContextMenu(menu);
        menu.add(0,3,0,R.string.category_total);
        if(categoryAdapter.getChildrenCount(groupPosition)!=0&&category.getParentId()==0) {
                menu.findItem(2).setEnabled(false);
        }
        //父类的方法     给menu项设置 id
    }
    //菜单项的点击
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case 1:
                //修改或添加
                intent = new Intent(this,CategoryAddOrEditActivity.class);
                intent.putExtra("category",(Serializable)category);
                startActivityForResult(intent, 1);
                break;
            case 2:
                //删除
                delete(category);
                break;
            case 3:
                //统计类别
                List<CategoryTotal> list=categoryBusiness.getCategoryTotalByParentId(category.getParentId());
                intent=new Intent();
                //list进行强转 需要实体类实现序列化接口
                intent.putExtra("total",(Serializable) list);
                intent.setClass(this,CategoryCharActivity.class);
                startActivity(intent);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        initData();
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void delete(Category category) {
            String msg = getString(R.string.dialog_title_category_delete,
                    new Object[]{category.getCategoryName()});
            showAlertDialog(R.string.dialog_title_delete,msg,new OnDeleteClickListener());

    }


    //初始化变量
    private void initVariable() {

        categoryBusiness = new CategoryBusiness(this);
    }
        //菜单项的点击
    @Override
    public void onslideMenuItemClick(SlideMenuItem item) {
        //点击关闭
        slideMenuToggle();
        if(item.getItemId()==0) {//新建类别
            Intent intent = new Intent(this,CategoryAddOrEditActivity.class);
            startActivityForResult(intent,1);
            return;
        }
        if(item.getItemId()==1) {
            List<CategoryTotal> list=categoryBusiness.categoryTotalByRootCategory();
            Intent intent=new Intent(this,CategoryCharActivity.class);
            //list进行强转 需要实体类实现序列化接口
            intent.putExtra("total",(Serializable) list);
            startActivity(intent);
            return;
        }
    }
    private class OnDeleteClickListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            boolean result = categoryBusiness.hideCategoryByCategoryId(category.getCategoryId());
            if(result) {
                showMsg(getString(R.string.tips_delete_scesses));
                initData();
            }else {
                showMsg(getString(R.string.tips_delete_fail));
            }

        }
    }
}
