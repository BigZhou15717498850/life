package com.zhou.life.data.source.local;

import android.provider.BaseColumns;

/**
 * 作者 ly309313
 * 日期 2018/7/16
 * 描述
 */

public class CreditCardPersistenceContract {

    private CreditCardPersistenceContract() {
    }

    public static abstract class CreditCardEntry implements BaseColumns{
        public static final String TABLE_NAME = "creditcard";
        public static final String CARD_NUM = "card_num";
        public static final String BANK_NAME = "bank_name";
        public static final String DATE_BILL = "date_bill";
        public static final String DATE_REPAYMENT = "date_repayment";
        public static final String BILL = "bill";
        public static final String REPAYMENT = "repayment";
    }
}
