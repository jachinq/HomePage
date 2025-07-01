package com.home.homepage.repository;

import com.home.homepage.entity.AppConfig;
import com.home.homepage.entity.GlobalConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Jachin
 * @since 25-06-13 17:21
 */
@Repository
public interface GlobalConfigRepository extends JpaRepository<GlobalConfig, Long> {
    GlobalConfig findByUserId(Long userId);
}
