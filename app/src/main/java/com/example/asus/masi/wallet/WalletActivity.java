package com.example.asus.masi.wallet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.masi.Account;
import com.example.asus.masi.AppDatabase;
import com.example.asus.masi.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WalletActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    ListView lv;
    Button btnSubmit;
    EditText txtAmount;
    TextView txtEmpty, txtTotal;

    ArrayList<Wallet> walletList = new ArrayList<>();
    ArrayList<Account> accountList = new ArrayList<>();
    WalletAdapter adapter;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        this.db = new AppDatabase(this);
        this.accountList = this.db.getAccountInfo();
        this.walletList = this.db.getWalletCustomer(accountList.get(0).getCustId());

        this.lv = this.findViewById(R.id.listView);
        this.txtTotal = this.findViewById(R.id.textView);
        this.txtEmpty = this.findViewById(R.id.textView2);
        this.txtAmount = this.findViewById(R.id.editText);
        this.btnSubmit = this.findViewById(R.id.button);

        this.txtTotal.setText(String.format(Locale.getDefault(), "Total Balance: \u20B1%.2f", accountList.get(0).getTotalBal()));
        if(walletList.isEmpty()) {
           txtEmpty.setVisibility(View.VISIBLE);
           lv.setVisibility(View.INVISIBLE);
        } else {
            txtEmpty.setVisibility(View.INVISIBLE);
            lv.setVisibility(View.VISIBLE);
        }

        this.adapter = new WalletAdapter(this, walletList);
        this.lv.setAdapter(adapter);
        setListViewHeightBasedOnChildren(lv);

        this.txtEmpty.addTextChangedListener(this);
        this.btnSubmit.setOnClickListener(this);
    }

    private static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public void onClick(View v) {
        if(!txtAmount.getText().toString().trim().equals("")) {
            String date = DateFormat.getDateInstance().format(new Date());
            double amount = Double.parseDouble(txtAmount.getText().toString());

            if(amount > 19) {
                int id = Integer.parseInt("" + db.addWallet(accountList.get(0).getCustId(), date, amount));

                if(id != -1) {
                    if(walletList.isEmpty()) {
                        txtEmpty.setVisibility(View.INVISIBLE);
                        lv.setVisibility(View.VISIBLE);
                    }

                    this.walletList.add(new Wallet(id, accountList.get(0).getCustId(), date, amount));
                    //double total = db.getTotalWalletCustomer(accountList.get(0).getCustId());
                    double total = accountList.get(0).getTotalBal() + amount;
                    db.editCustomerAccountBal(accountList.get(0).getCustId(), total);

                    txtAmount.setText("");
                    this.txtTotal.setText(String.format(Locale.getDefault(), "Total Balance: \u20B1%.2f", total));
                    Toast.makeText(this, String.format(Locale.getDefault(), "You successfully added \u20B1%.2f to your wallet", amount), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Error adding wallet", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Amount must be greater than \u20B120", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Amount must not be \u20B10", Toast.LENGTH_LONG).show();
        }

        this.adapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(lv);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s != null) {
            String amount = s.toString();

            if(s.length() >= 1) {
                if(s.charAt(0) == '0') {
                    amount = amount.substring(1, s.length());
                }
            }

            txtAmount.setText(amount);
            txtAmount.setSelection(amount.length());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(txtAmount.getText().toString().trim().equals("")) {
            txtAmount.setText("0");
            txtAmount.setSelection(1);
        }
    }
}