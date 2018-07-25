package com.zhou.life.cards;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.zhou.life.InJection;
import com.zhou.life.R;
import com.zhou.life.utils.ActivityUtils;


public class CreditCardActivity extends AppCompatActivity {

    private static final String CURRENT_FILTER_TYPE = "current_filter_type";
    private CreditCardFilterType mCreditCardFilterType;
    private CreditCardsContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creditcard_act);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CreditCardFragment fragment = (CreditCardFragment) getSupportFragmentManager().findFragmentById(R.id.frame);
        if (fragment == null) {
            fragment = CreditCardFragment.newInstance();
            ActivityUtils.addFragment(getSupportFragmentManager(), fragment, R.id.frame);
        }

        mPresenter = new CreditCardPresenter(
                InJection.provideCreditCardRespository(getApplicationContext()),
                fragment,
                InJection.provideSchedulerProvider());

        if (savedInstanceState != null) {
            mCreditCardFilterType = (CreditCardFilterType) savedInstanceState.getSerializable(CURRENT_FILTER_TYPE);
            mPresenter.setFilterType(mCreditCardFilterType);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(CURRENT_FILTER_TYPE,mPresenter.getCurrFilterType());
    }
}
