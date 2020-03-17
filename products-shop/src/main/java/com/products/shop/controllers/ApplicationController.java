package com.products.shop.controllers;

import com.google.gson.Gson;
import com.products.shop.dtos.*;
import com.products.shop.services.CategoryService;
import com.products.shop.services.ProductService;
import com.products.shop.services.UserService;
import com.products.shop.utils.FileIOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static com.products.shop.globalconstants.GlobalConstants.*;

@Controller
public class ApplicationController implements CommandLineRunner {

    private final BufferedReader reader;
    private final Gson gson;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final UserService userService;
    private final FileIOUtil fileIOUtil;

    @Autowired
    public ApplicationController(BufferedReader reader, Gson gson,
                                 CategoryService categoryService,
                                 ProductService productService,
                                 UserService userService, FileIOUtil fileIOUtil) {
        this.reader = reader;
        this.gson = gson;
        this.categoryService = categoryService;
        this.productService = productService;
        this.userService = userService;
        this.fileIOUtil = fileIOUtil;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("For instructions press 0.");
        System.out.println("Enter number:");
        String input = this.reader.readLine();

        while (!input.equals("End")) {
            switch (input) {
                case "1":
                    this.seedCategories();
                    this.seedUsers();
                    this.seedProducts();
                    break;
                case "2":
                    this.writeProductsInRange();
                    break;
                case "3":
                    this.findProductsWithBuyer();
                    break;
                case "4":
                    this.findAllCategories();
                    break;
                case "5":
                    //TODO
                    this.usersAndProducts();
                    break;
                case "0":
                    this.information();
                    break;
                default:
                    System.out.println("Invalid input.");
            }

            System.out.println("For instructions press 0.");
            System.out.println("Enter number:");
            input = this.reader.readLine();
        }
    }

    private void usersAndProducts() {
        //TODO
        List<UsersProductsDto> dtos = this.productService.getAllProductsWithSeller();
    }

    private void information() {
        System.out.println("For seeding the information from files press 1");
        System.out.println("To execute first query and write into file \"firstExercise\" press 2");
        System.out.println("To execute second query and write into file \"secondExercise\" press 3");
        System.out.println("To execute third query and write into file \"thirdExercise\" press 4");
    }

    private void findAllCategories() throws IOException {
        List<CategoryDto> categories = this.categoryService.getAllCategories();

        String json = this.gson.toJson(categories);
        this.fileIOUtil.write(json, JSON_FILE_PATH_EX3);
    }

    private void findProductsWithBuyer() throws IOException {
        List<SellerDto> sellerDtos = this.productService.getAllProductsWithBuyer();

        String json = this.gson.toJson(sellerDtos);
        this.fileIOUtil.write(json, JSON_FILE_PATH_EX2);
    }

    private void writeProductsInRange() throws IOException {
        List<ProductAndSellerDto> dtos = this.productService
                .getAllProductsInRange();

        String json = this.gson.toJson(dtos);
        this.fileIOUtil.write(json, JSON_FILE_PATH_EX1);
    }

    private void seedUsers() throws FileNotFoundException {
        UserSeedDto[] dtos = this.gson
                .fromJson(new FileReader(USERS_FILE_PATH),
                        UserSeedDto[].class);

        this.userService.seedUsers(dtos);
    }

    private void seedProducts() throws FileNotFoundException {
        ProductSeedDto[] dtos = this.gson
                .fromJson(new FileReader(PRODUCTS_FILE_PATH),
                        ProductSeedDto[].class);

        this.productService.seedProducts(dtos);
    }

    private void seedCategories() throws FileNotFoundException {
        CategorySeedDto[] dtos = this.gson
                .fromJson(new FileReader(CATEGORIES_FILE_PATH),
                        CategorySeedDto[].class);

        this.categoryService.seedCategories(dtos);
    }
}
