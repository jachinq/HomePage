import { Result } from '../utils/entity';
import fetchUtil from '../utils/fetchUtil';

// 文件信息接口
export interface FileInfo {
  id: number;
  fileName: string;
  originalFileName: string;
  filePath: string;
  fileSize: number;
  fileType: string;
  mimeType: string;
  description?: string;
  createTime: string;
}

// 文件上传
export const uploadFile = (formData: FormData): Promise<Result> => {
  return new Promise((resolve, reject) => {
    fetchUtil.post('/api/file/upload', formData, {})
      .then(res => res.json())
      .then(res => resolve(res))
      .catch(err => reject(err));
  });
}

// 批量文件上传
export const uploadFiles = (formData: FormData): Promise<Result> => {
  return new Promise((resolve, reject) => {
    fetchUtil.post('/api/file/batch-upload', formData, {})
      .then(res => res.json())
      .then(res => resolve(res))
      .catch(err => reject(err));
  });
};

// 获取文件信息
export const getFileInfo = (fileId: number): Promise<Result> => {
  return new Promise((resolve, reject) => {
    fetchUtil.get(`/api/file/info/${fileId}`, null)
      .then(res => res.json())
      .then(res => resolve(res))
      .catch(err => reject(err));
  });
};

// 文件下载地址
export const getFileDownloadUrl = (fileId: number): string => {
  return `/api/file/download/${fileId}`;
};

// 文件预览地址
export const getFilePreviewUrl = (fileId: number): string => {
  return `/api/file/preview/${fileId}`;
};

// 删除文件
export const deleteFile = (fileId: number): Promise<Result> => {
  return new Promise((resolve, reject) => {
    fetchUtil.delete(`/api/file/${fileId}`)
      .then(res => res.json())
      .then(res => resolve(res))
      .catch(err => reject(err));
  });
}; 