package com.example.asus.masi.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.asus.masi.Account;
import com.example.asus.masi.AppDatabase;
import com.example.asus.masi.Customer;
import com.example.asus.masi.R;
import com.example.asus.masi.profile.ProfileActivity;

import java.util.ArrayList;

public class ViewAccountActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    TextView txtEmptyList;
    ListView lv;

    ArrayList<Customer> customerList = new ArrayList<>();
    ArrayList<Account> list = new ArrayList<>();
    CustomerAdapter adapter;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account);

        this.db = new AppDatabase(this);
        this.customerList = this.db.getAllCustomer();

        this.lv = this.findViewById(R.id.listView);
        this.txtEmptyList = this.findViewById(R.id.textView);

        this.adapter = new CustomerAdapter(this, customerList);
        this.lv.setAdapter(adapter);

        if(!customerList.isEmpty()) {
            this.txtEmptyList.setVisibility(View.INVISIBLE);
        } else {
            this.txtEmptyList.setVisibility(View.VISIBLE);
        }

        this.lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.db.deleteAllAccount();
        this.list = this.db.findAccount(customerList.get(position).getUsername());

        if(!list.isEmpty()) {
            this.db.addAccount(list.get(0).getCustId(), list.get(0).getProfilePic().toString(), list.get(0).getUsername(), list.get(0).getPassword(), list.get(0).getFirstname(), list.get(0).getLastname(), list.get(0).getGender(), list.get(0).getEmail(), list.get(0).getContact(), list.get(0).getAddress(), list.get(0).getTotalBal(), list.get(0).getStatus());
            Intent profileIntent = new Intent(this, ProfileActivity.class);
            profileIntent.putExtra("type", "admin");
            startActivity(profileIntent);
        }
    }
}