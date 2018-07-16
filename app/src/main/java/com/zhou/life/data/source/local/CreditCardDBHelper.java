package com.zhou.life.data.source.local;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Factory;
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory;
import android.content.Context;

import com.orhanobut.logger.Logger;
import com.squareup.sqlbrite3.BriteDatabase;
import com.squareup.sqlbrite3.SqlBrite;

import io.reactivex.schedulers.Schedulers;

/**
 * 作者 ly309313
 * 日期 2018/7/16
 * 描述
 */

public class CreditCardDBHelper {

    private static CreditCardDBHelper mInstance;

    private static final String DATABASE_NAME = "life.db";
    public static CreditCardDBHelper getInstance() {
        if(mInstance ==null){
            return new CreditCardDBHelper();
        }
        return mInstance;
    }

    public SqlBrite privodeSqlBrite(){
       return new SqlBrite.Builder().logger(new SqlBrite.Logger() {
           @Override
           public void log(String message) {
               Logger.d(message);
           }
       }).build();
    }

    public SupportSQLiteOpenHelper privodeBriteDatabase(Context context){
        Configuration configuration = Configuration.builder(context)
                .name(DATABASE_NAME)
                .callback(new CreditCardCallback())
                .build();
        Factory factory = new FrameworkSQLiteOpenHelperFactory();
        return factory.create(configuration);
    }
}
