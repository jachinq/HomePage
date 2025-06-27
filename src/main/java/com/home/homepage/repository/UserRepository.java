package com.home.homepage.repository;

import com.home.homepage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Jachin
 * @since 25-06-13 17:21
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    User findOneByUsernameAndPassword(String username, String password);
}
