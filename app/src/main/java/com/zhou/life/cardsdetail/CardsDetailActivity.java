package com.zhou.life.cardsdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.zhou.life.InJection;
import com.zhou.life.R;
import com.zhou.life.addcards.AddCardsFragment;
import com.zhou.life.addcards.AddCardsPresenter;
import com.zhou.life.cards.CreditCardFragment;
import com.zhou.life.utils.ActivityUtils;

/**
 * @author 周志新
 * @date 2018/7/25 20:03
 * @description
 */
public class CardsDetailActivity extends AppCompatActivity {

    public static final int EDIT_CARDS_CODE = 0x02;
    public static final String EDIT_CARDS_KEY = "edit_cards_key";
    public static void start(Activity activity,String cardNumber) {
        Intent starter = new Intent(activity, CardsDetailActivity.class);
        starter.putExtra(EDIT_CARDS_KEY,cardNumber);
        activity.startActivityForResult(starter,EDIT_CARDS_CODE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carddetail_act);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CreditCardDetailFragment fragment = (CreditCardDetailFragment) getSupportFragmentManager().findFragmentById(R.id.frame);

        if (fragment==null) {
            fragment = CreditCardDetailFragment.newInstance();
            ActivityUtils.addFragment(getSupportFragmentManager(),fragment,R.id.frame);
        }
        String cardNumber = getIntent().getStringExtra(EDIT_CARDS_KEY);
        CreditCardDetailPresenter presenter = new CreditCardDetailPresenter(cardNumber,
                InJection.provideCreditCardRespository(this),
                fragment,InJection.provideSchedulerProvider());

    }
}
