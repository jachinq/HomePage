package com.home.homepage.service;

import com.home.homepage.entity.User;
import com.home.homepage.entity.modal.ChangeUserPwd;
import com.home.homepage.entity.modal.ListModal;
import com.home.homepage.jwt.PasswordConfig;
import com.home.homepage.repository.UserRepository;
import com.home.homepage.utils.Core;
import com.home.homepage.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Jachin
 * @since 25-06-13 17:23
 */
@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

//    private final PasswordEncoder passwordEncoder;

    private final PasswordConfig passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordConfig passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User save(User user) {
        user.setPassword(passwordEncoder.passwordEncoder().encode(user.getPassword()));
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

    public Result info() {
        Optional<User> user = userRepository.findById(Core.getUid());
        return user.map(Result::success).orElse(null);
    }

    public Result changeUserPwd(ChangeUserPwd dto) {
        String username = dto.getUsername();
        String newPassword = dto.getNewPassword();
        String oldPassword = dto.getOldPassword();
        log.info("change password, {}", dto);

        User oldUser = Core.getUser();
        String oldUsername = oldUser.getUsername();
        if (oldUsername.equals(username) && oldPassword.equals(newPassword)) {
            return Result.error(500, "什么都没修改");
        }

        if (newPassword.length() < 6) {
            return Result.error(500, "新密码长度不能小于6");
        }

        Optional<User> checkUser = userRepository.findByUsername(username);
        if (checkUser.isPresent() && !Objects.equals(checkUser.get().getId(), oldUser.getId())) {
            return Result.error(500, username + "账号已存在");
        }

        if (!passwordEncoder.passwordEncoder().matches(oldPassword, oldUser.getPassword())) {
            return Result.error(401, "旧密码错误");
        }

        oldUser.setUsername(username);
        oldUser.setPassword(passwordEncoder.passwordEncoder().encode(newPassword));
        userRepository.save(oldUser);
        return Result.success();
    }

    public Result setUserAvatar(User dto) {
        User user = Core.getUser();
        user.setAvatar(dto.getAvatar());
        userRepository.save(user);
        return Result.success();
    }
}
