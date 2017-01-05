package com.beicai.zhaodi.accountbook.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.beicai.zhaodi.accountbook.R;
import com.beicai.zhaodi.accountbook.business.CategoryBusiness;
import com.beicai.zhaodi.accountbook.entity.Category;

import java.util.List;

/**
 * Created by Administrator on 2016/12/20 0020.
 */
public class CategoryAdapter implements ExpandableListAdapter {
    private Context context;
    private List list;
    private CategoryBusiness categoryBusiness;

    public CategoryAdapter(Context context) {
        this.context = context;
        categoryBusiness =new CategoryBusiness(context);
        //获取所有根节点的类别
        list = categoryBusiness.getNotHideRootCategory();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //获取组 的类别
        Category parentCategory = (Category) getGroup(groupPosition);
        //获取所有为隐藏的子类的数量
        int count = categoryBusiness.getNotHideCountByParentId(parentCategory.getCategoryId());
        return count;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return (Category)list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Category parentCategory = (Category) getGroup(groupPosition);

        //获未隐藏取子的实体
        List<Category> childList = categoryBusiness.getNotHideCategoryListByParentId(
                parentCategory.getCategoryId()
        );
        return childList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder;
        if(convertView==null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.category_group_list_item,null
            );
            groupHolder = new GroupHolder();
            groupHolder.category_group_name_tv = (TextView) convertView.findViewById(R.id.category_group_item_name_tv);
            groupHolder.category_group_count_tv = (TextView) convertView.findViewById(R.id.category_group_count_tv);
            convertView.setTag(groupHolder);
        }else{
            groupHolder = (GroupHolder) convertView.getTag();
        }
        Category category = (Category) getGroup(groupPosition);
        groupHolder.category_group_name_tv.setText(category.getCategoryName());
        int count = getChildrenCount(groupPosition);
        groupHolder.category_group_count_tv.setText(context.getString(R.string.textview_text_children_category
                ,new Object[]{count}));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder;
        if(convertView==null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.category_child_list_item,null);
            childHolder = new ChildHolder();
            childHolder.category_children_name_tv = (TextView) convertView.findViewById(R.id.category_children_item_name_tv);
            convertView.setTag(childHolder);
        }else{
            childHolder = (ChildHolder) convertView.getTag();
        }
        Category category  = (Category) getChild(groupPosition,childPosition);
        childHolder.category_children_name_tv.setText(category.getCategoryName());
        return convertView;
    }

    private class GroupHolder{
    TextView category_group_name_tv;
        TextView category_group_count_tv;
    }
    private class ChildHolder{
        TextView category_children_name_tv;
    }







    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        //是否可选
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }
}
