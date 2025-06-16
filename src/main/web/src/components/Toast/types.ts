export type ToastType = 'success' | 'error' | 'warning'

export interface ToastOptions {
  message: string
  type?: ToastType
  duration?: number
  center?: boolean
  showIcon?: boolean
}

export interface ToastInstance {
  show: () => void
}

export interface ToastService {
  success: (message: string, options?: Omit<ToastOptions, 'type' | 'message'>) => void
  error: (message: string, options?: Omit<ToastOptions, 'type' | 'message'>) => void
  warning: (message: string, options?: Omit<ToastOptions, 'type' | 'message'>) => void
} 