<script setup lang="ts">
import {ref, watch} from 'vue'
import {saveAppSet, deleteAppSet} from "../../api/appSetApi";
import FormItem from "../../components/form/FormItem.vue";
import FormInput from "../../components/form/FormInput.vue";
import SaveModal from "../../components/SaveModal.vue";
import FormRadio from "../../components/form/FormRadio.vue";
import {useToast} from "@/components/toast";
import {AppSet} from "@/interface/appset.ts";
import { preProcFormData } from '@/utils/commUtil';

const toast = useToast();

const defaultFormData = {
  type: 1,
  name: '',
  description: '',
  port: '',
  category: '',
  outerUrl: '',
}


const props = defineProps<{
  openModal: {
    add: boolean,
    set: boolean
  },
  oldData: AppSet | any,
}>()
const emit = defineEmits(['close'])
const handleClose = (value: any) => {
  emit('close', value)
}

const submitForm = async () => {
  console.log(formData.value)
  if (!formData.value.name) {
    toast?.error('名称不能为空');
    return;
  }
  if (!formData.value.description) {
    formData.value.description = "我也不知道它是干嘛的";
  }
  // 处理formData
  preProcFormData(formData.value);
  
  let result = await saveAppSet(formData.value);
  if (result.success) {
    toast?.success(result.message);
    handleClose(true);
  } else {
    toast?.error(result.message);
  }
}

const del = async () => {
  if ((await deleteAppSet(formData.value)).success) {
    toast?.success("删除成功✌️")
    handleClose(true);
  }
}

// 监测oldData变化
watch(props, (newVal, _) => {
  formData.value = {...newVal.oldData}
})

const formData = ref<AppSet | any>(props.oldData ? {...props.oldData} : {...defaultFormData})
const options = [{value: 2, name: '应用'}, {value: 3, name: '书签'}];
</script>

<template>
  <SaveModal name="应用"
             :open-modal="props.openModal"
             :default-data="defaultFormData"
             @on-close="handleClose"
             @on-delete="del"
             @on-submit="submitForm"
  >
    <template #form>
      <FormItem label="类型">
        <FormRadio :options="options" v-model="formData.type"/>
      </FormItem>
      <FormItem label="名称">
        <FormInput v-model="formData.name" placeholder="应用名称"/>
      </FormItem>
      <FormItem label="描述">
        <FormInput type="textarea" v-model="formData.description" placeholder="应用描述"/>
      </FormItem>
      <FormItem label="端口号">
        <FormInput v-model="formData.port" placeholder="内部应用使用的端口"/>
      </FormItem>
      <FormItem label="外网地址">
        <FormInput v-model="formData.outerUrl" placeholder="在外网时的访问地址"/>
      </FormItem>
      <FormItem label="分类">
        <FormInput v-model="formData.category" placeholder="书签的分类"/>
      </FormItem>
      <FormItem label="图标">
        <template #label>
            <a href="https://www.iconfont.cn/" class="text-blue-400 hover:text-blue-500" target="_blank">更多svg</a>
        </template>
        <FormInput v-model="formData.icon" placeholder="支持svg图标"/>
      </FormItem>
    </template>
  </SaveModal>
</template>