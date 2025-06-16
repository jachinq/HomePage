<template>
  <Transition
    enter-active-class="transition-all duration-300 ease-out"
    enter-from-class="opacity-0 -translate-y-2"
    leave-active-class="transition-all duration-300 ease-in"
    leave-to-class="opacity-0 -translate-y-2"
  >
    <div
      v-if="visible"
      class="fixed left-1/2 z-50 flex -translate-x-1/2 items-center rounded-md px-5 py-3 shadow-lg"
      :style="{ top: `${top}px` }"
      :class="[
        {
          'bg-green-50 text-green-500': type === 'success',
          'bg-red-50 text-red-500': type === 'error',
          'bg-orange-50 text-orange-500': type === 'warning'
        }
      ]"
    >
      <div v-if="showIcon" class="mr-2 text-base">
        <i v-if="type === 'success'" class="icon-success"></i>
        <i v-else-if="type === 'error'" class="icon-error"></i>
        <i v-else-if="type === 'warning'" class="icon-warning"></i>
      </div>
      <div class="text-sm leading-normal" v-html="message"></div>
    </div>
  </Transition>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import {ToastOptions} from "./types.ts";

withDefaults(defineProps<ToastOptions>(), {
  type: 'success',
  duration: 3000,
  showIcon: true
})

const visible = ref(false)
const top = ref(16) // 默认距离顶部 16px

const show = () => {
  visible.value = true
}

const updatePosition = (newTop: number) => {
  top.value = newTop
}

defineExpose({
  show,
  updatePosition
})
</script> 