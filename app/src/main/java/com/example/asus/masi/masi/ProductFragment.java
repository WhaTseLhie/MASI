package com.example.asus.masi.masi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.masi.Account;
import com.example.asus.masi.AppDatabase;
import com.example.asus.masi.R;
import com.example.asus.masi.customer.ViewProductCustomerActivity;
import com.example.asus.masi.masi.add.Product;
import com.example.asus.masi.masi.add.ProductAdapter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductFragment extends Fragment {

    ListView lv;
    EditText txtSearch;
    TextView txtNoProducts;
    RelativeLayout relativeLayout;
    Spinner cboColor, cboCategory;
    SwipeRefreshLayout refreshLayout;

    ProductAdapter adapter;
    ArrayList<Product> sourceList = new ArrayList<>();
    ArrayList<Product> productList = new ArrayList<>();

    AppDatabase db;
    ArrayList<Account> accountList = new ArrayList<>();

    String color = "color", category = "category";

    public ProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        db = new AppDatabase(getContext());
        sourceList = db.getAllProduct();
        accountList = db.getAccountInfo();
        productList = db.getAllProduct();

        relativeLayout = view.findViewById(R.id.relativeLayout);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        txtNoProducts = view.findViewById(R.id.textView);
        cboCategory = view.findViewById(R.id.spinner);
        txtSearch = view.findViewById(R.id.editText);
        cboColor = view.findViewById(R.id.spinner2);
        lv = view.findViewById(R.id.listView);

        /*if(accountList.isEmpty()) {
            productList = db.getAllProductAdmin();
            Toast.makeText(getContext(), "admin", Toast.LENGTH_LONG).show();
        } else {
            productList = db.getAllProduct();
            Toast.makeText(getContext(), "not admin", Toast.LENGTH_LONG).show();
        }*/
        adapter = new ProductAdapter(getContext(), productList);
        lv.setAdapter(adapter);

        if(productList.isEmpty()) {
            txtNoProducts.setVisibility(View.VISIBLE);
        } else {
            txtNoProducts.setVisibility(View.INVISIBLE);
        }

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Pattern p = Pattern.compile(s.toString().toLowerCase());
                productList.clear();

                for(int i=0; i<sourceList.size(); i++) {
                    Matcher m = p.matcher(sourceList.get(i).getProductBrand().toLowerCase());
                    Matcher m1 = p.matcher(sourceList.get(i).getProductName().toLowerCase());

                    if(m.find() || m1.find()) {
                        productList.add(sourceList.get(i));
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cboColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                color = cboColor.getItemAtPosition(position).toString().toLowerCase();
                productList.clear();
                sourceList.clear();

                if(color.equals("color")) {
                    if(category.equals("category")) {
                        sourceList = db.getAllProduct();
                        productList = db.getAllProduct();
                    } else {
                        sourceList = db.getAllProductCategory(category);
                        productList = db.getAllProductCategory(category);
                    }
                } else if(category.equals("category")) {
                    sourceList = db.getAllProductColor(color);
                    productList = db.getAllProductColor(color);
                } else {
                    sourceList = db.getAllProductCategoryColor(category, color);
                    productList = db.getAllProductCategoryColor(category, color);
                }

                if(productList.isEmpty()) {
                    txtNoProducts.setVisibility(View.VISIBLE);
                    txtNoProducts.setText("No products with that description");
                } else {
                    txtNoProducts.setVisibility(View.INVISIBLE);
                    txtNoProducts.setText("No product has been posted");
                }
                adapter = new ProductAdapter(getContext(), productList);
                lv.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cboCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = cboCategory.getItemAtPosition(position).toString().toLowerCase();
                productList.clear();
                sourceList.clear();

                if(category.equals("category")) {
                    if(color.equals("color")) {
                        sourceList = db.getAllProduct();
                        productList = db.getAllProduct();
                    } else {
                        sourceList = db.getAllProductColor(color);
                        productList = db.getAllProductColor(color);
                    }
                } else if(color.equals("color")) {
                    if(category.equals("category")) {
                        sourceList = db.getAllProduct();
                        productList = db.getAllProduct();
                    } else {
                        sourceList = db.getAllProductCategory(category);
                        productList = db.getAllProductCategory(category);
                    }
                } else {
                    sourceList = db.getAllProductCategoryColor(category, color);
                    productList = db.getAllProductCategoryColor(category, color);
                }

                if(productList.isEmpty()) {
                    txtNoProducts.setVisibility(View.VISIBLE);
                    txtNoProducts.setText("No products with that description");
                } else {
                    txtNoProducts.setVisibility(View.INVISIBLE);
                    txtNoProducts.setText("No product has been posted");
                }
                adapter = new ProductAdapter(getContext(), productList);
                lv.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(accountList.isEmpty()) {
                    Intent adminIntent = new Intent(getContext(), ViewProductActivity.class);
                    adminIntent.putExtra("productid", productList.get(position).getProductId());
                    startActivity(adminIntent);
                } else {
                    Intent customerIntent = new Intent(getContext(), ViewProductCustomerActivity.class);
                    customerIntent.putExtra("productid", productList.get(position).getProductId());
                    startActivity(customerIntent);
                }
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                color = "color";
                category = "category";
                txtSearch.setText("");
                cboColor.setSelection(0);
                cboCategory.setSelection(0);
                productList = db.getAllProduct();
                sourceList = db.getAllProduct();

                if(productList.isEmpty()) {
                    txtNoProducts.setVisibility(View.VISIBLE);

                    refreshList();
                } else {
                    txtNoProducts.setVisibility(View.INVISIBLE);
                    productList.clear();

                    refreshList();
                }

                try {
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch(Exception e) {
                    e.printStackTrace();
                }
                relativeLayout.requestFocus();
                refreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void refreshList() {
        productList = db.getAllProduct();
        adapter = new ProductAdapter(getContext(), productList);
        lv.setAdapter(adapter);
    }
}