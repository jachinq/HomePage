package com.home.homepage.repository;

import com.home.homepage.entity.AppSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Jachin
 * @since 25-06-13 17:21
 */
@Repository
public interface AppSetRepository extends JpaRepository<AppSet, Long> {
//    public Page<AppSet> findByUserIdAndNameIsContainingOrDescriptionIsContaining(Long userId, String dtoName, String dtoDescription, Pageable pageable);

    @Query("SELECT u FROM AppSet u WHERE u.userId = :userId AND u.type = :type AND u.pinyin LIKE %:keyword%")
    Page<AppSet> findByKeyword(@Param("userId") Long userId,
                               @Param("type") Integer type,
                               @Param("keyword") String keyword,
                               Pageable pageable);

    Page<AppSet> findByUserIdAndType(Long uid, Integer type, Pageable pageable);
}
