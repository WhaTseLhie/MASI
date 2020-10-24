package com.example.asus.masi.rating;

import android.net.Uri;

public class Rating {

    int id, productId, customerId;
    Uri customerPic;
    float rating;
    String customerName, comment, date;

    public Rating(int id, int productId, int customerId, Uri customerPic, String customerName, float rating, String comment, String date) {
        this.id = id;
        this.productId = productId;
        this.customerId = customerId;
        this.customerPic = customerPic;
        this.customerName = customerName;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Uri getCustomerPic() {
        return customerPic;
    }

    public void setCustomerPic(Uri customerPic) {
        this.customerPic = customerPic;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}