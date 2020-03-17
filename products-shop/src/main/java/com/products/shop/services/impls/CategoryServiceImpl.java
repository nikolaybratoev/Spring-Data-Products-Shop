package com.products.shop.services.impls;

import com.products.shop.dtos.CategoryDto;
import com.products.shop.dtos.CategorySeedDto;
import com.products.shop.entities.Category;
import com.products.shop.entities.Product;
import com.products.shop.repositories.CategoryRepository;
import com.products.shop.services.CategoryService;
import com.products.shop.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final ValidationUtil validationUtil;

    @Autowired
    public CategoryServiceImpl(ModelMapper modelMapper, CategoryRepository categoryRepository,
                               ValidationUtil validationUtil) {
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
        this.validationUtil = validationUtil;
    }

    @Override
    public void seedCategories(CategorySeedDto[] categorySeedDtos) {
        if (this.categoryRepository.count() != 0) {
            return;
        }

        Arrays.stream(categorySeedDtos)
                .forEach(categorySeedDto -> {
                    if (this.isNotPresent(categorySeedDto)) {
                        if (this.validationUtil.isValid(categorySeedDto)) {
                            Category category = this.modelMapper
                                    .map(categorySeedDto, Category.class);
                            this.categoryRepository.saveAndFlush(category);
                        } else {
                            this.validationUtil.getViolations(categorySeedDto)
                                    .stream()
                                    .map(ConstraintViolation::getMessage)
                                    .forEach(System.out::println);
                        }
                    }
                });
    }

    private boolean isNotPresent(CategorySeedDto categorySeedDto) {
        Category category = this.categoryRepository.findFirstByName(categorySeedDto.getName());
        if (category != null) {
            return false;
        }
        return true;
    }

    @Override
    public List<Category> getRandomCategories() {
        Random random = new Random();
        int randomCounter = random.nextInt(3) + 1;
        List<Category> list = new ArrayList<>();
        for (int i = 0; i < randomCounter; i++) {
            long randomId = random.nextInt((int) this.categoryRepository.count()) + 1;
            list.add(this.categoryRepository.getOne(randomId));
        }
        return list;
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return this.categoryRepository.findAllByIdIsNotNull()
                .stream()
                .map(category -> {
                    CategoryDto categoryDto = new CategoryDto();
                    categoryDto.setCategory(category.getName());
                    categoryDto.setProductsCount(category.getProducts().size());
                    BigDecimal average = this.findAverageSum(category.getProducts());
                    categoryDto.setAveragePrice(average);
                    BigDecimal sum = this.findTotalSum(category.getProducts());
                    categoryDto.setTotalRevenue(sum);
                    return categoryDto;
                }).collect(Collectors.toList());
    }

    private BigDecimal findTotalSum(Set<Product> products) {
        return products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal findAverageSum(Set<Product> products) {
        MathContext mc = new MathContext(6);
        BigDecimal sum = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long count = products.size();
        return sum.divide(new BigDecimal(count), mc);
    }
}
