<template>
  <div class="p-6 max-w-4xl mx-auto">
    <h1 class="text-2xl font-bold mb-6">FilePreview 组件示例</h1>
    
    <!-- 文件列表 -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
      <div class="bg-white rounded-lg shadow-md p-4">
        <h2 class="text-lg font-semibold mb-3">单文件预览</h2>
        <div class="space-y-2">
          <button 
            v-for="file in singleFiles" 
            :key="file.id"
            @click="previewSingleFile(file.id)"
            class="w-full text-left p-3 bg-gray-50 hover:bg-gray-100 rounded border transition-colors"
          >
            <div class="font-medium">{{ file.name }}</div>
            <div class="text-sm text-gray-500">{{ file.type }}</div>
          </button>
        </div>
      </div>
      
      <div class="bg-white rounded-lg shadow-md p-4">
        <h2 class="text-lg font-semibold mb-3">多文件预览</h2>
        <div class="space-y-2">
          <button 
            @click="previewMultipleFiles"
            class="w-full text-left p-3 bg-blue-50 hover:bg-blue-100 rounded border border-blue-200 transition-colors"
          >
            <div class="font-medium">预览所有图片</div>
            <div class="text-sm text-gray-500">{{ multipleFiles.length }} 个文件</div>
          </button>
          
          <button 
            @click="previewDocuments"
            class="w-full text-left p-3 bg-green-50 hover:bg-green-100 rounded border border-green-200 transition-colors"
          >
            <div class="font-medium">预览所有文档</div>
            <div class="text-sm text-gray-500">{{ documentFiles.length }} 个文件</div>
          </button>
        </div>
      </div>
    </div>
    
    <!-- 当前预览信息 -->
    <div v-if="currentPreviewInfo" class="bg-blue-50 p-4 rounded-lg mb-6">
      <h3 class="font-semibold mb-2">当前预览信息</h3>
      <div class="text-sm space-y-1">
        <p><strong>文件ID:</strong> {{ currentPreviewInfo.fileId }}</p>
        <p><strong>文件索引:</strong> {{ currentPreviewInfo.index + 1 }} / {{ currentPreviewInfo.total }}</p>
        <p><strong>文件类型:</strong> {{ currentPreviewInfo.type }}</p>
      </div>
    </div>
    
    <!-- 预览组件 -->
    <FilePreview
      :file-ids="currentFileIds"
      :visible="isPreviewVisible"
      @update:visible="isPreviewVisible = $event"
      @close="handlePreviewClose"
      @file-changed="handleFileChanged"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import FilePreview from '@/components/FilePreview.vue'

// 模拟文件数据
const singleFiles = [
  { id: 1, name: '示例图片.jpg', type: 'image/jpeg' },
  { id: 2, name: '示例视频.mp4', type: 'video/mp4' },
  { id: 3, name: '示例PDF.pdf', type: 'application/pdf' },
  { id: 4, name: '示例文档.txt', type: 'text/plain' },
]

const multipleFiles = [101, 102, 103, 104, 105] // 图片文件ID
const documentFiles = [201, 202, 203] // 文档文件ID

// 响应式状态
const isPreviewVisible = ref(false)
const currentFileIds = ref<number[]>([])
const currentPreviewInfo = ref<{
  fileId: number
  index: number
  total: number
  type: string
} | null>(null)

// 方法
const previewSingleFile = (fileId: number) => {
  currentFileIds.value = [fileId]
  isPreviewVisible.value = true
}

const previewMultipleFiles = () => {
  currentFileIds.value = [...multipleFiles]
  isPreviewVisible.value = true
}

const previewDocuments = () => {
  currentFileIds.value = [...documentFiles]
  isPreviewVisible.value = true
}

const handleFileChanged = (fileId: number, index: number) => {
  console.log(`文件切换: ${fileId}, 索引: ${index}`)
  
  // 更新当前预览信息
  currentPreviewInfo.value = {
    fileId,
    index,
    total: currentFileIds.value.length,
    type: getFileType(fileId)
  }
}

const handlePreviewClose = () => {
  console.log('预览关闭')
  currentPreviewInfo.value = null
}

const getFileType = (fileId: number): string => {
  if (multipleFiles.includes(fileId)) return '图片'
  if (documentFiles.includes(fileId)) return '文档'
  
  const file = singleFiles.find(f => f.id === fileId)
  return file?.type || '未知'
}
</script>

<style scoped>
/* 可以添加自定义样式 */
</style> 