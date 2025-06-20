<template>
  <div class="relative">
    <input
      type="text"
      v-model="searchQuery"
      @input="handleSearch()"
      @keydown="handleKeyDown"
      placeholder="搜索..."
      class="group w-full px-4 py-2 text-gray-400 bg-gray-800 border rounded-lg focus:outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-500"
      :class="customClass"
    />
    <div class="absolute inset-y-0 right-0 flex items-center pr-3 cursor-pointer">

      <svg 
        @click="handleSearch(true)"
        v-show="searchQuery.length > 0" 
        class="w-5 h-5 fill-gray-400 mr-2" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg"><path d="M556.8 512l262.4-262.4c12.8-12.8 12.8-32 0-44.8s-32-12.8-44.8 0L512 467.2 249.6 198.4c-12.8-6.4-38.4-6.4-51.2 0s-6.4 38.4 0 51.2L467.2 512l-262.4 262.4c-12.8 12.8-12.8 32 0 44.8 0 12.8 12.8 12.8 19.2 12.8s19.2 0 25.6-6.4L512 556.8l262.4 262.4c6.4 6.4 12.8 6.4 25.6 6.4s19.2 0 25.6-6.4c12.8-12.8 12.8-32 0-44.8L556.8 512z"></path></svg>

      <svg  @click="handleEnter"
        class="w-5 h-5 text-gray-400"
        fill="none"
        stroke="currentColor"
        viewBox="0 0 24 24"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          stroke-width="2"
          d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
        ></path>
      </svg>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, defineProps } from 'vue'

defineProps({
  customClass: {
    type: [String, null],
    default: () => '',
  },
})

const searchQuery = ref('')

const emit = defineEmits(['search', 'enter', 'keydown'])

const handleSearch = (clear: boolean = false) => {
  if (clear) {
    searchQuery.value = ''
  }
  emit('search', searchQuery.value)
}
const handleEnter = () => {
    emit('enter', searchQuery.value)
}
const handleKeyDown = (event: KeyboardEvent) => {
  if (event.key === 'Enter') {
    handleEnter()
  }
  else {
    emit('keydown', event)
  }
}
</script> 