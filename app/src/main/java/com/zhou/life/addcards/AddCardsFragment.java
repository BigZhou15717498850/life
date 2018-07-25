package com.zhou.life.addcards;

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
import com.zhou.life.data.CreditCard;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author 周志新
 * @date 2018/7/25 20:05
 * @description
 */
public class AddCardsFragment extends Fragment implements AddCardsConstract.View {

    private AddCardsConstract.Presenter mPresenter;
    private EditText mBankname;
    private EditText mCardNumber;
    private EditText mBill;
    private EditText mRepaymentCount;
    private TextView mRepaymentDate;
    private TextView mBillDate;

    private TextView mCurTvDate;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.addcard_frag, container, false);
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

        FloatingActionButton floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab_confirm_creditcard);
        floatingActionButton.setOnClickListener(__->mPresenter.confirm());
        return root;
    }

    public static AddCardsFragment newInstance() {
        return new AddCardsFragment();
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
            CreditCard creditCard = new CreditCard(bankname,cardNum,dateBill,repaymentDate,bill,repayment);
            mPresenter.saveCreditCard(creditCard);
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
    public void setPresenter(AddCardsConstract.Presenter presenter) {
            this.mPresenter = presenter;
    }
}
