<script setup lang="ts">
import { ref, watch, getCurrentInstance } from 'vue';
import { TimelineEntity } from '../../interface/timeline.ts';
import { saveTimeline } from '../../api/timelineApi.ts';
import SaveModal, { OpenModal } from '../../components/SaveModal.vue';
import FormItem from '../../components/form/FormItem.vue';
import FormInput from '../../components/form/FormInput.vue';
import FormRadio, { Option } from '../../components/form/FormRadio.vue';
import { preProcFormData } from '../../utils/commUtil.ts';

const context = getCurrentInstance()?.appContext.config.globalProperties;
const toast = context?.$toast;

const props = defineProps<{
  openModal: OpenModal;
  oldData: TimelineEntity;
}>();

const emit = defineEmits<{
  close: [success?: boolean];
}>();

// 默认数据
const defaultData: TimelineEntity = {
  priority: 'medium',
  isPublic: false,
  eventDate: new Date().toISOString().split('T')[0], // 默认今天
};

const formData = ref<TimelineEntity>({ ...props.oldData, ...defaultData });

// 优先级选项
const priorityOptions: Option[] = [
  { name: '高', value: 'high' },
  { name: '中', value: 'medium' },
  { name: '低', value: 'low' }
];

// 常用分类选项
const categoryOptions = [
  '工作', '学习', '生活', '旅行', '健康', '家庭', 
  '朋友', '成就', '纪念', '其他'
];

// 表单验证
const validateForm = () => {
  if (!formData.value.title?.trim()) {
    toast?.error('事件标题不能为空');
    return false;
  }
  
  if (!formData.value.eventDate) {
    toast?.error('事件日期不能为空');
    return false;
  }
  
  // 验证日期格式
  const datePattern = /^\d{4}-\d{2}-\d{2}$/;
  if (!datePattern.test(formData.value.eventDate)) {
    toast?.error('日期格式不正确');
    return false;
  }
  
  // 验证时间格式（如果有）
  if (formData.value.eventTime) {
    const timePattern = /^\d{2}:\d{2}$/;
    if (!timePattern.test(formData.value.eventTime)) {
      toast?.error('时间格式不正确（HH:mm）');
      return false;
    }
  }
  
  return true;
};

// 提交表单
const onSubmit = async () => {
  if (!validateForm()) return;
  
  try {
    // 处理表单数据
    const submitData = { ...formData.value };
    
    // 清理空字段
    preProcFormData(submitData);
    
    // 处理标签（去重和格式化）
    if (submitData.tags) {
      const tags = submitData.tags.split(',')
        .map(tag => tag.trim())
        .filter(tag => tag)
        .filter((tag, index, arr) => arr.indexOf(tag) === index);
      submitData.tags = tags.join(',');
    }
    
    const result = await saveTimeline(submitData);
    
    if (result.success) {
      toast?.success(props.openModal.add ? '创建成功' : '更新成功');
      emit('close', true);
    } else {
      toast?.error(result.message);
    }
  } catch (error) {
    toast?.error('操作失败');
  }
};

// 取消操作
const onCancel = () => {
  emit('close');
};

// 监听 props 变化
watch(() => props.oldData, (newData) => {
  if (props.openModal.add) {
    formData.value = { ...defaultData };
  } else {
    formData.value = { ...newData };
  }
}, { deep: true });

// 监听模态框开关
watch(() => props.openModal, (newModal) => {
  if (newModal.add) {
    formData.value = { ...defaultData };
  } else if (newModal.set) {
    formData.value = { ...props.oldData };
  }
}, { deep: true });
</script>

<template>
  <div>
    <SaveModal 
      name="时间线事件" 
      :open-modal="openModal" 
      @on-submit="onSubmit" 
      @on-close="onCancel"
      :show-delete="false"
    >
      <template #form>
        <!-- 标题 -->
        <FormItem label="事件标题" required>
          <FormInput 
            v-model="formData.title" 
            placeholder="请输入事件标题"
            maxlength="200"
          />
        </FormItem>

        <!-- 描述 -->
        <FormItem label="事件描述">
          <FormInput 
            v-model="formData.description" 
            placeholder="请详细描述这个事件"
            type="textarea"
            rows="4"
          />
        </FormItem>

        <!-- 日期和时间 -->
        <FormItem label="事件日期" required>
          <FormInput 
            v-model="formData.eventDate" 
            type="date"
            width="1/2"
          />
          <FormInput 
            v-model="formData.eventTime" 
            type="time"
            placeholder="选择时间（可选）"
            width="1/2"
          />
        </FormItem>

        <!-- 分类 -->
        <FormItem label="事件分类">
          <div class="flex flex-wrap gap-2 mb-2">
            <button
              v-for="category in categoryOptions"
              :key="category"
              type="button"
              @click="formData.category = category"
              :class="[
                'px-3 py-1 rounded text-sm transition-colors',
                formData.category === category 
                  ? 'bg-blue-500 text-white' 
                  : 'bg-gray-600 text-gray-200 hover:bg-gray-500'
              ]"
            >
              {{ category }}
            </button>
          </div>
          <FormInput 
            v-model="formData.category" 
            placeholder="或输入自定义分类"
            maxlength="50"
          />
        </FormItem>

        <!-- 优先级 -->
        <FormItem label="优先级">
          <FormRadio 
            v-model="formData.priority" 
            :options="priorityOptions"
          />
        </FormItem>

        <!-- 标签 -->
        <FormItem label="标签">
          <FormInput 
            v-model="formData.tags" 
            placeholder="多个标签用逗号分隔，如：工作,重要,项目"
            maxlength="500"
          />
        </FormItem>

        <!-- 附件 -->
        <FormItem label="附件">
          <FormInput 
            v-model="formData.attachments" 
            placeholder="附件路径或链接，多个用逗号分隔"
            maxlength="500"
          />
        </FormItem>

        <!-- 公开设置 -->
        <FormItem label="公开设置">
          <div class="flex items-center gap-4">
            <label class="flex items-center gap-2 cursor-pointer">
              <input 
                type="checkbox" 
                v-model="formData.isPublic"
                class="rounded border-gray-600 bg-gray-700 text-blue-500"
              />
              <span>公开此事件</span>
            </label>
          </div>
          <p class="text-sm text-gray-500 mt-1">
            公开的事件可能会在时间线分享功能中显示给其他用户
          </p>
        </FormItem>
      </template>
    </SaveModal>
  </div>
</template>

<style scoped>
/* 自定义样式 */
</style>
