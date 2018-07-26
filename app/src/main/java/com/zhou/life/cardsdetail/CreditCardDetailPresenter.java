package com.zhou.life.cardsdetail;

import android.support.annotation.NonNull;

import com.zhou.life.data.CreditCard;
import com.zhou.life.data.source.CreditCardRespository;
import com.zhou.life.utils.schedulers.BaseSchedulerProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.reactivex.disposables.CompositeDisposable;

/**
 * 作者 LY309313
 * 日期 2018/7/26
 * 描述
 */

public class CreditCardDetailPresenter implements CreditCardDetailContract.Presenter{

    @NonNull
    private CreditCardRespository mCreditCardRespository;

    @NonNull
    private CreditCardDetailContract.View mView;

    @NonNull
    private BaseSchedulerProvider mSchedulerProvider;
    @NonNull
    private String mCardNumber;
    @NonNull
    private CompositeDisposable mCompositeDisposable;

    public CreditCardDetailPresenter(@NonNull String cardNumber,
            @NonNull CreditCardRespository creditCardRespository,
                                     @NonNull CreditCardDetailContract.View view,
                                     @NonNull BaseSchedulerProvider schedulerProvider) {
        mCardNumber = cardNumber;
        mCreditCardRespository = creditCardRespository;
        mView = view;
        mSchedulerProvider = schedulerProvider;
        mCompositeDisposable = new CompositeDisposable();
        mView.setPresenter(this);
    }

    @Override
    public void subcribe() {
        populateCard();
    }

    @Override
    public void unSubcribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void confirm() {
        mView.showConfirmDialog();
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

    @Override
    public void populateCard() {
        mCompositeDisposable.clear();
        mCreditCardRespository.
                getCreditCard(mCardNumber)
                .observeOn(mSchedulerProvider.io())
                .subscribeOn(mSchedulerProvider.ui())
                .subscribe(creditCardOptional -> {
                   if(creditCardOptional.isPresent()){
                       CreditCard creditCard = creditCardOptional.get();
                       mView.setBankname(creditCard.getBankname());
                       mView.setCreditCardNumber(creditCard.getCardNumber());
                       mView.setDateBill(creditCard.getDateBill());
                       mView.setDatePayment(creditCard.getDateRepayment());
                       mView.setBill(creditCard.getBill());
                       mView.setRepayment(creditCard.getRepayment());
                   }
                },throwable -> {
                    if(mView.isActive())mView.showCardEmptyError();
                });
    }

    @Override
    public void saveCard(CreditCard creditCard) {
        mCreditCardRespository.saveCreditCard(creditCard);
        mView.updateOk();
    }
}
