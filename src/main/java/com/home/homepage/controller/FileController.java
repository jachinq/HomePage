package com.home.homepage.controller;

import com.home.homepage.entity.FileInfo;
import com.home.homepage.service.FileService;
import com.home.homepage.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件服务控制器
 *
 * @author Jachin
 * @since 25-01-01 00:00
 */
@Slf4j
@RestController
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * 上传文件
     */
    @PostMapping("/api/file/upload")
    public Result uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description) {
        
        log.info("开始上传文件: {}", file.getOriginalFilename());
        return fileService.uploadFile(file, description);
    }

    /**
     * 下载文件
     */
    @GetMapping("/file/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        try {
            Resource resource = fileService.getFileResource(fileId);
            if (resource == null) {
                return ResponseEntity.notFound().build();
            }

            FileInfo fileInfo = fileService.getFileInfo(fileId);
            if (fileInfo == null) {
                return ResponseEntity.notFound().build();
            }

            String fileName = URLEncoder.encode(fileInfo.getOriginalFileName(), StandardCharsets.UTF_8);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);

        } catch (Exception e) {
            log.error("文件下载失败", e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 预览文件
     */
    @GetMapping(value = "/file/preview/{fileId}")
    public ResponseEntity<Resource> previewFile(@PathVariable Long fileId) {
        try {
            Resource resource = fileService.getFileResource(fileId);
            if (resource == null) {
                return ResponseEntity.notFound().build();
            }

            FileInfo fileInfo = fileService.getFileInfo(fileId);
            if (fileInfo == null) {
                return ResponseEntity.notFound().build();
            }

            // 根据文件类型设置Content-Type
            MediaType mediaType = getMediaType(fileInfo.getFileType());
            
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileInfo.getOriginalFileName() + "\"")
                    .body(resource);

        } catch (Exception e) {
            log.error("文件预览失败", e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 获取文件信息
     */
    @GetMapping("/api/file/info/{fileId}")
    public Result getFileInfo(@PathVariable Long fileId) {
        FileInfo fileInfo = fileService.getFileInfo(fileId);
        if (fileInfo == null) {
            return Result.error(404, "文件不存在");
        }
        return Result.success(fileInfo);
    }

    /**
     * 获取文件列表
     */
    @GetMapping("/api/file/list")
    public Result getFileList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String fileType) {
        
        return fileService.getFileList(page, size, fileType);
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/api/file/{fileId}")
    public Result deleteFile(@PathVariable Long fileId) {
        return fileService.deleteFile(fileId);
    }

    /**
     * 更新文件信息
     */
    @PutMapping("/api/file/{fileId}")
    public Result updateFileInfo(
            @PathVariable Long fileId,
            @RequestParam(required = false) String description) {
        
        return fileService.updateFileInfo(fileId, description);
    }

    /**
     * 批量上传文件
     */
    @PostMapping("/api/file/batch-upload")
    public Result batchUploadFiles(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "description", required = false) String description) {
        
        log.info("开始批量上传文件，文件数量: {}", files.length);
        
        List<FileInfo> uploadedFiles = new ArrayList<>();
        List<String> failedFiles = new ArrayList<>();
        List<String> failedFilesMsg = new ArrayList<>();

        for (MultipartFile file : files) {
            Result result = fileService.uploadFile(file, description);
            if (result.getSuccess()) {
                FileInfo fileInfo = (FileInfo) result.getData();
                uploadedFiles.add(fileInfo);
            } else {
                failedFiles.add(file.getOriginalFilename());
                failedFilesMsg.add(result.getMessage());
            }
        }


        Map<String, Object> resultData = new HashMap<>();
        resultData.put("success", uploadedFiles);
        resultData.put("failed", failedFiles);

        int size = uploadedFiles.size();
        int failSize = failedFiles.size();
        String okMsg = String.format("成功上传 %d 个文件", size);
        String errMsg = String.format("%d 个文件上传失败，%s", failSize, failedFilesMsg);

        // 构建返回结果
        if (uploadedFiles.isEmpty()) {
            return Result.error(400, errMsg).setData(resultData);
        } else if (failedFiles.isEmpty()) {
            return Result.success(okMsg, resultData);
        } else {
            // 部分成功的情况
            return Result.success(String.format("%s，%s", okMsg, errMsg), resultData);
        }
    }

    /**
     * 根据文件类型获取MediaType
     */
    private MediaType getMediaType(String fileType) {
        if (fileType == null) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }

        return switch (fileType.toLowerCase()) {
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
            case "png" -> MediaType.IMAGE_PNG;
            case "gif" -> MediaType.IMAGE_GIF;
            case "pdf" -> MediaType.APPLICATION_PDF;
            case "txt" -> MediaType.TEXT_PLAIN;
            case "json" -> MediaType.APPLICATION_JSON;
            case "xml" -> MediaType.APPLICATION_XML;
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
    }
} 