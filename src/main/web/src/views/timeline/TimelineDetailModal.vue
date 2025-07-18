<template>
  <div v-if="open" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
    <div class="bg-gray-800 rounded-2xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">
      <!-- 模态框头部 -->
      <div class="flex items-center justify-between p-6 border-b border-gray-700">
        <h2 class="text-2xl font-bold text-white">事件详情</h2>
        <button @click="closeModal" 
                class="text-gray-400 hover:text-white transition-colors">
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
          </svg>
        </button>
      </div>
      
      <!-- 模态框内容 -->
      <div class="p-6 space-y-6">
        <!-- 标题和优先级 -->
        <div class="flex items-start justify-between">
          <div class="flex-1">
            <h3 class="text-xl font-bold text-white mb-2">{{ timeline.title }}</h3>
            <div class="flex items-center gap-4 text-sm text-gray-400">
              <span class="flex items-center gap-1">
                📅 {{ formatDate(timeline.eventDate) }}
              </span>
              <span v-if="timeline.eventTime" class="flex items-center gap-1">
                ⏰ {{ timeline.eventTime }}
              </span>
            </div>
          </div>
          
          <div v-if="timeline.priority" 
               class="flex items-center gap-1 px-3 py-1 rounded-full text-sm font-medium"
               :class="getPriorityBadgeColor(timeline.priority)">
            {{ getPriorityIcon(timeline.priority) }}
            {{ getPriorityText(timeline.priority) }}
          </div>
        </div>
        
        <!-- 描述 -->
        <div v-if="timeline.description" class="bg-gray-700/50 rounded-xl p-4">
          <h4 class="text-lg font-semibold text-white mb-3">描述</h4>
          <p class="text-gray-300 leading-relaxed whitespace-pre-wrap">{{ timeline.description }}</p>
        </div>
        
        <!-- 分类和标签 -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div v-if="timeline.category" class="bg-gray-700/50 rounded-xl p-4">
            <h4 class="text-lg font-semibold text-white mb-3">分类</h4>
            <span class="inline-flex items-center gap-1 px-3 py-1 bg-sky-900/50 text-sky-300 text-sm rounded-full border border-sky-800">
              🏷️ {{ timeline.category }}
            </span>
          </div>
          
          <div v-if="timeline.tags" class="bg-gray-700/50 rounded-xl p-4">
            <h4 class="text-lg font-semibold text-white mb-3">标签</h4>
            <div class="flex flex-wrap gap-2">
              <span v-for="tag in parseTags(timeline.tags)" 
                    :key="tag"
                    class="px-3 py-1 bg-purple-900/50 text-purple-300 text-sm rounded-full border border-purple-800">
                # {{ tag }}
              </span>
            </div>
          </div>
        </div>
        
        <!-- 附件信息 -->
        <div v-if="timeline.attachments" class="bg-gray-700/50 rounded-xl p-4">
          <h4 class="text-lg font-semibold text-white mb-3">附件</h4>
          <div class="flex items-center gap-2 text-blue-400">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.172 7l-6.586 6.586a2 2 0 102.828 2.828l6.414-6.586a4 4 0 00-5.656-5.656l-6.415 6.585a6 6 0 108.486 8.486L20.5 13"></path>
            </svg>
            <span>{{ timeline.attachments }}</span>
          </div>
        </div>
        
        <!-- 状态信息 -->
        <div class="bg-gray-700/50 rounded-xl p-4">
          <h4 class="text-lg font-semibold text-white mb-3">状态信息</h4>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
            <div>
              <span class="text-gray-400">可见性:</span>
              <span class="ml-2 text-green-400" v-if="timeline.isPublic">🌐 公开</span>
              <span class="ml-2 text-orange-400" v-else>🔒 私有</span>
            </div>
            <div v-if="timeline.createTime">
              <span class="text-gray-400">创建时间:</span>
              <span class="ml-2 text-gray-300">{{ formatDateTime(timeline.createTime) }}</span>
            </div>
            <div v-if="timeline.updateTime && timeline.updateTime !== timeline.createTime">
              <span class="text-gray-400">更新时间:</span>
              <span class="ml-2 text-gray-300">{{ formatDateTime(timeline.updateTime) }}</span>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 模态框底部 -->
      <div class="flex justify-end gap-3 p-6 border-t border-gray-700">
        <button @click="closeModal" 
                class="px-4 py-2 bg-gray-600 hover:bg-gray-500 text-white rounded-lg transition-colors">
          关闭
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { TimelineEntity } from '../../interface/timeline';

defineProps<{
  open: boolean;
  timeline: TimelineEntity;
}>();

const emit = defineEmits<{
  close: [];
}>();

const closeModal = () => {
  emit('close');
};

// 工具方法
const getPriorityBadgeColor = (priority?: string) => {
  switch (priority) {
    case 'high': return 'bg-red-900/50 text-red-300 border border-red-800';
    case 'medium': return 'bg-yellow-900/50 text-yellow-300 border border-yellow-800';
    case 'low': return 'bg-green-900/50 text-green-300 border border-green-800';
    default: return 'bg-gray-900/50 text-gray-300 border border-gray-800';
  }
};

const getPriorityIcon = (priority?: string) => {
  switch (priority) {
    case 'high': return '🔴';
    case 'medium': return '🟡';
    case 'low': return '🟢';
    default: return '⚪';
  }
};

const getPriorityText = (priority?: string) => {
  switch (priority) {
    case 'high': return '高';
    case 'medium': return '中';
    case 'low': return '低';
    default: return '';
  }
};

const parseTags = (tags?: string) => {
  if (!tags) return [];
  return tags.split(',').map(tag => tag.trim()).filter(tag => tag);
};

const formatDate = (date?: string) => {
  if (!date) return '';
  return new Date(date).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
};

const formatDateTime = (dateTime?: string) => {
  if (!dateTime) return '';
  return new Date(dateTime).toLocaleString('zh-CN');
};
</script>

<style scoped>
/* 模态框动画 */
.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.3s ease;
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}
</style> 