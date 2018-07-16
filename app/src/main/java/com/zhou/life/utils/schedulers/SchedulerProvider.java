package com.zhou.life.utils.schedulers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者 ly309313
 * 日期 2018/7/16
 * 描述
 */

public class SchedulerProvider implements BaseSchedulerProvider {

    @Nullable
    private static SchedulerProvider INSTANCE;

    private SchedulerProvider(){

    }
    @Nullable
    public static SchedulerProvider getInstance() {
        if(INSTANCE==null){
            INSTANCE = new SchedulerProvider();
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public Scheduler computation() {
        return Schedulers.computation();
    }

    @NonNull
    @Override
    public Scheduler io() {
        return Schedulers.io();
    }

    @NonNull
    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }
}
