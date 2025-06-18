<script setup lang="ts">
import {ref} from 'vue'
import {
  TransitionRoot,
  TransitionChild,
  Dialog,
  DialogPanel,
  DialogTitle,
  DialogDescription, RadioGroup, RadioGroupOption,
} from '@headlessui/vue'
import {addAppSet} from "../../api/appSetApi.ts";
import { getCurrentInstance } from "vue";
const context = getCurrentInstance()?.appContext.config.globalProperties;
const toast = context?.$toast;

const formData = ref({
  type: 1,
  name: '',
  description: '',
  port: '',
  category: '',
  innerUrl: '',
  outerUrl: '',
})


const props = defineProps<{
  openModal: boolean
}>()
const emit = defineEmits<{
  (e: 'close', value: boolean): void
}>()
const handleOpenModalChange = (value: boolean) => {
  emit('close', value)
}

const submitForm = async () => {
  console.log(formData.value)
  let result = await addAppSet(formData.value);
  if (result.success) {
    toast?.success(result.msg);
    handleOpenModalChange(false);
  } else {
    toast?.error(result.msg);
  }es
}
</script>

<template>
  <TransitionRoot
      :show="openModal"
      as="template"
      enter="duration-100 ease-out"
      enter-from="opacity-0"
      enter-to="opacity-100"
      leave="duration-100 ease-in"
      leave-from="opacity-100"
      leave-to="opacity-0"
  >
    <Dialog :open="props.openModal" @close="handleOpenModalChange(false)" class="relative z-50">
      <TransitionChild
          enter="duration-100 ease-out"
          enter-from="opacity-0"
          enter-to="opacity-100"
          leave="duration-100 ease-in"
          leave-from="opacity-100"
          leave-to="opacity-0"
      >
        <div class="fixed inset-0 bg-black/30" aria-hidden="true"/>
      </TransitionChild>
      <TransitionChild
          enter="duration-100 ease-out"
          enter-from="opacity-0 scale-95"
          enter-to="opacity-100 scale-100"
          leave="duration-100 ease-in"
          leave-from="opacity-100 scale-100"
          leave-to="opacity-0 scale-95"
      >
        <div class="fixed inset-0 flex w-screen items-center justify-center p-4">
        <DialogPanel class="p-8 w-full max-w-sm rounded-lg bg-white">
          <DialogTitle>应用</DialogTitle>
          <DialogDescription>
            <form>
              <div class="space-y-4 mt-4">
                <label for="name" class="block text-sm font-medium text-gray-700">类型</label>
                <RadioGroup class="space-x-4 flex flex-row" :default-value="formData.type" name="type">
                  <RadioGroupOption v-slot="{ active, checked }" :value="1" class="cursor-pointer">
                    <span :class="!checked ? 'text-white' : 'text-blue-900'">应用</span>
                  </RadioGroupOption>
                  <RadioGroupOption v-slot="{ active, checked }" :value="2" class="cursor-pointer">
                    <span :class="!checked ? 'text-white' : 'text-blue-900'">书签</span>
                  </RadioGroupOption>
                </RadioGroup>
              </div>
              <div class="space-y-4 mt-4">
                <label for="name" class="block text-sm font-medium text-gray-700">名称</label>
                <input v-model="formData.name" type="text" id="name" class="px-4 py-2 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 sm:text-sm" placeholder="应用名称">
              </div>
              <div class="space-y-4 mt-4">
                <label for="description" class="block text-sm font-medium text-gray-700">描述</label>
                <textarea v-model="formData.description" id="description" class="px-4 py-2 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 sm:text-sm" rows="2" scroll-behavior="smooth" placeholder="应用描述"></textarea>
              </div>
              <div class="space-y-4 mt-4">
                <label for="name" class="block text-sm font-medium text-gray-700">端口号</label>
                <input v-model="formData.port" type="text" id="name" class="px-4 py-2 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 sm:text-sm" placeholder="使用的端口号">
              </div>
              <div class="space-y-4 mt-4">
                <label for="name" class="block text-sm font-medium text-gray-700">分类</label>
                <input v-model="formData.category" type="text" id="name" class="px-4 py-2 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 sm:text-sm" placeholder="当前分类">
              </div>
              <div class="space-y-4 mt-4">
                <label for="name" class="block text-sm font-medium text-gray-700">内网地址</label>
                <input v-model="formData.innerUrl" type="text" id="name" class="px-4 py-2 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 sm:text-sm" placeholder="在内网时的访问地址">
              </div>
              <div class="space-y-4 mt-4">
                <label for="name" class="block text-sm font-medium text-gray-700">外网地址</label>
                <input v-model="formData.outerUrl" type="text" id="name" class="px-4 py-2 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 sm:text-sm" placeholder="在外网时的访问地址">
              </div>
            </form>
          </DialogDescription>
          <div class="mt-4">
            <button type="button"
                    class="inline-flex justify-center px-4 py-2 text-sm font-medium text-blue-900 bg-blue-100 border border-transparent rounded-md hover:bg-blue-200 focus:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:ring-blue-500"
                    @click="submitForm">
              保存
            </button>
            <button type="button"
                    class="ml-3 inline-flex justify-center px-4 py-2 text-sm font-medium text-gray-900 bg-blue-100 border border-transparent rounded-md hover:bg-blue-200 focus:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:ring-blue-500"
                    @click="handleOpenModalChange(false)">
              取消
            </button>
          </div>
        </DialogPanel>
        </div>
      </TransitionChild>
    </Dialog>
  </TransitionRoot>
</template>

<style scoped>

</style>