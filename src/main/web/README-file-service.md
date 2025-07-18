# 文件服务说明文档

## 功能概述

文件服务为HomePage项目提供了完整的文件管理功能，包括：

- **文件上传**：支持单文件和批量上传
- **文件下载**：提供安全的文件下载功能
- **文件预览**：支持图片、文本、PDF等文件的在线预览
- **文件管理**：包括文件列表查看、信息更新、删除等功能
- **安全控制**：基于用户权限的文件访问控制
- **去重机制**：通过MD5校验避免重复文件存储

## 配置说明

### 配置文件设置

在 `application.properties` 中添加以下配置：

```properties
# 文件服务配置
file.upload.path=src/main/resources/files/
file.upload.max-size=10MB
file.upload.max-request-size=50MB
# 允许的文件类型
file.upload.allowed-types=jpg,jpeg,png,gif,pdf,doc,docx,txt,zip,rar,mp4,mp3

# Spring Boot 文件上传配置
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=50MB
spring.servlet.multipart.file-size-threshold=1KB
```

### 配置参数说明

- `file.upload.path`: 文件存储路径，支持相对路径和绝对路径
- `file.upload.max-size`: 单个文件最大大小
- `file.upload.max-request-size`: 请求最大大小（用于批量上传）
- `file.upload.allowed-types`: 允许上传的文件类型列表

## 数据库表结构

文件服务使用 `file_info` 表存储文件元信息：

```sql
CREATE TABLE file_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    original_file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT NOT NULL,
    file_type VARCHAR(50) NOT NULL,
    mime_type VARCHAR(50),
    md5 VARCHAR(32),
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## API 接口说明

### 1. 文件上传

```http
POST /api/file/upload
Content-Type: multipart/form-data
Authorization: Bearer {token}

参数：
- file: 文件（必须）
- description: 文件描述（可选）
```

### 2. 文件列表

```http
GET /api/file/list?page=0&size=10&fileType=jpg
Authorization: Bearer {token}

参数：
- page: 页码（默认0）
- size: 每页大小（默认10）
- fileType: 文件类型过滤（可选）
```

### 3. 文件信息

```http
GET /api/file/info/{fileId}
Authorization: Bearer {token}
```

### 4. 文件下载

```http
GET /api/file/download/{fileId}
Authorization: Bearer {token}
```

### 5. 文件预览

```http
GET /api/file/preview/{fileId}
Authorization: Bearer {token}
```

### 6. 更新文件信息

```http
PUT /api/file/{fileId}?description=新的文件描述
Authorization: Bearer {token}
```

### 7. 删除文件

```http
DELETE /api/file/{fileId}
Authorization: Bearer {token}
```

### 8. 批量上传

```http
POST /api/file/batch-upload
Content-Type: multipart/form-data
Authorization: Bearer {token}

参数：
- files: 文件数组（必须）
- description: 文件描述（可选）
```

## 功能特性

### 1. 文件去重

系统会自动计算上传文件的MD5值，如果发现相同文件（MD5相同），会返回已存在的文件信息，避免重复存储。

### 2. 文件类型限制

支持通过配置文件设置允许上传的文件类型，提高系统安全性。

### 3. 文件大小限制

支持配置单文件和请求的最大大小，防止恶意上传大文件。

### 4. 用户权限控制

每个用户只能访问自己上传的文件，确保数据安全。

### 5. 软删除机制

文件删除采用软删除机制，只将 `is_active` 设置为 `false`，物理文件仍然保留，可以根据需要实现恢复功能。

## 安全说明

1. **用户认证**：所有文件操作都需要JWT认证
2. **权限控制**：用户只能操作自己的文件
3. **文件类型检查**：上传时检查文件类型
4. **路径安全**：防止路径遍历攻击
5. **文件名处理**：自动生成唯一文件名，避免文件名冲突

## 使用示例

### 前端上传示例

```javascript
// 单文件上传
const uploadFile = async (file, description) => {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('description', description);
    
    const response = await fetch('/api/file/upload', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`
        },
        body: formData
    });
    
    return response.json();
};

// 获取文件列表
const getFileList = async (page = 0, size = 10, fileType = '') => {
    const params = new URLSearchParams({
        page,
        size,
        ...(fileType && { fileType })
    });
    
    const response = await fetch(`/api/file/list?${params}`, {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });
    
    return response.json();
};
```

### 错误处理

API返回的错误格式：

```json
{
    "code": 400,
    "message": "不支持的文件类型: exe",
    "success": false,
    "data": null
}
```

常见错误代码：
- 400: 请求参数错误
- 401: 未授权
- 404: 文件不存在
- 500: 服务器内部错误

## 扩展功能

### 可选增强功能

1. **缩略图生成**：为图片文件自动生成缩略图
2. **文件压缩**：对大文件进行自动压缩
3. **云存储支持**：集成阿里云OSS、腾讯云COS等
4. **文件版本控制**：支持文件版本历史
5. **文件分享**：支持生成文件分享链接
6. **统计分析**：文件上传下载统计

## 注意事项

1. 确保配置的文件存储路径具有读写权限
2. 定期清理软删除的文件以节省存储空间
3. 建议对文件存储目录进行备份
4. 根据实际需求调整文件大小和类型限制
5. 生产环境建议使用专门的文件服务器或云存储 