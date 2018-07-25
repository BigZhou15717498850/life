package com.zhou.life.addcards;

import android.support.annotation.NonNull;

import com.zhou.life.data.CreditCard;
import com.zhou.life.data.source.CreditCardRespository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 作者 ly309313
 * 日期 2018/7/24
 * 描述
 */

public class AddCardsPresenter implements AddCardsConstract.Presenter {

    @NonNull
    private CreditCardRespository mCreditCardRespository;

    @NonNull
    private AddCardsConstract.View mView;

    public AddCardsPresenter(@NonNull CreditCardRespository mCreditCardRespository, @NonNull AddCardsConstract.View mView) {
        this.mCreditCardRespository = mCreditCardRespository;
        this.mView = mView;
        this.mView.setPresenter(this);
    }

    @Override
    public void subcribe() {

    }

    @Override
    public void unSubcribe() {

    }

    @Override
    public void confirm() {
        mView.showConfirmDialog();
    }

    @Override
    public void saveCreditCard(CreditCard creditCard) {
            mCreditCardRespository.saveCreditCard(creditCard);
    }

    @Override
    public void selectTime() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String[] dateFormat = format.format(date).split("-");
        mView.showDatePicker(Integer.parseInt(dateFormat[0]),Integer.parseInt(dateFormat[1])-1,Integer.parseInt(dateFormat[2]));
    }

    @Override
    public void selectTime(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.set(year,month,day);

        SimpleDateFormat format = new SimpleDateFormat("MM-dd");

        String date = format.format(calendar.getTime());

        mView.showDate(date);
    }
}
