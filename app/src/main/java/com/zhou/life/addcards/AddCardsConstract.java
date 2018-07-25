package com.zhou.life.addcards;

import com.zhou.life.BasePresenter;
import com.zhou.life.BaseView;
import com.zhou.life.data.CreditCard;

/**
 * 作者 ly309313
 * 日期 2018/7/24
 * 描述
 */

public class AddCardsConstract {

    public interface View extends BaseView<Presenter>{

        void showConfirmDialog(CreditCard creditCard);

        boolean isActive();
    }

    public interface Presenter extends BasePresenter{

        void confirm(CreditCard creditCard);

        void saveCreditCard(CreditCard creditCard);

    }
}
