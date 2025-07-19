<script setup lang="ts">
import { TimelineEntity } from '../../interface/timeline.ts';
import { parseAttachments } from '@/utils/commUtil.ts';

const props = defineProps<{
  timeline: TimelineEntity;
}>();

const emit = defineEmits<{
  edit: [timeline: TimelineEntity];
}>();

// 编辑时间线事件
const handleEdit = () => {
  emit('edit', props.timeline);
};

// 获取优先级颜色
const getPriorityColor = (priority?: string) => {
  switch (priority) {
    // case 'high': return 'border-red-500';
    // case 'medium': return 'border-yellow-500';
    // case 'low': return 'border-green-500';
    default: return 'border-gray-500';
  }
};

/* // 获取优先级文本
const getPriorityText = (priority?: string) => {
  switch (priority) {
    case 'high': return '高';
    case 'medium': return '中';
    case 'low': return '低';
    default: return '';
  }
};

// 获取优先级标签颜色
const getPriorityBadgeColor = (priority?: string) => {
  switch (priority) {
    case 'high': return 'bg-red-100 text-red-800';
    case 'medium': return 'bg-yellow-100 text-yellow-800';
    case 'low': return 'bg-green-100 text-green-800';
    default: return 'bg-gray-100 text-gray-800';
  }
}; */

// 解析标签
const parseTags = (tags?: string) => {
  if (!tags) return [];
  return tags.split(',').map(tag => tag.trim()).filter(tag => tag);
};

// 格式化日期时间
const formatDateTime = (date?: string, time?: string) => {
  if (!date) return '';
  const dateStr = new Date(date).toLocaleDateString('zh-CN');
  return time ? `${dateStr} ${time}` : dateStr;
};
</script>

<template>
  <div class="group relative bg-gray-800 rounded-lg p-4 border-l-4 transition-all duration-200 hover:bg-gray-750"
    :class="getPriorityColor(timeline.priority)">


    <div class="flex gap-2 flex-col justify-between h-full">


      <!-- 头部信息 -->
      <div class="flex flex-col gap-2">
        <div class="flex justify-between items-center">

          <!-- 操作按钮 -->
          <div class="hidden absolute top-5 right-3 group-hover:block ">
            <div class="flex gap-2">
              <button @click="handleEdit" class="text-blue-400 hover:text-blue-300 transition-colors">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z">
                  </path>
                </svg>
              </button>
            </div>
          </div>

          <h3 class="text-lg font-semibold text-white">{{ timeline.title }}</h3>

        </div>

        <!-- 事件日期 -->
        <div class="flex items-center gap-4 text-sm text-gray-400">
          <span>📅 {{ formatDateTime(timeline.eventDate, timeline.eventTime) }}</span>
          <span v-if="timeline.category" class="flex items-center gap-1">
            🏷️ {{ timeline.category }}
          </span>
          <!-- <span v-if="timeline.isPublic" class="flex items-center gap-1">
            🌐 公开
          </span>
          <span v-else="timeline.isPublic" class="flex items-center gap-1">
            🔒 私密
          </span> -->
        </div>
      </div>

      <!-- <div class="flex items-center gap-2 mb-1">
        <span v-if="timeline.priority" class="px-2 py-1 rounded text-xs font-medium"
            :class="getPriorityBadgeColor(timeline.priority)">
            {{ getPriorityText(timeline.priority) }}
          </span>
      </div> -->



      <!-- 描述内容 -->
      <div>
        <template v-if="timeline.description">
          <p class="text-gray-300 leading-relaxed break-all whitespace-pre-line max-w-[250px] line-clamp-2">
            {{ timeline.description.length > 50 ? timeline.description.substring(0, 50) + '...' : timeline.description
            }}
          </p>
        </template>
        <template v-else>
          <p class="text-gray-300 leading-relaxed">暂无描述</p>
        </template>
      </div>

      <div class="flex flex-col gap-2">
        <!-- 标签 -->
        <div class="flex flex-wrap gap-2">
          <span v-if="timeline.attachments" class="flex items-center gap-1 text-blue-400">
            📃 {{ parseAttachments(timeline)?.length }} 个附件
          </span>
          <template v-if="timeline.tags">
            <span v-for="tag in parseTags(timeline.tags)" :key="tag"
              class="px-2 py-1 bg-blue-900 text-blue-200 text-xs rounded-full">
              # {{ tag }}
            </span>
          </template>
          <template v-else>
            <span class="px-2 py-1 bg-gray-900 text-blue-200 text-xs rounded-full">未设置标签</span>
          </template>
        </div>

        <!-- 时间信息 -->
        <div class="flex justify-between items-center text-xs text-gray-500 gap-2">
          <span v-if="timeline.createTime">
            创建于 {{ formatDateTime(timeline.createTime) }}
          </span>
          <span v-if="timeline.updateTime && timeline.updateTime !== timeline.createTime">
            更新于 {{ formatDateTime(timeline.updateTime) }}
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.bg-gray-750 {
  background-color: #374151;
}
</style>