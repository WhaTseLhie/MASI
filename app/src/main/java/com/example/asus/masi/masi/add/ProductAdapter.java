package com.example.asus.masi.masi.add;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.asus.masi.CircleTransform;
import com.example.asus.masi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {

    Context context;
    ArrayList<Product> list = new ArrayList<>();
    LayoutInflater inflater;

    public ProductAdapter(Context context, ArrayList<Product> list) {
        super();
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
        ProductHandler handler;

        if(convertView == null) {
            handler = new ProductHandler();
            convertView = inflater.inflate(R.layout.adapter_product, null);

            handler.iv = convertView.findViewById(R.id.imageView);
            handler.txtBrand = convertView.findViewById(R.id.textView);
            handler.txtName = convertView.findViewById(R.id.textView2);
            handler.txtColor = convertView.findViewById(R.id.textView3);
            handler.txtCategory = convertView.findViewById(R.id.textView4);
            handler.txtQty = convertView.findViewById(R.id.textView5);
            handler.txtPrice = convertView.findViewById(R.id.textView6);
            handler.ratingBar = convertView.findViewById(R.id.ratingBar);

            convertView.setTag(handler);
        } else {
            handler = (ProductHandler) convertView.getTag();
        }

        Picasso.with(context).load(list.get(position).getProductPic()).transform(new CircleTransform()).into(handler.iv);
        handler.txtBrand.setText("Brand: " +list.get(position).getProductBrand());
        handler.txtName.setText("Name: " +list.get(position).getProductName());
        handler.txtColor.setText("Color: " +list.get(position).getProductColor());
        handler.txtCategory.setText("Category: " +list.get(position).getProductCategory());
        handler.txtQty.setText("Quantity: " +list.get(position).getProductQty());
        handler.txtPrice.setText("Price: " +String.format("\u20B1%.2f", list.get(position).getProductPrice()));
        handler.ratingBar.setRating(list.get(position).getProductRating());

        return convertView;
    }

    static class ProductHandler {
        ImageView iv;
        TextView txtBrand, txtName, txtColor, txtCategory, txtQty, txtPrice;
        RatingBar ratingBar;
    }
}