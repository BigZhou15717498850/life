package com.zhou.life.data.source;

import android.content.Context;

import com.google.common.collect.Lists;
import com.zhou.life.data.CreditCard;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 作者 ly309313
 * 日期 2018/7/16
 * 描述
 */

public class CreditCardRespositoryTest {


    private static final List<CreditCard> CREDIT_CARDS = Lists.newArrayList(
            new CreditCard("中国地大物博","123456789","07-12","07-26"),
            new CreditCard("新氏阿哥","987654321","07-17","07-22"),
            new CreditCard("中国地大","123456","07-10","07-21")
    );

    private CreditCardRespository mCreditCardRespository;

    private TestSubscriber<List<CreditCard>> mTestSubscriber;
    @Mock
    private CreditCardDataSource mCreditCardLocalDataSource;
    @Mock
    private Context mContext;

    @Before
    public void setupRespository(){
        MockitoAnnotations.initMocks(this);

        mCreditCardRespository = CreditCardRespository.getInstance(mCreditCardLocalDataSource);

        mTestSubscriber = new TestSubscriber<>();
    }

    @After
    public void destoryRespository(){
        CreditCardRespository.destoryInstance();
    }

    @Test
    public void getCrads_RespositoryCachesAfterFirstSubscription_WherCardsAvailableInLocalStorage(){
        //第一次调用，因为没有缓存，必然会从数据库去拿
        setCardsAvailable(mCreditCardLocalDataSource,CREDIT_CARDS);

        TestSubscriber<List<CreditCard>> testSubscriber = new TestSubscriber<>();
        //当从仓库拿的时候，因为第一次取
        mCreditCardRespository.getCreditCards().subscribe(testSubscriber);
        //所以，本地数据源必然会调用一次
        verify(mCreditCardLocalDataSource).getCreditCards();//判断的必须是mock的数据

        testSubscriber.assertValue(CREDIT_CARDS);

    }
    @Test
    public void getCards_requestsAllCardsFromLocalDataSource(){
        //测试从本地拿所有数据
        setCardsAvailable(mCreditCardLocalDataSource,CREDIT_CARDS);

        mCreditCardLocalDataSource.getCreditCards().subscribe(mTestSubscriber);

        verify(mCreditCardLocalDataSource).getCreditCards();

        mTestSubscriber.assertValue(CREDIT_CARDS);
    }

    @Test
    public void saveCard_savesCardToServiceApi(){
        //测试保存数据
        CreditCard creditCard = new CreditCard("中国人大","1234","08-23","09-12");

        mCreditCardRespository.saveCreditCard(creditCard);

        verify(mCreditCardLocalDataSource).saveCreditCard(creditCard);

        assertThat(mCreditCardRespository.mCreditCardCaches.size(),is(1));
    }

    @Test
    public void billCard_billsCardToServiceApi(){
        CreditCard creditCard = new CreditCard("中国人大","1234","07-08","07-18");

        mCreditCardRespository.saveCreditCard(creditCard);
        mCreditCardRespository.bill(creditCard,"1256");

        verify(mCreditCardLocalDataSource).bill(creditCard,"1256");

        assertThat(mCreditCardRespository.mCreditCardCaches.size(),is(1));
        assertThat(mCreditCardRespository.mCreditCardCaches.get(creditCard.getCardNumber()).getBill(),is("1256"));
    }

    @Test
    public void billCard_billsCardNoOverBillDateToServiceApi(){
        CreditCard creditCard = new CreditCard("中国人大","1234","08-08","08-18");

        mCreditCardRespository.saveCreditCard(creditCard);
        mCreditCardRespository.bill(creditCard,"1256");

        verify(mCreditCardLocalDataSource).bill(creditCard,"1256");

        assertThat(mCreditCardRespository.mCreditCardCaches.size(),is(1));
        assertThat(mCreditCardRespository.mCreditCardCaches.get(creditCard.getCardNumber()).getBill(),is(""));
    }

    public void repaymentCard_repaymentsCardToServiceApi(){
        CreditCard creditCard = new CreditCard("中国人大","1234","08-08","08-18","5623");

        mCreditCardRespository.saveCreditCard(creditCard);
        //mCreditCardRespository
    }
    private void setCardsAvailable(CreditCardDataSource dataSource,List<CreditCard> creditCards) {
        when(dataSource.getCreditCards()).thenReturn(Flowable.just(creditCards).concatWith(Flowable.never()));
    }


}
