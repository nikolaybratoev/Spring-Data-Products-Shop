package com.products.shop.services;

import com.products.shop.dtos.ProductAndSellerDto;
import com.products.shop.dtos.ProductSeedDto;
import com.products.shop.dtos.SellerDto;
import com.products.shop.dtos.UsersProductsDto;

import java.util.List;

public interface ProductService {

    void seedProducts(ProductSeedDto[] productSeedDtos);

    List<ProductAndSellerDto> getAllProductsInRange();

    List<SellerDto> getAllProductsWithBuyer();

    List<UsersProductsDto> getAllProductsWithSeller();
}
