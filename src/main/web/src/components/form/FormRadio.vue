<script setup lang="ts">

import {RadioGroup, RadioGroupOption} from "@headlessui/vue";

interface Option {
  value: number | string,
  name: string
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
</script>

<template>
  <RadioGroup class="space-x-4 flex flex-row" :default-value="props.modelValue" :model-value="props.modelValue" @update:model-value="handleChange"
              name="type">
    <RadioGroupOption v-for="option in options" v-slot="{ checked }" :value="option.value" class="cursor-pointer">
      <div class="flex items-center space-x-2 text-sm">
              <span class="rounded-full w-4 h-4 flex items-center justify-center border-2 border-gray-600"
                    :class="checked ? 'bg-blue-500' : 'bg-gray-600'"></span>
        <span :class="!checked ? 'text-gray-400' : 'text-blue-400'">{{ option.name }}</span>
      </div>
    </RadioGroupOption>
  </RadioGroup>
</template>
