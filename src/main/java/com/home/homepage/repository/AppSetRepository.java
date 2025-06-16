package com.home.homepage.repository;

import com.home.homepage.entity.AppSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Jachin
 * @since 25-06-13 17:21
 */
@Repository
public interface AppSetRepository extends JpaRepository<AppSet, Long> {
}
