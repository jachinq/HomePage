<template>
  <div 
    class="group relative inline-block"
    @mouseenter="handleMouseEnter"
    @mouseleave="handleMouseLeave"
  >
    <!-- 触发元素插槽 -->
    <slot></slot>
    
    <!-- 提示框 -->
    <div 
      v-show="showTooltip"
      class="absolute z-50 mt-2 w-max max-w-xs px-3 py-2 text-sm bg-gray-800 text-white rounded-md shadow-lg transition-opacity duration-200"
      :class="{
        'opacity-0': !showTooltip,
        'opacity-100': showTooltip
      }"
    >
      {{ text }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

defineProps({
  text: {
    type: String,
    required: true
  }
})

const showTooltip = ref(false)

const handleMouseEnter = () => {
  showTooltip.value = true
}

const handleMouseLeave = () => {
  showTooltip.value = false
}
</script>
