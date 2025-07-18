<template>
  <div class="fixed inset-0 z-50 bg-slate-900/80 flex items-center justify-center p-4" v-if="isVisible"
    @click="handleBackgroundClick">
    <div
      class="relative max-w-7xl max-h-screen w-full h-full bg-gray-800 rounded-lg overflow-hidden shadow-2xl flex flex-col"
      @click.stop>
      <!-- 顶部工具栏 -->
      <div class="flex items-center justify-between p-4 border-b border-gray-700 bg-gray-800">
        <div class="flex items-center space-x-4">
          <h3 class="text-lg font-semibold text-white truncate">{{ fileInfo?.originalFileName || '文件预览' }}</h3>
          <span class="text-sm text-gray-400">{{ formatFileSize(fileInfo?.fileSize || 0) }}</span>
          <!-- 文件计数器 -->
          <span v-if="canSwitchFiles" class="text-sm text-gray-400">
            {{ currentFileIndex + 1 }} / {{ props.fileIds?.length }}
          </span>
        </div>
        <div class="flex items-center space-x-2">
          <!-- 工具按钮 -->
          <template v-if="isImage">
            <button @click="zoomIn"
              class="p-2 hover:bg-gray-700 rounded-lg text-gray-300 hover:text-white transition-colors" title="放大">
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v8m-4-4h8" />
              </svg>
            </button>
            <button @click="zoomOut"
              class="p-2 hover:bg-gray-700 rounded-lg text-gray-300 hover:text-white transition-colors" title="缩小">
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 12h8" />
              </svg>
            </button>
            <button @click="rotate"
              class="p-2 hover:bg-gray-700 rounded-lg text-gray-300 hover:text-white transition-colors" title="旋转">
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
              </svg>
            </button>
            <button @click="resetImageState"
              class="p-2 hover:bg-gray-700 rounded-lg text-gray-300 hover:text-white transition-colors"
              title="重置位置和缩放 (空格键)">
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 8h16M4 16h16M12 4v16" />
              </svg>
            </button>
          </template>

          <!-- 全屏按钮 -->
          <button @click="toggleFullscreen"
            class="p-2 hover:bg-gray-700 rounded-lg text-gray-300 hover:text-white transition-colors" title="全屏">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M4 8V4m0 0h4M4 4l5 5m11-1V4m0 0h-4m4 0l-5 5M4 16v4m0 0h4m-4 0l5-5m11 5l-5-5m5 5v-4m0 4h-4" />
            </svg>
          </button>

          <!-- 下载按钮 -->
          <button @click="downloadFile"
            class="p-2 hover:bg-gray-700 rounded-lg text-gray-300 hover:text-white transition-colors" title="下载">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4" />
            </svg>
          </button>

          <!-- 文件切换按钮 -->
          <template v-if="canSwitchFiles">
            <button @click="previousFile" :disabled="isLoading"
              class="p-2 hover:bg-gray-700 rounded-lg text-gray-300 hover:text-white transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
              title="上一个文件 (←)">
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
              </svg>
            </button>
            <button @click="nextFile" :disabled="isLoading"
              class="p-2 hover:bg-gray-700 rounded-lg text-gray-300 hover:text-white transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
              title="下一个文件 (→)">
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
              </svg>
            </button>
          </template>

          <!-- 关闭按钮 -->
          <button @click="close"
            class="p-2 hover:bg-gray-700 rounded-lg text-gray-300 hover:text-white transition-colors" title="关闭">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
      </div>

      <!-- 预览内容区域 -->
      <div class="flex-1 overflow-hidden relative bg-gray-900">
        <!-- 加载状态 -->
        <div v-if="isLoading" class="absolute inset-0 flex items-center justify-center bg-gray-900">
          <div class="text-center">
            <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500 mx-auto"></div>
            <p class="mt-2 text-gray-300">加载中...</p>
          </div>
        </div>

        <!-- 图片预览 -->
        <div v-else-if="isImage" class="h-full overflow-auto" ref="imageContainer">
          <div class="h-full flex items-center justify-center p-4 relative">
            <img :src="previewUrl" :alt="fileInfo?.originalFileName"
              :class="['max-w-full max-h-full object-contain', { 'transition-transform duration-200': !isDragging }]"
              :style="imageStyle" @load="handleImageLoad" @error="handleImageError" @mousedown="handleMouseDown"
              @touchstart="handleTouchStart" draggable="false" />
            <!-- 操作提示 -->
            <div v-if="showTip"
              class="absolute bottom-4 left-4 bg-black/50 text-white text-xs px-3 py-1 rounded-lg backdrop-blur-sm opacity-60 pointer-events-none transition-opacity duration-300">
              拖动图片以移动位置 · 空格键重置
            </div>
          </div>
        </div>

        <!-- 视频预览 -->
        <div v-else-if="isVideo" class="h-full flex items-center justify-center p-4">
          <video :src="previewUrl" controls class="max-w-full max-h-full" @error="handleVideoError">
            您的浏览器不支持视频播放。
          </video>
        </div>

        <!-- 音频预览 -->
        <div v-else-if="isAudio" class="h-full flex items-center justify-center p-4">
          <div class="text-center">
            <div class="w-32 h-32 bg-gray-700 rounded-full flex items-center justify-center mx-auto mb-4">
              <svg class="w-16 h-16 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M9 19V6l12-3v13M9 19c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zm12-3c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zM9 10l12-3" />
              </svg>
            </div>
            <audio :src="previewUrl" controls class="w-full max-w-md audio-dark" @error="handleAudioError">
              您的浏览器不支持音频播放。
            </audio>
          </div>
        </div>

        <!-- PDF预览 -->
        <div v-else-if="isPDF" class="h-full">
          <iframe :src="previewUrl" class="w-full h-full border-0 bg-gray-900" @error="handlePDFError">
            <p class="text-gray-300">您的浏览器不支持PDF预览。<a :href="downloadUrl" class="text-blue-400 hover:underline">点击下载</a>
            </p>
          </iframe>
        </div>

        <!-- 文本预览 -->
        <div v-else-if="isText" class="h-full overflow-auto p-4">
          <div v-if="textContent" class="max-w-full">
            <pre
              class="whitespace-pre-wrap text-sm font-mono bg-gray-800 text-gray-300 p-4 rounded-lg border border-gray-600">{{
        textContent }}</pre>
          </div>
          <div v-else class="text-center text-gray-400 mt-8">
            <p>无法预览该文本文件</p>
          </div>
        </div>

        <!-- 不支持预览的文件类型 -->
        <div v-else class="h-full flex items-center justify-center">
          <div class="text-center">
            <div class="w-32 h-32 bg-gray-700 rounded-lg flex items-center justify-center mx-auto mb-4">
              <svg class="w-16 h-16 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
              </svg>
            </div>
            <h3 class="text-lg font-semibold text-white mb-2">{{ fileInfo?.originalFileName }}</h3>
            <p class="text-gray-300 mb-4">此文件类型暂不支持预览</p>
            <p class="text-sm text-gray-400 mb-4">文件大小：{{ formatFileSize(fileInfo?.fileSize || 0) }}</p>
            <button @click="downloadFile"
              class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors">
              下载文件
            </button>
          </div>
        </div>

        <!-- 错误状态 -->
        <div v-if="hasError" class="absolute inset-0 flex items-center justify-center bg-gray-900">
          <div class="text-center">
            <div class="text-red-500 text-6xl mb-4">⚠️</div>
            <h3 class="text-lg font-semibold text-white mb-2">加载失败</h3>
            <p class="text-gray-300 mb-4">{{ errorMessage }}</p>
            <button @click="downloadFile"
              class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors">
              下载文件
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, watch, onMounted, onUnmounted } from 'vue'
import { getFilePreviewUrl, getFileDownloadUrl, getFileInfo, FileInfo } from '../api/fileApi'

