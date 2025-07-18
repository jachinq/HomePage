<template>
  <Transition name="fade">
    <div
      v-if="isVisible"
      @click="scrollToTop"
      class="fixed bottom-6 right-6 z-50 cursor-pointer group w-12 h-12 bg-blue-600 hover:bg-blue-700 rounded-full shadow-lg hover:shadow-xl transition-all duration-300 flex items-center justify-center"
    >
      <svg
        class="w-6 h-6 text-white group-hover:scale-110 transition-transform duration-200"
        fill="none"
        stroke="currentColor"
        viewBox="0 0 24 24"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          stroke-width="2"
          d="M7 11l5-5m0 0l5 5m-5-5v12"
        />
      </svg>
    </div>
  </Transition>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'

const isVisible = ref(false)
const scrollThreshold = 300 // 滚动超过300px时显示按钮

const handleScroll = () => {
  isVisible.value = window.scrollY > scrollThreshold
}

const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  })
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style> 