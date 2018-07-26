package com.zhou.life.cardsdetail;

import com.zhou.life.BasePresenter;
import com.zhou.life.BaseView;
import com.zhou.life.data.CreditCard;

/**
 * 作者 LY309313
 * 日期 2018/7/26
 * 描述
 */

public class CreditCardDetailContract {

    public interface View extends BaseView<Presenter>{

        void showCardEmptyError();

        void showConfirmDialog();

        boolean isActive();

        void showDatePicker(int year, int month, int day);

        void showDate(String date);

        void setBankname(String backname);

        void setCreditCardNumber(String cardNumber);

        void setDateBill(String dateBill);

        void setDatePayment(String datePayment);

        void setBill(float bill);

        void setRepayment(float repayment);

        void updateOk();
    }

    public interface Presenter extends BasePresenter{
        void confirm();

        void selectTime();

        void selectTime(int year, int month, int day);

        void populateCard();

        void saveCard(CreditCard creditCard);

    }
}
