<script setup lang="ts">
import {ref, watch} from 'vue'
import {getCurrentInstance} from "vue";
import FormItem from "../../components/form/FormItem.vue";
import {HabitEntity} from '../../interface/habit.ts';
import {deleteHabit, saveHabit} from "../../api/habitApi.ts";
import SaveModal from "../../components/SaveModal.vue";
import FormInput from "../../components/form/FormInput.vue";
import { preProcFormData } from '@/utils/commUtil.ts';

const context = getCurrentInstance()?.appContext.config.globalProperties;
const toast = context?.$toast;

const props = defineProps<{
  openModal: {
    add: boolean,
    set: boolean
  },
  oldData: HabitEntity,
}>()

const emit = defineEmits(['close'])

const onSubmit = async () => {
  console.log(formData.value)
  if (!formData.value.name) {
    toast?.error('名称不能为空');
    return;
  }
  preProcFormData(formData.value);
  let result = await saveHabit(formData.value);
  if (result.success) {
    toast?.success(result.message);
  } else {
    toast?.error(result.message);
  }
  emit('close', result.success)
}

const onDelete = async () => {
  const result = await  deleteHabit(formData.value);
  if (result.success) {
    toast?.success("删除成功✌️")
  } else {
    toast?.error(result.message)
  }
  emit('close', result.success)
}

const onCancel = () => {
  emit("close")
}

const defaultData: HabitEntity = {
  doneIcon: '✅',
  failIcon: '❌'
}
const formData = ref<HabitEntity>({...props.oldData, ...defaultData})
// 监测oldData变化
watch(props, (newVal, _) => {
  formData.value = { ...newVal.oldData, ...defaultData }
  if (props.openModal.add) {
    formData.value = { ...defaultData }
  }
})
</script>

<template>
  <div>
    <SaveModal name="习惯" :open-modal="openModal" @on-delete="onDelete" @on-submit="onSubmit" @on-close="onCancel">
      <template #form>
        <FormItem label="名称">
          <FormInput v-model="formData.name" placeholder="习惯名称"/>
        </FormItem>
        <FormItem label="描述">
          <FormInput v-model="formData.description" placeholder="习惯描述" type="textarea"/>
        </FormItem>

        <FormItem label="完成图标-失败图标">
          <template #label>
            <a href="https://www.emojiall.com/zh-hans" class="text-blue-400 hover:text-blue-500"
               target="_blank">更多Emoji图标</a>
          </template>
          <FormInput v-model="formData.doneIcon" placeholder="完成状态下显示在日历上的图标" width="1/2"/>
          <FormInput v-model="formData.failIcon" placeholder="失败状态下显示在日历上的图标" width="1/2"/>
        </FormItem>
        <FormItem label="频率">
          <FormInput v-model.number="formData.frequency" placeholder="单位：天"/>
        </FormItem>
        <FormItem label="开始-结束">
          <FormInput v-model="formData.startDate" placeholder="习惯开始日期" width="1/3" type="date"/>
          <span class="mx-2">--</span>
          <FormInput v-model="formData.endDate" placeholder="习惯结束日期" width="1/3" type="date"/>
        </FormItem>
      </template>
    </SaveModal>
  </div>
</template>