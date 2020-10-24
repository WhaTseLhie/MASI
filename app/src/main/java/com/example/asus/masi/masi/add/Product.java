package com.example.asus.masi.masi.add;

import android.net.Uri;

public class Product {

    int productId;
    Uri productPic;
    String productBrand, productName, productCategory, productColor;
    double productPrice;
    int productQty;
    float productRating;

    public Product(int productId, Uri productPic, String productBrand, String productName, String productCategory, String productColor, double productPrice, int productQty, float productRating) {
        this.productId = productId;
        this.productPic = productPic;
        this.productBrand = productBrand;
        this.productName = productName;
        this.productCategory = productCategory;
        this.productColor = productColor;
        this.productPrice = productPrice;
        this.productQty = productQty;
        this.productRating = productRating;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Uri getProductPic() {
        return productPic;
    }

    public void setProductPic(Uri productPic) {
        this.productPic = productPic;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQty() {
        return productQty;
    }

    public void setProductQty(int productQty) {
        this.productQty = productQty;
    }

    public float getProductRating() {
        return productRating;
    }

    public void setProductRating(float productRating) {
        this.productRating = productRating;
    }
}