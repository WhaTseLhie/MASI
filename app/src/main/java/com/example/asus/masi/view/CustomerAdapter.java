package com.example.asus.masi.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.masi.CircleTransform;
import com.example.asus.masi.Customer;
import com.example.asus.masi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class CustomerAdapter extends BaseAdapter {

    Context context;
    ArrayList<Customer> list = new ArrayList<>();
    LayoutInflater inflater;

    public CustomerAdapter(Context context, ArrayList<Customer> list) {
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
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomerHandler handler;

        if(convertView == null) {
            handler = new CustomerHandler();
            convertView = inflater.inflate(R.layout.adapter_customer, null);

            handler.iv = convertView.findViewById(R.id.imageView);
            handler.txtName = convertView.findViewById(R.id.textView);
            handler.txtGender = convertView.findViewById(R.id.textView2);
            handler.txtContact = convertView.findViewById(R.id.textView3);
            handler.txtEmail = convertView.findViewById(R.id.textView4);
            handler.txtAddress = convertView.findViewById(R.id.textView5);

            convertView.setTag(handler);
        } else {
            handler = (CustomerHandler) convertView.getTag();
        }

        Picasso.with(context).load(list.get(position).getProfilePic()).transform(new CircleTransform()).into(handler.iv);
        handler.txtName.setText(String.format(Locale.getDefault(), "Name: %s %s", list.get(position).getFirstname(), list.get(position).getLastname()));
        handler.txtGender.setText(String.format(Locale.getDefault(), "Gender: %s", list.get(position).getGender()));
        handler.txtContact.setText(String.format(Locale.getDefault(), "Contact: %s", list.get(position).getContact()));
        handler.txtEmail.setText(String.format(Locale.getDefault(), "Email: %s", list.get(position).getEmail()));
        handler.txtAddress.setText(String.format(Locale.getDefault(), "Address: %s", list.get(position).getAddress()));

        return convertView;
    }

    static class CustomerHandler {
        ImageView iv;
        TextView txtName, txtGender, txtContact, txtEmail, txtAddress;
    }
}