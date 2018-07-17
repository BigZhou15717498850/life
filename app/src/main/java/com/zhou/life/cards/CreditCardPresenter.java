package com.zhou.life.cards;

import com.zhou.life.data.CreditCard;
import com.zhou.life.data.source.CreditCardRespository;
import com.zhou.life.utils.schedulers.BaseSchedulerProvider;
import com.zhou.life.utils.schedulers.SchedulerProvider;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author 周志新
 * @date 2018/7/17 22:26
 * @description
 */
public class CreditCardPresenter implements CreditCardsContract.Presenter {

    @NonNull
    private final CreditCardRespository mCreditCardRespository;
    @NonNull
    private final CreditCardsContract.View mView;
    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    private CreditCardFilterType mCurrFilterType = CreditCardFilterType.ALL_CARDS;
    @NonNull
    private final CompositeDisposable mCompositeDisposable;

    public CreditCardPresenter(CreditCardRespository mCreditCardRespository, CreditCardsContract.View mView, BaseSchedulerProvider mSchedulerProvider) {
        this.mCreditCardRespository = checkNotNull(mCreditCardRespository);
        this.mView = checkNotNull(mView);
        this.mSchedulerProvider = mSchedulerProvider;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void loadCreditCards() {
        Disposable disposable = mCreditCardRespository.getCreditCards().observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<CreditCard>>() {
            @Override
            public void accept(List<CreditCard> creditCards) throws Exception {
                mView.showCreditCards(creditCards);
            }
        });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void addNewCreditCards() {

    }

    @Override
    public void payment(CreditCard creditCard, float money) {

    }

    @Override
    public void bill(CreditCard creditCard, float money) {

    }

    @Override
    public void openCreditCardDetials(CreditCard creditCard) {

    }

    @Override
    public void setFilterType(CreditCardFilterType filterType) {

    }

    @Override
    public CreditCardFilterType getCurrFilterType() {
        return null;
    }
}