interface Props {
  fileIds?: number[]
  visible?: boolean
  fileInfo?: FileInfo
}

const props = withDefaults(defineProps<Props>(), {
  visible: false,
  fileIds: () => []
})

const emit = defineEmits<{
  close: []
  'update:visible': [value: boolean]
  'file-changed': [fileId: number, index: number]
}>()

// 响应式状态
const isVisible = ref(props.visible)
const isLoading = ref(false)
const hasError = ref(false)
const errorMessage = ref('')
const textContent = ref('')
const currentFileInfo = ref<FileInfo | null>(props.fileInfo || null)
const currentFileIndex = ref(0)

// 图片操作状态
const imageScale = ref(1)
const imageRotation = ref(0)
const imagePosition = ref({ x: 0, y: 0 })

// 拖动状态
const isDragging = ref(false)
const dragStart = ref({ x: 0, y: 0 })
const dragStartPosition = ref({ x: 0, y: 0 })
const showTip = ref(false)
const tipTimer = ref<any>(null)

const previewUrl = computed(() => getFilePreviewUrl(currentFileId.value || -1))
const downloadUrl = computed(() => getFileDownloadUrl(currentFileId.value || -1))

// 计算属性
const currentFileId = computed(() => {
  if (!props.fileIds || props.fileIds.length === 0) return undefined
  return props.fileIds[currentFileIndex.value]
})

const fileInfo = computed(() => {
  return currentFileInfo.value || props.fileInfo
})

const mimeType = computed(() => {
  return fileInfo.value?.mimeType || ''
})

const isImage = computed(() => {
  return mimeType.value.startsWith('image/')
})

const isVideo = computed(() => {
  return mimeType.value.startsWith('video/')
})

