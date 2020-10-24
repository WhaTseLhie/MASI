package com.example.asus.masi.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.masi.Account;
import com.example.asus.masi.AppDatabase;
import com.example.asus.masi.R;
import com.example.asus.masi.customer.CustomerActivity;
import com.example.asus.masi.masi.MasiActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener {

    ImageView iv;
    EditText txtUsername, txtPassword;
    Button btnLogin, btnRegister;

    AppDatabase db;
    ArrayList<Account> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.db = new AppDatabase(this);
        this.db.deleteAllAccount();

        this.iv = this.findViewById(R.id.imageView);
        this.txtUsername = this.findViewById(R.id.editText);
        this.txtPassword = this.findViewById(R.id.editText2);
        this.btnLogin = this.findViewById(R.id.button);
        this.btnRegister = this.findViewById(R.id.button2);

        this.btnLogin.setOnClickListener(this);
        this.btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.button:
                db.deleteAllAccount();
                String errMsg = "This field is empty.";
                String username = txtUsername.getText().toString().toLowerCase();
                String password = txtPassword.getText().toString();

                if(TextUtils.isEmpty(username)) {
                    txtUsername.requestFocus();
                    txtUsername.setError(errMsg);
                } else if(TextUtils.isEmpty(password)) {
                    txtPassword.requestFocus();
                    txtPassword.setError(errMsg);
                } else {
                    if(TextUtils.equals(username, "admin") && TextUtils.equals(password, "admin")) {
                        this.db.deleteAllAccount();
                        Intent intent = new Intent(this, MasiActivity.class);
                        this.startActivity(intent);
                    } else {
                        this.list = this.db.findAccount(username);

                        if (!list.isEmpty()) {
                            if (!list.get(0).getUsername().equals(username)) {
                                Toast.makeText(this, "Invalid Username! Try Again", Toast.LENGTH_LONG).show();
                                txtUsername.setText("");
                            } else if (!list.get(0).getPassword().equals(password)) {
                                Toast.makeText(this, "Invalid Password! Try Again", Toast.LENGTH_LONG).show();
                                txtPassword.setText("");
                            } else {
                                if(list.get(0).getStatus().equals("ACTIVATED")) {
                                    this.db.deleteAllAccount();
                                    this.db.addAccount(list.get(0).getCustId(), list.get(0).getProfilePic().toString(), list.get(0).getUsername(), list.get(0).getPassword(), list.get(0).getFirstname(), list.get(0).getLastname(), list.get(0).getGender(), list.get(0).getEmail(), list.get(0).getContact(), list.get(0).getAddress(), list.get(0).getTotalBal(), list.get(0).getStatus());

                                    Intent intent = new Intent(this, CustomerActivity.class);
                                    this.startActivity(intent);
                                } else {
                                    Toast.makeText(this, "Your account has been deactivated by the administrator", Toast.LENGTH_LONG).show();
                                }
                            }
                        } else {
                            Toast.makeText(this, "Login Failed! Try Again", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                break;
            case R.id.button2:
                Intent intentRegister = new Intent(this, RegisterActivity.class);
                startActivity(intentRegister);

                break;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("Yes", this);
        builder.setNegativeButton("No", this);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch(i) {
            case DialogInterface.BUTTON_POSITIVE:
                System.exit(0);
                break;
        }
    }
}