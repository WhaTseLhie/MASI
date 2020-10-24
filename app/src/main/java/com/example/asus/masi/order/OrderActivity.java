package com.example.asus.masi.order;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.asus.masi.Account;
import com.example.asus.masi.AppDatabase;
import com.example.asus.masi.R;
import com.example.asus.masi.cart.CartActivity;
import com.example.asus.masi.cart.PaymentActivity;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    ListView lv;
    Button btnShopping;
    TextView txtEmptyList;

    AppDatabase db;
    OrderAdapter adapter;
    ArrayList<Order> orderList = new ArrayList<>();
    ArrayList<Account> accountList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        this.db = new AppDatabase(this);
        this.accountList = this.db.getAccountInfo();
        this.orderList = this.db.getAllOrderCustomer(accountList.get(0).getCustId());

        this.lv = this.findViewById(R.id.listView);
        this.btnShopping = this.findViewById(R.id.button);
        this.txtEmptyList = this.findViewById(R.id.textView);

        this.adapter = new OrderAdapter(this, orderList);
        this.lv.setAdapter(adapter);

        if(orderList.isEmpty()) {
            this.txtEmptyList.setVisibility(View.VISIBLE);
            this.btnShopping.setVisibility(View.VISIBLE);
            this.lv.setVisibility(View.INVISIBLE);
        } else {
            this.txtEmptyList.setVisibility(View.INVISIBLE);
            this.btnShopping.setVisibility(View.INVISIBLE);
            this.lv.setVisibility(View.VISIBLE);
        }

        this.btnShopping.setOnClickListener(this);
        this.lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(orderList.get(position).getOrderStatus().equals("PENDING")) {
            Intent cartIntent = new Intent(this, CartActivity.class);
            startActivity(cartIntent);
        } else if(orderList.get(position).getOrderStatus().equals("PAID")) {
            Intent paymentIntent = new Intent(this, PaymentActivity.class);
            paymentIntent.putExtra("orderid", orderList.get(position).getOrderId());
            paymentIntent.putExtra("custid", accountList.get(0).getCustId());
            startActivity(paymentIntent);
        }
    }

    @Override
    public void onClick(View v) {
        this.finish();
    }
}