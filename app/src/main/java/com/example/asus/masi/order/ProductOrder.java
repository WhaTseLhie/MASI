package com.example.asus.masi.order;

public class ProductOrder {

    int orderId, productId, productQty;
    double productTotalPrice;

    public ProductOrder(int orderId, int productId, int productQty, double productTotalPrice) {
        this.orderId = orderId;
        this.productId = productId;
        this.productQty = productQty;
        this.productTotalPrice = productTotalPrice;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductQty() {
        return productQty;
    }

    public void setProductQty(int productQty) {
        this.productQty = productQty;
    }

    public double getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(double productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }
}