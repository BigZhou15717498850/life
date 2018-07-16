package com.zhou.life.utils.schedulers;

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;


/**
 * 作者 ly309313
 * 日期 2018/7/16
 * 描述
 */

public interface BaseSchedulerProvider {

    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();
}
