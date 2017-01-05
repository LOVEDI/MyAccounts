package com.beicai.zhaodi.accountbook.utils;

import com.beicai.zhaodi.accountbook.entity.Payout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/12/16 0016.
 */
public class RegexTools {
    public static boolean RegexName(String str) {
        if (str.matches("^(?!_)(?!.*?_$)[a-zA-Z0-9_\\u4e00-\\u9fa5]+$")) {
            return true;
        } else {
            return false;
        }
    }
   /* public static boolean isMoney(String str){
        *//**
         * 1、正数不能为零、最多有2有小数，如果第一位为0则下一位必须为小数点
         ^[+]?(([1-9]\\d*[.]?)|(0.))(\\d{0,2})?$
         2、正数不能为0
         ^[\\d&&[^0]]{1}$
         *//*
        if(str.matches("^[\\\\d&&[^0]]{1}$")&&str.matches("^[+]?(([1-9]\\\\d*[.]?)|(0.))(\\\\d{0,2})?$")) {
            return true;
        }else{
             return false;
        }
    }*/

    public static boolean isMoney(String trim) {
        String regEx = "^[0-9]+(.[0-9]{0,2})?$";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(trim);
        // 字符串是否与正则表达式相匹配
        boolean rs = matcher.matches();

        return rs;
    }

    public static boolean isNull(Object obj) {
        return obj==null?true:false;
    }

        public static boolean isDate(String date){
            Payout payout = new Payout();
            String data1 = DateUtil.getFormatDate(payout.getCreateDate());
            String[] defaultDate = data1.split("-");
            int[] defaultIntDate = new int[defaultDate.length];
            for(int i = 0; i < defaultDate.length; i++) {
             defaultIntDate[i]=Integer.parseInt(defaultDate[i]);
            }
            String[] edittDate = date.split("-");
            int[] editIntDate = new int[edittDate.length];
            for(int i = 0; i < edittDate.length; i++) {
                editIntDate[i]=Integer.parseInt(edittDate[i]);
            }
            for(int i = 0; i < defaultDate.length; i++) {
                if (editIntDate[i] > defaultIntDate[i]) {
                    return true;
                }
            }
            return false;
    }

}