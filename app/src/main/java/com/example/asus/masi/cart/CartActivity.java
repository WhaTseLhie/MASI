package com.example.asus.masi.cart;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.masi.Account;
import com.example.asus.masi.AppDatabase;
import com.example.asus.masi.Customer;
import com.example.asus.masi.R;
import com.example.asus.masi.masi.add.Product;
import com.example.asus.masi.order.Order;
import com.example.asus.masi.order.ProductOrder;

import java.util.ArrayList;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    ListView lv;
    Button btnShopping;
    TextView txtEmptyList, txtMess;
    SwipeRefreshLayout refreshLayout;

    AppDatabase db;
    CartAdapter adapter;
    ArrayList<ProductOrder> poList = new ArrayList<>();

    AdapterView.AdapterContextMenuInfo info;

    ArrayList<Order> orderList = new ArrayList<>();
    ArrayList<Account> accountList = new ArrayList<>();

    ArrayList<Product> productList = new ArrayList<>();
    ImageView imgProduct;
    EditText txtQty;
    Dialog dialog2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        this.db = new AppDatabase(this);
        this.accountList = this.db.getAccountInfo();
        this.orderList = this.db.getAllCurrentOrderCustomer(accountList.get(0).getCustId());

        this.lv = this.findViewById(R.id.listView);
        this.btnShopping = this.findViewById(R.id.button);
        this.txtMess = this.findViewById(R.id.textView);
        this.txtEmptyList = this.findViewById(R.id.textView2);
        this.refreshLayout = this.findViewById(R.id.refreshLayout);

        if(!orderList.isEmpty()) {
            this.poList = this.db.getAllProductOrderId(orderList.get(0).getOrderId());
            this.adapter = new CartAdapter(this, poList);
            this.lv.setAdapter(adapter);
        }

        try {
            if(orderList.isEmpty()) {
                this.txtEmptyList.setVisibility(View.VISIBLE);
                this.btnShopping.setVisibility(View.VISIBLE);
                this.txtMess.setVisibility(View.INVISIBLE);
                this.lv.setVisibility(View.INVISIBLE);
                this.refreshLayout.setVisibility(View.INVISIBLE);
            } else {
                this.txtEmptyList.setVisibility(View.INVISIBLE);
                this.btnShopping.setVisibility(View.INVISIBLE);
                this.txtMess.setVisibility(View.VISIBLE);
                this.lv.setVisibility(View.VISIBLE);
                this.refreshLayout.setVisibility(View.VISIBLE);
            }
        } catch(Exception e) {
            Log.d("CART ERR", e.getMessage());
        }

        this.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                poList.clear();
                orderList = db.getAllCurrentOrderCustomer(accountList.get(0).getCustId());

                if(!orderList.isEmpty()) {
                    refreshList();
                }

                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        });

        this.registerForContextMenu(this.lv);
        this.btnShopping.setOnClickListener(this);
    }

    private void refreshList() {
        this.poList = this.db.getAllProductOrderId(orderList.get(0).getOrderId());
        this.adapter = new CartAdapter(this, poList);
        this.lv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.payment, menu);

        if(orderList.isEmpty()) {
            menu.findItem(R.id.payment).setVisible(false);
        } else {
            menu.findItem(R.id.payment).setVisible(true);
        }

        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contextmenu_cart, menu);
        info = (AdapterView.AdapterContextMenuInfo) menuInfo;

        ArrayList<Product> productList = db.findProduct(poList.get(info.position).getProductId());
        menu.setHeaderTitle("What are you gonna do to " +productList.get(0).getProductName()+ "?");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.edit:
                editCartProduct();

                break;
            case R.id.delete:
                deleteCartProduct();

                break;
        }


        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.payment) {
            Intent paymentIntent = new Intent(this, PaymentActivity.class);
            paymentIntent.putExtra("orderid", orderList.get(0).getOrderId());
            paymentIntent.putExtra("custid", accountList.get(0).getCustId());
            startActivityForResult(paymentIntent, 10);
        }

        return super.onOptionsItemSelected(item);
    }

    private void editCartProduct() {
        dialog2 = new Dialog(this);
        dialog2.setContentView(R.layout.layout_add_to_cart);

        imgProduct = dialog2.findViewById(R.id.imageView);
        ImageView imgMinus= dialog2.findViewById(R.id.imageView2);
        ImageView imgAdd = dialog2.findViewById(R.id.imageView3);
        txtQty = dialog2.findViewById(R.id.editText);
        Button btnEdit = dialog2.findViewById(R.id.button);
        Button btnCancel = dialog2.findViewById(R.id.button2);

        btnEdit.setText("Edit");
        productList = db.findProduct(poList.get(info.position).getProductId());
        imgProduct.setImageURI(productList.get(0).getProductPic());
        if(poList.get(info.position).getProductQty() <= productList.get(0).getProductQty()) {
            txtQty.setText("" +poList.get(info.position).getProductQty());
        } else {
            txtQty.setText("" +productList.get(0).getProductQty());
            Toast.makeText(this, String.format(Locale.getDefault(), "The quantity you entered is %d which is greater than product's total quantity", poList.get(info.position).getProductQty()), Toast.LENGTH_LONG).show();
        }

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

                if(productList.get(0).getProductQty() >= qty) {
                    qty += 1;
                    txtQty.setText("" + qty);
                } else {
                    Toast.makeText(getApplicationContext(), "You reach the max quantity of this product", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.equals("0", txtQty.getText().toString())) {
                    int qty = Integer.parseInt(txtQty.getText().toString());
                    double total = qty * productList.get(0).getProductPrice();
                    db.editProductOrderPID(poList.get(info.position).getOrderId(), productList.get(0).getProductId(), qty, total);

                    poList.get(info.position).setProductQty(qty);
                    poList.get(info.position).setProductTotalPrice(total);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "You updated " + productList.get(0).getProductName() + " by " +qty, Toast.LENGTH_LONG).show();
                    dialog2.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "0 quantity is invalid", Toast.LENGTH_LONG).show();
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

    private void deleteCartProduct() {
        productList = db.findProduct(poList.get(info.position).getProductId());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to remove " +productList.get(0).getProductName()+ "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), productList.get(0).getProductName()+ " has been removed from the cart", Toast.LENGTH_LONG).show();
                db.deleteProductOrder(poList.get(info.position).getOrderId(), poList.get(info.position).getProductId());

                if(poList.size() == 1) {
                    db.deleteOrder(poList.get(info.position).getOrderId());
                    finish();
                }

                poList.remove(info.position);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("No", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 10) {
            orderList = this.db.getAllCurrentOrderCustomer(accountList.get(0).getCustId());
            if(orderList.isEmpty() || orderList.get(0).getOrderStatus().equals("PAID")) {
                this.finish();
            }
        }
    }
}