package com.example.asus.masi.customer;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.masi.Account;
import com.example.asus.masi.AppDatabase;
import com.example.asus.masi.CircleTransform;
import com.example.asus.masi.R;
import com.example.asus.masi.about.AboutActivity;
import com.example.asus.masi.cart.CartActivity;
import com.example.asus.masi.masi.ProductFragment;
import com.example.asus.masi.order.OrderActivity;
import com.example.asus.masi.profile.ProfileActivity;
import com.example.asus.masi.view.ViewAccountActivity;
import com.example.asus.masi.wallet.WalletActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DialogInterface.OnClickListener {

    AppDatabase db;
    ArrayList<Account> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        db = new AppDatabase(this);
        list = db.getAccountInfo();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                try {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch(Exception e) {
                    Log.d("InputMethodManager ERR:", e.getMessage());
                }
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        ImageView iv = headerView.findViewById(R.id.imageView);
        TextView txtName = headerView.findViewById(R.id.textView);
        TextView txtEmail = headerView.findViewById(R.id.textView2);

        Picasso.with(this).load(list.get(0).getProfilePic()).transform(new CircleTransform()).into(iv);
        txtName.setText("Name: " +list.get(0).getFirstname()+ " " +list.get(0).getLastname());
        txtEmail.setText("Email: " +list.get(0).getEmail());

        ProductFragment productFragment = new ProductFragment();
        FragmentManager productManager = getSupportFragmentManager();
        productManager.beginTransaction().replace(
                R.id.constraintLayout,
                productFragment,
                productFragment.getTag()
        ).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to logout?");
            builder.setPositiveButton("Yes", this);
            builder.setNegativeButton("No", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent profileIntent = new Intent(this, ProfileActivity.class);
            profileIntent.putExtra("type", "customer");
            startActivity(profileIntent);
        } else if (id == R.id.nav_wallet) {
            Intent walletIntent = new Intent(this, WalletActivity.class);
            startActivity(walletIntent);
        } else if (id == R.id.nav_order) {
            Intent orderIntent = new Intent(this, OrderActivity.class);
            startActivity(orderIntent);
        } else if (id == R.id.nav_about) {
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            aboutIntent.putExtra("type", "customer");
            startActivity(aboutIntent);
        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to logout?");
            builder.setPositiveButton("Yes", this);
            builder.setNegativeButton("No", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.customer, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.cart) {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch(i) {
            case DialogInterface.BUTTON_POSITIVE:
                this.finish();

                break;
        }
    }
}