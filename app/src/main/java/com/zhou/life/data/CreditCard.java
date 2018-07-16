package com.zhou.life.data;

import android.support.annotation.NonNull;

import com.google.common.base.Objects;
import com.zhou.life.utils.TimeUtil;

/**
 * 作者 ly309313
 * 日期 2018/7/15
 * 描述
 */

public class CreditCard {

     /**
      * 银行名称
      */
     @NonNull
     private final String bankname;
     /**
      * 卡号
      */
     @NonNull
     private final String cardNumber;
     /**
      * 账单日 应该存储最近的一次账单日，确认还款之后，存储下一次账单日日期
      * 是否提示未还款，取决于当前日期是否超过账单日
      */
     @NonNull
     private final String dateBill;
     /**
      * 还款日 确认还款之后修改为下一月
      */
     @NonNull
     private final  String dateRepayment;

     private final String mBill;

     private final String repayment;


     public CreditCard(@NonNull String bankname, @NonNull String cardNumber,
                       @NonNull String dateBill, @NonNull String dateRepayment, String bill, String repayment) {
          this.bankname = bankname;
          this.cardNumber = cardNumber;
          this.dateBill = dateBill;
          this.dateRepayment = dateRepayment;
          this.mBill = bill;
          this.repayment = repayment;
     }

     public CreditCard(@NonNull String bankname, @NonNull String cardNumber,
                       @NonNull String dateBill, @NonNull String dateRepayment, String bill) {
          this(bankname,cardNumber,dateBill,dateRepayment,bill,"");
     }

     public CreditCard(@NonNull String bankname, @NonNull String cardNumber,
                       @NonNull String dateBill, @NonNull String dateRepayment) {
          this(bankname,cardNumber,dateBill,dateRepayment,"","");
     }
     @NonNull
     public String getBankname() {
          return bankname;
     }

     @NonNull
     public String getCardNumber() {
          return cardNumber;
     }

     @NonNull
     public String getDateBill() {
          return dateBill;
     }

     @NonNull
     public String getDateRepayment() {
          return dateRepayment;
     }

     public String getBill() {
          if(TimeUtil.overBillDate(dateBill)){
               return mBill;
          }
          return "";
     }

     public String getRepayment() {
          return repayment;
     }

     public String getNextDateBill(){
          String month = dateBill.substring(0,2);
          String newMonth;
          int monthNum = Integer.parseInt(month);
          if(monthNum==12){
               monthNum = 1;
          }else{
               monthNum++;
          }
          if(monthNum<10){
               newMonth = "0" + month;
          }else{
               newMonth = month;
          }
          return dateBill.replace(month,newMonth);
     }
     public String getNextDateRepayment(){
          String month = dateRepayment.substring(0,2);
          String newMonth;
          int monthNum = Integer.parseInt(month);
          if(monthNum==12){
               monthNum = 1;
          }else{
               monthNum++;
          }
          if(monthNum<10){
               newMonth = "0" + month;
          }else{
               newMonth = month;
          }
          return dateRepayment.replace(month,newMonth);
     }
     @Override
     public boolean equals(Object o) {
          if (this == o) return true;
          if (!(o instanceof CreditCard)) return false;

          CreditCard that = (CreditCard) o;

          return Objects.equal(this.bankname,that.bankname)&&
                  Objects.equal(this.cardNumber,that.cardNumber)&&
                  Objects.equal(this.dateBill,that.dateBill)&&
                  Objects.equal(this.dateRepayment,that.dateRepayment)
                  ;
     }

     @Override
     public int hashCode() {
          return Objects.hashCode(bankname,cardNumber,dateBill,dateRepayment);
     }
}