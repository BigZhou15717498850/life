package com.zhou.life.utils;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * 作者 ly309313
 * 日期 2018/7/24
 * 描述
 */

public class ActivityUtils {

    public static void addFragment(@NonNull FragmentManager fragmentManager,@NonNull Fragment fragment, int fragmentId){
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(fragmentId,fragment);
        transaction.commit();
    }
}
