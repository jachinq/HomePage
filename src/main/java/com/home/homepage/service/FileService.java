package com.home.homepage.service;

import com.home.homepage.config.FileConfig;
import com.home.homepage.entity.FileInfo;
import com.home.homepage.repository.FileInfoRepository;
import com.home.homepage.utils.Core;
import com.home.homepage.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * 文件服务类
 *
 * @author Jachin
 * @since 25-01-01 00:00
 */
@Slf4j
@Service
public class FileService {

    private final FileInfoRepository fileInfoRepository;
    private final FileConfig fileConfig;

    @Autowired
    public FileService(FileInfoRepository fileInfoRepository, FileConfig fileConfig) {
        this.fileInfoRepository = fileInfoRepository;
        this.fileConfig = fileConfig;
    }

    /**
     * 上传文件
     */
    public Result uploadFile(MultipartFile file, String description) {
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                return Result.error(400, "文件不能为空");
            }

            // 计算文件MD5
            String md5 = calculateMD5(file.getBytes());

            // 检查是否已存在相同文件
            Optional<FileInfo> existingFile = fileInfoRepository.findByMd5AndUserId(md5, Core.getUid());
            if (existingFile.isPresent()) {
                FileInfo fileInfo = existingFile.get();
//                Files.deleteIfExists(filePath); // 删除刚上传的文件
                if (!fileInfo.getIsActive()) {
                    // 修改 isActive 状态
                    fileInfo.setIsActive(true);
                    fileInfoRepository.save(fileInfo);
                }
                return Result.success("文件已存在", fileInfo);
            }

            // 获取文件信息
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null || originalFileName.trim().isEmpty()) {
                return Result.error(400, "文件名不能为空");
            }

            String fileType = getFileExtension(originalFileName);
            
            // 检查文件类型是否被允许
            if (!fileConfig.isAllowedFileType(fileType)) {
                return Result.error(400, "不支持的文件类型: " + fileType);
            }

            // 生成唯一文件名
            String fileName = generateUniqueFileName(originalFileName);
            
            // 创建上传目录
            Path uploadPath = Paths.get(fileConfig.getUploadPath());
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 保存文件
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 保存文件信息到数据库
            FileInfo fileInfo = new FileInfo()
                    .setUserId(Core.getUid())
                    .setFileName(fileName)
                    .setOriginalFileName(originalFileName)
                    .setFilePath(filePath.toString())
                    .setFileSize(file.getSize())
                    .setFileType(fileType)
                    .setMimeType(file.getContentType())
                    .setMd5(md5)
                    .setDescription(description)
                    .setIsActive(true);

            FileInfo savedFile = fileInfoRepository.save(fileInfo);
            log.info("文件上传成功: {}", savedFile.getFileName());

            return Result.success("文件上传成功", savedFile);

        } catch (IOException e) {
            log.error("文件上传失败", e);
            return Result.error(500, "文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件资源用于下载
     */
    public Resource getFileResource(Long fileId) {
        try {
            Optional<FileInfo> fileInfoOpt = fileInfoRepository.findByIdAndIsActiveTrue(fileId);
            if (fileInfoOpt.isEmpty()) {
                return null;
            }

            FileInfo fileInfo = fileInfoOpt.get();
            Path filePath = Paths.get(fileInfo.getFilePath());
            
            if (!Files.exists(filePath)) {
                log.error("文件不存在: {}", filePath);
                return null;
            }

            return new UrlResource(filePath.toUri());
        } catch (Exception e) {
            log.error("获取文件资源失败", e);
            return null;
        }
    }

    /**
     * 获取文件信息
     */
    public FileInfo getFileInfo(Long fileId) {
        return fileInfoRepository.findByIdAndUserIdAndIsActiveTrue(fileId, Core.getUid()).orElse(null);
    }

    /**
     * 获取用户的文件列表
     */
    public Result getFileList(int page, int size, String fileType) {
        try {
            Long userId = Core.getUid();
            
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
            
            Page<FileInfo> fileInfoPage;
            if (fileType != null && !fileType.trim().isEmpty()) {
                List<FileInfo> fileInfoList = fileInfoRepository.findAllByFileTypeAndUserIdAndIsActiveTrue(fileType, userId);
                fileInfoPage = fileInfoRepository.findAllByUserIdAndIsActiveTrue(userId, pageable);
            } else {
                fileInfoPage = fileInfoRepository.findAllByUserIdAndIsActiveTrue(userId, pageable);
            }
            
            return Result.success(fileInfoPage);
        } catch (Exception e) {
            log.error("获取文件列表失败", e);
            return Result.error(500, "获取文件列表失败: " + e.getMessage());
        }
    }

    /**
     * 删除文件
     */
    public Result deleteFile(Long fileId) {
        try {
            Optional<FileInfo> fileInfoOpt = fileInfoRepository.findByIdAndUserIdAndIsActiveTrue(fileId, Core.getUid());
            if (fileInfoOpt.isEmpty()) {
                return Result.error(404, "文件不存在");
            }

            FileInfo fileInfo = fileInfoOpt.get();
            
            // 软删除，将isActive设置为false
            fileInfo.setIsActive(false);
            fileInfoRepository.save(fileInfo);

            // 可选：删除物理文件
            // Path filePath = Paths.get(fileInfo.getFilePath());
            // Files.deleteIfExists(filePath);

            log.info("文件删除成功: {}", fileInfo.getFileName());
            return Result.success("文件删除成功");

        } catch (Exception e) {
            log.error("文件删除失败", e);
            return Result.error(500, "文件删除失败: " + e.getMessage());
        }
    }

    /**
     * 更新文件信息
     */
    public Result updateFileInfo(Long fileId, String description) {
        try {
            Optional<FileInfo> fileInfoOpt = fileInfoRepository.findByIdAndUserIdAndIsActiveTrue(fileId, Core.getUid());
            if (fileInfoOpt.isEmpty()) {
                return Result.error(404, "文件不存在");
            }

            FileInfo fileInfo = fileInfoOpt.get();
            fileInfo.setDescription(description);
            FileInfo updatedFile = fileInfoRepository.save(fileInfo);

            return Result.success("文件信息更新成功", updatedFile);

        } catch (Exception e) {
            log.error("文件信息更新失败", e);
            return Result.error(500, "文件信息更新失败: " + e.getMessage());
        }
    }

    /**
     * 生成唯一文件名
     */
    private String generateUniqueFileName(String originalFileName) {
        String fileExtension = getFileExtension(originalFileName);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid + "." + fileExtension;
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }

    /**
     * 计算文件MD5
     */
    private String calculateMD5(byte[] fileBytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(fileBytes);
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("MD5计算失败", e);
            return null;
        }
    }
} 