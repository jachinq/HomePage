package com.home.homepage.service;

import com.home.homepage.entity.User;
import com.home.homepage.repository.UserRepository;
import com.home.homepage.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jachin
 * @since 25-06-13 17:23
 */
@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository sexDailyRepository) {
        this.userRepository = sexDailyRepository;
    }

    public void save(User data) {
        User save = userRepository.save(data);
        System.out.println(save);
    }

    public Result list() {
        List<User> all = userRepository.findAll();
        return Result.success(all, all.size());
    }
}
