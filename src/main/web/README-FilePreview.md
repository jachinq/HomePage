# FilePreview 组件

一个支持多文件预览的Vue组件，支持图片、视频、音频、PDF、文本等多种文件类型的预览。

## 主要特性

- 支持多文件预览和切换
- 图片预览（缩放、旋转）
- 视频/音频播放
- PDF预览
- 文本文件预览
- 键盘快捷键支持
- 全屏模式
- 文件下载

## 使用方式

### 单文件预览
```vue
<template>
  <FilePreview 
    :file-ids="[123]"
    :visible="isPreviewVisible"
    @update:visible="isPreviewVisible = $event"
    @close="handleClose"
  />
</template>
```

### 多文件预览
```vue
<template>
  <FilePreview 
    :file-ids="[123, 456, 789]"
    :visible="isPreviewVisible"
    @update:visible="isPreviewVisible = $event"
    @close="handleClose"
    @file-changed="handleFileChanged"
  />
</template>

<script setup>
const handleFileChanged = (fileId, index) => {
  console.log(`切换到文件 ${fileId}，索引 ${index}`)
}
</script>
```

## Props

| 参数 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| fileIds | `number[]` | `[]` | 要预览的文件ID数组 |
| visible | `boolean` | `false` | 是否显示预览组件 |
| fileInfo | `FileInfo` | `undefined` | 预设的文件信息（可选） |

## Events

| 事件名 | 参数 | 描述 |
|--------|------|------|
| close | - | 关闭预览时触发 |
| update:visible | `boolean` | 显示状态改变时触发 |
| file-changed | `(fileId: number, index: number)` | 文件切换时触发 |

## 键盘快捷键

- `Esc` - 关闭预览
- `←` - 上一个文件
- `→` - 下一个文件
- `+/=` - 放大图片
- `-` - 缩小图片
- `R` - 旋转图片

## 文件类型支持

- 图片：jpg, jpeg, png, gif, webp, svg
- 视频：mp4, webm, ogg, mov, avi
- 音频：mp3, wav, ogg, m4a
- PDF：pdf
- 文本：txt, json, xml, js, css, html, md

## 更新记录

### v2.0.0
- 添加多文件预览支持
- 添加文件切换功能
- 添加文件计数器
- 添加 `file-changed` 事件
- 改进键盘导航
- 优化用户体验 