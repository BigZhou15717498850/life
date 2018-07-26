package com.zhou.life.addcards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.zhou.life.InJection;
import com.zhou.life.R;
import com.zhou.life.utils.ActivityUtils;

/**
 * @author 周志新
 * @date 2018/7/25 20:03
 * @description
 */
public class AddCardsActivity extends AppCompatActivity {

    public static final int ADD_CARDS_CODE = 0x01;
    public static void start(Activity activity) {
        Intent starter = new Intent(activity, AddCardsActivity.class);
        activity.startActivityForResult(starter,ADD_CARDS_CODE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcard_act);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AddCardsFragment fragment = (AddCardsFragment) getSupportFragmentManager().findFragmentById(R.id.frame);

        if (fragment==null) {
            fragment = AddCardsFragment.newInstance();
            ActivityUtils.addFragment(getSupportFragmentManager(),fragment,R.id.frame);
        }

        AddCardsPresenter presenter = new AddCardsPresenter(InJection.provideCreditCardRespository(this),fragment);


    }
}
