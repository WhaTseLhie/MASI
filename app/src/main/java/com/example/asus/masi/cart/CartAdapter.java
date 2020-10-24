package com.example.asus.masi.cart;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.masi.Account;
import com.example.asus.masi.AppDatabase;
import com.example.asus.masi.CircleTransform;
import com.example.asus.masi.Customer;
import com.example.asus.masi.R;
import com.example.asus.masi.masi.add.Product;
import com.example.asus.masi.order.Order;
import com.example.asus.masi.order.ProductOrder;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CartAdapter extends BaseAdapter {

    Context context;
    ArrayList<ProductOrder> list = new ArrayList<>();
    LayoutInflater inflater;
    int pos;

    ArrayList<Product> productList;
    AppDatabase db;

    public CartAdapter(Context context, ArrayList<ProductOrder> list) {
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
        CartHandler handler;
        pos = position;

        if(convertView == null) {
            handler = new CartHandler();
            convertView = inflater.inflate(R.layout.adapter_cart, null);

            handler.iv = convertView.findViewById(R.id.imageView);
            handler.txtId = convertView.findViewById(R.id.textView);
            handler.txtName = convertView.findViewById(R.id.textView2);
            handler.txtQty = convertView.findViewById(R.id.textView3);
            handler.txtRegular = convertView.findViewById(R.id.textView4);
            handler.txtTotal = convertView.findViewById(R.id.textView5);

            convertView.setTag(handler);
        } else {
            handler = (CartHandler) convertView.getTag();
        }

        db = new AppDatabase(context);
        productList = db.findProduct(list.get(position).getProductId());

        Picasso.with(context).load(productList.get(0).getProductPic()).transform(new CircleTransform()).into(handler.iv);
        handler.txtId.setText(String.format(Locale.getDefault(), "Product Id: %d", list.get(position).getProductId()));
        handler.txtName.setText(String.format(Locale.getDefault(), "Name: %s", productList.get(0).getProductName()));
        handler.txtQty.setText(String.format(Locale.getDefault(), "Qty: %d", list.get(position).getProductQty()));
        handler.txtRegular.setText(String.format(Locale.getDefault(), "Regular Price: \u20B1%.2f", productList.get(0).getProductPrice()));
        handler.txtTotal.setText(String.format(Locale.getDefault(), "Product Total Price: \u20B1%.2f", list.get(position).getProductTotalPrice()));

        /*handler.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2 = new Dialog(context);
                dialog2.setContentView(R.layout.layout_add_to_cart);

                imgProduct = dialog2.findViewById(R.id.imageView);
                ImageView imgMinus= dialog2.findViewById(R.id.imageView2);
                ImageView imgAdd = dialog2.findViewById(R.id.imageView3);
                txtQty = dialog2.findViewById(R.id.editText);
                Button btnEdit = dialog2.findViewById(R.id.button);
                Button btnCancel = dialog2.findViewById(R.id.button2);

                txtQty.setText("" +list.get(pos).getProductQty());
                //imgProduct.setImageURI(list.get(pos).getProductPic());

                btnEdit.setText("Edit");

                txtQty.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(s != null) {
                            if(s.length() > 1) {
                                if(s.toString().startsWith("0")) {
                                    s = s.toString().substring(1, s.toString().length());
                                    txtQty.setText(s);
                                    txtQty.setSelection(1);
                                }
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(txtQty.getText().length() == 0) {
                            txtQty.setText("0");
                            txtQty.setSelection(1);
                        }
                    }
                });

                imgMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int qty = Integer.parseInt(txtQty.getText().toString());

                        if(qty > 0) {
                            qty -= 1;
                            txtQty.setText("" +qty);
                        }
                    }
                });

                imgAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int qty = Integer.parseInt(txtQty.getText().toString());
                        qty += 1;
                        txtQty.setText("" +qty);
                    }
                });

                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.equals("0", txtQty.getText().toString())) {
                            int qty = Integer.parseInt(txtQty.getText().toString());
                            double total = qty * productList.get(0).getProductPrice();
                            db.editProductOrderPID(list.get(0).getOrderId(), productList.get(0).getProductId(), qty, total);

                            Toast.makeText(context, "You updated " + productList.get(0).getProductName() + " by " + qty + " quantity to cart", Toast.LENGTH_LONG).show();

                            dialog2.dismiss();
                            ((Activity)context).finish();
                            context.startActivity(((Activity)context).getIntent());
                        } else {
                            Toast.makeText(context, "0 quantity is invalid", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog2.dismiss();
                    }
                });

                dialog2.show();
            }
        });

        handler.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to remove " +productList.get(0).getProductName()+ "?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, productList.get(0).getProductName()+ " has been removed", Toast.LENGTH_LONG).show();
                        db.deleteProductOrder(list.get(pos).getOrderId(), list.get(pos).getProductId());

                        if(list.size() == 1) {
                            db.deleteOrder(list.get(pos).getOrderId());
                        }

                        ((Activity)context).finish();
                        context.startActivity(((Activity)context).getIntent());
                    }
                });
                builder.setNegativeButton("No", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });*/

        return convertView;
    }

    static class CartHandler {
        ImageView iv;
        TextView txtId, txtName, txtQty, txtRegular, txtTotal;
    }
}