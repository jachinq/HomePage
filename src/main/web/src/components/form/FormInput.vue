<script setup lang="ts">

import { ref } from 'vue'

type input = string | number

const props = withDefaults(defineProps<{
  modelValue?: input,
  placeholder?: string,
  type?: string,
  class?: string
  width?: string
}>(), {
  type: 'text',
  width: 'full'
})
const emit = defineEmits<(e: 'update:modelValue', value: input) => void>()

const handleInput = () => {
  emit('update:modelValue', value.value)
}

const handleCompositionStart = () => {
  isComposing.value = true
}

const handleCompositionEnd = () => {
  isComposing.value = false
  handleInput()
}

const value = ref(props.modelValue || '')
const isComposing = ref(false)
</script>

<template>

  <textarea v-if="type==='textarea'" v-model="value"
            @input="handleInput"
            @compositionstart="handleCompositionStart"
            @compositionend="handleCompositionEnd"
            class="px-4 py-2 block w-full border border-gray-600 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
            rows="2" :placeholder="placeholder"></textarea>
  <input v-else v-model="value" :type="type"
         @input="handleInput"
         @compositionstart="handleCompositionStart"
         @compositionend="handleCompositionEnd"
         class="px-4 py-2 border border-gray-600 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
         :class="'w-' + props.width"
         :placeholder="placeholder">
</template>