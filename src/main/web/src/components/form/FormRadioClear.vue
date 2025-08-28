<script setup lang="ts">

import { computed } from "vue";

export interface Option {
  value: number | string | boolean,
  label: string
}

const props = withDefaults(defineProps<{
  modelValue?: any,
  options: Option[]
}>(), {
})
const emit = defineEmits<(e: 'update:modelValue', value: any) => void>()

const handleChange = (value: any) => {
  emit('update:modelValue', value)
}


// const checked = ref(false)
const options = computed(() => props.options.map(option => ({
  value: option.value,
  label: option.label,
  checked: option.value === props.modelValue
})))
</script>

<template>
  <div class="space-x-4   flex flex-row" :default-value="props.modelValue" :model-value="props.modelValue"
    @update:model-value="handleChange" name="type">
    <div v-for="option in options" :value="option.value" class="cursor-pointer">
      <div class="flex items-center space-x-2 text-sm" @click="handleChange(option.value)">
        <span class="rounded-full w-4 h-4 flex items-center justify-center border-2 border-gray-600"
          :class="option.checked ? 'bg-blue-500' : 'bg-gray-600'"></span>
        <span :class="!option.checked ? 'text-gray-400' : 'text-blue-400'">{{ option.label }}</span>
      </div>
    </div>
  </div>
</template>
