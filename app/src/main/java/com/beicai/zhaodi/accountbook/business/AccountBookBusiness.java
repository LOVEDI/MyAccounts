package com.beicai.zhaodi.accountbook.business;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.beicai.zhaodi.accountbook.business.base.BaseBusiness;
import com.beicai.zhaodi.accountbook.database.dao.AccountBookDAO;
import com.beicai.zhaodi.accountbook.entity.AccountBook;

import java.util.List;

/**
 * 业务层
 */
public class AccountBookBusiness extends BaseBusiness {
    private AccountBookDAO accountBookDAO;
    List<AccountBook> list;
    public AccountBookBusiness(Context context) {
        super(context);
        accountBookDAO = new AccountBookDAO(context);
    }
        //插入人
    public boolean insertAccountBook(AccountBook accountBook){

        return insertAndUpdate(accountBook);
    }
    //更新
    public boolean updateAccountBook(AccountBook accountBook){
             return insertAndUpdate(accountBook);

    }
    public boolean insertAndUpdate(AccountBook accountBook){
        //开启事务
        boolean result;
        accountBookDAO.beginTransaction();
        try {
            if(accountBook.getAccountBookId()==0) {
                 result = accountBookDAO.insertAccountBook(accountBook);
                Log.e("TAG","在aaccountBookBusiness中看看插入数据的情况  result=="+result);
                //如果插入成功 则  把这个实体拿出来获取他的id 进行下一步的操作，，通过姓名查实体
                String condition = " and accountBookName = '"+accountBook.getAccountBookName()+"'";
                Log.e("TAG","然后再看看查询出来的id 到死是几？？？"+accountBook.getAccountBookId());
                accountBook = getAccountBook(condition);
                Log.e("TAG","然后再看看查询出来的id 到di是几？？？"+accountBook.getAccountBookId());
            }else {
                String condition = " accountBookId=" + accountBook.getAccountBookId();
                 result = accountBookDAO.updateAccountBook(condition, accountBook);
            }
            boolean result2 = true;
            if (accountBook.getIsDefault() == 1 && result) {

                result2 = setIsDefault(accountBook.getAccountBookId());
            }
            if(result2) {
                Log.e("TAG","修改成功");
            }
            if(result&&result2) {
                accountBookDAO.setTransactionSuccessful();
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            accountBookDAO.endTransaction();
        }
    }
    public List<AccountBook> getAccountBooks(String condition){
        return accountBookDAO.getAccountBook(condition);
    }
    //修改默认账本
    public boolean setIsDefault(int accountBookId)throws Exception{
        //TODO 在这里查看这个是不是默认账本   如果是的话则不可以取消，通过id查出这个账本的信息
        /*String condition1 = " and isDefault = 1";1
        Log.e("TAG","查询出来的结果是   isDefault=="+getAccountBook(condition1).getIsDefault());
        if(getAccountBook(condition1)!=null) {
            //不可以没有默认账本
            return false;
        }else {*/
            String condition = " isDefault = 1";
            ContentValues contentValues = new ContentValues();
            contentValues.put("isDefault", 0);
            //先查找 默认账本  把状态改为0  就是非默认的
            boolean reault = accountBookDAO.updateAccountBook(condition, contentValues);
            Log.e("TAG", "修改默认账本reault==" + reault);

            contentValues.clear();
        condition = "accountBookId = " + accountBookId;
        //再根据id修改这个账本为默认账本
            contentValues.put("isDefault", 1);
            boolean result2 = accountBookDAO.updateAccountBook(condition, contentValues);
            Log.e("TAG", "修改默认账本reault222==" + reault);
            if (reault && result2) {
                return true;
            } else {
                return false;
            }

        }
   // }

    public AccountBook getAccountBookByAccountBookId(int accountBookId){
        List<AccountBook> list = accountBookDAO.getAccountBook(" and accountBookId = "+accountBookId);
        if(list!=null&&list.size()==1) {
            return list.get(0);
        }else {
            return null;
        }
    }

    public List<AccountBook> getNotHideAccountBook(){
        return accountBookDAO.getAccountBook(" and state=1");
    }

    public boolean  isExistAccountBookByAccountBookName(String accountBookname,Integer accountBookId){
        String condition = " and accountBookName='"+ accountBookname +"'";
        if(accountBookId!=null) {
            condition+= " and accountBookId<> "+accountBookId;
        }
         list = accountBookDAO.getAccountBook(condition);
        if(list!=null && list.size()>0) {
            return true;
        }else {
            return false;
        }
    }
    //在这里封装一个方法，用来判断 已经存在的 ，状态为删除的 用户
    public  boolean isStateDel(String accountBookname){
        boolean boo = true;
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getState()==0) {
                //在这里进行修改用户状态的语句
                Log.e("TAG", "进来修改状态的语句里面了么？");
                ContentValues contentValues = new ContentValues();
                contentValues.put("state",1);
                String condition1 =" accountBookName='"+ accountBookname +"'";
                accountBookDAO.updateAccountBook(condition1,contentValues);
                boo = false;
            }
        }
        return boo;
    }
    //查看单个的方法
    public AccountBook getAccountBook(String conticion){
        List<AccountBook> accountBooks = accountBookDAO.getAccountBook(conticion);
        return accountBooks.get(0);
    }
    public boolean hideAccountBookByAccountBookId(int accountBookId) {
        String condition = " accountBookId = "+accountBookId;
        ContentValues contentValues = new ContentValues();
        contentValues.put("state",0);
        return accountBookDAO.updateAccountBook(condition, contentValues);
    }
    //获取默认账本
    public AccountBook getDefaultAccountBook() {
        List<AccountBook> list=accountBookDAO.getAccountBook(" and state=1 and isDefault=1");
        return list.get(0);
    }
    public String getAccountBookNameByAccountId(int accountBookId) {
        AccountBook accountBook=getAccountBookByAccountBookId(accountBookId);
        return accountBook==null?null:accountBook.getAccountBookName();
    }

   /* // TODO: 根据账本的id 删除账本的方法   操作两个表 （账本，和消费记录）
    public boolean deleteAccountByAccountId(int accountBookId) {
        String condition = "and accountBookid = " +accountBookId;
        ContentValues contentValues = new ContentValues();
        contentValues.put("state");
        return false;
    }*/
}
