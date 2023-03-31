package com.sortscript.serfix;

public class CartModel {
    String Description,Image,Price,Private,Product,Quantity,TotalAmount;

    public CartModel() {
    }

    public CartModel(String description, String image, String price, String aPrivate, String product, String quantity, String totalAmount) {
        Description = description;
        Image = image;
        Price = price;
        Private = aPrivate;
        Product = product;
        Quantity = quantity;
        TotalAmount = totalAmount;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getPrivate() {
        return Private;
    }

    public void setPrivate(String aPrivate) {
        Private = aPrivate;
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }
}
