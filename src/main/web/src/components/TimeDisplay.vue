<template>
  <span class="text-gray-400 font-mono text-4xl w-full h-full flex justify-center items-center text-center" :class="customClass">{{ formattedTime }}</span>
  <div class="text-sm text-gray-500 w-full flex justify-center items-center text-center mt-1" :class="customDateClass">{{ formattedDate }}</div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps({
  time: {
    type: [Date, String, Number],
    default: () => new Date(),
  },
  format: {
    type: String,
    default: 'HH:mm', // 默认格式 23:12
  },
  dateFormat: {
    type: String,
    default: 'MM-dd 星期d', // 默认格式 06-19 星期四
  },
  customClass: {
    type: String,
    default: '',
  },
  customDateClass: {
    type: String,
    default: '',
  },
})

function pad(num: number) {
  return num < 10 ? '0' + num : num
}

const weekMap = ['日', '一', '二', '三', '四', '五', '六']

const formattedTime = computed(() => {
  let date: Date
  if (props.time instanceof Date) {
    date = props.time
  } else if (typeof props.time === 'string' || typeof props.time === 'number') {
    date = new Date(props.time)
  } else {
    date = new Date()
  }

  // 简单格式化，仅支持 HH:mm, HH:mm:ss, yyyy-MM-dd, yyyy/MM/dd
  let fmt = props.format
  fmt = fmt.replace('yyyy', date.getFullYear().toString())
  fmt = fmt.replace('MM', pad(date.getMonth() + 1).toString())
  fmt = fmt.replace('dd', pad(date.getDate()).toString())
  fmt = fmt.replace('HH', pad(date.getHours()).toString())
  fmt = fmt.replace('mm', pad(date.getMinutes()).toString())
  fmt = fmt.replace('mm', pad(date.getMinutes()).toString())
  fmt = fmt.replace('ss', pad(date.getSeconds()).toString())
  return fmt
})

const formattedDate = computed(() => {
  let date: Date
  if (props.time instanceof Date) {
    date = props.time
  } else if (typeof props.time === 'string' || typeof props.time === 'number') {
    date = new Date(props.time)
  } else {
    date = new Date()
  }
  let fmt = props.dateFormat
  fmt = fmt.replace('yyyy', date.getFullYear().toString())
  fmt = fmt.replace('MM', pad(date.getMonth() + 1).toString())
  fmt = fmt.replace('dd', pad(date.getDate()).toString())
  fmt = fmt.replace('d', weekMap[date.getDay()])
  return fmt
})
</script>

<style scoped>
</style> 