const isAudio = computed(() => {
  return mimeType.value.startsWith('audio/')
})

const isPDF = computed(() => {
  return mimeType.value === 'application/pdf'
})

const isText = computed(() => {
  return mimeType.value.startsWith('text/') ||
    ['application/json', 'application/javascript', 'application/xml'].includes(mimeType.value)
})

const imageStyle = computed(() => {
  return {
    transform: `translate(${imagePosition.value.x}px, ${imagePosition.value.y}px) scale(${imageScale.value}) rotate(${imageRotation.value}deg)`,
    transformOrigin: 'center',
    cursor: isDragging.value ? 'grabbing' : 'grab'
  }
})

// 监听器
watch(() => props.visible, (newVal) => {
  isVisible.value = newVal
  if (newVal) {
    // 确保文件索引在有效范围内
    if (props.fileIds && props.fileIds.length > 0) {
      currentFileIndex.value = Math.max(0, Math.min(currentFileIndex.value, props.fileIds.length - 1))
    }
    loadFileInfo()
  } else {
    resetImageState()
  }
})

watch(() => props.fileIds, () => {
  if (props.fileIds && props.fileIds.length > 0 && isVisible.value) {
    currentFileIndex.value = 0
    loadFileInfo()
  }
})

watch(() => currentFileIndex.value, () => {
  if (currentFileId.value && isVisible.value) {
    loadFileInfo()
    emit('file-changed', currentFileId.value, currentFileIndex.value)
  }
})

// 方法
const loadFileInfo = async () => {
  if (!currentFileId.value) return

  isLoading.value = true
  hasError.value = false
  errorMessage.value = ''
  textContent.value = ''

  try {
    if (!props.fileInfo) {
      const result = await getFileInfo(currentFileId.value)
      if (result.success) {
        currentFileInfo.value = result.data
      } else {
        throw new Error(result.message || '获取文件信息失败')
      }
    }

    // 如果是文本文件，加载文本内容
    if (isText.value) {
      await loadTextContent()
    }
  } catch (error) {
    hasError.value = true
    errorMessage.value = error instanceof Error ? error.message : '加载失败'
  } finally {
    isLoading.value = false
  }
}

const loadTextContent = async () => {
  if (!currentFileId.value) return

  try {
    const response = await fetch(previewUrl.value)
    if (!response.ok) {
      throw new Error('加载文本内容失败')
    }
    textContent.value = await response.text()
  } catch (error) {
    console.error('加载文本内容失败:', error)
  }
}

const close = () => {
  isVisible.value = false
  emit('update:visible', false)
  emit('close')
  resetImageState()
  // 重置状态
  hasError.value = false
  errorMessage.value = ''
  textContent.value = ''
  showTip.value = false
  if (tipTimer.value) {
    clearTimeout(tipTimer.value)
  }
}

const handleBackgroundClick = () => {
  close()
}

const downloadFile = () => {
  if (!currentFileId.value) return

  const link = document.createElement('a')
  link.href = downloadUrl.value
  link.download = fileInfo.value?.originalFileName || 'download'
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

const toggleFullscreen = () => {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen()
  } else {
    document.exitFullscreen()
  }
}

// 图片操作方法
const zoomIn = () => {
  imageScale.value = Math.min(imageScale.value * 1.2, 5)
}

const zoomOut = () => {
  imageScale.value = Math.max(imageScale.value / 1.2, 0.1)
}

const rotate = () => {
  imageRotation.value = (imageRotation.value + 90) % 360
}

const resetImageState = async () => {
  imageScale.value = 1
  imageRotation.value = 0
  imagePosition.value = { x: 0, y: 0 }
}

// 显示操作提示
const showOperationTip = () => {
  showTip.value = true
  if (tipTimer.value) {
    clearTimeout(tipTimer.value)
  }
  tipTimer.value = setTimeout(() => {
    showTip.value = false
  }, 3000)
}

// 拖动事件处理
const handleMouseDown = (event: MouseEvent) => {
  if (!isImage.value) return

  isDragging.value = true
  dragStart.value = { x: event.clientX, y: event.clientY }
  dragStartPosition.value = { ...imagePosition.value }

  event.preventDefault()
}

const handleMouseMove = (event: MouseEvent) => {
  if (!isDragging.value || !isImage.value) return

  const deltaX = event.clientX - dragStart.value.x
  const deltaY = event.clientY - dragStart.value.y

  imagePosition.value = {
    x: dragStartPosition.value.x + deltaX,
    y: dragStartPosition.value.y + deltaY
  }

  event.preventDefault()
}

const handleMouseUp = () => {
  isDragging.value = false
}

