package com.zhou.life.cards;

import android.app.Activity;
import android.content.Intent;
import android.widget.TableRow;

import com.orhanobut.logger.Logger;
import com.zhou.life.addcards.AddCardsActivity;
import com.zhou.life.data.CreditCard;
import com.zhou.life.data.source.CreditCardRespository;
import com.zhou.life.utils.schedulers.BaseSchedulerProvider;

import java.util.List;

import io.reactivex.Flowable;
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

    public CreditCardPresenter(@NonNull CreditCardRespository mCreditCardRespository,
                               @NonNull CreditCardsContract.View mView,
                               @NonNull BaseSchedulerProvider mSchedulerProvider) {
        this.mCreditCardRespository = checkNotNull(mCreditCardRespository);
        this.mView = checkNotNull(mView);
        this.mSchedulerProvider = checkNotNull(mSchedulerProvider);
        mCompositeDisposable = new CompositeDisposable();
        this.mView.setPresenter(this);
    }

    @Override
    public void loadCreditCards(boolean showLoadingUI) {
        if (showLoadingUI) {
            mView.showLoadingIndicator(true);
        }
        mCompositeDisposable.clear();
        Disposable disposable = mCreditCardRespository
                .getCreditCards()
//                .flatMap(Flowable::fromIterable)
//                .filter(creditCard -> {
//                    Logger.i("获取到卡片%s",creditCard.toString());
//                    switch (mCurrFilterType){
//                        case ACTIVE_CARDS:
//                            return creditCard.paymentActive();
//                        case COMPLETED_CARDS:
//                            return creditCard.paymentComplete();
//                        case ALL_CARDS:
//                            default:
//                            return true;
//                    }
//                })
//                .toList()
                .subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(new Consumer<List<CreditCard>>() {
                    @Override
                    public void accept(List<CreditCard> creditCards) throws Exception {
                        Logger.i("获取到卡片数量:%s",creditCards.size());
                        processTasks(creditCards);
                        mView.showLoadingIndicator(false);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.i("出错了%s",throwable.getMessage());
                        mView.showLoadingCardsError();
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    private void processTasks(@NonNull List<CreditCard> creditCards) {
        if(creditCards.isEmpty()){
            processEmptyCards();
        }else{
            mView.showCreditCards(creditCards);
            showFilterLabel();
        }
    }

    private void showFilterLabel() {
        switch (mCurrFilterType){
            case ACTIVE_CARDS:
                mView.showActiveCreditCardFileter();
                break;
            case COMPLETED_CARDS:
                mView.showCompletedCreditCardsFilter();
                break;
            case ALL_CARDS:
            default:
                mView.showAllCardsFilter();
                break;
        }

    }

    private void processEmptyCards() {
        switch (mCurrFilterType){
            case ACTIVE_CARDS:
                mView.showNoActiveCreditCards();
                break;
            case COMPLETED_CARDS:
                mView.showNoCompletedCreditCards();
                break;
            case ALL_CARDS:
            default:
                mView.showNoCreditCards();
                break;
        }
    }

    @Override
    public void addNewCreditCards() {
        mView.showAddCreditCards();
    }

//    @Override
//    public void payment(CreditCard creditCard, float money) {
//        checkNotNull(creditCard);
//        mCreditCardRespository.repayment(creditCard,money);
//        loadCreditCards();
//    }
//
//    @Override
//    public void bill(CreditCard creditCard, float money) {
//        checkNotNull(creditCard);
//        mCreditCardRespository.bill(creditCard,money);
//        loadCreditCards();
//
//    }

    @Override
    public void openCreditCardDetials(CreditCard creditCard) {
        checkNotNull(creditCard);
        mView.showCreditCardDetailsUi(creditCard.getCardNumber());
    }

    @Override
    public void onResult(int requestCode, int responseCode) {
        if(requestCode== AddCardsActivity.ADD_CARDS_CODE && responseCode== Activity.RESULT_OK){
            mView.showCreateNewCreditCardSuccessFully();
        }
    }

    @Override
    public void setFilterType(CreditCardFilterType filterType) {
        mCurrFilterType = filterType;

    }

    @Override
    public CreditCardFilterType getCurrFilterType() {
        return mCurrFilterType;
    }

    @Override
    public void subcribe() {
        loadCreditCards(true);
    }

    @Override
    public void unSubcribe() {
        mCompositeDisposable.clear();
    }
}
