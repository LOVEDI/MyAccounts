package com.beicai.zhaodi.accountbook.activity.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.beicai.zhaodi.accountbook.R;
import com.beicai.zhaodi.accountbook.activity.CategoryActivity;
import com.beicai.zhaodi.accountbook.utils.MyUtil;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016/12/12 0012.
 */
public class BaseActivity extends Activity{
    private ProgressDialog progressDialog;
    private CategoryActivity categoryActivity;
        /*
        打印吐司的方法
         */
    protected void showMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    };

    protected void openActivity(Class<?> cls){
        Intent intent = new Intent();
        intent.setClass(this,cls);
        startActivity(intent);
    }

    protected LayoutInflater getInflater(){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        return layoutInflater;
    }

    protected void setAlertDialogIsClose(DialogInterface dialog,boolean isClose){
        try {
            //获取类的超类的实例的  mShowing 字段的  来控制 开关
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            //设置访问的权限为true
            field.setAccessible(true);
            field.set(dialog,isClose);
        } catch (Exception e) {
            Log.e("TAG", "已经生成啦异常");
            e.printStackTrace();
        }
    }
    public AlertDialog showAlertDialog(int titleResId,String msg,DialogInterface.OnClickListener clickListener){
        String title = getResources().getString(titleResId);
        return  showAlertDialog(title,msg,clickListener);
    }
    public AlertDialog showAlertDialog(String title,String msg,DialogInterface.OnClickListener clickListener){
        return  new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(R.string.button_text_yes,clickListener )
                .setNegativeButton(R.string.button_text_no,null)
                .show();


    }
    //返回键的方法
    //TODO
    protected void showProgressDdialog(int titleResId,int msgResId){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(titleResId);
        progressDialog.setMessage(getString(msgResId));
        progressDialog.show();
    }
    protected void dismissProgressDialog(){
        String runningActivityName = MyUtil.getRunningActivityName(this);
        categoryActivity = new CategoryActivity();
        if(progressDialog!=null) {
            if("CategoryActivity".equals(runningActivityName)) {
                categoryActivity.toMain();
            }

            progressDialog.dismiss();
        }
    }

}
