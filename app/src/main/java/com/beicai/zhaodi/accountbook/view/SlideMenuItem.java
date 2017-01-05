package com.beicai.zhaodi.accountbook.view;

/**
 * 再实验一下
 */
public class SlideMenuItem {
    private  int itemId;

    public SlideMenuItem(int itemId,String title){
        this.itemId = itemId;
        this.title = title;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public int getItemId() {
        return itemId;
    }

    private String title;
}
