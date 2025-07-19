<script lang="ts" setup>
import { randomString } from '@/utils/commUtil';
import { ref } from 'vue';

// 添加展开状态管理
const expandedDescriptions = ref<Set<string>>(new Set());
const uid = ref(randomString())

defineProps<{
  text: string;
}>();

// 添加描述展开/收起方法
const toggleDescription = () => {
  if (expandedDescriptions.value.has(uid.value)) {
    expandedDescriptions.value.delete(uid.value);
  } else {
    expandedDescriptions.value.add(uid.value);
  }
};

const isDescriptionExpanded = () => {
  return expandedDescriptions.value.has(uid.value);
};

const getDisplayDescription = (description: string, maxLength = 150) => {
  if (!description) return '';
  if (description.length <= maxLength || isDescriptionExpanded()) {
    return description;
  }
  return description.substring(0, maxLength) + '...';
};

const needsExpansion = (description: string, maxLength = 150) => {
  return description && description.length > maxLength;
};
</script>

<template>
  <button v-if="needsExpansion(text, 150) && isDescriptionExpanded()" @click="toggleDescription()"
    class="inline-flex items-center gap-1 text-sm text-sky-400 hover:text-sky-300 transition-colors mt-3 px-2 py-1 rounded hover:bg-sky-900/20">
    {{ isDescriptionExpanded() ? '📖 收起' : '📑 展开更多' }}
  </button>
  <div class="relative">
    <p class="text-gray-400 leading-relaxed transition-all duration-300"
      :class="{ 'line-clamp-4': !isDescriptionExpanded() && needsExpansion(text, 150) }">
      {{ getDisplayDescription(text) }}
    </p>
    <!-- 渐变遮罩效果 -->
    <div v-if="!isDescriptionExpanded() && needsExpansion(text, 150)"
      class="absolute bottom-0 left-0 right-0 h-6 bg-gradient-to-t from-gray-800 to-transparent pointer-events-none">
    </div>
  </div>
  <button v-if="needsExpansion(text, 150)" @click="toggleDescription()"
    class="inline-flex items-center gap-1 text-sm text-sky-400 hover:text-sky-300 transition-colors mt-3 px-2 py-1 rounded hover:bg-sky-900/20">
    {{ isDescriptionExpanded() ? '📖 收起' : '📑 展开更多' }}
  </button>
</template>

<style scoped>
/* 文本截断样式 */
.line-clamp-4 {
  display: -webkit-box;
  line-clamp: 4;
  -webkit-box-orient: vertical;
  overflow: hidden;
  max-height: 6rem;
  /* 大约4行的高度 */
}

/* 平滑的高度过渡 */
.description-text {
  transition: max-height 0.3s ease-in-out;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .line-clamp-4 {
    line-clamp: 3;
    max-height: 4.5rem;
    /* 移动设备上显示3行 */
  }
}
</style>