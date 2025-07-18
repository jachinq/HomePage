package com.home.homepage.repository;

import com.home.homepage.entity.FileInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 文件信息Repository接口
 *
 * @author Jachin
 * @since 25-01-01 00:00
 */
@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {

    /**
     * 根据用户ID查询文件列表
     */
    List<FileInfo> findAllByUserIdAndIsActiveTrue(Long userId);

    /**
     * 根据用户ID分页查询文件列表
     */
    Page<FileInfo> findAllByUserIdAndIsActiveTrue(Long userId, Pageable pageable);

    /**
     * 根据文件名查询文件
     */
    Optional<FileInfo> findByFileNameAndIsActiveTrue(String fileName);

    /**
     * 根据MD5查询文件
     */
    Optional<FileInfo> findByMd5AndUserId(String md5,  Long userId);

    /**
     * 根据文件类型查询文件
     */
    List<FileInfo> findAllByFileTypeAndUserIdAndIsActiveTrue(String fileType, Long userId);

    /**
     * 根据ID和用户ID查询文件
     */
    Optional<FileInfo> findByIdAndUserIdAndIsActiveTrue(Long id, Long userId);

    /**
     * 根据ID查询文件
     */
    Optional<FileInfo> findByIdAndIsActiveTrue(Long id);
} 