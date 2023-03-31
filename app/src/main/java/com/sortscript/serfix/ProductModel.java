package com.sortscript.serfix;

public class ProductModel {
    String ProductName,ProductDescription,Price,ProductImage,Key;

    public ProductModel() {
    }

    public ProductModel(String productName, String productDescription, String price, String productImage, String key) {
        ProductName = productName;
        ProductDescription = productDescription;
        Price = price;
        ProductImage = productImage;
        Key = key;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductDescription() {
        return ProductDescription;
    }

    public void setProductDescription(String productDescription) {
        ProductDescription = productDescription;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
