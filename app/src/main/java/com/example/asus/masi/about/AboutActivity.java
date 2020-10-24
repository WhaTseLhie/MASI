package com.example.asus.masi.about;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.masi.AppDatabase;
import com.example.asus.masi.R;

import java.util.ArrayList;

public class AboutActivity extends AppCompatActivity {

    String type;
    ImageView iv;
    TextView txtAbout, txtServiceHours, txtAddress;

    Uri imageUri;
    Dialog dialog;
    EditText txtEditAbout, txtEditHours, txtEditAddress;

    AppDatabase db;
    ArrayList<About> list = new ArrayList<>();

    String about = "       Boyboy Motorparts and Accessories Store is the place where you can buy motorcycle parts and accessories for your motorcycle." +
            " It is perfect for customize motorcycle. We offer various parts with various brands/owners so you can have a lot to choose from." +
            " It was established at 2010. We are open all days.";
    String serviceHours = "9:00am - 7:00pm";
    String address = "UBCA II Quiot Cebu City Philippines";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        this.db = new AppDatabase(this);
        this.list = this.db.getAllAbout();

        this.iv = this.findViewById(R.id.imageView);
        this.txtAbout = this.findViewById(R.id.textView);
        this.txtServiceHours = this.findViewById(R.id.textView2);
        this.txtAddress = this.findViewById(R.id.textView3);
        //this.iv.setImageResource(R.drawable.app_icon);
        this.iv.setImageResource(R.drawable.bbmasi_icon);

        try {
            Bundle b = getIntent().getExtras();
            type = b.getString("type");
        } catch(Exception e) {
            Log.d("Bundle error: ", e.getMessage());
        }

        if(list.isEmpty()) {
            this.txtAbout.setText(about);
            this.txtServiceHours.setText("Service Hours: " +serviceHours);
            this.txtAddress.setText("Address: " +address);
            //this.db.addAbout(about, serviceHours, address);
        } else {
            this.txtAbout.setText(list.get(0).getAppAbout());
            this.txtServiceHours.setText("Service Hours: " +list.get(0).getAppServiceHours());
            this.txtAddress.setText("Address: " +list.get(0).getAppAddress());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);

        if(type.equals("customer")) {
            menu.findItem(R.id.edit).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.edit) {
            dialog = new Dialog(this);
            dialog.setContentView(R.layout.layout_about_edit);

            txtEditAbout = dialog.findViewById(R.id.editText);
            txtEditHours = dialog.findViewById(R.id.editText2);
            txtEditAddress = dialog.findViewById(R.id.editText3);
            Button btnSave = dialog.findViewById(R.id.button);
            Button btnCancel = dialog.findViewById(R.id.button2);

            list.clear();
            list = db.getAllAbout();
            if(!list.isEmpty()) {
                txtEditAbout.setText(list.get(0).getAppAbout());
                txtEditHours.setText(list.get(0).getAppServiceHours());
                txtEditAddress.setText(list.get(0).getAppAddress());
            } else {
                txtEditAbout.setText(about);
                txtEditHours.setText(serviceHours);
                txtEditAddress.setText(address);
            }

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String about = txtEditAbout.getText().toString();
                    String hours = txtEditHours.getText().toString();
                    String address = txtEditAddress.getText().toString();

                    /*if(imageUri == null) {
                        Toast.makeText(getApplicationContext(), "Please select logo", Toast.LENGTH_LONG).show();
                    } else */if(TextUtils.isEmpty(about)) {
                        txtEditAbout.requestFocus();
                        txtEditAbout.setError("This field is empty");
                    } else {
                        db.deleteAllAbout();
                        db.addAbout(about, hours, address);

                        dialog.dismiss();
                        finish();
                        startActivity(getIntent());
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
        }

        return super.onOptionsItemSelected(item);
    }
}