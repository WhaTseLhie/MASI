package com.example.asus.masi.masi;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.asus.masi.AppDatabase;
import com.example.asus.masi.R;
import com.example.asus.masi.masi.add.Product;

import java.util.ArrayList;
import java.util.Arrays;

public class EditProductActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView btnMinusQty, btnPlusQty, btnMinusPrice, btnPlusPrice, iv;
    EditText txtBrand, txtName, txtPrice, txtDecimal, txtQuantity;
    Spinner cboCategory, cboColor;
    Button btnEdit, btnCancel;
    String color = "Color", category = "Category";
    Uri imageUri;
    Character c1;

    AppDatabase db;
    ArrayList<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        this.iv = this.findViewById(R.id.imageView);
        this.txtBrand = this.findViewById(R.id.editText);
        this.txtName = this.findViewById(R.id.editText2);
        this.txtPrice = this.findViewById(R.id.editText3);
        this.txtDecimal = this.findViewById(R.id.editText4);
        this.txtQuantity = this.findViewById(R.id.editText5);
        this.cboCategory = this.findViewById(R.id.spinner);
        this.cboColor = this.findViewById(R.id.spinner2);
        this.btnMinusPrice = this.findViewById(R.id.imageView2);
        this.btnPlusPrice = this.findViewById(R.id.imageView3);
        this.btnMinusQty = this.findViewById(R.id.imageView4);
        this.btnPlusQty = this.findViewById(R.id.imageView5);
        this.btnEdit = this.findViewById(R.id.button);
        this.btnCancel = this.findViewById(R.id.button2);

        //try {
            Bundle b = getIntent().getExtras();
            int productId = b.getInt("productid");
            this.db = new AppDatabase(this);
            this.productList = this.db.findProduct(productId);

            if(productList.isEmpty()) {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
                this.finish();
            } else {
                imageUri = productList.get(0).getProductPic();
                this.iv.setImageURI(imageUri);

                String p = "" +productList.get(0).getProductPrice();
                int ind = p.indexOf('.');
                //p = p.substring(0, p.length()-3);
                p = p.substring(0, ind);
                this.txtPrice.setText(p);

                String d = "" +productList.get(0).getProductPrice();
                //d = d.substring(p.length()-2, p.length());
                d = d.substring(ind, d.length());
                this.txtDecimal.setText(d);

                this.txtBrand.setText(productList.get(0).getProductBrand());
                this.txtName.setText(productList.get(0).getProductName());

                String cat[] = getResources().getStringArray(R.array.category);
                int catIndex = Arrays.asList(cat).indexOf(productList.get(0).getProductCategory());
                category = cat[catIndex];
                this.cboCategory.setSelection(catIndex);

                String col[] = getResources().getStringArray(R.array.color);
                int colorIndex = Arrays.asList(col).indexOf(productList.get(0).getProductColor());
                color = col[colorIndex];
                this.cboColor.setSelection(colorIndex);

                this.txtQuantity.setText("" +productList.get(0).getProductQty());
            }
        /*} catch(Exception e) {
            Log.d("SET TEXT ERR: ", e.getMessage());
        }*/

        this.btnMinusPrice.setOnClickListener(this);
        this.btnPlusPrice.setOnClickListener(this);
        this.btnMinusQty.setOnClickListener(this);
        this.btnPlusQty.setOnClickListener(this);

        this.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 10);
            }
        });

        this.txtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s != null) {
                    if(s.length() > 1) {
                        if(s.toString().startsWith("0")) {
                            s = s.toString().substring(1, s.toString().length());
                            txtPrice.setText(s);
                            txtPrice.setSelection(1);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(txtPrice.getText().length() == 0) {
                    txtPrice.setText("0");
                    txtPrice.setSelection(1);
                }
            }
        });

        this.txtDecimal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s != null) {
                    c1 = s.toString().charAt(0);
                    if(c1 != '.') {
                        s = s.toString().substring(1, s.length()) + c1;
                        txtDecimal.setText(s);
                    } else if(s.length() == 1) {
                        txtDecimal.setText(".0");
                        txtDecimal.setSelection(2);
                    } else if(s.length() == 3) {
                        Character c2 = s.toString().charAt(1);
                        if(c2.equals('0')) {
                            s = "." +s.toString().substring(2, 3);
                            txtDecimal.setText(s);
                            txtDecimal.setSelection(s.length());
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        this.txtQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s != null) {
                    if(s.length() > 1) {
                        if(s.toString().startsWith("0")) {
                            s = s.toString().substring(1, s.toString().length());
                            txtQuantity.setText(s);
                            txtQuantity.setSelection(1);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(txtQuantity.getText().length() == 0) {
                    txtQuantity.setText("0");
                    txtQuantity.setSelection(1);
                }
            }
        });

        this.cboColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                color = cboColor.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        this.cboCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = cboCategory.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        this.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String errMsg = "This field is empty.";
                String brand = txtBrand.getText().toString();
                String name = txtName.getText().toString();
                int qty = Integer.parseInt(txtQuantity.getText().toString());

                String decimal = txtDecimal.getText().toString();
                String strPrice = txtPrice.getText().toString() +""+ decimal;
                Double price = Double.parseDouble(strPrice);

                if(imageUri == null) {
                    Toast.makeText(getApplicationContext(), "Please select an image", Toast.LENGTH_LONG).show();
                } else if(TextUtils.isEmpty(brand)) {
                    txtBrand.requestFocus();
                    txtBrand.setError(errMsg);
                } else if(TextUtils.isEmpty(name)) {
                    txtName.requestFocus();
                    txtName.setError(errMsg);
                } else if(TextUtils.equals(category, "Category")) {
                    Toast.makeText(getApplicationContext(), "Please select category", Toast.LENGTH_LONG).show();
                } else if(TextUtils.equals(color, "Color")) {
                    Toast.makeText(getApplicationContext(), "Please select color", Toast.LENGTH_LONG).show();
                } else if(price < 20) {
                    Toast.makeText(getApplicationContext(), "Minimum Price is 20", Toast.LENGTH_LONG).show();
                } else if(qty < 1) {
                    Toast.makeText(getApplicationContext(), "Quantity must not be 0", Toast.LENGTH_LONG).show();
                } else {
                    db.editProduct(productList.get(0).getProductId(), imageUri.toString(), brand, name, category, color, price, qty, productList.get(0).getProductRating());

                    Toast.makeText(getApplicationContext(), "Product Successfully Updated", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        this.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int price = Integer.parseInt(txtPrice.getText().toString());
        int qty = Integer.parseInt(txtQuantity.getText().toString());

        switch(v.getId()) {
            case R.id.imageView2:
                if(price > 0) {
                    price -= 1;
                }

                break;
            case R.id.imageView3:
                if(price < 100000) {
                    price += 1;
                }

                break;
            case R.id.imageView4:
                if(qty > 0) {
                    qty -= 1;
                }

                break;
            case R.id.imageView5:
                qty += 1;

                break;
        }

        txtPrice.setText("" +price);
        txtQuantity.setText("" +qty);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            imageUri = data.getData();

            if (imageUri != null) {
                this.iv.setImageURI(imageUri);
            }
        } catch(Exception e) {
            Log.d("IMAGE ERR: ", e.getMessage());
        }
    }
}