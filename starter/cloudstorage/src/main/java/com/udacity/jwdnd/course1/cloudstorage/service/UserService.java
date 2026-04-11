package com.udacity.jwdnd.course1.cloudstorage.service;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final HashService hashService;

    public UserService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    public User getUser(String userName) {
        return userMapper.getUser(userName);
    }

    public boolean isUsernameAvailable(String userName) {
        return userMapper.getUser(userName) == null;
    }

    public int createUser(User user) {
        String salt = UUID.randomUUID().toString();
        String hashedPassword = hashService.getHashedValue(user.getPassword(), salt);

        user.setSalt(salt);
        user.setPassword(hashedPassword);

        return userMapper.insert(user);
    }
}