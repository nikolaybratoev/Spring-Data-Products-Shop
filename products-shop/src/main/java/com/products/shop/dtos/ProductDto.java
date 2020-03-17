package com.products.shop.dtos;

import com.google.gson.annotations.Expose;

import java.math.BigDecimal;

public class ProductDto {

    @Expose
    private String name;

    @Expose
    private BigDecimal price;

    @Expose
    private String buyerFirstName;

    @Expose
    private String buyerLastName;

    public ProductDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getFirstName() {
        return buyerFirstName;
    }

    public void setFirstName(String buyerFirstName) {
        this.buyerFirstName = buyerFirstName;
    }

    public String getLastName() {
        return buyerLastName;
    }

    public void setLastName(String buyerLastName) {
        this.buyerLastName = buyerLastName;
    }
}
