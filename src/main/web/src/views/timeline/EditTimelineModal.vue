<script setup lang="ts">
import { ref, watch, getCurrentInstance } from 'vue';
import { TimelineEntity } from '../../interface/timeline';
import { deleteTimeline, saveTimeline } from '../../api/timelineApi';
import SaveModal, { OpenModal } from '../../components/SaveModal.vue';
import FormItem from '../../components/form/FormItem.vue';
import FormInput from '../../components/form/FormInput.vue';
// import FormRadio, { Option } from '../../components/form/FormRadio.vue';
import { preProcFormData } from '../../utils/commUtil';
import { uploadFiles, FileInfo, deleteFile, getFileListInIds } from '../../api/fileApi';
import { VueDraggableNext as Draggable } from 'vue-draggable-next'

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

// 文件上传相关状态
const selectedFiles = ref<File[]>([]);
const uploadedFiles = ref<FileInfo[]>([]);
const uploading = ref(false);

// 优先级选项
// const priorityOptions: Option[] = [
//   { name: '高', value: 'high' },
//   { name: '中', value: 'medium' },
//   { name: '低', value: 'low' }
// ];

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

// 文件选择处理
const handleFileSelect = (event: Event) => {
  const target = event.target as HTMLInputElement;
  if (target.files) {
    selectedFiles.value = Array.from(target.files);
  }
};

// 上传文件
const uploadFileList = async () => {
  if (selectedFiles.value.length === 0) return;

  uploading.value = true;
  try {
    const formData = new FormData();
    selectedFiles.value.forEach(file => {
      formData.append('files', file);
    });
    formData.append("description", "upload files");

    // console.log("upload", formData, selectedFiles.value)

    const result = await uploadFiles(formData);
    if (result.success) {
      uploadedFiles.value = [...uploadedFiles.value, ...result.data.success];
      const failedFiles: string[] = result.data?.failed || [];
      // 过滤掉失败的文件
      selectedFiles.value = selectedFiles.value.filter(file => failedFiles.some(name => name === file.name));
      toast?.success(result.message);
    } else {
      toast?.error(result.message);
    }
  } catch (error) {
    console.error('文件上传失败:', error);
    toast?.error('文件上传失败:' + error);
  } finally {
    uploading.value = false;
  }
};

// 删除已上传的文件
const removeUploadedFile = async (fileInfo: FileInfo) => {
  try {
    const result = await deleteFile(fileInfo.id);
    if (result.success) {
      uploadedFiles.value = uploadedFiles.value.filter(f => f.id !== fileInfo.id);
      toast?.success('文件删除成功');
    } else {
      toast?.error(result.message);
    }
  } catch (error) {
    toast?.error('文件删除失败');
  }
};

// 删除选中的文件
const removeSelectedFile = (index: number) => {
  selectedFiles.value.splice(index, 1);
};

const changeUploadedFilesOrder = (_oldIndex: number, _newIndex: number) => {
  // const file = uploadedFiles.value[oldIndex];
  // uploadedFiles.value.splice(oldIndex, 1);
  // uploadedFiles.value.splice(newIndex, 0, file);
  // uploadedFiles.value.forEach((file, index) => {
  //   console.log(index, file.id)
  // });
};

