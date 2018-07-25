package com.zhou.life.cards;

import android.content.Intent;

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

       void showAddCreditCards();

       void showCreateNewCreditCardSuccessFully();

        void showEditCreditCardSuccessFully();

       boolean isActive();

       void showCardFilterPopMenu();

       void showLoadingIndicator(boolean active);

       void showLoadingCardsError();
    }

    interface Presenter extends BasePresenter{

        void loadCreditCards(boolean showloadingUI);

        void addNewCreditCards();

        void openCreditCardDetials(CreditCard creditCard);

        void onResult(int requestCode, int responseCode);

        void setFilterType(CreditCardFilterType filterType);

        CreditCardFilterType getCurrFilterType();
    }
}
