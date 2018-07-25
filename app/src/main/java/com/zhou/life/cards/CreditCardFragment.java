package com.zhou.life.cards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.zhou.life.R;
import com.zhou.life.addcards.AddCardsActivity;
import com.zhou.life.data.CreditCard;

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
    };
    private TextView mFilterLabel;
    private View mllCards;
    private View mllNoCards;
    private ImageView mIvNoCards;
    private TextView mTvNoCardHint;
    private TextView mTvNoCardAdd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new CreditCardAdapter(new ArrayList<>(),cardItemClickListener);
    }

    public static CreditCardFragment newInstance() {
        return new CreditCardFragment();
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.creditcard_frag, container, false);
        mFilterLabel = (TextView) root.findViewById(R.id.tv_filter_label);
        mllCards = root.findViewById(R.id.ll_cards);
        RecyclerView mCards = (RecyclerView) root.findViewById(R.id.cards);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mCards.setHasFixedSize(true);
        mCards.setLayoutManager(layoutManager);
        mCards.setAdapter(adapter);

        mllNoCards = root.findViewById(R.id.ll_nocards);
        mIvNoCards = (ImageView) root.findViewById(R.id.iv_no_cards);
        mTvNoCardHint = (TextView) root.findViewById(R.id.tv_no_cards_hint);
        mTvNoCardAdd = (TextView) root.findViewById(R.id.tv_no_cards_add);

        FloatingActionButton addNewCreditCard = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_creditcard);
        addNewCreditCard.setOnClickListener(__->mPresenter.addNewCreditCards());

        setHasOptionsMenu(true);
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
        showNoCreditCardsView(
                R.drawable.ic_assignment_turned_in_24dp,
                getResources().getString(R.string.no_creditcards),
                true);
    }

    private void showNoCreditCardsView(int image,String hint,boolean showAddView){
        mllCards.setVisibility(View.GONE);
        mllNoCards.setVisibility(View.VISIBLE);

        mIvNoCards.setImageDrawable(getResources().getDrawable(image));
        mTvNoCardHint.setText(hint);
        mTvNoCardAdd.setVisibility(showAddView?View.VISIBLE:View.GONE);
    }
    @Override
    public void showCreditCards(List<CreditCard> creditCards) {
        adapter.refreshData(creditCards);

        mllNoCards.setVisibility(View.GONE);
        mllCards.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCreditCardDetailsUi(String cardNum) {
        // TODO: 2018/7/24 跳转
    }

    @Override
    public void showNoCompletedCreditCards() {
        showNoCreditCardsView(
                R.drawable.ic_verified_user_24dp,
                getResources().getString(R.string.no_completed_creditcards),
                false);
    }

    @Override
    public void showNoActiveCreditCards() {
        showNoCreditCardsView(
                R.drawable.ic_check_circle_24dp,
                getResources().getString(R.string.no_active_creditcards),false
        );
    }

    @Override
    public void showAllCardsFilter() {
        mFilterLabel.setText(getResources().getString(R.string.all_cards));
    }

    @Override
    public void showCompletedCreditCardsFilter() {
        mFilterLabel.setText(getResources().getString(R.string.all_completed_cards));
    }

    @Override
    public void showActiveCreditCardFileter() {
        mFilterLabel.setText(getResources().getString(R.string.all_active_cards));
    }

    @Override
    public void showAddCreditCards() {
        // TODO: 2018/7/24 跳到添加新卡片页面
        AddCardsActivity.start(getActivity());
    }


    @Override
    public void showCreateNewCreditCardSuccessFully() {
        showMessage(getResources().getString(R.string.create_new_card_success));
    }

    private void showMessage(String string) {
        Snackbar.make(mllCards, string,Snackbar.LENGTH_SHORT);
    }

    @Override
    public void showEditCreditCardSuccessFully() {
        showMessage(getResources().getString(R.string.edit_card_success));
    }

    @Override
    public boolean isActive() {
       return isAdded();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_filter:
                showCardFilterPopMenu();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cards_fagment_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void showCardFilterPopMenu() {
        PopupMenu popupMenu = new PopupMenu(getContext(),getActivity().findViewById(R.id.menu_filter));
        popupMenu.getMenuInflater().inflate(R.menu.cards_popupmenu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item->{
            switch (item.getItemId()){
                case R.id.all_filter:
                    mPresenter.setFilterType(CreditCardFilterType.ALL_CARDS);
                    break;
                case R.id.active_filter:
                    mPresenter.setFilterType(CreditCardFilterType.ACTIVE_CARDS);
                    break;
                case R.id.completed_filter:
                    mPresenter.setFilterType(CreditCardFilterType.COMPLETED_CARDS);
                    break;
            }
            mPresenter.loadCreditCards(false);
            return true;
        });
        popupMenu.show();
    }

    @Override
    public void showLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingCardsError() {

    }

    @Override
    public void setPresenter(CreditCardsContract.Presenter presenter) {
            checkNotNull(presenter);
            this.mPresenter = presenter;
    }




    public interface CardItemClickListener{
        void onCardClick(CreditCard creditCard);
    }

    private static class CreditCardAdapter extends RecyclerView.Adapter<CreditCardAdapter.CreditCardViewHolder>{

        private List<CreditCard> creditCards;
        private CardItemClickListener mCardItemClickListener;

        public CreditCardAdapter(List<CreditCard> creditCards, CardItemClickListener mCardItemClickListener) {
            setList(creditCards);
            this.mCardItemClickListener = mCardItemClickListener;
        }

       public void  refreshData(List<CreditCard> creditCards){
            setList(creditCards);
            notifyDataSetChanged();
       }

        private void setList(List<CreditCard> creditCards){
            checkNotNull(creditCards);
            this.creditCards = creditCards;
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
