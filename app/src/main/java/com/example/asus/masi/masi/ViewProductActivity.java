package com.example.asus.masi.masi;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.masi.AppDatabase;
import com.example.asus.masi.CircleTransform;
import com.example.asus.masi.R;
import com.example.asus.masi.masi.add.Product;
import com.example.asus.masi.rating.Rating;
import com.example.asus.masi.rating.RatingAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewProductActivity extends AppCompatActivity {

    TextView txtPrice, txtBrand, txtName, txtCategory, txtColor, txtReviews, txtNoReviews;
    RatingBar ratingBar;
    ImageView iv;
    ListView lv;

    ArrayList<Product> productList = new ArrayList<>();
    AppDatabase db;

    ArrayList<Rating> ratingList = new ArrayList<>();
    RatingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        this.db = new AppDatabase(this);

        this.iv = this.findViewById(R.id.imageView);
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

            if(productList.isEmpty()) {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
                this.finish();
            } else {
                this.iv.setImageURI(productList.get(0).getProductPic());
                this.txtPrice.setText(String.format("\u20B1 %.2f", productList.get(0).getProductPrice()));
                this.txtBrand.setText(productList.get(0).getProductBrand());
                this.txtName.setText(productList.get(0).getProductName());
                this.txtCategory.setText(productList.get(0).getProductCategory());
                this.txtColor.setText(productList.get(0).getProductColor());
                this.ratingBar.setRating(productList.get(0).getProductRating());
                this.txtReviews.setText(ratingList.size()+ " Reviews");
                this.ratingBar.setRating(db.getProductRating(productId));
            }

            if(ratingList.isEmpty()) {
                txtNoReviews.setVisibility(View.VISIBLE);
            } else {
                txtNoReviews.setVisibility(View.INVISIBLE);

                setListViewHeightBasedOnChildren(lv);
            }
        } catch(Exception e) {
            Log.d("SET TEXT ERR: ", e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.edit) {
            Intent intent = new Intent(this, EditProductActivity.class);
            intent.putExtra("productid", productList.get(0).getProductId());
            startActivityForResult(intent, 10);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        this.finish();
        this.startActivity(getIntent());
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