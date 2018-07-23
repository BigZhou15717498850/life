package com.zhou.life.cards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhou.life.R;
import com.zhou.life.data.CreditCard;

import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author 周志新
 * @date 2018/7/17 22:26
 * @description
 */
public class CreditCardFragment extends Fragment implements CreditCardsContract.View {

    private CreditCardsContract.Presenter mPresenter;

    private CreditCardAdapter adapter;

    CardItemClickListener cardItemClickListener = new CardItemClickListener() {
        @Override
        public void onCardClick(CreditCard creditCard) {
            mPresenter.openCreditCardDetials(creditCard);
        }

        @Override
        public void onBill(CreditCard creditCard,float money) {
            mPresenter.bill(creditCard,money);
        }

        @Override
        public void onPayment(CreditCard creditCard,float money) {
            mPresenter.payment(creditCard,money);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new CreditCardAdapter(new ArrayList<>(),cardItemClickListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.creditcard_frag, container, false);
        TextView filterLabel = (TextView) root.findViewById(R.id.tv_filter_label);

        return root;

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subcribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unSubcribe();
    }

    @Override
    public void showNoCreditCards() {

    }

    @Override
    public void showCreditCards(List<CreditCard> creditCards) {

    }

    @Override
    public void showCreditCardDetailsUi(String cardNum) {

    }

    @Override
    public void showNoCompletedCreditCards() {

    }

    @Override
    public void showNoActiveCreditCards() {

    }

    @Override
    public void showAllCardsFilter() {

    }

    @Override
    public void showCompletedCreditCardsFilter() {

    }

    @Override
    public void showActiveCreditCardFileter() {

    }

    @Override
    public void showAddCreditCards() {

    }

    @Override
    public void showCreditCardBilled() {

    }

    @Override
    public void showCreditCardRepayment() {

    }

    @Override
    public void showCreateNewCreditCardSuccessFully() {

    }

    @Override
    public void isActive() {

    }

    @Override
    public void showCardFilterPopMenu() {

    }

    @Override
    public void setPresenter(CreditCardsContract.Presenter presenter) {
            checkNotNull(presenter);
            this.mPresenter = presenter;
    }


    public interface CardItemClickListener{
        void onCardClick(CreditCard creditCard);

        void onBill(CreditCard creditCard,float money);

        void onPayment(CreditCard creditCard,float money);
    }

    private static class CreditCardAdapter extends RecyclerView.Adapter<CreditCardAdapter.CreditCardViewHolder>{

        private List<CreditCard> creditCards;
        private CardItemClickListener mCardItemClickListener;

        public CreditCardAdapter(List<CreditCard> creditCards, CardItemClickListener mCardItemClickListener) {
            this.creditCards = creditCards;
            this.mCardItemClickListener = mCardItemClickListener;
        }

        @Override
        public CreditCardAdapter.CreditCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View root = inflater.inflate(R.layout.creditcard_item, parent, false);
            final CreditCardViewHolder viewHolder = new CreditCardViewHolder(root);
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CreditCard creditCard = (CreditCard) viewHolder.tvBankname.getTag();
                    if(mCardItemClickListener!=null)mCardItemClickListener.onCardClick(creditCard);
                }
            });
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(CreditCardAdapter.CreditCardViewHolder holder, int position) {
            CreditCard creditCard = creditCards.get(position);
            holder.tvBankname.setText(creditCard.getBankname());
            holder.tvCardNum.setText(creditCard.getCardNumber());
            holder.tvDatebill.setText(creditCard.getDateBill());
            holder.tvDatePayment.setText(creditCard.getDateRepayment());
            holder.tvBill.setText("已出账单："+creditCard.getBill());
            holder.tvPayment.setText("已还款："+creditCard.getRepayment());
        }

        @Override
        public int getItemCount() {
            return creditCards.size();
        }

        class CreditCardViewHolder extends RecyclerView.ViewHolder{

            private final TextView tvBankname;
            private final TextView tvCardNum;
            private final TextView tvDatebill;
            private final TextView tvBill;
            private final TextView tvDatePayment;
            private final TextView tvPayment;

            public CreditCardViewHolder(View itemView) {
                super(itemView);
                tvBankname = (TextView) itemView.findViewById(R.id.tv_bankname);
                tvCardNum = (TextView) itemView.findViewById(R.id.tv_credicard_num);
                tvDatebill = (TextView) itemView.findViewById(R.id.tv_date_bill);
                tvBill = (TextView) itemView.findViewById(R.id.tv_bill);
                tvDatePayment = (TextView) itemView.findViewById(R.id.tv_date_payment);
                tvPayment = (TextView) itemView.findViewById(R.id.tv_payment);
            }
        }
    }
}
