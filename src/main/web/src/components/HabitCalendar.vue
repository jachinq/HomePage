<script setup lang="ts">

import { onMounted, onUnmounted, ref } from 'vue';
import {CalendarProp} from "../interface/habit.ts";
import { randomString } from '@/utils/commUtil.ts';

const props = withDefaults(defineProps<{
  month?: number,
  year?: number,
  logs?: any[],
}>(), {
  month: () => new Date().getMonth() + 1,
  year: () => new Date().getFullYear(),
});

const daysOfWeek = ['一', '二', '三', '四', '五', '六', '日'];
const month = ref<number>(props.month || new Date().getMonth() + 1); // Default to current month
const year = ref<number>(props.year || new Date().getFullYear()); // Default to current year

const dates = ref(() => {
  const today = new Date().toISOString().split('T')[0];

  const dates: CalendarProp[] = [];
  const startDate = new Date(year.value, month.value - 1, 1);
  const endDate = new Date(year.value, month.value, 0);
  let startDay = startDate.getDay()
  if (startDay === 0) {
    startDay = 7
  }
  // console.log('Start day:', startDay, 'for month:', month.value);

  // Fill empty cells before the first day of the month
  for (let i = 1; i < startDay; i++) {
    dates.push({number: -1, date: '', key: randomString()});
  }

  const map: { [key: string]: any } = {}
  props.logs?.forEach(log => {
    let logs = map[log.date];
    if (!logs) {
      logs = [];
      map[log.date] = logs;
    }
    logs.push(log);
  });

  // Fill cells with dates
  for (let date = 1; date <= endDate.getDate(); date++) {
    const currentDate = new Date(year.value, month.value - 1, date + 1).toISOString().split('T')[0];
    const logs = map[currentDate];
    dates.push({
      key: randomString(),
      number: date,
      date: currentDate,
      isToday: today === currentDate,
      logs
    });
  }
  // console.log('Generated dates array:', dates, map);
  return dates;
});

const prevMonth = () => {
  if (month.value === 1) {
    year.value--;
    month.value = 12;
  } else {
    month.value--;
  }
  emit('dateChange', { month: month.value, year: year.value })
};

const nextMonth = () => {
  if (month.value === 12) {
    year.value++;
    month.value = 1;
  } else {
    month.value++;
  }
  emit('dateChange', { month: month.value, year: year.value })
};

const emit = defineEmits(['dateClick', 'dateChange'])
const handleDate = (date: CalendarProp) => {
  emit('dateClick', date)
}

// 键盘事件
const keydown = (event: KeyboardEvent) => {
  switch (event.key) {
    case 'ArrowLeft':
      prevMonth();
      break;
    case 'ArrowRight':
      nextMonth();
      break;
  }
}
onMounted(() => {
  document.addEventListener('keydown', keydown); // 监听键盘按键
});
onUnmounted(() => {
  document.removeEventListener('keydown', keydown); // 移除键盘按键监听
});

</script>

<template>
  <div>
    <!-- Month and year -->
    <div class="flex justify-center items-center mb-2 gap-4">
      <div class="text-center font-bold cursor-pointer select-none" @click="prevMonth()">上个月</div>
      <div class="text-center font-bold">{{ year }}-{{ month.toString().padStart(2, '0') }}</div>
      <div class="text-center font-bold cursor-pointer select-none" @click="nextMonth()">下个月</div>
    </div>

    <div class="grid grid-cols-7 gap-2">

      <!-- Days of the week -->
      <div v-for="day in daysOfWeek" :key="day" class="text-center font-bold">{{ day }}</div>

      <!-- Dates -->
      <div v-for="date in dates()" :key="date.key" class="text-center flex justify-center">
        <div class="group cursor-pointer border border-gray-700 rounded-sm p-2 w-20 h-20"
             :class="{ 'bg-sky-800 border-0': date.isToday }" @click="handleDate(date)">
          <div class="group-hover:text-sky-400">
            {{ date.number > 0 ? date.number : '' }}
          </div>
          <div class="min-w-8 min-h-8 text-center">
            <slot :date="date"></slot>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
