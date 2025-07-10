<script setup lang="ts">
import { ref, watch } from 'vue'
import { getCurrentInstance } from "vue";
import {deleteHabitLog, saveHabitLog} from "../../api/habitApi";
import SaveModal from "../../components/SaveModal.vue";
import FormItem from "../../components/form/FormItem.vue";
import FormInput from "../../components/form/FormInput.vue";
import {HabitEntity, HabitLogEntity} from "../../interface/habit.ts";
import { preProcFormData } from '@/utils/commUtil.ts';

const context = getCurrentInstance()?.appContext.config.globalProperties;
const toast = context?.$toast;

const props = defineProps<{
  openModal: {
    add: boolean,
    set: boolean
  },
  oldData: HabitLogEntity,
  habit: HabitEntity
}>()
const emit = defineEmits<{
  (e: 'close', { }): void
}>()
const handleClose = (value: any) => {
  emit('close', value)
}

const handleStatus = async (done: boolean) => {
  const habitLog = {
    ...formData.value,
    done,
    extra: JSON.stringify({
      remark: formData.value.extra,
    })
  }
  console.log(habitLog)
  preProcFormData(habitLog);
  let result = await saveHabitLog(habitLog);
  if (result.success) {
    toast?.success(result.message);
    handleClose(true);
  } else {
    toast?.error(result.message);
  }
}

const handleDel = async () => {
  const result = await deleteHabitLog(formData.value);
  if (result.success) {
    toast?.success("删除成功✌️")
    handleClose(true);
  } else {
    toast?.error(result.message)
  }
}

// 监测oldData变化
watch(props, (newVal, _) => {
  formData.value = { ...newVal.oldData }
})

const formData = ref({ ...props.oldData } )
</script>

<template>
  <SaveModal name="习惯追踪记录" :open-modal="props.openModal"
             @on-submit="handleStatus(true)" confirm-text="标记完成"
             @on-cancel="handleStatus(false)" cancel-text="标记失败"
             @on-close="handleClose"
             @on-delete="handleDel">
    <template #form>
      <FormItem label="备注">
        <FormInput type="textarea" v-model="formData.extra" placeholder="备注"/>
      </FormItem>
    </template>
  </SaveModal>
</template>

<style scoped></style>