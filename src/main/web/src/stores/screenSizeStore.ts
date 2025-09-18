// src/stores/screenSizeStore.ts
import { ref, onMounted, onUnmounted, computed, watch, reactive } from 'vue'

// 定义尺寸类型
export type ScreenSize = 'small' | 'middle' | 'large'

// 定义断点配置接口
interface Breakpoints {
  small: number
  middle: number
  large: number
}

// 默认断点配置（可根据需求修改）
const DEFAULT_BREAKPOINTS: Breakpoints = {
  small: 768,   // <= 768px 为 small
  middle: 1024, // <= 1024px 为 middle
  large: 1025   // > 1024px 为 large
}

const state = reactive<{
    windowWidth: number
    currentSize: ScreenSize
    isSmall: boolean
    isMiddle: boolean
    isLarge: boolean
    breakpoints: Breakpoints
}>({
  windowWidth: 0,
  currentSize: 'small',
  isSmall: false,
  isMiddle: false,
  isLarge: false,
  breakpoints: DEFAULT_BREAKPOINTS
})

export function useScreenSizeStore(breakpoints: Breakpoints = DEFAULT_BREAKPOINTS) {
  // 响应式窗口宽度
  const windowWidth = ref(window.innerWidth)

  // 更新窗口宽度
  const updateWindowWidth = () => {
    windowWidth.value = window.innerWidth
  }

  // 计算当前尺寸类型
  const currentSize = computed<ScreenSize>(() => {
    if (windowWidth.value <= breakpoints.small) {
      return 'small'
    } else if (windowWidth.value <= breakpoints.middle) {
      return 'middle'
    } else {
      return 'large'
    }
  })

  const updateSize = () => {
    state.windowWidth = windowWidth.value
    state.currentSize = currentSize.value
    state.isSmall = isSmall.value
    state.isMiddle = isMiddle.value
    state.isLarge = isLarge.value

  }
  watch(windowWidth, updateSize)

  // 挂载时添加事件监听
  onMounted(() => {
    window.addEventListener('resize', updateWindowWidth)
    updateSize()
  })

  // 卸载时移除事件监听
  onUnmounted(() => {
    window.removeEventListener('resize', updateWindowWidth)
  })

  const isSmall = computed(() => currentSize.value === 'small');
  const isMiddle = computed(() => currentSize.value === 'middle');
  const isLarge = computed(() => currentSize.value === 'large');

  // 返回尺寸信息和工具函数
  return state
}