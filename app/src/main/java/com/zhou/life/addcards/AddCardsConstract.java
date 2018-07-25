package com.zhou.life.addcards;

import com.zhou.life.BasePresenter;
import com.zhou.life.BaseView;
import com.zhou.life.data.CreditCard;

import java.util.Date;

/**
 * 作者 ly309313
 * 日期 2018/7/24
 * 描述
 */

public class AddCardsConstract {

    public interface View extends BaseView<Presenter>{

        void showConfirmDialog();

        boolean isActive();

        void showDatePicker(int year, int month, int day);

        void showDate(String date);
    }

    public interface Presenter extends BasePresenter{

        void confirm();

        void saveCreditCard(CreditCard creditCard);

        void selectTime();

        void selectTime(int year, int month, int day);
    }
}
