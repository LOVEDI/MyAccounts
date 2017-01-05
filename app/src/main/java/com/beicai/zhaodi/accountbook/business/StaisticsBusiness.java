package com.beicai.zhaodi.accountbook.business;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.beicai.zhaodi.accountbook.R;
import com.beicai.zhaodi.accountbook.business.base.BaseBusiness;
import com.beicai.zhaodi.accountbook.entity.Payout;
import com.beicai.zhaodi.accountbook.entity.Statistics;
import com.beicai.zhaodi.accountbook.utils.DateUtil;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Created by Administrator on 2016/12/28 0028.
 */
public class StaisticsBusiness extends BaseBusiness{
    //sdcard的保存路径
    public static final String SDCARD_PATH= Environment.getExternalStorageDirectory().getPath()
            +"/Readily/Export/";
    private PayoutBsiness payoutBsiness;
    private UserBusiness userBusiness;
    private AccountBookBusiness accountBookBusiness;
    public StaisticsBusiness(Context context) {
        super(context);
        payoutBsiness =new PayoutBsiness(context);
        userBusiness =new UserBusiness(context);
        accountBookBusiness =new AccountBookBusiness(context);

    }
    //得到拆分好的统计信息
    //得到一个  王小强 王小强 ？元
    //王小强  小李  ？元
    private List<Statistics> getStatisticsList(String condition){
        //按照付款人的ID排序取出消费记录
        List<Payout> payoutList = payoutBsiness.getpayoutOrderByPayoutUserId(condition);
      /*  for(int a = 0; a < payoutList.size(); a++) {
          Log.e("TAG","按照付款人id取出的消费记录是:"+payoutList.get(a));
        }*/
        //获取计算方式数组
        String[] payoutTypeArray = context.getResources().getStringArray(R.array.payoutType);
        List<Statistics> statisticsesList = new ArrayList<>();
        if(payoutList!=null) {
            //遍历这个消费记录的列表
            for(int i = 0; i < payoutList.size(); i++) {
             //取出一条消费记录
                Payout payout = payoutList.get(i);
                //Log.e("TAG","消费记录是:"+payoutList.get(i));
                //将消费人的id转换为真是的名称
                String[] payoutUserName = userBusiness.getUserNameByUserId(payout.getPayoutUserId())
                        .split(",");//得到人员名称的数组
                String[] payoutUserId = payout.getPayoutUserId().split(",");//人员id的数组
                //取出当前的消费记录的计算方式
                String payoutType = payout.getPayoutType();
                //存放计算后的消费金额的
                BigDecimal cost;
                //判断本次消费记录的消费类型
                if(payout.getPayoutType().equals(payoutTypeArray[0])) {//均分
                    //得到消费人数
                    int payoutTotal = payoutUserName.length;
                    //得到计算后的平均消费金额
                    // divide：除法 2：保留两位小数  BigDecimal.ROUND_HALF_EVEN：四舍五入
                    cost = payout.getAmount().divide(new BigDecimal(payoutTotal),2, BigDecimal.ROUND_HALF_EVEN);
                    Log.e("TAG","如果是均分的话;计算结果是："+i+"===="+cost);
                }else{
                     //借贷或者个人消费   直接取出消费金额 因为只需要给一个人累加
                    cost = payout.getAmount();
                }
                //消费人的数组
                for(int j = 0; j < payoutUserId.length; j++) {
                  //如果是借贷  则跳过一个索引  ，因为第一个是借贷人自己
                    if(payoutType.equals(payoutTypeArray[1])&&j==0) {
                        continue;
                    }
                    //生命一个统计类
                    Statistics statistics = new Statistics();
                    //将统计类的付款人设计为消费人数组的第一个人
                    statistics.payerUserId = payoutUserName[0];
                    //设置消费人
                    statistics.consumerUserId = payoutUserName[j];
                    //设置消费类型
                    statistics.payoutType = payoutType;
                    //设置算好的消费金额
                    statistics.cast = cost;
                    // 付款人  消费人  金额
                    //王小强   均分   10 元
                    statisticsesList.add(statistics);
                }
            }
        }
        return statisticsesList;

    }
    //得到总的统计结果的集合
    public List<Statistics> getPayoutUserId(String condition){
        //得到拆分好的信息
        List<Statistics> list = getStatisticsList(condition);
        for(int i = 0; i < list.size(); i++) {
         Log.e("TAG","得到拆分好的信息是："+list.get(i));
        }
        //存放按付款人分类的临时统计信息
        List<Statistics> listTemp = new ArrayList<>();
        //用来存放统计好的汇总
        List<Statistics> totalList = new ArrayList<>();
        String result = "";
        //遍历拆分好的统计信息
        for(int i = 0; i < list.size(); i++) {
         //得到拆分好的一条信息
            Statistics statistics = list.get(i);
            result += statistics.payerUserId+"#"+statistics.consumerUserId+"#"
                    +statistics.cast+"#"+"\r\n";
            //保存当前的付款人的id
            String curentPayerUserId = statistics.payerUserId;
            //把当前信息给付款跟分类的临时数据
            Statistics statistics1Temp = new Statistics();
            statistics1Temp.payerUserId = statistics.payerUserId;
            statistics1Temp.consumerUserId = statistics.consumerUserId;
            statistics1Temp.cast = statistics.cast;
            statistics1Temp.payoutType = statistics.payoutType;
            listTemp.add(statistics1Temp);

            //计算下一行的索引
            int  nextIndex;
            //如果下一行索引小于统计 信息索引 ，则可以+1
            if((i+1)<list.size()) {
                nextIndex = i+1;
            }else {
                //否则证明已经到位，则索引赋值为前行
                nextIndex = i;
            }
            //如果当前付款人与下一位付款人不同，则证明分裂统计已经到尾，或者已经循环到统计数组最后一位
            if(!curentPayerUserId.equals(list.get(nextIndex).payerUserId)||nextIndex==i) {
                // 进行当前分类统计数组的统计
                for(int j = 0; j <listTemp.size(); j++) {
                    Statistics statistics1Total=listTemp.get(j);
                    //判断在总的统计数组当中是否已存在付款人和消费人的信息
                    int index=getPoisitionBycurrentPayoutId(totalList,statistics1Total.payerUserId,statistics1Total.consumerUserId);
                    //如果存在对应的统计类对象，则累加金额
                    if(index!=-1){
                        //add  表示累加
                        totalList.get(index).cast = (totalList.get(index).cast.add(statistics1Total.cast));
                    } else {//否则创建一条新的信息，添加到总统计数组
                        totalList.add(statistics1Total);

                    }
                }
                //清空临时统计的数组
                listTemp.clear();
            }
        }
        for(int i = 0; i < totalList.size(); i++) {
            Log.e("TAG", "第二次得到的结果是：" + totalList.get(i));
        }
        return totalList;
    }
    //判断在总的统计数组当做是否存在该付款人和消费人信息
    private int getPoisitionBycurrentPayoutId(List<Statistics> totallist, String payerUserId, String consumerUserId) {
        int index=-1;
        for(int i = 0; i < totallist.size(); i++) {
            if(totallist.get(i).payerUserId.equals(payerUserId)&&totallist.get(i).consumerUserId.equals(consumerUserId)) {
                index=i;
                break;
            }
        }
        return  index;
    }

