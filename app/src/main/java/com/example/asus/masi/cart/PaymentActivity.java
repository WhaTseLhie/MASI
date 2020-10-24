package com.example.asus.masi.cart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.masi.Account;
import com.example.asus.masi.AppDatabase;
import com.example.asus.masi.R;
import com.example.asus.masi.masi.add.Product;
import com.example.asus.masi.order.Order;
import com.example.asus.masi.order.ProductOrder;
import com.example.asus.masi.wallet.WalletActivity;

import java.util.ArrayList;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {

    ListView lv;
    Button btnPay, btnCancel;
    TextView txtId, txtName, txtDate, txtStatus, txtTotal;

    AppDatabase db;
    CartAdapter adapter;
    ArrayList<Account> accountList = new ArrayList<>();
    ArrayList<ProductOrder> poList = new ArrayList<>();
    ArrayList<Order> orderList = new ArrayList<>();

    int orderId;
    Dialog dialog, dialog2;
    double poTotal, totalBal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        this.db = new AppDatabase(this);
        //this.accountList = this.db.getAccountInfo();

        this.txtId = this.findViewById(R.id.textView);
        this.txtName = this.findViewById(R.id.textView2);
        this.txtDate = this.findViewById(R.id.textView3);
        this.txtStatus = this.findViewById(R.id.textView4);
        this.txtTotal = this.findViewById(R.id.textView5);
        this.btnPay = this.findViewById(R.id.button);
        this.btnCancel = this.findViewById(R.id.button2);
        this.lv = this.findViewById(R.id.listView);

        try {
            Bundle b = getIntent().getExtras();
            orderId = b.getInt("orderid");
            //
            int custId = b.getInt("custid");
            //
            this.accountList = this.db.findCustomerAccount(custId);
        } catch(Exception e) {
            e.printStackTrace();
        }

        //this.orderList = this.db.getAllCurrentOrderCustomer(accountList.get(0).getCustId());
        this.orderList = this.db.getAllOrderId(orderId);
        this.txtId.setText("" +orderList.get(0).getOrderId());
        this.txtName.setText(this.accountList.get(0).getFirstname()+ " " +this.accountList.get(0).getLastname());
        this.txtDate.setText(orderList.get(0).getOrderDate());
        this.txtStatus.setText(orderList.get(0).getOrderStatus());
        double totalPrice = this.db.getProductOrderTotal(orderList.get(0).getOrderId());
        this.txtTotal.setText("" +String.format("\u20B1%.2f", totalPrice));
        //double totalPrice = orderList.get(0).getOrderTotalPrice();
        //this.txtTotal.setText("" +String.format("\u20B1%.2f", totalPrice));

        this.poList = this.db.getAllProductOrderId(orderId);
        this.adapter = new CartAdapter(this, poList);
        this.lv.setAdapter(adapter);
        setListViewHeightBasedOnChildren(lv);
        this.orderList.get(0).setOrderTotalPrice(totalPrice);
        this.adapter.notifyDataSetChanged();

        if(orderList.get(0).getOrderStatus().equals("PENDING")) {
            btnPay.setText("PAY");
        } else {
            btnPay.setText("CONFIRM");
        }

        this.btnPay.setOnClickListener(this);
        this.btnCancel.setOnClickListener(this);
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
        switch(v.getId()) {
            case R.id.button:
                //double total = db.getTotalWalletCustomer(accountList.get(0).getCustId());
                //db.editCustomerAccountBal(accountList.get(0).getCustId(), total);

                if(btnPay.getText().toString().equals("PAY")) {
                    if(accountList.get(0).getTotalBal() >= orderList.get(0).getOrderTotalPrice()) {
                        dialog = new Dialog(this);
                        dialog.setContentView(R.layout.layout_pay_confirmation);
                        poTotal = db.getProductOrderTotal(orderList.get(0).getOrderId());
                        //totalBal = total;
                        totalBal = accountList.get(0).getTotalBal();

                        TextView txtTitle = dialog.findViewById(R.id.textView);
                        TextView txtMessage = dialog.findViewById(R.id.textView2);
                        Button btnConfirm = dialog.findViewById(R.id.button);
                        Button btnCancel = dialog.findViewById(R.id.button2);

                        txtTitle.setText("Confirmation");
                        txtMessage.setText(String.format(Locale.getDefault(), "Your \u20B1%.2f total balance will be deducted by \u20B1%.2f", totalBal, poTotal));

                        btnConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String message = db.editOrderProductQty(orderList.get(0).getOrderId());

                                if(message.equals("f")) {
                                    db.editOrder(orderList.get(0).getOrderId(), orderList.get(0).getCustomerId(), poTotal, "PAID");
                                    db.editCustomerAccountBal(accountList.get(0).getCustId(), totalBal - poTotal);
                                    db.editOrderProductQuantity(orderList.get(0).getOrderId());
                                    Toast.makeText(getApplicationContext(), "Order Id " + orderList.get(0).getOrderId() + " has been paid", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    finish();
                                } else {
                                    //Toast.makeText(getApplicationContext(), "Err message: " +message, Toast.LENGTH_LONG).show();
                                    /*String errorIdArray[] = message.split(",");
                                    ArrayList<Product> prodList = new ArrayList<>();

                                    for(int i=0; i<errorIdArray.length; i++) {
                                        int id = Integer.parseInt(errorIdArray[i]);
                                        prodList.add(db.findErrorProduct(id));
                                    }*/
                                    //String errorIdArray[] = message.split(",");

                                    AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
                                    builder.setTitle("Quantity Error");
                                    builder.setMessage("Your inputted quantity is more than the product total quantity.\nPlease edit product quantity with these ids: " +message);
                                    builder.setNeutralButton("Confirm", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    });

                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                }
                            }
                        });

                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                    } else {
                        dialog2 = new Dialog(this);
                        dialog2.setContentView(R.layout.layout_permission);
                        poTotal = db.getProductOrderTotal(orderList.get(0).getOrderId());
                        totalBal = accountList.get(0).getTotalBal();

                        TextView txtTitle = dialog2.findViewById(R.id.textView);
                        TextView txtMessage = dialog2.findViewById(R.id.textView2);
                        Button btnConfirm = dialog2.findViewById(R.id.button);

                        double shortOf = orderList.get(0).getOrderTotalPrice() - accountList.get(0).getTotalBal();
                        txtTitle.setText("Payment Error");
                        txtTitle.setTextColor(Color.RED);
                        txtMessage.setText(String.format(Locale.getDefault(), "Your balance is \u20B1%.2f\nYou need to deposit at least \u20B1%.2f to pay", accountList.get(0).getTotalBal(), shortOf));
                        btnConfirm.setText("Confirm");

                        btnConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog2.dismiss();
                                Intent walletIntent = new Intent(PaymentActivity.this, WalletActivity.class);
                                startActivityForResult(walletIntent, 10);
                            }
                        });

                        dialog2.show();
                    }
                } else {
                    this.finish();
                }

                break;
            case R.id.button2:
                this.finish();

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        finish();
        startActivity(getIntent());
    }
}