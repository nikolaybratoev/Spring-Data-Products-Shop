package com.products.shop.services;

import com.products.shop.dtos.UserSeedDto;
import com.products.shop.entities.User;

import java.util.List;

public interface UserService {

    void seedUsers(UserSeedDto[] userSeedDtos);

    User getRandomUser();

    User getUserByLastName(String lastName);
}
