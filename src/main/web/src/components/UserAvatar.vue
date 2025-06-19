<template>
  <div
    class="fixed top-4 right-4 z-50 flex items-center justify-center cursor-pointer"
    :class="customClass"
    @click="onClick"
  >
    <template v-if="avatarUrl">
      <img
        :src="avatarUrl"
        alt="用户头像"
        class="w-12 h-12 rounded-full border-2 border-gray-200 shadow object-cover bg-gray-800 hover:border-blue-500 hover:shadow-lg"
      />
    </template>
    <template v-else>
      <div
        class="w-12 h-12 rounded-full flex items-center justify-center border-2 border-gray-400 shadow text-2xl font-bold select-none"
        :class="login ? 'bg-blue-800 text-gray-200 hover:bg-blue-700' : 'bg-gray-200 text-gray-500 hover:bg-gray-300'"
      >
        U
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { defineProps, defineEmits } from 'vue'

const props = defineProps({
  login: {
    type: Boolean,
    default: false,
  },
  avatarUrl: {
    type: [String, null],
    default: '',
  },
  customClass: {
    type: String,
    default: '',
  },
})

const emits = defineEmits(['click'])

function onClick() {
  emits('click')
}
</script>

<style scoped>
</style> 