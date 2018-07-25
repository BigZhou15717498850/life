package com.zhou.life;

import android.content.Context;

import com.zhou.life.data.source.CreditCardRespository;
import com.zhou.life.data.source.local.CreditCardLocalDataSource;
import com.zhou.life.utils.schedulers.BaseSchedulerProvider;
import com.zhou.life.utils.schedulers.SchedulerProvider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 作者 ly309313
 * 日期 2018/7/24
 * 描述
 */

public class InJection {

    public static CreditCardRespository provideCreditCardRespository(Context context){
            checkNotNull(context);
        return CreditCardRespository.getInstance(CreditCardLocalDataSource.getInstance(context,provideSchedulerProvider()));
    }

    public static BaseSchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }
}
