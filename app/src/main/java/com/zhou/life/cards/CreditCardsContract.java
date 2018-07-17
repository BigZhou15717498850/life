package com.zhou.life.cards;

import com.zhou.life.BasePresenter;
import com.zhou.life.BaseView;
import com.zhou.life.data.CreditCard;

import java.util.List;

/**
 * @author 周志新
 * @date 2018/7/17 22:08
 * @description
 */
public class CreditCardsContract {


    interface View extends BaseView<Presenter>{

       void showNoCreditCards();

       void showCreditCards(List<CreditCard> creditCards);

       void showCreditCardDetailsUi(String cardNum);

       void showNoCompletedCreditCards();

       void showNoActiveCreditCards();

       void showAllCardsFilter();

       void showCompletedCreditCardsFilter();

       void showActiveCreditCardFileter();

       void showCreateNewCreditCardSuccessFully();

       void isActive();

       void showCardFilterPopMenu();
    }

    interface Presenter extends BasePresenter{

        void loadCreditCards();

        void addNewCreditCards();

        void payment(CreditCard creditCard,float money);

        void bill(CreditCard creditCard,float money);

        void openCreditCardDetials(CreditCard creditCard);

        void setFilterType(CreditCardFilterType filterType);

        CreditCardFilterType getCurrFilterType();
    }
}
