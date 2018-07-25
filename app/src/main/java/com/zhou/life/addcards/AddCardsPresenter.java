package com.zhou.life.addcards;

import android.support.annotation.NonNull;

import com.zhou.life.data.CreditCard;
import com.zhou.life.data.source.CreditCardRespository;

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

    @Override
    public void subcribe() {

    }

    @Override
    public void unSubcribe() {

    }

    @Override
    public void confirm(CreditCard creditCard) {

    }

    @Override
    public void saveCreditCard(CreditCard creditCard) {

    }
}
