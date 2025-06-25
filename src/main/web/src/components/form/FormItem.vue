<script setup lang="ts">

const props = defineProps<{
    label?: string,
}>()
</script>

<template>
    <div class="space-y-4 mt-4">
        <label v-if="props.label" for="name" class="block text-sm font-bold text-gray-400">
          {{ props.label }}
          <slot name="label"></slot>
        </label>
        <slot></slot>
    </div>
</template>