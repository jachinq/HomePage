<script setup lang="ts">

const props = defineProps<{
    label?: string,
    inline?: boolean,
    name?: string,
    labelClass?: string,
}>()
</script>

<template>
    <div class="flex gap-2" 
    :class="{'flex-row': props.inline === true, 'flex-col space-y-4 mt-4': props.inline === false || props.label === undefined}"
    >
        <label v-if="props.label" :for="props.name || props.label" class="text-sm font-bold text-gray-400"
        :class="props.labelClass || '' + props.inline ? ' min-w-20' : ''"
        >
          {{ props.label }}
          <slot name="label"></slot>
        </label>
        <div>
            <slot></slot>
        </div>
    </div>
</template>