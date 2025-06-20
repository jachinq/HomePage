<script setup lang="ts">
import { ref, watch } from 'vue'
import {
  Dialog,
  DialogPanel,
  DialogTitle,
  DialogDescription, RadioGroup, RadioGroupOption,
} from '@headlessui/vue'
import { saveAppSet, deleteAppSet } from "../../api/appSetApi.ts";
import { getCurrentInstance } from "vue";

const context = getCurrentInstance()?.appContext.config.globalProperties;
const toast = context?.$toast;

const defaultFormData = {
  type: 1,
  name: '',
  description: '',
  port: '',
  category: '',
  innerUrl: '',
  outerUrl: '',
}


const props = defineProps<{
  openModal: boolean,
  oldData: any,
}>()
const emit = defineEmits<{
  (e: 'close', { }): void
}>()
const handleOpenModalChange = (value: any) => {
  emit('close', value)
}

const submitForm = async () => {
  console.log(formData.value)
  if (!formData.value.name) {
    toast?.error('名称不能为空');
    return;
  }
  if (!formData.value.description) {
    formData.value.description = "我也不知道它是干嘛的";
  }
  let result = await saveAppSet(formData.value);
  if (result.success) {
    toast?.success(result.msg);
    handleOpenModalChange({
      open: false,
      success: true,
    });
  } else {
    toast?.error(result.msg);
  }
}

const del = async () => {
  if (!confirmDelete.value) {
    confirmDelete.value = true;
    deleteBtnText.value = "确认删除";
    setTimeout(() => {
      confirmDelete.value = false;
      deleteBtnText.value = "删除";
    }, 3000); // 3秒后恢复
    return;
  }
  if ((await deleteAppSet(formData.value)).success) {
    toast?.success("删除成功✌️")
    handleOpenModalChange({
      open: false,
      success: true,
    });
  }
}

// 监测oldData变化
watch(props, (newVal, _) => {
  formData.value = { ...newVal.oldData }
})

const formData = ref(props.oldData ? { ...props.oldData } : { ...defaultFormData })
const confirmDelete = ref(false)
const deleteBtnText = ref("删除")
</script>

<template>
  <Dialog :open="props.openModal" @close="handleOpenModalChange({ open: false })" class="relative z-50">
    <div class="fixed inset-0 flex w-screen items-center justify-center p-4 bg-slate-900/80">
      <DialogPanel class="p-8 w-full max-w-sm rounded-lg bg-gray-800">
        <DialogTitle>应用</DialogTitle>
        <DialogDescription>
          <form>
            <div class="space-y-4 mt-4">
              <label for="name" class="block text-sm font-bold text-gray-400">类型</label>
              <RadioGroup class="space-x-4 flex flex-row" :default-value="formData.type" v-model="formData.type" name="type">
                <RadioGroupOption v-slot="{ checked }" :value="1" class="cursor-pointer">
                  <div class="flex items-center space-x-2 text-sm">
                    <span class="rounded-full w-4 h-4 flex items-center justify-center border-2 border-gray-300"
                      :class="checked ? 'bg-blue-600' : 'bg-gray-600'"></span>
                    <span :class="!checked ? 'text-gray-400' : 'text-blue-500'">应用</span>
                  </div>
                </RadioGroupOption>
                <RadioGroupOption v-slot="{ checked }" :value="2" class="cursor-pointer">
                  <div class="flex items-center space-x-2 text-sm">
                    <span class="rounded-full w-4 h-4 flex items-center justify-center border-2 border-gray-300"
                      :class="checked ? 'bg-blue-600' : 'bg-gray-600'"></span>
                    <span :class="!checked ? 'text-gray-400' : 'text-blue-500'">书签</span>
                  </div>
                </RadioGroupOption>
              </RadioGroup>
            </div>
            <div class="space-y-4 mt-4">
              <label for="name" class="block text-sm font-bold text-gray-400">名称</label>
              <input v-model="formData.name" type="text" id="name"
                class="px-4 py-2 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                placeholder="应用名称">
            </div>
            <div class="space-y-4 mt-4">
              <label for="description" class="block text-sm font-bold text-gray-400">描述</label>
              <textarea v-model="formData.description" id="description"
                class="px-4 py-2 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                rows="2" scroll-behavior="smooth" placeholder="应用描述"></textarea>
            </div>
            <div class="space-y-4 mt-4">
              <label for="name" class="block text-sm font-bold text-gray-400">端口号</label>
              <input v-model="formData.port" type="text" id="name"
                class="px-4 py-2 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                placeholder="使用的端口号">
            </div>
            <div class="space-y-4 mt-4">
              <label for="name" class="block text-sm font-bold text-gray-400">分类</label>
              <input v-model="formData.category" type="text" id="name"
                class="px-4 py-2 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                placeholder="当前分类">
            </div>
            <div class="space-y-4 mt-4">
              <label for="name" class="block text-sm font-bold text-gray-400">图标
                 <a href="https://www.iconfont.cn/" class="text-blue-400 hover:text-blue-500" target="_blank">更多svg</a>
                </label>
              <input v-model="formData.icon" type="text" id="name"
                class="px-4 py-2 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                placeholder="应用图标">
            </div>
            <div class="space-y-4 mt-4">
              <label for="name" class="block text-sm font-bold text-gray-400">外网地址</label>
              <input v-model="formData.outerUrl" type="text" id="name"
                class="px-4 py-2 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                placeholder="在外网时的访问地址">
            </div>
          </form>
        </DialogDescription>
        <div class="mt-4">
          <button type="button"
            class="inline-flex justify-center px-4 py-2 text-sm font-medium text-gray-200 bg-blue-400 border border-transparent rounded-md hover:bg-blue-500 focus:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:ring-blue-500"
            @click="submitForm">
            保存
          </button>
          <button type="button"
            class="ml-3 inline-flex justify-center px-4 py-2 text-sm font-medium text-gray-400 bg-slate-600 border border-transparent rounded-md hover:bg-slate-500 focus:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:ring-blue-500"
            @click="handleOpenModalChange({ open: false })">
            取消
          </button>
          <button type="button"
            class="ml-3 inline-flex justify-center px-4 py-2 text-sm font-medium text-gray-400 bg-slate-600 border border-transparent rounded-md hover:bg-slate-500 focus:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:ring-blue-500"
            @click="formData = { ...defaultFormData }">
            重置
          </button>
          <button v-if="props.oldData"
            class=" bg-red-800 rounded-md px-4 py-2 text-sm text-gray-400 hover:bg-red-700 mx-2" @click="del">
            {{ deleteBtnText }}
          </button>
        </div>
      </DialogPanel>
    </div>
  </Dialog>
</template>

<style scoped></style>