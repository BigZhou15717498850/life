package com.zhou.life.data.source.local;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.common.base.Optional;
import com.squareup.sqlbrite3.BriteDatabase;
import com.squareup.sqlbrite3.SqlBrite;
import com.zhou.life.data.CreditCard;
import com.zhou.life.data.source.CreditCardDataSource;
import com.zhou.life.utils.schedulers.BaseSchedulerProvider;
import com.zhou.life.data.source.local.CreditCardPersistenceContract.CreditCardEntry;

import java.util.List;



import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 作者 ly309313
 * 日期 2018/7/16
 * 描述
 */

public class CreditCardLocalDataSource implements CreditCardDataSource {

    @Nullable
    private static CreditCardLocalDataSource INSTANCE;

    @NonNull
    private final BriteDatabase mDatabaseHelper;
    @NonNull
    private final Function<Cursor,CreditCard> mCreditCardMapperFunction;


    private CreditCardLocalDataSource(
            @NonNull Context context,
            @NonNull BaseSchedulerProvider baseSchedulerProvider) {
        checkNotNull(context,"create CreditCardLocalDataSource context can not be null!!!");
        checkNotNull(baseSchedulerProvider,"create CreditCardLocalDataSource baseSchedulerProvider can not be null!!!");
        SqlBrite sqlBrite = CreditCardDBHelper.getInstance().privodeSqlBrite();
        SupportSQLiteOpenHelper supportSQLiteOpenHelper = CreditCardDBHelper.getInstance().privodeBriteDatabase(context);
        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(supportSQLiteOpenHelper, baseSchedulerProvider.io());
        mCreditCardMapperFunction = this::getCreditCard;
    }

    @Nullable
    public static CreditCardLocalDataSource getInstance(@NonNull Context context,
                                                        @NonNull BaseSchedulerProvider baseSchedulerProvider) {
        if (INSTANCE == null) {
            INSTANCE = new CreditCardLocalDataSource(context,baseSchedulerProvider);
        }
        return INSTANCE;
    }

    public static void destoryInstance(){
        INSTANCE = null;
    }

    @NonNull
    private CreditCard getCreditCard(@NonNull Cursor cursor) {
        String bankname = cursor.getString(cursor.getColumnIndex(CreditCardEntry.BANK_NAME));
        String cardNum = cursor.getString(cursor.getColumnIndex(CreditCardEntry.CARD_NUM));
        String datebill = cursor.getString(cursor.getColumnIndex(CreditCardEntry.DATE_BILL));
        String dateRepayment = cursor.getString(cursor.getColumnIndex(CreditCardEntry.DATE_REPAYMENT));
        float bill = cursor.getFloat(cursor.getColumnIndex(CreditCardEntry.BILL));
        float repayment = cursor.getFloat(cursor.getColumnIndex(CreditCardEntry.REPAYMENT));
        return new CreditCard(bankname, cardNum, datebill, dateRepayment, bill, repayment);
    }

    @Override
    public Flowable<List<CreditCard>> getCreditCards() {
        String[] projection = {
                CreditCardEntry.BANK_NAME,
                CreditCardEntry.CARD_NUM,
                CreditCardEntry.DATE_BILL,
                CreditCardEntry.DATE_REPAYMENT,
                CreditCardEntry.BILL,
                CreditCardEntry.REPAYMENT
        };
        String sql = String.format("SELECT %s FROM %s", TextUtils.join(",",projection),CreditCardEntry.TABLE_NAME);
        return mDatabaseHelper.createQuery(CreditCardEntry.TABLE_NAME,sql)
                .mapToList(mCreditCardMapperFunction)
                .toFlowable(BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<Optional<CreditCard>> getCreditCard(String cardNumber) {
        String[] projection = {
                CreditCardEntry.BANK_NAME,
                CreditCardEntry.CARD_NUM,
                CreditCardEntry.DATE_BILL,
                CreditCardEntry.DATE_REPAYMENT,
                CreditCardEntry.BILL,
                CreditCardEntry.REPAYMENT
        };
        String sql = String.format("SELECT %s FROM %s WHERE %s LIKE %s",
                TextUtils.join(",",projection),CreditCardEntry.TABLE_NAME,
                CreditCardEntry.CARD_NUM,cardNumber);
        return mDatabaseHelper.createQuery(CreditCardEntry.TABLE_NAME,sql)
                .mapToOneOrDefault(cursor -> Optional.of(mCreditCardMapperFunction.apply(cursor)),Optional.<CreditCard>absent())
                .toFlowable(BackpressureStrategy.BUFFER);
    }

    @Override
    public void saveCreditCard(CreditCard creditCard) {
            checkNotNull(creditCard);
        ContentValues values = new ContentValues();
        values.put(CreditCardEntry.BANK_NAME,creditCard.getBankname());
        values.put(CreditCardEntry.CARD_NUM,creditCard.getBankname());
        values.put(CreditCardEntry.DATE_BILL,creditCard.getBankname());
        values.put(CreditCardEntry.DATE_REPAYMENT,creditCard.getBankname());
        values.put(CreditCardEntry.BILL,creditCard.getBankname());
        values.put(CreditCardEntry.REPAYMENT,creditCard.getBankname());
        mDatabaseHelper.insert(CreditCardEntry.TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE,values);
    }

    @Override
    public void deleteCreditCard(String cardNumber) {
        checkNotNull(cardNumber);
        String projection = CreditCardEntry.CARD_NUM + " LIKE ?";
        String[] args = {cardNumber};
        mDatabaseHelper.delete(CreditCardEntry.TABLE_NAME,projection,args);
    }

    @Override
    public void bill(CreditCard creditCard, float money) {
        checkNotNull(creditCard);
        bill(creditCard.getCardNumber(),money);
        ContentValues values = new ContentValues();
        values.put(CreditCardEntry.BILL,money);
        values.put(CreditCardEntry.REPAYMENT,"");

        String selection = CreditCardEntry.CARD_NUM + " LIKE ?";
        String[] args = {creditCard.getCardNumber()};
        mDatabaseHelper.update(CreditCardEntry.TABLE_NAME,SQLiteDatabase.CONFLICT_IGNORE,values,selection,args);
    }

    @Override
    public void repayment(CreditCard creditCard, float money) {
        checkNotNull(creditCard);
        float totalPayment = creditCard.getRepayment() + money;
        ContentValues values = new ContentValues();
        values.put(CreditCardEntry.REPAYMENT,totalPayment);
        if(totalPayment >= creditCard.getBill()){
            values.put(CreditCardEntry.DATE_REPAYMENT,creditCard.getNextDateRepayment());
            values.put(CreditCardEntry.DATE_BILL,creditCard.getNextDateBill());
            values.put(CreditCardEntry.BILL,0.0f);
        }
        String selection = CreditCardEntry.CARD_NUM + " LIKE ?";
        String[] args = {creditCard.getCardNumber()};
        mDatabaseHelper.update(CreditCardEntry.TABLE_NAME,SQLiteDatabase.CONFLICT_IGNORE,values,selection,args);
    }

    @Override
    public void bill(String cardNumber, float money) {
        checkNotNull(cardNumber);
        ContentValues values = new ContentValues();
        values.put(CreditCardEntry.BILL,money);
        values.put(CreditCardEntry.REPAYMENT,"");

        String selection = CreditCardEntry.CARD_NUM + " LIKE ?";
        String[] args = {cardNumber};
        mDatabaseHelper.update(CreditCardEntry.TABLE_NAME,SQLiteDatabase.CONFLICT_IGNORE,values,selection,args);
    }



}
