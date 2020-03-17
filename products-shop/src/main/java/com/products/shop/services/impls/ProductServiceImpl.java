package com.products.shop.services.impls;

import com.products.shop.dtos.*;
import com.products.shop.entities.Product;
import com.products.shop.entities.User;
import com.products.shop.repositories.ProductRepository;
import com.products.shop.services.CategoryService;
import com.products.shop.services.ProductService;
import com.products.shop.services.UserService;
import com.products.shop.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    private static Comparator<Product> compareSize = (p1, p2) -> Integer.compare(p2.getSeller()
            .getProducts().size(), p1.getSeller().getProducts().size());

    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final ValidationUtil validationUtil;
    private final UserService userService;
    private final CategoryService categoryService;

    @Autowired
    public ProductServiceImpl(ModelMapper modelMapper, ProductRepository productRepository,
                              ValidationUtil validationUtil,
                              UserService userService,
                              CategoryService categoryService) {
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
        this.validationUtil = validationUtil;
        this.userService = userService;
        this.categoryService = categoryService;
    }


    @Override
    public void seedProducts(ProductSeedDto[] productSeedDtos) {

        Arrays.stream(productSeedDtos)
                .forEach(productSeedDto -> {
                    if (this.isNotPresent(productSeedDto)) {

                        if (this.validationUtil.isValid(productSeedDto)) {
                            Product product = this.modelMapper
                                    .map(productSeedDto, Product.class);

                            product.setSeller(this.userService.getRandomUser());

                            Random random = new Random();
                            int randomNumber = random.nextInt(2);
                            if (randomNumber == 1) {
                                product.setBuyer(this.userService.getRandomUser());
                            }

                            product.setCategories(new HashSet<>(this.categoryService
                                    .getRandomCategories()));

                            this.productRepository.saveAndFlush(product);
                        } else {
                            this.validationUtil.getViolations(productSeedDto)
                                    .stream()
                                    .map(ConstraintViolation::getMessage)
                                    .forEach(System.out::println);
                        }
                    }
                });
    }

    @Override
    public List<ProductAndSellerDto> getAllProductsInRange() {
        return this.productRepository
                .findAllByPriceBetweenAndBuyerIsNull(BigDecimal.valueOf(500),
                        BigDecimal.valueOf(1000))
                .stream()
                .map(product -> {
                    ProductAndSellerDto productAndSellerDto = this.modelMapper
                            .map(product, ProductAndSellerDto.class);

                    productAndSellerDto.setSeller(String.format("%s %s",
                            product.getSeller().getFirstName(),
                            product.getSeller().getLastName()));

                    return productAndSellerDto;
                }).collect(Collectors.toList());
    }

    @Override
    public List<SellerDto> getAllProductsWithBuyer() {
       return this.productRepository
                .findProductsByBuyerIsNotNull()
                .stream()
                .sorted(Comparator.comparing(p -> p.getSeller().getLastName()))
                .map(product -> {
                    User user = this.userService.getUserByLastName(product.getSeller().getLastName());
                    SellerDto sellerDto = this.modelMapper
                                .map(user, SellerDto.class);
                    Set<ProductDto> dtos = new HashSet<>();
                    user.getProducts()
                            .stream()
                            .map(p -> modelMapper.map(p, ProductDto.class))
                            .forEach(p -> {
                                p.setFirstName(product.getBuyer().getFirstName());
                                p.setLastName(product.getBuyer().getLastName());
                                dtos.add(p);
                            });

                    sellerDto.setSoldProducts(dtos);
                    return sellerDto;
                }).collect(Collectors.toList());
    }

    @Override
    public List<UsersProductsDto> getAllProductsWithSeller() {
        //TODO

        return null;
    }

    private boolean isNotPresent(ProductSeedDto productSeedDto) {
        Product product = this.productRepository.findFirstByName(productSeedDto.getName());
        if (product != null) {
            return false;
        }
        return true;
    }
}