    public String getPayoutUserIdByAccountBookId(int accountBookId){
        String result = "";
        //得到一个总统计结果集合
        List<Statistics> totalLsit = getPayoutUserId("and accountBookId = "+ accountBookId);
        for(int i = 0; i < totalLsit.size(); i++) {
            Statistics statistics=totalLsit.get(i);
            if("个人消费".equals(statistics.payoutType)){
                result+=statistics.payerUserId+"个人消费"+statistics.cast.toString()+"元\r\n";
            } else if("均分".equals(statistics.payoutType)){
                if(statistics.payerUserId.equals(statistics.consumerUserId)) {
                    result+=statistics.payerUserId+"个人消费"+statistics.cast.toString()+"元\r\n";
                }else{
                    result+=statistics.consumerUserId+"应该支付给"+statistics.payerUserId+statistics.cast.toString()+"元\r\n";
                }
            }else if("借贷".equals(statistics.payoutType)){
                result+=statistics.consumerUserId+"应该支付给"+statistics.payerUserId+statistics.cast.toString()+"元\r\n";
            }
        }
        return  result;
    }
    public String exportStatistics(int accountBookId) throws Exception{
        String result="";
        //判断是否有外存储设备
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //根据账本ID取出账本名称
            String accountBookName=accountBookBusiness.getAccountBookNameByAccountId(accountBookId);
            String fileName=accountBookName+ DateUtil.getTFormatString("yyyyMMdd")+".xls";
            Log.e("TAG", SDCARD_PATH + fileName);
            File fileDir=new File(SDCARD_PATH);
            if(!fileDir.exists()) {
                fileDir.mkdirs();
            }
            File file=new File(SDCARD_PATH+fileName);
            if(!file.exists()) {
                file.createNewFile();
            }
            //声明一个可写的表对象
            WritableWorkbook workBookData;
            //创建工具薄。需要告诉他往哪个文件里面创建
            workBookData= Workbook.createWorkbook(file);
            //创建工作表
            WritableSheet wsAccountBook=workBookData.createSheet(accountBookName,0);
            //声明表头数据
            String[] titles={"编号","姓名","金额","消费信息","消费类型"};
            //声明一个文本标签
            Label lable;
            //添加标题行
            for(int i = 0; i <titles.length ; i++) {
                //列，行，内容
                lable=new Label(i,0,titles[i]);
                //将文本标签填入一个单元格
                wsAccountBook.addCell(lable);
            }
            //添加行
            List<Statistics> totalList=getPayoutUserId(" and accountBookId="+accountBookId);
            for(int i = 0; i < totalList.size(); i++) {
                Statistics statistics=totalList.get(i);
                //添加标号列
                //number导入的包是jxl的
                Number idCell=new Number(0,i+1,i+1);
                wsAccountBook.addCell(idCell);
                //添加姓名
                Label nameLable=new Label(1,i+1,statistics.payerUserId);
                wsAccountBook.addCell(nameLable);
                //格式化金额类型显示 #.##表示格式化小数点后两位
                //注意导入的包 jxl
                NumberFormat moneyFormat=new NumberFormat("#.##");
                WritableCellFormat wcf=new WritableCellFormat(moneyFormat);
                //添加金额
                Number costCell=new Number(2,i+1,statistics.cast.doubleValue(),wcf);
                wsAccountBook.addCell(costCell);
                //添加消费信息
                String info= "";
                if ("个人".equals(statistics.payoutType)) {
                    info += statistics.payerUserId + "个人消费" +
                            statistics.cast.toString() + "元\r\n";
                } else if ("均分".equals(statistics.payoutType)) {
                    if (statistics.payerUserId.equals(statistics.consumerUserId)) {
                        info += statistics.payerUserId + "个人消费" + statistics.cast.toString() + "元\r\n";
                    } else {
                        info += statistics.consumerUserId + "应支付给" + statistics.payerUserId +
                                statistics.cast.toString() + "元\r\n";
                    }
                } else if ("借贷".equals(statistics.payoutType)) {
                    info += statistics.consumerUserId + "应支付给" + statistics.payerUserId +
                            statistics.cast.toString() + "元\r\n";
                }
                Label infoLabel=new Label(3,i+1,info);
                wsAccountBook.addCell(infoLabel);
                //添加消费类型
                Label payoutTypeLabel=new Label(4,i+1,statistics.payoutType);
                wsAccountBook.addCell(payoutTypeLabel);
            }
            //写入sd卡
            workBookData.write();
            workBookData.close();
            result="数据已经导出，位置在"+SDCARD_PATH+fileName;
        }else{
            result="抱歉，未检测出SD卡,数据无法导出";
        }
        return result;
    }
}
