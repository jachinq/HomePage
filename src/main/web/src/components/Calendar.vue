<script setup lang="ts">

import {ref, withDefaults, defineProps, defineEmits} from 'vue';

interface DateProp {
  number: Number,
  date: String,
  isToday?: Boolean
}

const props = withDefaults(defineProps<{
  month?: Number,
  year?: Number,
}>(), {
  month: new Date().getMonth() + 1,
  year: new Date().getFullYear(),
});

const daysOfWeek = ['一', '二', '三', '四', '五', '六', '日'];
const month = ref(props.month); // Default to current month
const year = ref(props.year); // Default to current year

const dates = ref(() => {
  const today = new Date().toISOString().split('T')[0];

  const dates: DateProp[] = [];
  const startDate = new Date(year.value, month.value - 1, 1);
  const endDate = new Date(year.value, month.value, 0);
  let startDay = startDate.getDay()
  if (startDay === 0) {
    startDay = 7
  }
  console.log('Start day:', startDay, 'for month:', month.value);

  // Fill empty cells before the first day of the month
  for (let i = 1; i < startDay; i++) {
    dates.push({number: '', date: ''});
  }

  // Fill cells with dates
  for (let date = 1; date <= endDate.getDate(); date++) {
    const currentDate = new Date(year.value, month.value - 1, date + 1).toISOString().split('T')[0];
    dates.push({
      number: date,
      date: currentDate,
      isToday: today === currentDate,
    });
  }
  console.log('Generated dates array:', dates);
  return dates;
});

const prevMonth = () => {
  if (month.value === 1) {
    year.value--;
    month.value = 12;
  } else {
    month.value--;
  }
};

const nextMonth = () => {
  if (month.value === 12) {
    year.value++;
    month.value = 1;
  } else {
    month.value++;
  }
};

const emit = defineEmits(['dateClick'])
const handleDate = (date: DateProp) => {
  emit('dateClick', date)
}

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
      <div v-for="date in dates()" :key="date.date" class="text-center flex justify-center">
        <div class="border border-gray-700 rounded-sm p-2 w-20 h-20"
             :class="{ 'bg-sky-800 border-0': date.isToday }">
          <div class="cursor-pointer hover:text-sky-400" @click="handleDate(date)">
            {{ date.number }}
          </div>
          <div class="min-w-8 min-h-8 text-center">
            <slot :date="date"></slot>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
