<script setup lang="ts">
import {defineProps, defineEmits, computed, ref} from 'vue'
import {Dialog, DialogDescription, DialogPanel, DialogTitle} from "@headlessui/vue";

interface OpenModal {
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
const emit = defineEmits(["onClose", "onDelete", "onSubmit", "onCancel", "onReset"])
const handleClose = (value: any) => {
  emit('onClose', value)
}

const handleSubmit = () => {
  emit("onSubmit")
}
const handleCancel = () => {
  emit("onCancel")
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
    <div class="fixed inset-0 flex w-screen items-center justify-center p-4 bg-slate-900/80" @click="handleClose">
      <DialogPanel class="p-8 w-full max-w-lg rounded-lg bg-gray-800">
        <DialogTitle>习惯</DialogTitle>
        <DialogDescription>
          <form>
            <slot name="form"></slot>
          </form>
        </DialogDescription>


        <div class="mt-4 flex gap-2">
          <button type="button"
                  class="inline-flex justify-center px-4 py-2 text-sm font-medium text-gray-200 bg-blue-400 border border-transparent rounded-md hover:bg-blue-500 focus:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:ring-blue-500"
                  @click="handleSubmit">
            {{ props.confirmText || '保存'}}
          </button>
          <button type="button"
                  class="ml-3 inline-flex justify-center px-4 py-2 text-sm font-medium text-gray-400 bg-slate-600 border border-transparent rounded-md hover:bg-slate-500 focus:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:ring-blue-500"
                  @click="handleCancel">
            {{ props.cancelText || '取消' }}
          </button>
          <button type="button" v-if="props.defaultData"
                  class="ml-3 inline-flex justify-center px-4 py-2 text-sm font-medium text-gray-900 bg-blue-100 border border-transparent rounded-md hover:bg-blue-200 focus:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:ring-blue-500"
                  @click="handleResetClick">
            重置
          </button>
          <button v-if="props.openModal.set"
                  class=" bg-red-800 rounded-md px-4 py-2 text-sm text-gray-400 hover:bg-red-700 mx-2" @click="handleDelClick">
            {{ deleteBtnText }}
          </button>
        </div>
      </DialogPanel>
    </div>
  </Dialog>
</template>