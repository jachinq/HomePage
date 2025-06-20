package com.home.homepage.service;

import com.home.homepage.entity.User;
import com.home.homepage.entity.modal.ListModal;
import com.home.homepage.repository.UserRepository;
import com.home.homepage.utils.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Jachin
 * @since 25-06-13 17:23
 */
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
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

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            User user = byUsername.get();
            System.out.println("find user by username: " + user);
            return user;
        }

        System.out.println("find user error: " + username);
        return null;
    }
}
