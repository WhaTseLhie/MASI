package com.example.asus.masi.customer;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.masi.Account;
import com.example.asus.masi.AppDatabase;
import com.example.asus.masi.CircleTransform;
import com.example.asus.masi.R;
import com.example.asus.masi.masi.add.Product;
import com.example.asus.masi.order.Order;
import com.example.asus.masi.order.ProductOrder;
import com.example.asus.masi.rating.Rating;
import com.example.asus.masi.rating.RatingAdapter;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ViewProductCustomerActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtPrice, txtBrand, txtName, txtCategory, txtColor, txtReviews, txtNoReviews;
    ImageView iv, btnAdd, btnRating;
    RatingBar ratingBar;
    ListView lv;

    AppDatabase db;
    ArrayList<Product> productList = new ArrayList<>();
    ArrayList<Account> accountList = new ArrayList<>();

    ArrayList<Rating> tempRatingList = new ArrayList<>();
    ArrayList<Rating> ratingList = new ArrayList<>();
    RatingAdapter adapter;

    Dialog dialog;
    RatingBar addRating;
    EditText txtAddComment;

    Dialog dialog2;
    EditText txtQty;
    ImageView imgProduct;

    Double productTotal;//, orderTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product_customer);

        this.db = new AppDatabase(this);
        this.accountList = this.db.getAccountInfo();

        this.iv = this.findViewById(R.id.imageView);
        this.btnAdd = this.findViewById(R.id.imageView2);
        this.btnRating = this.findViewById(R.id.imageView3);
        this.txtPrice = this.findViewById(R.id.textView);
        this.txtBrand = this.findViewById(R.id.textView2);
        this.txtName = this.findViewById(R.id.textView3);
        this.txtCategory = this.findViewById(R.id.textView4);
        this.txtColor = this.findViewById(R.id.textView5);
        this.txtReviews = this.findViewById(R.id.textView6);
        this.txtNoReviews = this.findViewById(R.id.textView7);
        this.ratingBar = this.findViewById(R.id.ratingBar);
        this.lv = this.findViewById(R.id.listView);

        try {
            Bundle b = getIntent().getExtras();
            int productId = b.getInt("productid");
            this.productList = this.db.findProduct(productId);
            this.ratingList = this.db.getAllProductRating(productId);
            this.adapter = new RatingAdapter(this, ratingList);
            this.lv.setAdapter(adapter);

            this.tempRatingList = this.db.findProductRating(productList.get(0).getProductId(), accountList.get(0).getCustId());
            if(tempRatingList.isEmpty()) {
                btnRating.setImageResource(R.drawable.ic_add_circle);
            } else {
                btnRating.setImageResource(R.drawable.ic_mode_edit);
            }

            if(ratingList.isEmpty()) {
                txtNoReviews.setVisibility(View.VISIBLE);
            } else {
                txtNoReviews.setVisibility(View.INVISIBLE);

                setListViewHeightBasedOnChildren(lv);
            }

            if(productList.isEmpty()) {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
                this.finish();
            } else {
                this.iv.setImageURI(productList.get(0).getProductPic());
                this.txtPrice.setText(String.format(Locale.getDefault(), "\u20B1 %.2f", productList.get(0).getProductPrice()));
                this.txtBrand.setText(productList.get(0).getProductBrand());
                this.txtName.setText(productList.get(0).getProductName());
                this.txtCategory.setText(productList.get(0).getProductCategory());
                this.txtColor.setText(productList.get(0).getProductColor());
                this.ratingBar.setRating(productList.get(0).getProductRating());
                this.txtReviews.setText(String.format(Locale.getDefault(), "%d Reviews", ratingList.size()));
                this.ratingBar.setRating(db.getProductRating(productId));
            }
        } catch(Exception e) {
            Log.d("SET TEXT ERR: ", e.getMessage());
        }

        this.btnAdd.setOnClickListener(this);
        this.btnRating.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.imageView2:
                dialog2 = new Dialog(this);
                dialog2.setContentView(R.layout.layout_add_to_cart);

                imgProduct = dialog2.findViewById(R.id.imageView);
                ImageView imgMinus= dialog2.findViewById(R.id.imageView2);
                ImageView imgAdd = dialog2.findViewById(R.id.imageView3);
                txtQty = dialog2.findViewById(R.id.editText);
                Button btnAdd = dialog2.findViewById(R.id.button);
                Button btnCancel = dialog2.findViewById(R.id.button2);

                imgProduct.setImageURI(productList.get(0).getProductPic());

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
                        } else if(Integer.parseInt(txtQty.getText().toString()) > productList.get(0).getProductQty()) {
                            txtQty.setText(String.format(Locale.getDefault(), "%d", productList.get(0).getProductQty()));
                            Toast.makeText(getApplicationContext(), "You reach the max quantity of this product", Toast.LENGTH_LONG).show();
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

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<Order> orderList = db.getAllCurrentOrderCustomer(accountList.get(0).getCustId());

                        if (!TextUtils.equals("0", txtQty.getText().toString())) {
                            if (orderList.isEmpty()) {
                                String orderDate = DateFormat.getDateInstance().format(new Date());
                                int qty = Integer.parseInt(txtQty.getText().toString());
                                productTotal = qty * productList.get(0).getProductPrice();
                                int id = Integer.parseInt("" +db.addOrder(accountList.get(0).getCustId(), orderDate, productTotal, "PENDING"));

                                if(id == -1) {
                                    Toast.makeText(getApplicationContext(), "Add Error!", Toast.LENGTH_LONG).show();
                                } else {
                                    qty = Integer.parseInt(txtQty.getText().toString());
                                    productTotal = qty * productList.get(0).getProductPrice();
                                    //orderTotal = db.getProductOrderTotal(orderList.get(0).getOrderId());

                                    db.addProductOrder(id, productList.get(0).getProductId(), qty, productTotal);
                                    //db.editOrder(id, accountList.get(0).getCustId(), orderDate, productTotal, "PENDING");
                                    Toast.makeText(getApplicationContext(), "You added " + qty + " " + productList.get(0).getProductName() + " to cart", Toast.LENGTH_LONG).show();
                                    dialog2.dismiss();
                                }
                            } else {
                                ArrayList<ProductOrder> poList = db.findProductOrderId(orderList.get(0).getOrderId(), productList.get(0).getProductId());

                                if (poList.isEmpty()) {
                                    int qty = Integer.parseInt(txtQty.getText().toString());
                                    productTotal = qty * productList.get(0).getProductPrice();
                                    //orderTotal = db.getProductOrderTotal(orderList.get(0).getOrderId());

                                    //db.editOrder(orderList.get(0).getOrderId(), accountList.get(0).getCustId(), orderList.get(0).getOrderDate(), orderTotal, "PENDING");
                                    db.addProductOrder(orderList.get(0).getOrderId(), productList.get(0).getProductId(), qty, productTotal);

                                    Toast.makeText(getApplicationContext(), "You added " + qty + " " + productList.get(0).getProductName() + " to cart", Toast.LENGTH_LONG).show();
                                } else {
                                    int qty = Integer.parseInt(txtQty.getText().toString());
                                    productTotal = qty * productList.get(0).getProductPrice();
                                    //orderTotal = db.getProductOrderTotal(orderList.get(0).getOrderId());

                                    //db.editOrder(orderList.get(0).getOrderId(), accountList.get(0).getCustId(), orderList.get(0).getOrderDate(), orderTotal, "PENDING");
                                    db.editProductOrderPID(orderList.get(0).getOrderId(), productList.get(0).getProductId(), qty, productTotal);

                                    Toast.makeText(getApplicationContext(), "You updated " + productList.get(0).getProductName() + " by " + qty + " quantity to cart", Toast.LENGTH_LONG).show();
                                }

                                dialog2.dismiss();
                            }
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

                break;
            case R.id.imageView3:
                dialog = new Dialog(this);
                dialog.setContentView(R.layout.layout_rating_edit);

                TextView txtTitle = dialog.findViewById(R.id.textView);
                ImageView iv = dialog.findViewById(R.id.imageView);
                addRating = dialog.findViewById(R.id.ratingBar);
                txtAddComment = dialog.findViewById(R.id.textView2);
                Button btnReviewSave = dialog.findViewById(R.id.button);
                Button btnReviewCancel = dialog.findViewById(R.id.button2);

                txtTitle.setText("Review " +productList.get(0).getProductName());
                Picasso.with(getApplicationContext()).load(productList.get(0).getProductPic()).transform(new CircleTransform()).into(iv);

                if(!tempRatingList.isEmpty()) {
                    addRating.setRating(tempRatingList.get(0).getRating());
                    txtAddComment.setText(tempRatingList.get(0).getComment());
                }

                btnReviewSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        float rating = addRating.getRating();
                        String comment = txtAddComment.getText().toString();
                        String date = DateFormat.getDateInstance().format(new Date());

                        if(!tempRatingList.isEmpty()) {
                            db.editRating(productList.get(0).getProductId(), accountList.get(0).getCustId(), rating, comment);
                            Toast.makeText(getApplicationContext(), "Review Successfully Edited", Toast.LENGTH_LONG).show();
                        } else {
                            db.addRating(productList.get(0).getProductId(), accountList.get(0).getCustId(), accountList.get(0).getProfilePic().toString(), accountList.get(0).getFirstname() +" "+ accountList.get(0).getLastname(), rating, comment, date);
                            Toast.makeText(getApplicationContext(), "Review Successfully Added", Toast.LENGTH_LONG).show();
                        }

                        dialog.dismiss();
                        finish();
                        startActivity(getIntent());
                    }
                });

                btnReviewCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

                break;
        }
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
}