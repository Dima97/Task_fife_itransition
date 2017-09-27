package net.apanasik.springsecurityapp.service;

import net.apanasik.springsecurityapp.model.User;

import java.util.List;



public interface UserService {

    void save(User user);
    void delete(Long id);
    User findByUsername(String username);
    List<User> findAll();
    List<User> updateStatus(Long id, boolean blocked);
}
