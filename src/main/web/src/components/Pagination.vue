<template>
  <div class="pagination">
    <button 
      class="pagination-btn" 
      :disabled="currentPage === 1"
      @click="handlePageChange(currentPage - 1)"
    >
      上一页
    </button>
    
    <div class="pagination-pages">
      <template v-for="page in displayedPages" :key="page">
        <button 
          class="pagination-btn"
          :class="{ active: page === currentPage }"
          @click="handlePageChange(page)"
        >
          {{ page }}
        </button>
      </template>
    </div>

    <button 
      class="pagination-btn" 
      :disabled="currentPage === totalPages"
      @click="handlePageChange(currentPage + 1)"
    >
      下一页
    </button>

    <div class="pagination-info">
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

<style scoped>
.pagination {
  display: flex;
  align-items: center;
  gap: 8px;
}

.pagination-btn {
  padding: 6px 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  background-color: #fff;
  color: #606266;
  cursor: pointer;
  transition: all 0.3s;
}

.pagination-btn:hover:not(:disabled) {
  color: #409eff;
  border-color: #c6e2ff;
  background-color: #ecf5ff;
}

.pagination-btn.active {
  color: #fff;
  background-color: #409eff;
  border-color: #409eff;
}

.pagination-btn:disabled {
  cursor: not-allowed;
  color: #c0c4cc;
  background-color: #f5f7fa;
}

.pagination-pages {
  display: flex;
  gap: 4px;
}

.pagination-info {
  margin-left: 16px;
  color: #606266;
  font-size: 14px;
}
</style> 