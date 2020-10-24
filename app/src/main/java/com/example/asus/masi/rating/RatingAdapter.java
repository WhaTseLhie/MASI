package com.example.asus.masi.rating;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.asus.masi.Account;
import com.example.asus.masi.AppDatabase;
import com.example.asus.masi.CircleTransform;
import com.example.asus.masi.Customer;
import com.example.asus.masi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class RatingAdapter extends BaseAdapter {

    Context context;
    ArrayList<Rating> list;
    LayoutInflater inflater;
    AppDatabase db;

    public RatingAdapter(Context context, ArrayList<Rating> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        this.db = new AppDatabase(context);
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
        RatingHandler handler;

        if(convertView == null) {
            handler = new RatingHandler();
            convertView = inflater.inflate(R.layout.adapter_rating, null);

            handler.iv = convertView.findViewById(R.id.imageView);
            handler.txtName = convertView.findViewById(R.id.textView);
            handler.ratingBar = convertView.findViewById(R.id.ratingBar);
            handler.txtDate = convertView.findViewById(R.id.textView2);
            handler.txtComment = convertView.findViewById(R.id.textView3);

            convertView.setTag(handler);
        } else {
            handler = (RatingHandler) convertView.getTag();
        }

        ArrayList<Customer> customerList = db.findCustomer(list.get(position).getCustomerId());
        Picasso.with(context).load(customerList.get(position).getProfilePic()).transform(new CircleTransform()).into(handler.iv);
        handler.txtName.setText(String.format(Locale.getDefault(), "%s %s", customerList.get(position).getFirstname(), customerList.get(position).getLastname()));
        handler.ratingBar.setRating(list.get(position).getRating());
        handler.txtDate.setText(list.get(position).getDate());
        handler.txtComment.setText(list.get(position).getComment());

        return convertView;
    }

    static class RatingHandler {
        ImageView iv;
        RatingBar ratingBar;
        TextView txtName, txtComment, txtDate;
    }
}