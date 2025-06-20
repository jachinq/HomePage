<template>
  <div class="flex items-center gap-2">
    <button 
      class="px-2 py-1 rounded-md text-sm font-medium leading-5 text-gray-400 bg-gray-600 border border-gray-500 cursor-pointer hover:bg-gray-800 focus:outline-none focus:border-blue-300 focus:shadow-outline-blue active:bg-gray-600 active:text-gray-400 transition ease-in-out duration-150 disabled:cursor-not-allowed"
      :disabled="currentPage === 1"
      @click="handlePageChange(currentPage - 1)"
    >
      上一页
    </button>
    
    <div class="flex gap-2">
      <template v-for="page in displayedPages" :key="page">
        <button 
          class="px-2 py-1 rounded-md text-sm font-medium leading-5 text-gray-400 bg-gray-600 border border-gray-500 cursor-pointer hover:bg-gray-800 focus:outline-none focus:border-blue-300 focus:shadow-outline-blue active:bg-gray-600 active:text-gray-400 transition ease-in-out duration-150 disabled:cursor-not-allowed"
          :class="{ active: page === currentPage }"
          @click="handlePageChange(page)"
        >
          {{ page }}
        </button>
      </template>
    </div>

    <button 
      class="px-2 py-1 rounded-md text-sm font-medium leading-5 text-gray-400 bg-gray-600 border border-gray-500 cursor-pointer hover:bg-gray-800 focus:outline-none focus:border-blue-300 focus:shadow-outline-blue active:bg-gray-600 active:text-gray-400 transition ease-in-out duration-150 disabled:cursor-not-allowed"
      :disabled="currentPage === totalPages"
      @click="handlePageChange(currentPage + 1)"
    >
      下一页
    </button>

    <div class="text-sm text-gray-400">
      共 {{ totalPages }} 页
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  currentPage: number
  totalPages: number
  maxDisplayedPages?: number
}>()

const emit = defineEmits<{
  (e: 'update:currentPage', page: number): void
}>()

const maxDisplayedPages = props.maxDisplayedPages || 5

const displayedPages = computed(() => {
  const pages: number[] = []
  const halfDisplay = Math.floor(maxDisplayedPages / 2)
  
  let start = Math.max(1, props.currentPage - halfDisplay)
  let end = Math.min(props.totalPages, start + maxDisplayedPages - 1)
  
  if (end - start + 1 < maxDisplayedPages) {
    start = Math.max(1, end - maxDisplayedPages + 1)
  }
  
  for (let i = start; i <= end; i++) {
    pages.push(i)
  }
  
  return pages
})

const handlePageChange = (page: number) => {
  if (page >= 1 && page <= props.totalPages) {
    emit('update:currentPage', page)
  }
}
</script>