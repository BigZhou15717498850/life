package com.zhou.life.cards;

import com.google.common.collect.Lists;
import com.zhou.life.data.CreditCard;
import com.zhou.life.data.source.CreditCardRespository;
import com.zhou.life.utils.schedulers.BaseSchedulerProvider;
import com.zhou.life.utils.schedulers.ImmediateSchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import io.reactivex.Flowable;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 作者 ly309313
 * 日期 2018/7/24
 * 描述
 */

public class CardsPresenterTest {

    private List<CreditCard> mCreditCards;

    @Mock
    private CreditCardRespository mCreditCardRespository;
    @Mock
    private CreditCardsContract.View mCardsView;

    private CreditCardPresenter mCreditCardPresenter;

    private BaseSchedulerProvider mBaseSchedulerProvider;

    @Before
    public void setUp(){

        MockitoAnnotations.initMocks(this);

        mBaseSchedulerProvider = new ImmediateSchedulerProvider();

        mCreditCardPresenter = new CreditCardPresenter(mCreditCardRespository,mCardsView,mBaseSchedulerProvider);

        when(mCardsView.isActive()).thenReturn(true);

        mCreditCards = Lists.newArrayList(
                new CreditCard("中国人民银行","1111111","0","0"),
                new CreditCard("光大银行","222222","1234","2222"),
                new CreditCard("兴业银行","333333","34443","322"));
    }
    @Test
    public void createPresenter_setsPresenterToView(){
        mCreditCardPresenter = new CreditCardPresenter(mCreditCardRespository,mCardsView,mBaseSchedulerProvider);

        verify(mCardsView).setPresenter(mCreditCardPresenter);
    }
    @Test
    public void loadAllCardsFromRespositoryLoadIntoView(){
        when(mCreditCardRespository.getCreditCards()).thenReturn(Flowable.just(mCreditCards));

        mCreditCardPresenter.setFilterType(CreditCardFilterType.ALL_CARDS);
        mCreditCardPresenter.loadCreditCards(true);

        verify(mCardsView).showLoadingIndicator(true);
        verify(mCardsView).showLoadingIndicator(false);

    }
    @Test
    public void loadActiveCardsFromRespositoryLoadIntoView(){
        when(mCreditCardRespository.getCreditCards()).thenReturn(Flowable.just(mCreditCards));

        mCreditCardPresenter.setFilterType(CreditCardFilterType.ACTIVE_CARDS);
        mCreditCardPresenter.loadCreditCards(true);

       // verify(mCardsView).showLoadingIndicator(true);
        verify(mCardsView).showLoadingIndicator(false);
    }
    @Test
    public void loadCompletedCardsFromRespositoryLoadIntoView(){
        when(mCreditCardRespository.getCreditCards()).thenReturn(Flowable.just(mCreditCards));

        mCreditCardPresenter.setFilterType(CreditCardFilterType.COMPLETED_CARDS);
        mCreditCardPresenter.loadCreditCards(true);

        // verify(mCardsView).showLoadingIndicator(true);
        verify(mCardsView).showLoadingIndicator(false);

    }
    @Test
    public void fabOnClick_addCreditCards(){
//        CreditCard creditCard = new CreditCard("国大银行","22332","06-23","07-08");
        mCreditCardPresenter.addNewCreditCards();
        verify(mCardsView).showAddCreditCards();
    }
    @Test
    public void editCards_clickOnItem(){
        CreditCard creditCard = new CreditCard("国大银行","22332","06-23","07-08");
        mCreditCardPresenter.openCreditCardDetials(creditCard);

        verify(mCardsView).showCreditCardDetailsUi(creditCard.getCardNumber());
    }
    @Test
    public void errorLoadingCards_showError(){
        when(mCreditCardRespository.getCreditCards()).thenReturn(Flowable.error(new Exception()));
        mCreditCardPresenter.setFilterType(CreditCardFilterType.ALL_CARDS);
        mCreditCardPresenter.loadCreditCards(true);

        verify(mCardsView).showLoadingCardsError();
    }
}
