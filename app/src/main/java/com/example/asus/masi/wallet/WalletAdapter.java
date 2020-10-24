package com.example.asus.masi.wallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asus.masi.R;

import java.util.ArrayList;
import java.util.Locale;

public class WalletAdapter extends BaseAdapter {

    Context context;
    ArrayList<Wallet> list = new ArrayList<>();
    LayoutInflater inflater;

    public WalletAdapter(Context context, ArrayList<Wallet> list) {
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
        WalletHandler handler;

        if(convertView == null) {
            handler = new WalletHandler();
            convertView = inflater.inflate(R.layout.adapter_wallet, null);

            handler.txtWalletId = convertView.findViewById(R.id.textView);
            handler.txtDate = convertView.findViewById(R.id.textView2);
            handler.txtAmount = convertView.findViewById(R.id.textView3);

            convertView.setTag(handler);
        } else {
            handler = (WalletHandler) convertView.getTag();
        }

        handler.txtWalletId.setText(String.format(Locale.getDefault(), "Wallet Id: %s", list.get(position).getWalletId()));
        handler.txtDate.setText(String.format(Locale.getDefault(), "Date: %s", list.get(position).getDate()));
        handler.txtAmount.setText(String.format(Locale.getDefault(), "Amount: \u20B1%.2f", list.get(position).getAmount()));

        return convertView;
    }

    static class WalletHandler {
        TextView txtWalletId, txtDate, txtAmount;
    }
}