package com.zhou.life.cardsdetail;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.zhou.life.R;
import com.zhou.life.addcards.AddCardsConstract;
import com.zhou.life.addcards.AddCardsFragment;
import com.zhou.life.data.CreditCard;

/**
 * 作者 LY309313
 * 日期 2018/7/26
 * 描述
 */

public class CreditCardDetailFragment extends Fragment implements CreditCardDetailContract.View {

    private CreditCardDetailContract.Presenter mPresenter;
    private EditText mBankname;
    private EditText mCardNumber;
    private EditText mBill;
    private EditText mRepaymentCount;
    private TextView mRepaymentDate;
    private TextView mBillDate;
    private TextView mCurTvDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.carddetail_frag, container, false);
        mBankname = (EditText) root.findViewById(R.id.et_bankname);
        mCardNumber = (EditText) root.findViewById(R.id.et_card_number);
        mBill = (EditText) root.findViewById(R.id.et_bill);
        mRepaymentCount = (EditText) root.findViewById(R.id.et_repayment_count);
        mRepaymentDate = (TextView) root.findViewById(R.id.tv_select_date_repayment);
        mBillDate = (TextView) root.findViewById(R.id.tv_select_date_bill);
        mRepaymentDate.setOnClickListener(view->{
            mCurTvDate = ((TextView) view);
            mPresenter.selectTime();
        } );

        mBillDate.setOnClickListener(view->{
            mCurTvDate = ((TextView) view);
            mPresenter.selectTime();
        } );

        FloatingActionButton floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab_update_creditcard);
        floatingActionButton.setOnClickListener(__->mPresenter.confirm());
        return root;
    }

    public static CreditCardDetailFragment newInstance() {
        return new CreditCardDetailFragment();
    }

    @Override
    public void showConfirmDialog() {
        String bankname = mBankname.getText().toString();
        String cardNum = mCardNumber.getText().toString();
        String dateBill = mBillDate.getText().toString();
        String sbill = mBill.getText().toString();
        String repaymentDate = mRepaymentDate.getText().toString();
        String srepayment = mRepaymentCount.getText().toString();
        if(Strings.isNullOrEmpty(bankname)||Strings.isNullOrEmpty(cardNum)||Strings.isNullOrEmpty(dateBill)||Strings.isNullOrEmpty(repaymentDate)){
            showMessage(getResources().getString(R.string.imcomplete_information));
        }else{
            float bill = Strings.isNullOrEmpty(sbill)?0.0f:Float.parseFloat(sbill);
            float repayment = Strings.isNullOrEmpty(srepayment)?0.0f:Float.parseFloat(sbill);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(bankname).append("\n")
                    .append(cardNum).append("\n")
                    .append(dateBill).append("\n")
                    .append(sbill).append("\n")
                    .append(repaymentDate).append("\n")
                    .append(srepayment);
            CreditCard creditCard = new CreditCard(bankname,cardNum,dateBill,repaymentDate,bill,repayment);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getResources().getString(R.string.add_cards_information_confirm))
                    .setMessage(stringBuilder.toString())
                    .setPositiveButton(getResources().getString(R.string.confirm),(v,n)->mPresenter.saveCard(creditCard))
                    .setNegativeButton(getResources().getString(R.string.cancel),(v,n)->v.dismiss()).show();
        }
    }

    private void showMessage(String message) {
        Snackbar.make(getActivity().findViewById(R.id.frame), message,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
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
    public void showDatePicker(int year, int month, int day) {
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mPresenter.selectTime(year,month,day);
            }
        },year,month,day);

        dialog.setCancelable(true);
        dialog.show();

    }

    @Override
    public void showDate(String date) {
        mCurTvDate.setText(date);
    }

    @Override
    public void updateOk() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void setPresenter(CreditCardDetailContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showCardEmptyError() {
        showMessage(getResources().getString(R.string.no_card));
    }

    @Override
    public void setBankname(String backname) {
        mBankname.setText(backname);
    }

    @Override
    public void setCreditCardNumber(String cardNumber) {
        mCardNumber.setText(cardNumber);
    }

    @Override
    public void setDateBill(String dateBill) {
        mBillDate.setText(dateBill);
    }

    @Override
    public void setDatePayment(String datePayment) {
        mRepaymentDate.setText(datePayment);
    }

    @Override
    public void setBill(float bill) {
        mBill.setText(bill+"");
    }

    @Override
    public void setRepayment(float repayment) {
        mRepaymentCount.setText(repayment+"");
    }

}
