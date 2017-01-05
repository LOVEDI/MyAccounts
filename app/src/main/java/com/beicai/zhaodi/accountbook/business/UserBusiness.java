package com.beicai.zhaodi.accountbook.business;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.beicai.zhaodi.accountbook.business.base.BaseBusiness;
import com.beicai.zhaodi.accountbook.database.dao.UserDAO;
import com.beicai.zhaodi.accountbook.entity.Users;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务层
 */
public class UserBusiness extends BaseBusiness{
    private UserDAO userDAO;
    List<Users> list;
    public UserBusiness(Context context) {
        super(context);
        userDAO = new UserDAO(context);
    }
        //插入人
    public boolean insertUser(Users user){
        boolean result = userDAO.insertUser(user);
        return result;
    }
            //物理删除
    public boolean deleteUserByUserId(int userId){
        String condition = " and userId="+userId;
        boolean result = userDAO.deleteUsers(condition);
        return result;
    }
    public boolean updateUser(Users user){
        String condition = " userId="+user.getUserId();
        boolean result = userDAO.updateUsers(condition, user);
        return  result;

    }
    public List<Users> getUsers (String condition){
        return userDAO.getUsers(condition);
    }

    public Users getUserByUserId(int userId){
        List<Users> list = userDAO.getUsers(" and userId = "+userId);
        if(list!=null&&list.size()==1) {
            return list.get(0);
        }else {
            return null;
        }

    }

    public List<Users> getNotHideUser(){
        return userDAO.getUsers(" and state=1");
    }

    public boolean  isExistUserByUserName(String username,Integer userId){
        String condition = " and userName='"+ username +"'";
        if(userId!=null) {
            condition+= " and userId<> "+userId;
        }
         list = userDAO.getUsers(condition);
        if(list!=null && list.size()>0) {
            return true;
        }else {
            return false;
        }
    }
    //在这里封装一个方法，用来判断 已经存在的 ，状态为删除的 用户
    public  boolean isStateDel(String username){
        boolean boo = true;
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getState()==0) {
                //在这里进行修改用户状态的语句
                Log.e("TAG", "进来修改状态的语句里面了么？");
                ContentValues contentValues = new ContentValues();
                contentValues.put("state",1);
                String condition1 =" userName='"+ username +"'";
                userDAO.updateUsers(condition1,contentValues);
                boo = false;
            }
        }
        return boo;
    }
    //查看单个的方法
    public Users getUser(String conticion){
        List<Users> users = userDAO.getUsers(conticion);
        return users.get(0);
    }
    public boolean hideUserByUserId(int userId) {
        String condition = " userId = "+userId;
        ContentValues contentValues = new ContentValues();
        contentValues.put("state",0);
        return userDAO.updateUsers(condition, contentValues);
    }

    public String getUserNameByUserId(String UserId){//UserId : 1,2,3,
        List<Users> list = getUserListByUserIdArray(UserId.split(","));
        String name = "";
        for(int i = 0; i < list.size(); i++) {
         name+=list.get(i).getUserName()+",";
        }
        return name;//王小强，小李，小张
    }
    public List<Users> getUserListByUserIdArray(String[] usetIds){
        List<Users> list = new ArrayList<>();
        for(int i = 0; i < usetIds.length; i++) {
            list.add(getUserByUserId(Integer.valueOf(usetIds[i])));
        }
            return list;
    }


}
