package com.zhou.life.data.source.local;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import com.zhou.life.data.source.local.CreditCardPersistenceContract.CreditCardEntry;
import org.w3c.dom.ProcessingInstruction;

/**
 * 作者 ly309313
 * 日期 2018/7/16
 * 描述
 */

public class CreditCardCallback extends SupportSQLiteOpenHelper.Callback {

    private static final int DATABASE_VERSION = 1;

    private static final String TYPE_TEXT = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_CREDITCARD_DB =
            "CREATE TABLE "+ CreditCardEntry.TABLE_NAME + "("
            + CreditCardEntry.CARD_NUM + TYPE_TEXT + " PRIMARY KEY" + COMMA_SEP
            + CreditCardEntry.BANK_NAME + TYPE_TEXT + COMMA_SEP
            + CreditCardEntry.DATE_BILL + TYPE_TEXT + COMMA_SEP
            + CreditCardEntry.DATE_REPAYMENT + TYPE_TEXT + COMMA_SEP
            + CreditCardEntry.BILL + TYPE_TEXT + COMMA_SEP
            + CreditCardEntry.REPAYMENT + TYPE_TEXT + ")";

    public CreditCardCallback() {
        super(DATABASE_VERSION);
    }

    @Override
    public void onCreate(SupportSQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CREDITCARD_DB);
    }

    @Override
    public void onUpgrade(SupportSQLiteDatabase db, int oldVersion, int newVersion) {
            //
    }
}
