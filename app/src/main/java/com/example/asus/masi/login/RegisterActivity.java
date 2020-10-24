package com.example.asus.masi.login;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.masi.Account;
import com.example.asus.masi.AppDatabase;
import com.example.asus.masi.R;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    final static int UPLOAD_IMAGE_REQUEST_CODE = 10;

    Button btnRegister, btnCancel;
    ImageView iv;
    EditText txtFname, txtLname, txtUsername, txtEmail, txtContact, txtPass, txtPass2;
    RadioGroup rdoGender;
    String fname, lname, username, email, contact, pass, pass2;

    AppDatabase db;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new AppDatabase(this);

        this.btnRegister = this.findViewById(R.id.button);
        this.btnCancel = this.findViewById(R.id.button2);
        this.rdoGender = this.findViewById(R.id.radioGroup);
        this.iv = this.findViewById(R.id.imageView);
        this.txtFname = this.findViewById(R.id.editText);
        this.txtLname = this.findViewById(R.id.editText2);
        this.txtEmail = this.findViewById(R.id.editText3);
        this.txtContact = this.findViewById(R.id.editText4);
        this.txtUsername = this.findViewById(R.id.editText5);
        this.txtPass = this.findViewById(R.id.editText6);
        this.txtPass2 = this.findViewById(R.id.editText7);

        this.iv.setOnClickListener(this);
        this.btnRegister.setOnClickListener(this);
        this.btnCancel.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            imageUri = data.getData();

            if (imageUri != null) {
                iv.setImageURI(imageUri);
            }
        } catch(Exception e) {
            Log.d("Image on SD Card Err=", e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.imageView:
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{

                                Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                    }
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    this.startActivityForResult(intent, UPLOAD_IMAGE_REQUEST_CODE);
                }

                break;
            case R.id.button:
                fname = txtFname.getText().toString();
                lname = txtLname.getText().toString();
                email = txtEmail.getText().toString();
                contact = txtContact.getText().toString();
                username = txtUsername.getText().toString().toLowerCase();
                pass = txtPass.getText().toString();
                pass2 = txtPass2.getText().toString();
                checkFields(fname, lname, email, contact, username, pass, pass2);
                break;

            case R.id.button2:
                this.finish();
                break;
        }
    }

    public void checkFields(String fname, String lname, String email, String contact, String username, String pass, String pass2){
        String errMsg = "This field is empty.";

        if(TextUtils.isEmpty(fname)) {
            txtFname.requestFocus();
            txtFname.setError(errMsg);
        } else if(TextUtils.isEmpty(lname)) {
            txtLname.requestFocus();
            txtLname.setError(errMsg);
        } else if(TextUtils.isEmpty(email)) {
            txtEmail.requestFocus();
            txtEmail.setError(errMsg);
        } else if(TextUtils.isEmpty(contact)) {
            txtContact.requestFocus();
            txtContact.setError(errMsg);
        } else if(TextUtils.isEmpty(username)) {
            txtUsername.requestFocus();
            txtUsername.setError(errMsg);
        } else if(TextUtils.isEmpty(pass)) {
            txtPass.requestFocus();
            txtPass.setError(errMsg);
        } else if(TextUtils.isEmpty(pass2)) {
            txtPass2.requestFocus();
            txtPass2.setError(errMsg);
        } else if(!TextUtils.equals(pass, pass2)) {
            errMsg = "Passwords did not match! Please try again";
            txtPass2.requestFocus();
            txtPass2.setError(errMsg);
        } else if(imageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<Account> accountList = db.findAccount(username);

            if(accountList.isEmpty()) {
                int selectedSex = this.rdoGender.getCheckedRadioButtonId();
                RadioButton selectedButton = this.findViewById(selectedSex);
                String gender = selectedButton.getText().toString();

                db.addCustomer(imageUri.toString(), username, pass, fname, lname, gender, email, contact, "address", 0, "");
                Toast.makeText(this, "Account Successfully Registered", Toast.LENGTH_LONG).show();
                this.finish();
            } else {
                txtUsername.requestFocus();
                txtUsername.setError("Username is taken");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case 100:
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    final Dialog dialog = new Dialog(this);
                    dialog.setContentView(R.layout.layout_permission);

                    TextView t1 = (TextView) dialog.findViewById(R.id.textView);
                    TextView t2 = (TextView) dialog.findViewById(R.id.textView2);
                    t1.setText("Request Permissions");
                    t2.setText("Please allow permissions if you want this application to perform the task.");

                    Button dialogButton = (Button) dialog.findViewById(R.id.button);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    this.startActivityForResult(intent, UPLOAD_IMAGE_REQUEST_CODE);
                }

                break;
        }
    }
}