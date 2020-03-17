package com.products.shop.services.impls;

import com.products.shop.dtos.UserSeedDto;
import com.products.shop.entities.User;
import com.products.shop.repositories.UserRepository;
import com.products.shop.services.UserService;
import com.products.shop.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.util.Arrays;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ValidationUtil validationUtil;

    @Autowired
    public UserServiceImpl(ModelMapper modelMapper, UserRepository userRepository,
                           ValidationUtil validationUtil) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.validationUtil = validationUtil;
    }


    @Override
    public void seedUsers(UserSeedDto[] userSeedDtos) {
        if (this.userRepository.count() != 0) {
            return;
        }

        Arrays.stream(userSeedDtos)
                .forEach(userSeedDto -> {
                    if (this.isNotPresent(userSeedDto)) {
                        if (this.validationUtil.isValid(userSeedDto)) {
                            User user = this.modelMapper
                                    .map(userSeedDto, User.class);
                            this.userRepository.saveAndFlush(user);
                        } else {
                            this.validationUtil.getViolations(userSeedDto)
                                    .stream()
                                    .map(ConstraintViolation::getMessage)
                                    .forEach(System.out::println);
                        }
                    }
                });
    }

    private boolean isNotPresent(UserSeedDto userSeedDto) {
        User user = this.userRepository.findFirstByLastName(userSeedDto.getLastName());
        if (user != null) {
            return false;
        }
        return true;
    }

    @Override
    public User getRandomUser() {
        Random random = new Random();
        long randomId = random.nextInt((int) this.userRepository.count()) + 1;
        return this.userRepository.getOne(randomId);
    }

    @Override
    public User getUserByLastName(String lastName) {
        return this.userRepository.findFirstByLastName(lastName);
    }
}
