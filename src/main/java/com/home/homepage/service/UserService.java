package com.home.homepage.service;

import com.home.homepage.entity.User;
import com.home.homepage.entity.modal.ListModal;
import com.home.homepage.repository.UserRepository;
import com.home.homepage.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public Result save(User data) {
        User save = userRepository.save(data);
        return Result.success(save);
    }

    public Result delete(User data) {
        User save = userRepository.save(data);
        return Result.success(save);
    }

    public Result list(ListModal dto) {
        Pageable pageable = dto.getPageable();
        Page<User> all = userRepository.findAll(pageable);
        return Result.success(all);
    }

    public User login(String name, String password) {
        User user = userRepository.findByNameAndPassword(name, password);
        return user;
    }

}
