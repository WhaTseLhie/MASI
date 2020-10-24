package com.example.asus.masi.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.masi.AppDatabase;
import com.example.asus.masi.CircleTransform;
import com.example.asus.masi.Customer;
import com.example.asus.masi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class OrderAdapter extends BaseAdapter {

    Context context;
    ArrayList<Order> list = new ArrayList<>();
    LayoutInflater inflater;

    public OrderAdapter(Context context, ArrayList<Order> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderHandler handler;

        if(convertView == null) {
            handler = new OrderHandler();
            convertView = inflater.inflate(R.layout.adapter_order, null);

            handler.iv = convertView.findViewById(R.id.imageView);
            handler.txtId = convertView.findViewById(R.id.textView);
            handler.txtName = convertView.findViewById(R.id.textView2);
            handler.txtDate = convertView.findViewById(R.id.textView3);
            handler.txtStatus = convertView.findViewById(R.id.textView4);
            handler.txtTotal = convertView.findViewById(R.id.textView5);

            convertView.setTag(handler);
        } else {
            handler = (OrderHandler) convertView.getTag();
        }

        AppDatabase db = new AppDatabase(context);
        ArrayList<Customer> customerList = db.findCustomer(list.get(position).getCustomerId());

        Picasso.with(context).load(customerList.get(0).getProfilePic()).transform(new CircleTransform()).into(handler.iv);
        handler.txtId.setText(String.format(Locale.getDefault(), "Order Id: %d", list.get(position).getOrderId()));
        handler.txtName.setText(String.format(Locale.getDefault(), "Name: %s %s", customerList.get(0).getFirstname(), customerList.get(0).getLastname()));
        handler.txtDate.setText(String.format(Locale.getDefault(), "Date: %s", list.get(position).getOrderDate()));
        handler.txtTotal.setText(String.format(Locale.getDefault(), "Total Price: \u20B1%.2f", db.getProductOrderTotal(list.get(position).getOrderId())));
        handler.txtStatus.setText(String.format(Locale.getDefault(), "Status: %s", list.get(position).getOrderStatus()));

        return convertView;
    }

    static class OrderHandler {
        ImageView iv;
        TextView txtId, txtName, txtDate, txtTotal, txtStatus;
    }
}