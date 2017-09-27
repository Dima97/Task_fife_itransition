package net.apanasik.springsecurityapp.service;

import net.apanasik.springsecurityapp.dao.RoleDao;
import net.apanasik.springsecurityapp.dao.UserDao;
import net.apanasik.springsecurityapp.model.Role;
import net.apanasik.springsecurityapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(User user) {
        if(userDao.findByUsername(user.getUsername()) != null) {
            return;
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setBlocked(false);
        Set<Role> roles = new HashSet<>();
        roles.add(roleDao.getOne(1L));
        user.setRoles(roles);
        userDao.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public List<User> updateStatus(Long id, boolean blocked) {
        User user = userDao.findOne(id);
        user.setBlocked(blocked);
        userDao.saveAndFlush(user);
        return userDao.findAll();
    }

    @Override
    public void delete(Long id) {
        userDao.delete(id);
    }
}
