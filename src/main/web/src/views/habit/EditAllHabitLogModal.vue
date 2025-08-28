<script setup lang="ts">
import { ref, watch } from 'vue'
import { getCurrentInstance } from "vue";
import { batchSaveHabitLog, getHabitList } from "../../api/habitApi.ts";
import SaveModal from "../../components/SaveModal.vue";
import FormItem from "../../components/form/FormItem.vue";
import FormInput from "../../components/form/FormInput.vue";
import { CalendarProp, HabitEntity, HabitLogEntity } from "../../interface/habit.ts";
import { preProcFormData } from '@/utils/commUtil.ts';
import Loading from '@/components/Loading.vue';
import FormRadioClear from '@/components/form/FormRadioClear.vue';

const context = getCurrentInstance()?.appContext.config.globalProperties;
const toast = context?.$toast;

const props = defineProps<{
  openModal: {
    add: boolean,
    set: boolean
  },
  oldData: CalendarProp,
}>()
const emit = defineEmits<{
  (e: 'close', { }): void
}>()
const handleClose = (value: any) => {
  formData.value = {}
  emit('close', value)
}

const handleStatus = async () => {
  console.log(formData.value)

  const map:any = {}
  Object.keys(formData.value).forEach(key => {
    if (key.startsWith('habit_')) {
      const habitId = parseInt(key.split('_')[1])
      const done = formData.value[key]
      map[habitId] = done
    }
  })

  const delList: number[] = []
  const setList: HabitLogEntity[] = []
  habitLogs.value.forEach(habitLog => {
    const habitId = habitLog.habitId || 0;
    const done = map[habitId];
    if (done === undefined) {
      delList.push(habitLog.id || 0)
      return;
    }

    habitLog.done = done
    setList.push(habitLog)
  });

  const addList: HabitLogEntity[] = []
  Object.keys(map).forEach(habitId => {
    const done = map[habitId]
    if (done === undefined) {
      return;
    }
    // 过滤掉setlist
    if (setList.some(item => item.habitId === parseInt(habitId))) {
      return;
    }
    addList.push({
      date: props.oldData.date,
      habitId: parseInt(habitId),
      done,
    })
  })

  console.log(delList, setList, addList)

  const data = {
    delList, setList, addList
  }
  console.log(data)
  preProcFormData(data);
  let result = await batchSaveHabitLog(data);
  if (result.success) {
    toast?.success(result.message);
    handleClose(true);
  } else {
    toast?.error(result.message);
  }
}

const clearFormData = (habitId: number) => {
  // delete formData.value['habit_' + habitId]
  formData.value['habit_' + habitId] = undefined

}

const habitLogs = ref<HabitLogEntity[]>([]);
// 监测oldData变化
watch(props, (newVal, _) => {
  // formData.value = { ...newVal.oldData }

  const calendarProp = newVal.oldData;
  // console.log(calendarProp, habits.value)
  const logs = calendarProp.logs || [];

  const habitMap = new Map<number, HabitEntity>();
  habits.value.forEach(habit => habitMap.set(habit.id || -1, habit))

  formData.value = {}
  logs.forEach(log => {
    const habit = habitMap.get(log.habitId || 0) || {};
    formData.value['habit_' + habit.id] = log.done || false;
    habitLogs.value.push(log);
  });

  // console.log(formData.value)
})

const formData = ref<any>({})

const habits = ref<HabitEntity[]>([]);
const loading = ref(false)
const fetchHabits = async () => {
  loading.value = true;
  const result = await getHabitList({});
  if (result.success) {
    habits.value = result.data
  } else {
    toast.error(result.message)
  }
  loading.value = false;
}
fetchHabits();
</script>

<template>
  <SaveModal name="习惯追踪记录" :open-modal="props.openModal" 
    @on-submit="handleStatus" confirm-text="提交"
    @on-cancel="handleClose" cancel-text="取消" 
    @on-close="handleClose">
    <template #form>
      <Loading :loading="loading" v-if="loading" />
      <template v-else>
        <FormItem label="日期">
          <FormInput v-model="props.oldData.date" type="date" :disable="true" />
        </FormItem>

        <div v-for="habit in habits" class="flex flex-row items-center mt-2 gap-2">
          <FormItem :label="habit.name" :inline="true">
            <FormRadioClear  
              :options="[{ label: '完成' + habit.doneIcon, value: true }, { label: '失败' + habit.failIcon, value: false }]"
              v-model="formData['habit_' + habit.id]"/>
          </FormItem>
          <button v-if="formData['habit_' + habit.id] !== undefined" @click="clearFormData(habit.id || 0)">清空</button>
        </div>
      </template>
    </template>
  </SaveModal>
</template>
