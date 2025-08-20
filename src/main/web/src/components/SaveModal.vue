<script setup lang="ts">
import {computed, ref, getCurrentInstance, onMounted} from 'vue'
import {Dialog, DialogDescription, DialogPanel, DialogTitle} from "@headlessui/vue";

export interface OpenModal {
  add: boolean,
  set: boolean
}

const props = defineProps<{
  name: string
  openModal: OpenModal
  confirmText?: string
  cancelText?: string
  defaultData?: any
}>()
const emitList = ["onOnClose", "onOnDelete", "onOnSubmit", "onOnCancel", "onOnReset"]
const emit = defineEmits(["onClose", "onDelete", "onSubmit", "onCancel", "onReset"])

// 判断父组件是否有对应的事件监听
const hasEmitListener = ref<any>({})
onMounted(() => {
  const internal = getCurrentInstance();
  const props = (internal?.vnode.props || {}) as Record<string, any>
  const check = (key: string) => typeof props[key] === 'function'
  emitList.forEach(key => hasEmitListener.value[key] = check(key))
})

const handleClose = (value: any) => {
  emit('onClose', value)
}

const handleSubmit = () => {
  emit("onSubmit")
}
const handleCancel = () => {
  // console.log(hasEmitListener.value)
  if (hasEmitListener.value.onOnCancel) {
    emit("onCancel")
  } else {
    handleClose(false)
  }
}

const handleDelClick = () => {
  if (!confirmDelete.value) {
    confirmDelete.value = true;
    deleteBtnText.value = "确认删除";
    setTimeout(() => {
      confirmDelete.value = false;
      deleteBtnText.value = "删除";
    }, 3000); // 3秒后恢复
    return;
  }
  emit("onDelete")
}

const handleResetClick = () => {
  emit("onReset")
}

const isOpen = computed(() => props.openModal.add || props.openModal.set)
const confirmDelete = ref(false)
const deleteBtnText = ref("删除")

</script>

<template>
  <Dialog :open="isOpen" @close="handleClose" class="relative z-50">
    <div class="fixed inset-0 flex w-screen items-center justify-center p-4 bg-slate-900/80">
      <DialogPanel class="flex flex-col w-full max-w-lg max-h-[90vh] rounded-lg bg-gray-800 overflow-hidden">
        <!-- 固定头部 -->
        <div class="flex-shrink-0 p-6 pb-4 border-b border-gray-700">
          <DialogTitle class="text-lg font-medium text-gray-100">{{ props.name }}</DialogTitle>
        </div>
        
        <!-- 可滚动的内容区域 -->
        <div class="flex-1 overflow-y-auto custom-scrollbar">
          <DialogDescription class="p-6 pt-4">
            <form @submit.prevent="handleSubmit">
              <slot name="form"></slot>
            </form>
          </DialogDescription>
        </div>

        <!-- 固定底部按钮区域 -->
        <div class="flex-shrink-0 p-6 pt-4 border-t border-gray-700 bg-gray-800">
          <div class="flex gap-2 flex-wrap">
            <button type="button"
                    class="inline-flex justify-center px-4 py-2 text-sm font-medium text-gray-200 bg-blue-400 border border-transparent rounded-md hover:bg-blue-500 focus:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:ring-blue-500"
                    @click="handleSubmit">
              {{ props.confirmText || '保存'}}
            </button>
            <button type="button"
                    class="inline-flex justify-center px-4 py-2 text-sm font-medium text-gray-400 bg-slate-600 border border-transparent rounded-md hover:bg-slate-500 focus:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:ring-blue-500"
                    @click="handleCancel">
              {{ props.cancelText || '取消' }}
            </button>
            <button type="button" v-if="props.defaultData"
                    class="inline-flex justify-center px-4 py-2 text-sm font-medium text-gray-900 bg-blue-100 border border-transparent rounded-md hover:bg-blue-200 focus:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:ring-blue-500"
                    @click="handleResetClick">
              重置
            </button>
            <button v-if="props.openModal.set"
                    class="inline-flex justify-center bg-red-800 rounded-md px-4 py-2 text-sm text-gray-400 hover:bg-red-700"
                    @click="handleDelClick">
              {{ deleteBtnText }}
            </button>
          </div>
        </div>
      </DialogPanel>
    </div>
  </Dialog>
</template>

<style scoped>
/* 自定义滚动条样式 */
.custom-scrollbar::-webkit-scrollbar {
  width: 8px;
}

.custom-scrollbar::-webkit-scrollbar-track {
  background: #374151; /* gray-700 */
  border-radius: 4px;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
  background: #6B7280; /* gray-500 */
  border-radius: 4px;
  transition: background-color 0.2s ease;
}

.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background: #9CA3AF; /* gray-400 */
}

.custom-scrollbar::-webkit-scrollbar-thumb:active {
  background: #D1D5DB; /* gray-300 */
}

/* Firefox 滚动条样式 */
.custom-scrollbar {
  scrollbar-width: thin;
  scrollbar-color: #6B7280 #374151; /* thumb color, track color */
}

/* 平滑滚动 */
.custom-scrollbar {
  scroll-behavior: smooth;
}
</style>