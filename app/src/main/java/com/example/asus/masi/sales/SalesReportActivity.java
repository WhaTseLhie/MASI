package com.example.asus.masi.sales;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.asus.masi.AppDatabase;
import com.example.asus.masi.R;
import com.example.asus.masi.cart.PaymentActivity;
import com.example.asus.masi.order.Order;
import com.example.asus.masi.order.OrderAdapter;
import java.util.ArrayList;

public class SalesReportActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lv;
    TextView txtSales, txtEmptyList;

    AppDatabase db;
    OrderAdapter adapter;
    ArrayList<Order> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report );

        this.db = new AppDatabase(this);
        this.orderList = this.db.getAllOrder();

        this.lv = this.findViewById(R.id.listView);
        this.txtSales = this.findViewById(R.id.textView);
        this.txtEmptyList = this.findViewById(R.id.textView2);

        this.adapter = new OrderAdapter(this, orderList);
        this.lv.setAdapter(adapter);

        if(orderList.isEmpty()) {
            this.txtEmptyList.setVisibility(View.VISIBLE);
            this.txtSales.setVisibility(View.INVISIBLE);
            this.lv.setVisibility(View.INVISIBLE);
        } else {
            this.txtEmptyList.setVisibility(View.INVISIBLE);
            this.txtSales.setVisibility(View.VISIBLE);
            this.lv.setVisibility(View.VISIBLE);

            this.txtSales.setText("Total Sales: " +String.format("\u20B1%.2f", this.db.getSales()));
        }

        this.lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent paymentIntent = new Intent(this, PaymentActivity.class);
        paymentIntent.putExtra("orderid", orderList.get(position).getOrderId());
        paymentIntent.putExtra("custid", orderList.get(position).getCustomerId());
        startActivity(paymentIntent);

        /*if(orderList.get(position).getOrderStatus().equals("PENDING")) {
            Intent cartIntent = new Intent(this, CartActivity.class);
            startActivity(cartIntent);
        } else if(orderList.get(position).getOrderStatus().equals("PAID")) {
            Intent paymentIntent = new Intent(this, PaymentActivity.class);
            paymentIntent.putExtra("orderid", orderList.get(position).getOrderId());
            startActivity(paymentIntent);
        }*/
    }
}