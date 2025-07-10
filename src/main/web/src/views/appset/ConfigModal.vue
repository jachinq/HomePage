<script setup lang="ts">
import { ref, watch } from 'vue'
import {
  TransitionRoot,
  TransitionChild,
  Dialog,
  DialogPanel,
  DialogTitle,
  DialogDescription,
  Switch,
} from '@headlessui/vue'
import { saveAppConfig, getAppConfig } from "@/api/appConfigApi.ts";
import { getCurrentInstance } from "vue";
import { preProcFormData } from '@/utils/commUtil';

const context = getCurrentInstance()?.appContext.config.globalProperties;
const toast = context?.$toast;

const defaultFormData = {
  blankApp: true,
  blankBookmark: true,
  innerDomain: '192.162.0.0',
  outerDomain: ''
}


const props = defineProps<{
  openModal: boolean
}>()
const emit = defineEmits<{
  (e: 'close', { }): void
}>()
const handleOpenModalChange = (value: any) => {
  emit('close', value)
}

const submitForm = async () => {
  console.log(formData.value)
  preProcFormData(formData.value);
  let result = await saveAppConfig(formData.value);
  if (result.success) {
    toast?.success(result.message);
    handleOpenModalChange({
      open: false,
      success: true,
    });
    toast?.error(result.message);
  }
}


const formData = ref({ ...defaultFormData })
watch(props, async (value) => {
  if (value && value.openModal) {
    const result = await getAppConfig();
    if (result.success) {
      formData.value = result.data;
    } else {
      toast.error(result.message);
      handleOpenModalChange({
        open: false,
        success: false,
      });
    }
  }
})
</script>

<template>
  <TransitionRoot :show="openModal" as="template" enter="duration-100 ease-out" enter-from="opacity-0"
    enter-to="opacity-100" leave="duration-100 ease-in" leave-from="opacity-100" leave-to="opacity-0">
    <Dialog :open="props.openModal" @close="handleOpenModalChange({ open: false })" class="relative z-50">
      <TransitionChild enter="duration-100 ease-out" enter-from="opacity-0" enter-to="opacity-100"
        leave="duration-100 ease-in" leave-from="opacity-100" leave-to="opacity-0">
        <div class="fixed inset-0 bg-black/30" aria-hidden="true"></div>
      </TransitionChild>
      <TransitionChild enter="duration-100 ease-out" enter-from="opacity-0 scale-95" enter-to="opacity-100 scale-100"
        leave="duration-100 ease-in" leave-from="opacity-100 scale-100" leave-to="opacity-0 scale-95">
        <div class="fixed inset-0 flex w-screen items-center justify-center p-4">
          <DialogPanel class="p-8 w-full max-w-sm rounded-lg bg-gray-800">
            <DialogTitle>应用配置</DialogTitle>
            <DialogDescription>
              <form>
                <div class="space-y-4 mt-4">
                  <label for="name" class="block text-sm font-bold text-gray-400">新窗口打开应用</label>
                  <Switch v-model="formData.blankApp" :class="formData.blankApp ? 'bg-blue-400' : 'bg-gray-500'"
                    class="relative inline-flex h-4 w-8 items-center rounded-full">
                    <span class="sr-only">blank app</span>
                    <span
                      :class="formData.blankApp ? 'translate-x-4 h-5 w-5 bg-blue-500' : 'translate-x-0 h-4 w-4 bg-gray-700 '"
                      class="inline-block transform rounded-full transition"></span>
                  </Switch>
                </div>
                <div class="space-y-4 mt-4">
                  <label for="name" class="block text-sm font-bold text-gray-400">新窗口打开书签</label>
                  <Switch v-model="formData.blankBookmark"
                    :class="formData.blankBookmark ? 'bg-blue-400' : 'bg-gray-500'"
                    class="relative inline-flex h-4 w-8 items-center rounded-full">
                    <span class="sr-only">blank app</span>
                    <span
                      :class="formData.blankBookmark ? 'translate-x-4 h-5 w-5 bg-blue-500' : 'translate-x-0 h-4 w-4 bg-gray-700 '"
                      class="inline-block transform rounded-full transition"></span>
                  </Switch>
                </div>

                <div class="space-y-4 mt-4">
                  <label for="name" class="block text-sm font-bold text-gray-400">内网域名</label>
                  <input v-model="formData.innerDomain" type="text" id="name"
                    class="px-4 py-2 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                    placeholder="在内网时的访问地址">
                </div>
                <div class="space-y-4 mt-4">
                  <label for="name" class="block text-sm font-bold text-gray-400">外网域名</label>
                  <input v-model="formData.outerDomain" type="text" id="name"
                    class="px-4 py-2 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                    placeholder="在外网时的访问地址">
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
                @click="handleOpenModalChange({ open: false })">
                取消
              </button>
              <button type="button"
                class="ml-3 inline-flex justify-center px-4 py-2 text-sm font-medium text-gray-900 bg-blue-100 border border-transparent rounded-md hover:bg-blue-200 focus:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:ring-blue-500"
                @click="formData = { ...defaultFormData }">
                重置
              </button>
            </div>
          </DialogPanel>
        </div>
      </TransitionChild>
    </Dialog>
  </TransitionRoot>
</template>

<style scoped></style>