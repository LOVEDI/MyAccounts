package com.beicai.zhaodi.accountbook.business.base;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.beicai.zhaodi.accountbook.R;

/**
 * Created by Administrator on 2016/12/22 0022.
 */
public class DialogCountBassness  {
    View view;
    Context context;
    AlertDialog show;
    private StringBuffer sb;//用来存放输入数字的sb，呵呵
    private EditText phone;//电话号码输入
    private Button one, two, three, four, five, six,
            seven, eight, night, zero, reset, delete, dian, cancle;
    EditText payout_enter_amount_et;
    public DialogCountBassness(Context context,EditText payout_enter_amount_et){
        this.context = context;
        this.payout_enter_amount_et = payout_enter_amount_et;
        view = LayoutInflater.from(context).inflate(R.layout.keyboart_account, null);
         show = new AlertDialog.Builder(context)
                .setView(view)
                .show();
        initDialogView(context);
    }
    //初始化 dialog中的view
    private void initDialogView(Context context) {
        phone = (EditText)  view.findViewById(R.id.keyboard_account_et);
        one = (Button) view.findViewById(R.id.keyboard_account_1);
        two = (Button) view.findViewById(R.id.keyboard_account_2);
        three = (Button) view.findViewById(R.id.keyboard_account_3);
        four = (Button) view.findViewById(R.id.keyboard_account_4);
        five = (Button) view.findViewById(R.id.keyboard_account_5);
        six = (Button) view.findViewById(R.id.keyboard_account_6);
        seven = (Button) view.findViewById(R.id.keyboard_account_7);
        eight = (Button) view.findViewById(R.id.keyboard_account_8);
        night = (Button) view.findViewById(R.id.keyboard_account_9);
        zero = (Button) view.findViewById(R.id.keyboard_account_0);
        reset = (Button) view.findViewById(R.id.keyboard_account_result);
        delete = (Button) view.findViewById(R.id.keyboard_account_del);
        dian = (Button) view.findViewById(R.id.keyboard_account_dian);
        cancle = (Button) view.findViewById(R.id.keyboard_account_save);
        sb = new StringBuffer();
        one.setOnClickListener(new ShowDialogListener());
        two.setOnClickListener(new ShowDialogListener());
        three.setOnClickListener(new ShowDialogListener());
        four.setOnClickListener(new ShowDialogListener());
        five.setOnClickListener(new ShowDialogListener());
        six.setOnClickListener(new ShowDialogListener());
        seven.setOnClickListener(new ShowDialogListener());
        eight.setOnClickListener(new ShowDialogListener());
        night.setOnClickListener(new ShowDialogListener());
        zero.setOnClickListener(new ShowDialogListener());
        reset.setOnClickListener(new ShowDialogListener());
        delete.setOnClickListener(new ShowDialogListener());
        dian.setOnClickListener(new ShowDialogListener());
        cancle.setOnClickListener(new ShowDialogListener());
        phone.setFocusable(false);//不让该edittext获得焦点
        phone.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 关闭软键盘，这样当点击该edittext的时候，不会弹出系统自带的输入法
                phone.setInputType(InputType.TYPE_NULL);
                return false;
            }
        });

    }


    public static boolean isContain(String s1,String s2) {
        return s1.contains(s2);
    }
public class ShowDialogListener implements View.OnClickListener{

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.keyboard_account_save:
                Log.e("TAG", "点击的是save 保存");
                String number = phone.getText().toString();
                payout_enter_amount_et.setText(number);
                show.dismiss();//消失
                break;
            case R.id.keyboard_account_dian:
                String str = sb.toString();
                boolean b = isContain(str, ".");
                if (!b) {
                    if(sb.length()==0){
                        sb.append("0");
                    }
                    sb.append(dian.getText().toString().trim());
                    phone.setText(sb.toString().trim());
                }
                break;
            case R.id.keyboard_account_1:
                Log.e("TAG", "点击的是1");
                sb.append(one.getText().toString().trim());
                phone.setText(sb.toString().trim());
                break;
            case R.id.keyboard_account_2:
                sb.append(two.getText().toString().trim());
                phone.setText(sb.toString().trim());
                break;
            case R.id.keyboard_account_3:
                sb.append(three.getText().toString().trim());
                phone.setText(sb.toString().trim());
                break;
            case R.id.keyboard_account_4:
                sb.append(four.getText().toString().trim());
                phone.setText(sb.toString().trim());
                break;
            case R.id.keyboard_account_5:
                sb.append(five.getText().toString().trim());
                phone.setText(sb.toString().trim());
                break;
            case R.id.keyboard_account_6:
                sb.append(six.getText().toString().trim());
                phone.setText(sb.toString().trim());
                break;
            case R.id.keyboard_account_7:
                sb.append(seven.getText().toString().trim());
                phone.setText(sb.toString().trim());
                break;
            case R.id.keyboard_account_8:
                sb.append(eight.getText().toString().trim());
                phone.setText(sb.toString().trim());
                break;
            case R.id.keyboard_account_9:
                sb.append(night.getText().toString().trim());
                phone.setText(sb.toString().trim());
                break;
            case R.id.keyboard_account_0:
                sb.append(zero.getText().toString().trim());
                phone.setText(sb.toString().trim());
                break;
            case R.id.keyboard_account_del:
                if (sb.length() - 1 >= 0) {
                    sb.delete(sb.length() - 1, sb.length());
                    phone.setText(sb.toString().trim());
                }
                break;
            case R.id.keyboard_account_result:
                sb.replace(0, sb.length(), "");
                phone.setText(sb.toString().trim());
                break;
            default:
                break;
        }
    }
}

}