// 格式化文件大小
const formatFileSize = (bytes: number): string => {
  if (bytes === 0) return '0 B';
  const k = 1024;
  const sizes = ['B', 'KB', 'MB', 'GB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
};

// 解析已存在的附件
const parseExistingAttachments = async () => {
  if (!formData.value.attachments) return;

  const fileIds = formData.value.attachments.split(',').map(id => parseInt(id.trim())).filter(id => !isNaN(id));

  try {
    const result = await getFileListInIds(fileIds);
    if (result.success) {
      fileIds.forEach(id => {
        if (uploadedFiles.value.some(f => f.id === id)) return;
        const file = result.data.find((f: FileInfo) => f.id === id);
        if (file) uploadedFiles.value.push(file);
      });
    }
  } catch (error) {
    console.error('获取文件信息失败:', error);
  }
};

// 提交表单
const onSubmit = async () => {

  if (!validateForm()) return;

  try {
    // 处理表单数据
    const submitData = { ...formData.value };

    // 处理附件：将上传的文件ID以逗号分隔的形式保存
    if (uploadedFiles.value.length > 0) {
      submitData.attachments = uploadedFiles.value.map(file => file.id).join(',');
    }

    // 清理空字段
    preProcFormData(submitData);

    // return;
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

const onDelete = async () => {
  if (!props.oldData.id) return;
  const result = await deleteTimeline(props.oldData.id);
  if (result.success) {
    toast?.success("删除成功✌️")
  } else {
    toast?.error(result.message)
  }
  emit('close', result.success)
};

// 取消操作
const onCancel = () => {
  emit('close');
};

// 监听 props 变化
watch(() => props.oldData, (newData) => {
  if (props.openModal.add) {
    formData.value = { ...defaultData };
    // 重置文件状态
    selectedFiles.value = [];
    uploadedFiles.value = [];
  } else {
    formData.value = { ...newData };
    // 解析已存在的附件
    uploadedFiles.value = [];
    parseExistingAttachments();
  }
}, { deep: true });

// 监听模态框开关
watch(() => props.openModal, (newModal) => {
  if (newModal.add) {
    formData.value = { ...defaultData };
    // 重置文件状态
    selectedFiles.value = [];
    uploadedFiles.value = [];
  } else if (newModal.set) {
    formData.value = { ...props.oldData };
    // 解析已存在的附件
    uploadedFiles.value = [];
    parseExistingAttachments();
  }
}, { deep: true });
</script>

<template>
  <div>
    <SaveModal name="时间线事件" :open-modal="openModal" @on-submit="onSubmit" @on-close="onCancel" @on-delete="onDelete"
      :show-delete="false">
      <template #form>
        <!-- 标题 -->
        <FormItem label="事件标题" required>
          <FormInput v-model="formData.title" placeholder="请输入事件标题" maxlength="200" />
        </FormItem>

        <!-- 描述 -->
        <FormItem label="事件描述">
          <FormInput v-model="formData.description" placeholder="请详细描述这个事件" type="textarea" rows="4" />
        </FormItem>

        <!-- 日期和时间 -->
        <FormItem label="事件日期" required>
          <FormInput v-model="formData.eventDate" type="date" width="1/2" />
          <FormInput v-model="formData.eventTime" type="time" placeholder="选择时间（可选）" width="1/2" />
        </FormItem>

        <!-- 分类 -->
        <FormItem label="事件分类">
          <div class="flex flex-wrap gap-2 mb-2">
            <button v-for="category in categoryOptions" :key="category" type="button"
              @click="formData.category = category" :class="[
                'px-3 py-1 rounded text-sm transition-colors',
                formData.category === category
                  ? 'bg-blue-500 text-white'
                  : 'bg-gray-600 text-gray-200 hover:bg-gray-500'
              ]">
              {{ category }}
            </button>
          </div>
          <FormInput v-model="formData.category" placeholder="或输入自定义分类" maxlength="50" />
        </FormItem>

        <!-- 优先级 -->
        <!-- <FormItem label="优先级">
          <FormRadio v-model="formData.priority" :options="priorityOptions" />
        </FormItem> -->

        <!-- 标签 -->
        <FormItem label="标签">
          <FormInput v-model="formData.tags" placeholder="多个标签用逗号分隔，如：工作,重要,项目" maxlength="500" />
        </FormItem>

        <!-- 附件 -->
        <FormItem label="附件">
          <!-- 文件选择 -->
          <div class="mb-4">
            <input type="file" multiple @change="handleFileSelect"
              class="block w-full text-sm text-gray-400 file:mr-4 file:py-2 file:px-4 file:rounded-md file:border-0 file:text-sm file:font-medium file:bg-blue-500 file:text-white hover:file:bg-blue-600"
              accept="*/*" />
          </div>

          <!-- 选中的文件列表 -->
          <div v-if="selectedFiles.length > 0" class="mb-4">
            <div class="flex items-center justify-between mb-2">
              <span class="text-sm font-medium text-gray-300">待上传文件：</span>
              <button type="button" @click="uploadFileList" :disabled="uploading"
                class="px-3 py-1 bg-blue-500 text-white rounded text-sm hover:bg-blue-600 disabled:opacity-50">
                {{ uploading ? '上传中...' : '上传文件' }}
              </button>
            </div>
            <div class="space-y-2">
              <div v-for="(file, index) in selectedFiles" :key="index"
                class="flex items-center justify-between p-2 bg-gray-700 rounded">
                <div class="flex items-center">
                  <span class="text-sm text-gray-300">{{ file.name }}</span>
                  <span class="ml-2 text-xs text-gray-500">({{ formatFileSize(file.size) }})</span>
                </div>
                <button type="button" @click="removeSelectedFile(index)" class="text-red-400 hover:text-red-300">
                  ×
                </button>
              </div>
            </div>
          </div>

          <!-- 已上传的文件列表 -->
          <div v-if="uploadedFiles.length > 0" class="mb-4">
            <span class="text-sm font-medium text-gray-300">已上传文件：</span>
            <div class="space-y-2 mt-2">


              <Draggable :list="uploadedFiles" class="flex flex-col gap-2 items-start w-full" @change="changeUploadedFilesOrder">
                <transition-group>
                  <div v-for="file in uploadedFiles" :key="file.id"
                    class="flex w-full items-center justify-between p-2 bg-gray-700 rounded cursor-pointer">
                    <div class="flex items-center">
                      <span class="text-sm text-gray-300">{{ file.originalFileName }}</span>
                      <span class="ml-2 text-xs text-gray-500">({{ formatFileSize(file.fileSize) }})</span>
                    </div>
                    <button type="button" @click="removeUploadedFile(file)" class="text-red-400 hover:text-red-300">
                      删除
                    </button>
                  </div>
                </transition-group>
              </Draggable>


            </div>
          </div>

          <p class="text-xs text-gray-500 mt-2">
            支持多文件上传，文件将以逗号分隔的ID形式保存
          </p>
        </FormItem>

        <!-- 公开设置 -->
        <!-- <FormItem label="公开设置">
          <div class="flex items-center gap-4">
            <label class="flex items-center gap-2 cursor-pointer">
              <input type="checkbox" v-model="formData.isPublic"
                class="rounded border-gray-600 bg-gray-700 text-blue-500" />
              <span>公开此事件</span>
            </label>
          </div>
          <p class="text-sm text-gray-500 mt-1">
            公开的事件可能会在时间线分享功能中显示给其他用户
          </p>
        </FormItem> -->
      </template>
    </SaveModal>
  </div>
</template>

<style scoped>
/* 自定义样式 */
</style>
