package com.zhou.life.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;


import com.google.common.base.Optional;
import com.zhou.life.data.CreditCard;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 作者 ly309313
 * 日期 2018/7/16
 * 描述
 */

public class CreditCardRespository implements CreditCardDataSource {
    @Nullable
    private static CreditCardRespository INSTANCE;

    @NonNull
    private final CreditCardDataSource mCreditCardLocalDataSource;

    @VisibleForTesting
    @Nullable
    Map<String, CreditCard> mCreditCardCaches;

    private CreditCardRespository(@NonNull CreditCardDataSource mCreditCardLocalDataSource) {
        this.mCreditCardLocalDataSource = mCreditCardLocalDataSource;
    }

    @Nullable
    public static CreditCardRespository getInstance(@NonNull CreditCardDataSource mCreditCardLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new CreditCardRespository(mCreditCardLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destoryInstance() {
        INSTANCE = null;
    }

    @Override
    public Flowable<List<CreditCard>> getCreditCards() {
        if(mCreditCardCaches!=null){
            return Flowable.fromIterable(mCreditCardCaches.values()).toList().toFlowable();
        }else if(mCreditCardCaches==null){
            mCreditCardCaches = new LinkedHashMap<>();//插入、删除快，查询慢
        }
        Flowable<List<CreditCard>> andCacheCreditCard = getAndCacheCreditCard();
        return andCacheCreditCard;

    }

    private Flowable<List<CreditCard>> getAndCacheCreditCard() {

        return mCreditCardLocalDataSource.getCreditCards()
                .flatMap(cards->Flowable.fromIterable(cards)
                        .doOnNext(card->mCreditCardCaches.put(card.getCardNumber(),card))
                        .toList()
                        .toFlowable());
    }

    @Override
    public Flowable<Optional<CreditCard>> getCreditCard(String cardNumber) {
        checkNotNull(cardNumber);
        CreditCard creditCard = getCardWithNum(cardNumber);
        if(creditCard!=null)return Flowable.just(Optional.of(creditCard));
        if(mCreditCardCaches == null){
            mCreditCardCaches = new LinkedHashMap<>();
        }
        return getCardWithNumFromLocalSource(cardNumber);
    }

    private Flowable<Optional<CreditCard>> getCardWithNumFromLocalSource(String cardNumber) {
        checkNotNull(cardNumber);
        return mCreditCardLocalDataSource.getCreditCard(cardNumber)
                .doOnNext(card->{
                    if(card.isPresent())
                        mCreditCardCaches.put(cardNumber,card.get());
                })
                .firstElement()
                .toFlowable();
    }

    private CreditCard getCardWithNum(String cardNumber) {
        checkNotNull(cardNumber);
        if(mCreditCardCaches==null||mCreditCardCaches.isEmpty())return null;
        return mCreditCardCaches.get(cardNumber);
    }

    @Override
    public void saveCreditCard(CreditCard creditCard) {
        checkNotNull(creditCard);
        mCreditCardLocalDataSource.saveCreditCard(creditCard);
        if(mCreditCardCaches==null)mCreditCardCaches = new LinkedHashMap<>();
        mCreditCardCaches.put(creditCard.getCardNumber(),creditCard);
    }

    @Override
    public void deleteCreditCard(String cardNumber) {
        checkNotNull(cardNumber);
        if(mCreditCardCaches!=null)mCreditCardCaches.remove(cardNumber);
        mCreditCardLocalDataSource.deleteCreditCard(cardNumber);
    }

    @Override
    public void bill(CreditCard creditCard, float money) {
        checkNotNull(creditCard);
        checkNotNull(money);
        mCreditCardLocalDataSource.bill(creditCard,money);
        CreditCard newCreditCard = new CreditCard(creditCard.getBankname(),
                creditCard.getCardNumber(),
                creditCard.getDateBill(),
                creditCard.getDateRepayment(),
                money,
                0.0f);
        if(mCreditCardCaches==null)mCreditCardCaches = new LinkedHashMap<>();
        mCreditCardCaches.put(newCreditCard.getCardNumber(),newCreditCard);
    }

    /**
     * 还款之后，账单日和还款日自动跳到下一月
     * @param creditCard
     * @param money
     */
    @Override
    public void repayment(CreditCard creditCard, float money) {
        checkNotNull(creditCard);
        checkNotNull(money);
        float totalRepayment = creditCard.getRepayment() + money;
        mCreditCardLocalDataSource.repayment(creditCard,money);
        CreditCard newCreditCard = new CreditCard(creditCard.getBankname(),
                creditCard.getCardNumber(),
                totalRepayment >= creditCard.getBill() ? creditCard.getNextDateBill() :creditCard.getDateBill() ,
                totalRepayment >= creditCard.getBill() ? creditCard.getNextDateRepayment() : creditCard.getDateRepayment(),
                totalRepayment >= creditCard.getBill() ? 0.0f : creditCard.getBill(),
                creditCard.getRepayment() + money);
        if(mCreditCardCaches==null)mCreditCardCaches = new LinkedHashMap<>();
        mCreditCardCaches.put(newCreditCard.getCardNumber(),newCreditCard);
    }

    @Override
    public void bill(String cardNumber, float money) {
        checkNotNull(cardNumber);
        checkNotNull(money);
        CreditCard creditCard = getCardWithNum(cardNumber);
        if(creditCard!=null){
            bill(creditCard,money);
        }
    }


}
