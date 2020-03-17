package com.products.shop.dtos;

import com.google.gson.annotations.Expose;
import com.products.shop.entities.User;

import java.util.Set;

public class UsersProductsDto {

    @Expose
    private int usersCount;

    @Expose
    private Set<User> users;

    public UsersProductsDto() {
    }

    public int getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(int usersCount) {
        this.usersCount = usersCount;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
