package com.example.asus.masi.profile;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.masi.Account;
import com.example.asus.masi.AppDatabase;
import com.example.asus.masi.CircleTransform;
import com.example.asus.masi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;


public class ProfileActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    final static int PICK_IMAGE_REQUEST_CODE = 10;

    ImageView iv;
    TextView txtEditImage, txtFullname, txtGender, txtContact, txtEmail, txtAddress;

    Uri imageUri;
    Dialog dialog;
    RadioGroup rdoEditGender;
    TextView txtEditFname, txtEditLname, txtEditEmail, txtEditContact, txtTotalBal;
    Button btnEdit, btnCancel;

    TextView txtUsername, txtPassword;
    CheckBox checkBox;
    Button btnChangePassword;

    Dialog dialog2;
    EditText txtOld, txtNew, txtConfirm;
    Button btnEditSave, btnEditCancel;

    AppDatabase db;
    ArrayList<Account> list = new ArrayList<>();

    String type = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.db = new AppDatabase(this);
        this.list = db.getAccountInfo();

        this.txtEditImage = this.findViewById(R.id.txtEditImage);
        this.iv = this.findViewById(R.id.imageView);
        this.txtFullname = this.findViewById(R.id.textView);
        this.txtGender = this.findViewById(R.id.textView2);
        this.txtContact = this.findViewById(R.id.textView3);
        this.txtEmail = this.findViewById(R.id.textView4);
        this.txtAddress = this.findViewById(R.id.textView5);
        this.txtUsername = this.findViewById(R.id.textView6);
        this.txtPassword = this.findViewById(R.id.textView7);
        this.txtTotalBal = this.findViewById(R.id.textView8);
        this.checkBox = this.findViewById(R.id.checkBox);
        this.btnChangePassword = this.findViewById(R.id.button);

        try {
            Bundle b = getIntent().getExtras();
            type = b.getString("type");

            Picasso.with(this).load(list.get(0).getProfilePic()).transform(new CircleTransform()).into(iv);
            this.txtFullname.setText(list.get(0).getFirstname() +" "+ list.get(0).getLastname());
            this.txtGender.setText(list.get(0).getGender());
            this.txtContact.setText(list.get(0).getContact());
            this.txtEmail.setText(list.get(0).getEmail());
            this.txtAddress.setText(list.get(0).getAddress());
            this.txtUsername.setText(list.get(0).getUsername());
            this.txtPassword.setText(list.get(0).getPassword());
            this.txtTotalBal.setText(String.format(Locale.getDefault(), "\u20B1%.2f" ,list.get(0).getTotalBal()));

            if(type.equals("admin")) {
                db.deleteAllAccount();
                this.txtEditImage.setVisibility(View.INVISIBLE);
                //this.btnChangePassword.setVisibility(View.INVISIBLE);

                if(list.get(0).getStatus().equals("ACTIVATED")) {
                    this.btnChangePassword.setText("DEACTIVATE");
                } else {
                    this.btnChangePassword.setText("ACTIVATE");
                }
            }
        } catch(Exception e) {
            Log.d("Bundle error: ", e.getMessage());
        }

        this.txtEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtEditImage.getText().toString().equals("Edit")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
                    txtEditImage.setText("Save");
                } else {
                    db.editCustomerAccount(list.get(0).getCustId(), imageUri.toString(), list.get(0).getUsername(), list.get(0).getPassword(), list.get(0).getFirstname(), list.get(0).getLastname(), list.get(0).getGender(), list.get(0).getEmail(), list.get(0).getContact(), list.get(0).getAddress(), list.get(0).getStatus());
                    txtEditImage.setText("Edit");
                    Toast.makeText(getApplicationContext(), "Image Updated", Toast.LENGTH_LONG).show();

                    finish();
                    startActivity(getIntent());
                }
            }
        });

        this.checkBox.setOnCheckedChangeListener(this);
        this.btnChangePassword.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);

        if(type.equals("admin")) {
            menu.findItem(R.id.edit).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.edit) {
            dialog = new Dialog(this);
            dialog.setContentView(R.layout.layout_profile_edit);

            txtEditFname = dialog.findViewById(R.id.editText);
            txtEditLname = dialog.findViewById(R.id.editText2);
            rdoEditGender = dialog.findViewById(R.id.radioGroup);
            txtEditEmail = dialog.findViewById(R.id.editText3);
            txtEditContact = dialog.findViewById(R.id.editText4);
            btnEdit = dialog.findViewById(R.id.button);
            btnCancel = dialog.findViewById(R.id.button2);

            txtEditFname.setText(list.get(0).getFirstname());
            txtEditLname.setText(list.get(0).getLastname());
            if(list.get(0).getGender().equals("Male")) {
                rdoEditGender.check(R.id.radioButton);
            } else {
                rdoEditGender.check(R.id.radioButton2);
            }
            txtEditEmail.setText(list.get(0).getEmail());
            txtEditContact.setText(list.get(0).getContact());

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String errMsg = "This field is empty.";
                    String fname = txtEditFname.getText().toString();
                    String lname = txtEditLname.getText().toString();
                    String email = txtEditEmail.getText().toString();
                    String contact = txtEditContact.getText().toString();

                    if(TextUtils.isEmpty(fname)) {
                        txtEditFname.requestFocus();
                        txtEditFname.setError(errMsg);
                    } else if(TextUtils.isEmpty(lname)) {
                        txtEditLname.requestFocus();
                        txtEditLname.setError(errMsg);
                    } else if(TextUtils.isEmpty(email)) {
                        txtEditEmail.requestFocus();
                        txtEditEmail.setError(errMsg);
                    } else if(TextUtils.isEmpty(contact)) {
                        txtEditContact.requestFocus();
                        txtEditContact.setError(errMsg);
                    } else {
                        int selectedSex = rdoEditGender.getCheckedRadioButtonId();
                        RadioButton selectedButton = dialog.findViewById(selectedSex);
                        String gender = selectedButton.getText().toString();

                        db.editCustomerAccount(list.get(0).getCustId(), list.get(0).getProfilePic().toString(), list.get(0).getUsername(), list.get(0).getPassword(), txtEditFname.getText().toString(), txtEditLname.getText().toString(), gender, txtEditEmail.getText().toString(), txtEditContact.getText().toString(), list.get(0).getAddress(), list.get(0).getStatus());
                        Toast.makeText(getApplicationContext(), "Update Saved", Toast.LENGTH_LONG).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            imageUri = data.getData();

            if(imageUri != null) {
                this.iv.setImageURI(imageUri);
            }
        } catch(Exception e) {
            Log.d("IMAGE ERR: ", e.getMessage());
            txtEditImage.setText("Edit");
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!isChecked) {
            txtPassword.setTransformationMethod(new PasswordTransformationMethod());
        } else {
            txtPassword.setTransformationMethod(null);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button:
                if(btnChangePassword.getText().toString().equals("CHANGE PASSWORD")) {
                    dialog2 = new Dialog(this);
                    dialog2.setContentView(R.layout.layout_change_password);

                    txtOld = dialog2.findViewById(R.id.editText);
                    txtNew = dialog2.findViewById(R.id.editText2);
                    txtConfirm = dialog2.findViewById(R.id.editText3);
                    btnEditSave = dialog2.findViewById(R.id.button);
                    btnEditCancel = dialog2.findViewById(R.id.button2);

                    btnEditSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String newPass = txtNew.getText().toString();
                            String confirmPass = txtConfirm.getText().toString();
                            String errMsg = "Passwords did not match! Please try again";

                            if (TextUtils.equals(txtOld.getText().toString(), list.get(0).getPassword())) {
                                if (!TextUtils.isEmpty(newPass)) {
                                    if (TextUtils.equals(newPass, confirmPass)) {
                                        db.editCustomerAccount(list.get(0).getCustId(), list.get(0).getProfilePic().toString(), list.get(0).getUsername(), confirmPass, list.get(0).getFirstname(), list.get(0).getLastname(), list.get(0).getGender(), list.get(0).getEmail(), list.get(0).getContact(), list.get(0).getAddress(), list.get(0).getStatus());
                                        Toast.makeText(getApplicationContext(), "Password Changed", Toast.LENGTH_LONG).show();
                                        dialog2.dismiss();
                                        finish();
                                        startActivity(getIntent());
                                    } else {
                                        txtConfirm.requestFocus();
                                        txtConfirm.setError(errMsg);
                                    }
                                } else {
                                    txtNew.requestFocus();
                                    txtNew.setError("This field is empty.");
                                }
                            } else {
                                txtOld.requestFocus();
                                txtOld.setError(errMsg);
                            }
                        }
                    });

                    btnEditCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });

                    dialog2.show();
                } else {
                    this.db.deactivateCustomerAccount(list.get(0).getCustId(), btnChangePassword.getText().toString()+ "D");
                    Toast.makeText(this, "Customer has been " +btnChangePassword.getText().toString().toLowerCase()+ "d", Toast.LENGTH_LONG).show();
                    this.finish();
                }
                break;
        }
    }
}