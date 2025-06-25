import { createVNode, render, App, getCurrentInstance } from 'vue'
import Toast from './Toast.vue'
import type { ToastOptions, ToastService } from './types'

interface ToastInstance {
  vm: any
  container: HTMLElement
  top: number
}

const toastInstances: ToastInstance[] = []
const GAP = 16 // 消息之间的间距

const createToast = (options: ToastOptions) => {
  const container = document.createElement('div')
  const vnode = createVNode(Toast, { ...options })
  render(vnode, container)
  document.body.appendChild(container)
  
  const instance: ToastInstance = {
    vm: vnode.component,
    container,
    top: 0
  }
  
  // 计算新消息的位置
  const updatePositions = () => {
    setTimeout(() => {
      let currentTop = GAP; // 从顶部开始，留出初始间距
      toastInstances.forEach((instance, _index) => {
        const element = instance.container.firstElementChild as HTMLElement
        const height = element ? element.offsetHeight : 48
        
        // 计算当前消息的位置，考虑之前所有消息的高度
        instance.top = currentTop
        currentTop += height + GAP
        
        instance.vm.exposed.updatePosition(instance.top)
      })
    }, 100);
  
  }
  
  // 添加新消息
  toastInstances.push(instance)
  updatePositions()
  
  // 监听消息关闭
  const removeInstance = () => {
    const index = toastInstances.indexOf(instance)
    if (index > -1) {
      toastInstances.splice(index, 1)
      document.body.removeChild(instance.container!)
      updatePositions()
    }
  }
  
  return { instance, removeInstance }
}

const showToast = (options: ToastOptions) => {
  const { instance, removeInstance } = createToast(options)
  instance.vm.exposed.show()

  let duration = options.duration || 3000;
  if (duration > 0) {
    setTimeout(() => {
      removeInstance()
    }, duration)
  }
}

const ToastPlugin = {
  install(app: App) {
    const toast: ToastService = {
      success(message: string, options = {}) {
        showToast({ ...options, message, type: 'success' })
      },
      error(message: string, options = {}) {
        showToast({ ...options, message, type: 'error' })
      },
      warning(message: string, options = {}) {
        showToast({ ...options, message, type: 'warning' })
      }
    }

    app.config.globalProperties.$toast = toast
  }
}

export const useToast = () => {
  return getCurrentInstance()?.proxy?.$toast;
}
export default ToastPlugin
export type { ToastOptions, ToastService } 