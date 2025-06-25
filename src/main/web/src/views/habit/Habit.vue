<script setup lang="ts">
import HabitCalendar from '../../components/HabitCalendar.vue';
import Loading from "../../components/Loading.vue";
import {getCurrentInstance, ref} from 'vue';
import Tooltips from "../../components/Tooltips.vue";
import EditHabitModal from "./EditHabitModal.vue";
import EditHabitLogModal from "./EditHabitLogModal.vue";
import {HabitEntity, HabitLogEntity} from "../../interface/habit.ts";
import {getHabitList, getHabitLogList} from "../../api/habitApi.ts";
import BackToHome from "../../components/BackToHome.vue";

const context = getCurrentInstance()?.appContext.config.globalProperties;
const toast = context?.$toast;

const habits = ref<HabitEntity[]>([]);
const loading = ref(false);
const activeHabit = ref<HabitEntity | any>({});
const activeHabitLog = ref({});
const habitLogs = ref<HabitLogEntity[]>([]);
const openHabitModal = ref({add: false, set: false});
const openHabitLogModal = ref({add: false, set: false});

const fetchHabits = async () => {
  loading.value = true;
  const result = await getHabitList({});
  if (result.success) {
    habits.value = result.data
    if (habits.value && habits.value.length > 0) {
      activeHabit.value = habits.value[0];
      fetchHabitLogs();
    }
  } else {
    toast.error(result.message)
  }
  loading.value = false;
}

const fetchHabitLogs = async () => {
  loading.value = true;
  const result = await getHabitLogList({
    habitId: activeHabit.value.id,
  });
  if (result.success) {
    habitLogs.value = result.data
  } else {
    toast.error(result.message)
  }
  loading.value = false;
}

const handelEditHabitModelClose = (success: any) => {
  openHabitModal.value = {add: false, set: false}
  if (success === true) {
    fetchHabits()
  }
}
const handelEditHabitLogModelClose = (success: any) => {
  openHabitLogModal.value = {add: false, set: false}
  if (success === true) {
    fetchHabitLogs()
  }
}

const handleChangeHabit = (habit: HabitEntity) => {
  if (activeHabit.value.id == habit.id) return
  activeHabit.value = habit
  // 请求具体数据
  fetchHabitLogs();
}

const handleDateClick = (date: any) => {
  if (!activeHabit.value?.id) {
    toast.warn("先选择习惯再做记录")
    return
  }
  activeHabitLog.value = date?.log;
  if (activeHabitLog.value) {
    openHabitLogModal.value = {add: false, set: true};
  } else {
    openHabitLogModal.value = {add: true, set: false};
    activeHabitLog.value = {
      date: date.date,
      habitId: activeHabit.value.id
    }
  }

}
fetchHabits();
</script>

<template>
  <div class="flex flex-col w-full h-full">

    <div class="flex justify-between mb-2">
      <span class="text-2xl font-bold">习惯追踪</span>
      <BackToHome/>
    </div>

    <!-- Habit List for selection -->
    <div class="flex flex-row items-center gap-2">
      <div class="">
        <button @click="openHabitModal = {add: true, set: false}"
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
        <button @click="openHabitModal = {add: false, set: true}"
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
      <HabitCalendar :year="2025" :month="6" :habit="activeHabit" :logs="habitLogs" v-slot="{ date }"
                     @date-click="handleDateClick">
        <div v-if="date?.log">
          <Tooltips :text="date.log?.extra || ''">
            <span class="text-lg">{{ date.log?.done ? activeHabit?.doneIcon : activeHabit?.failIcon }}</span>
          </Tooltips>
        </div>
      </HabitCalendar>
    </div>

    <Loading :loading="loading"/>

    <EditHabitModal :open-modal="openHabitModal" :oldData="activeHabit" @close="handelEditHabitModelClose"/>
    <EditHabitLogModal
        :open-modal="openHabitLogModal"
        :oldData="activeHabitLog"
        :habit="activeHabit"
        @close="handelEditHabitLogModelClose"/>
  </div>
</template>