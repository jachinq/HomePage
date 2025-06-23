<script setup lang="ts">
import Calendar from '../../components/Calendar.vue';
import Loading from "../../components/Loading.vue";
import {ref} from 'vue';
import Tooltips from "../../components/Tooltips.vue";
import EditHabitModal from "./EditModal.vue";

interface Habit {
  id: number;
  name: string;
  description: string;
  doneIcon?: String,
  failIcon?: String,
}

interface HabitLog {
  habitId: number;
  date: string;
  icon: string;
}

const habits = ref<Habit[]>([
  {id: 1, name: 'Test 1', description: 'this is description for Test 1'},
  {id: 2, name: 'Test 2', description: 'this is description for Test 2'},
  {id: 3, name: 'Test 3', description: 'this is description for Test 3'}
]);
const loading = ref(false);
const activeHabit = ref<Habit | null>(habits.value?.length > 0 ? habits.value[0] : null);
const habitLogs = ref<HabitLog[]>([]);
const openHabitModal = ref(false);

const fetchHabitLogs = async () => {
  loading.value = true;
  const logs = [
      {habitId: 1, date: '2025-06-23', icon: '✅'},
      {habitId: 2, date: '2025-06-24', icon: '❌'},
      {habitId: 2, date: '2025-06-05', icon: '✅'},
      {habitId: 3, date: '2025-06-09', icon: '❌'},
      {habitId: 3, date: '2025-06-11', icon: '❌'},
      {habitId: 3, date: '2025-06-15', icon: '❌'},
    ];
  await new Promise(resolve => setTimeout(() => {
    loading.value = false
    habitLogs.value = logs.filter(log => log.habitId === activeHabit.value?.id);
  }, 200));
}

const handleChangeHabit = (habit: Habit) => {
  activeHabit.value = habit
  loading.value = true
  setTimeout(() => {
    loading.value = false;
  }, 300);

  // 请求具体数据
  fetchHabitLogs();
}

const handleDateClick = (date: string) => {
  console.log(date);
}
fetchHabitLogs();
</script>

<template>
  <div class="flex flex-col w-full h-full">

    <!-- Habit List for selection -->
    <div class="flex flex-row items-center gap-2">
      <div class="">
        <button @click="openHabitModal = true"
            class="bg-sky-600 hover:bg-sky-500 text-gray-200 font-bold px-2 rounded cursor-pointer">新增
        </button>
      </div>
      <div v-for="habit in habits" class="">
                <span class="text-xl font-bold cursor-pointer hover:text-sky-400"
                      :class="{ 'text-sky-400': activeHabit === habit }" @click="handleChangeHabit(habit)">{{
                    habit.name
                  }}</span>
      </div>
    </div>

    <!-- Habit Detail -->
    <div v-if="activeHabit" class="flex flex-col w-full h-full">
      <div class="flex flex-row items-center justify-between">
        <div>
          <span class="text-lg">{{ activeHabit.description }}</span>
        </div>
        <button @click="openHabitModal = true"
            class="bg-sky-600 hover:bg-sky-500 text-gray-200 font-bold px-2 rounded cursor-pointer">Edit
        </button>
      </div>
    </div>

    <!-- 统计 -->
    <div class="flex flex-col items-center justify-center w-full h-16">
      <div>
        <span class="font-bold">统计: </span>
        <span>当前月份总次数：{{ habitLogs.length }}</span>
      </div>
    </div>

    <!-- Calendar -->
    <div class="flex items-center justify-center w-full h-full">
      <Calendar :year="2025" :month="6" v-slot="{ date }" @date-click="handleDateClick">
        <div v-if="habitLogs?.find(log => log?.date === date?.date)">
          <Tooltips text="123">
            <span class="text-lg">{{ habitLogs?.find(log => log?.date === date?.date)?.icon }}</span>
          </Tooltips>
        </div>
      </Calendar>
    </div>

    <Loading :loading="loading"/>

    <EditHabitModal :open-modal="openHabitModal" :oldData="activeHabit" @close="openHabitModal = false"/>
  </div>
</template>