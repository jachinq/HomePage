<template>
  <span class="text-gray-300 font-mono font-bold text-6xl w-full h-full flex justify-center items-center text-center" :class="customClass">{{ formattedTime }}</span>
  <span class="text-2xl text-gray-300 font-bold w-full flex justify-center items-center text-center mt-1" :class="customDateClass">{{ formattedDate }}</span>
</template>

<script setup lang="ts">
import { onUnmounted, ref } from 'vue'
import { computed, onMounted } from 'vue'

const props = defineProps({
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
  let date = time.value
  // 简单格式化，仅支持 HH:mm, HH:mm:ss, yyyy-MM-dd, yyyy/MM/dd
  let fmt = props.format
  fmt = fmt.replace('HH', pad(date.getHours()).toString())
  fmt = fmt.replace('mm', pad(date.getMinutes()).toString())
  fmt = fmt.replace('ss', pad(date.getSeconds()).toString())
  return fmt
})

const formattedDate = computed(() => {
  let date: Date = time.value
  let fmt = props.dateFormat
  fmt = fmt.replace('yyyy', date.getFullYear().toString())
  fmt = fmt.replace('MM', pad(date.getMonth() + 1).toString())
  fmt = fmt.replace('dd', pad(date.getDate()).toString())
  fmt = fmt.replace('d', weekMap[date.getDay()])
  return fmt
})

const time = ref(new Date())
const timeInterval = ref()
onMounted(() => {
  timeInterval.value = setInterval(() => {  
    time.value = new Date()
  }, 1000)
})
onUnmounted(() => {
  clearInterval(timeInterval.value)
})

</script>

<style scoped>
span {
  -webkit-text-stroke: 1px black;
}
</style> 