// 触摸事件处理
const handleTouchStart = (event: TouchEvent) => {
  if (!isImage.value || event.touches.length !== 1) return

  const touch = event.touches[0]
  isDragging.value = true
  dragStart.value = { x: touch.clientX, y: touch.clientY }
  dragStartPosition.value = { ...imagePosition.value }

  event.preventDefault()
}

const handleTouchMove = (event: TouchEvent) => {
  if (!isDragging.value || !isImage.value || event.touches.length !== 1) return

  const touch = event.touches[0]
  const deltaX = touch.clientX - dragStart.value.x
  const deltaY = touch.clientY - dragStart.value.y

  imagePosition.value = {
    x: dragStartPosition.value.x + deltaX,
    y: dragStartPosition.value.y + deltaY
  }

  event.preventDefault()
}

const handleTouchEnd = () => {
  isDragging.value = false
}

// 文件切换方法
const previousFile = () => {
  if (!props.fileIds || props.fileIds.length <= 1) return
  if (isLoading.value) return // 正在加载时不允许切换
  currentFileIndex.value = currentFileIndex.value > 0 ? currentFileIndex.value - 1 : props.fileIds.length - 1
  resetImageState()
}

const nextFile = () => {
  if (!props.fileIds || props.fileIds.length <= 1) return
  if (isLoading.value) return // 正在加载时不允许切换
  currentFileIndex.value = currentFileIndex.value < props.fileIds.length - 1 ? currentFileIndex.value + 1 : 0
  resetImageState()
}

const canSwitchFiles = computed(() => {
  return props.fileIds && props.fileIds.length > 1
})

// 错误处理
const handleImageError = () => {
  hasError.value = true
  errorMessage.value = '图片加载失败'
}

const handleVideoError = () => {
  hasError.value = true
  errorMessage.value = '视频加载失败'
}

const handleAudioError = () => {
  hasError.value = true
  errorMessage.value = '音频加载失败'
}

const handlePDFError = () => {
  hasError.value = true
  errorMessage.value = 'PDF加载失败'
}

const handleImageLoad = () => {
  hasError.value = false
  showOperationTip()
}

// 工具方法
const formatFileSize = (bytes: number): string => {
  if (bytes === 0) return '0 B'

  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))

  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// 键盘事件处理
const handleKeyDown = (event: KeyboardEvent) => {
  if (!isVisible.value) return

  switch (event.key) {
    case 'Escape':
      close()
      break
    case '+':
    case '=':
      if (isImage.value) {
        zoomIn()
      }
      break
    case '-':
      if (isImage.value) {
        zoomOut()
      }
      break
    case 'r':
    case 'R':
      if (isImage.value) {
        rotate()
      }
      break
    case 'ArrowLeft':
      if (canSwitchFiles.value) {
        previousFile()
      }
      break
    case 'ArrowRight':
      if (canSwitchFiles.value) {
        nextFile()
      }
      break
    case ' ':
    case '0':
      if (isImage.value) {
        resetImageState()
      }
      break
  }
}

// 生命周期
onMounted(() => {
  document.addEventListener('keydown', handleKeyDown)
  document.addEventListener('mousemove', handleMouseMove)
  document.addEventListener('mouseup', handleMouseUp)
  document.addEventListener('touchmove', handleTouchMove)
  document.addEventListener('touchend', handleTouchEnd)
  if (props.visible && props.fileIds && props.fileIds.length > 0) {
    loadFileInfo()
  }
})

onUnmounted(() => {
  document.removeEventListener('keydown', handleKeyDown)
  document.removeEventListener('mousemove', handleMouseMove)
  document.removeEventListener('mouseup', handleMouseUp)
  document.removeEventListener('touchmove', handleTouchMove)
  document.removeEventListener('touchend', handleTouchEnd)
  if (tipTimer.value) {
    clearTimeout(tipTimer.value)
  }
})
</script>

<style scoped>
/* 自定义滚动条 - 黑暗主题 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: #374151;
  /* gray-700 */
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: #6B7280;
  /* gray-500 */
  border-radius: 4px;
  transition: background-color 0.2s ease;
}

::-webkit-scrollbar-thumb:hover {
  background: #9CA3AF;
  /* gray-400 */
}

/* Firefox 滚动条样式 */
* {
  scrollbar-width: thin;
  scrollbar-color: #6B7280 #374151;
  /* thumb color, track color */
}

/* 平滑滚动 */
* {
  scroll-behavior: smooth;
}

/* 音频播放器深色样式 */
.audio-dark {
  filter: invert(1) hue-rotate(180deg);
}

/* 全屏模式样式 */
:fullscreen {
  background: #111827;
  /* gray-900 */
}

:fullscreen .max-w-7xl {
  max-width: 100%;
}

:fullscreen .max-h-screen {
  max-height: 100vh;
}

/* 视频播放器深色样式 */
video {
  background: #111827;
}

/* PDF iframe 深色背景 */
iframe {
  background: #111827;
}
</style>