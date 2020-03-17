package com.products.shop.dtos;

import com.google.gson.annotations.Expose;
import com.products.shop.entities.Product;

import java.util.Set;

public class SellerDto {

    @Expose
    private String firstName;

    @Expose
    private String lastName;

    @Expose
    private Set<ProductDto> products;

    public SellerDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<ProductDto> getSoldProducts() {
        return products;
    }

    public void setSoldProducts(Set<ProductDto> products) {
        this.products = products;
    }
}
