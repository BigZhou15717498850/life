package com.zhou.life.data.source;

import com.google.common.base.Optional;
import com.zhou.life.data.CreditCard;

import java.util.List;


import io.reactivex.Flowable;

/**
 * 作者 ly309313
 * 日期 2018/7/16
 * 描述
 */

public interface CreditCardDataSource {

    Flowable<List<CreditCard>> getCreditCards();

    Flowable<Optional<CreditCard>> getCreditCard(String cardNumber);

    void saveCreditCard(CreditCard creditCard);

    void deleteCreditCard(String cardNumber);

    /**
     * 产生账单的时候，需要清空上月还款
     * @param creditCard
     * @param money
     */
    void bill(CreditCard creditCard,float money);

    /**
     * 如果款项已还清，则账单日和还款日更新到下一个月
     * 如果
     * @param creditCard
     * @param money
     */
    void repayment(CreditCard creditCard,float money);

    void bill(String cardNumber,float money);

}
