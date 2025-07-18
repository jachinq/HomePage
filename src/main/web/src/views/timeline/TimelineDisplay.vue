<template>
  <div class="min-h-screen bg-gradient-to-br from-gray-900 via-gray-800 to-gray-900 p-4">
    <div class="max-w-6xl mx-auto">
      <!-- 头部区域 -->
      <div class="text-center mb-12">
        <h1 class="text-4xl font-bold text-white mb-4">
          ✨ 时间线展示
        </h1>
        <p class="text-gray-400 text-lg">记录人生中的重要时刻</p>

        <!-- 统计信息 -->
        <div v-if="statistics" class="flex justify-center gap-8 mt-8">
          <div class="text-center">
            <div class="text-3xl font-bold text-sky-400">{{ statistics.total }}</div>
            <div class="text-gray-400 text-sm">总事件</div>
          </div>
          <div class="text-center">
            <div class="text-3xl font-bold text-emerald-400">{{ statistics.categoryCount }}</div>
            <div class="text-gray-400 text-sm">分类数</div>
          </div>
          <div class="text-center">
            <div class="text-3xl font-bold text-purple-400">{{ completedCount }}</div>
            <div class="text-gray-400 text-sm">已完成</div>
          </div>
        </div>
      </div>

      <!-- 过滤器区域 -->
      <div class="bg-gray-800/50 backdrop-blur-sm rounded-2xl p-6 mb-16 border border-gray-700">
        <div class="flex flex-wrap gap-4 items-center justify-center">
          <div class="flex items-center gap-2">
            <label class="text-gray-300 text-sm">年份:</label>
            <select v-model="selectedYear"
              class="bg-gray-700 text-white px-4 py-2 rounded-lg border border-gray-600 focus:border-sky-500 focus:outline-none transition-colors">
              <option value="">全部年份</option>
              <option v-for="year in years" :key="year" :value="year">
                {{ year }}
              </option>
            </select>
          </div>
          <div class="flex items-center gap-2">
            <label class="text-gray-300 text-sm">分类:</label>
            <select v-model="selectedCategory"
              class="bg-gray-700 text-white px-4 py-2 rounded-lg border border-gray-600 focus:border-sky-500 focus:outline-none transition-colors">
              <option value="">全部分类</option>
              <option v-for="category in categories" :key="category" :value="category">
                {{ category }}
              </option>
            </select>
          </div>

          <div class="flex items-center gap-2">
            <label class="text-gray-300 text-sm">优先级:</label>
            <select v-model="selectedPriority"
              class="bg-gray-700 text-white px-4 py-2 rounded-lg border border-gray-600 focus:border-sky-500 focus:outline-none transition-colors">
              <option value="">全部优先级</option>
              <option value="high">🔴 高</option>
              <option value="medium">🟡 中</option>
              <option value="low">🟢 低</option>
            </select>
          </div>

          <div class="flex items-center gap-2">
            <label class="text-gray-300 text-sm">排序:</label>
            <select v-model="sortBy"
              class="bg-gray-700 text-white px-4 py-2 rounded-lg border border-gray-600 focus:border-sky-500 focus:outline-none transition-colors">
              <option value="eventDate">按事件日期</option>
              <option value="createTime">按创建时间</option>
              <option value="priority">按优先级</option>
            </select>
          </div>

          <button @click="toggleSortDirection"
            class="bg-gray-700 hover:bg-gray-600 text-white px-4 py-2 rounded-lg border border-gray-600 transition-colors">
            {{ sortDirection === 'asc' ? '📈 升序' : '📉 降序' }}
          </button>


          <button @click="showTimelineManager"
            class="bg-gray-700 hover:bg-gray-600 text-white px-4 py-2 rounded-lg border border-gray-600 transition-colors">
            管理时间线
          </button>


        </div>
      </div>

      <!-- 时间线容器 -->
      <div class="relative">
        <!-- 垂直时间线 -->
        <div v-if="filteredAndSortedTimelines.length > 0"
          class="absolute left-1/2 transform -translate-x-1/2 h-full w-1 bg-gradient-to-b from-sky-500 via-purple-500 to-pink-500 rounded-full">
        </div>

        <!-- 时间线事件 -->
        <div v-if="filteredAndSortedTimelines.length === 0" class="text-center py-20">
          <div class="text-6xl mb-4">📅</div>
          <p class="text-gray-400 text-xl">暂无时间线事件</p>
          <!-- <p class="text-gray-500 text-sm mt-2">开始记录您的重要时刻吧</p> -->
        </div>

        <div v-else class="space-y-16">
          <div v-for="(timeline, index) in filteredAndSortedTimelines" :key="timeline.id"
            class="relative justify-center" :class="index % 2 === 1 ? '' : 'md:text-right'">


            <!-- 事件卡片 -->
            <div class="timeline-card group" :class="index % 2 === 0 ? 'md:mr-[51%]' : 'md:ml-[51%]'"
              @click="selectTimeline(timeline)">

              <!-- 时间线节点 -->
              <div
                class="absolute left-1/2 translate-y-[-26px] transform -translate-x-1/2 w-6 h-6 rounded-full border-4 border-white shadow-lg z-10 group-hover:translate-y-[-16px] group-hover:shadow-xl transition-all duration-300"
                :class="getPriorityNodeColor(timeline.priority)">
              </div>

              <!-- 卡片内容 -->
              <div
                class="bg-gray-800/70 backdrop-blur-sm rounded-2xl p-6 border border-gray-700 hover:border-sky-500 transition-all duration-300 shadow-lg hover:shadow-xl transform hover:scale-105 cursor-pointer">


                <!-- 卡片头部 -->
                <div class="flex items-center justify-between mb-4">
                  <!-- 优先级标签 -->
                  <div v-if="timeline.priority && index % 2 === 0"
                    class="flex items-center gap-1 px-3 py-1 rounded-full text-xs font-medium"
                    :class="getPriorityBadgeColor(timeline.priority)">
                    {{ getPriorityIcon(timeline.priority) }}
                    {{ getPriorityText(timeline.priority) }}
                  </div>

                  <div class="flex items-center  gap-3 text-2xl font-bold text-gray-100  group-hover:underline">
                    <span class="flex items-center gap-1">
                      📅 {{ formatDate(timeline.eventDate) }}
                    </span>
                    <span v-if="timeline.eventTime" class="flex items-center gap-1">
                      ⏰ {{ timeline.eventTime }}
                    </span>
                  </div>


                  <!-- 优先级标签 -->
                  <div v-if="timeline.priority && index % 2 === 1"
                    class="flex items-center gap-1 px-3 py-1 rounded-full text-xs font-medium"
                    :class="getPriorityBadgeColor(timeline.priority)">
                    {{ getPriorityIcon(timeline.priority) }}
                    {{ getPriorityText(timeline.priority) }}
                  </div>
                </div>

                <!-- 事件标题 -->
                <h3 class="text-xl font-bold text-white mb-2 group-hover:text-sky-400 transition-colors">
                  {{ timeline.title }}
                </h3>

                <!-- 描述内容 -->
                <div v-if="timeline.description" class="mb-4">
                  <p class="text-gray-400 leading-relaxed">
                    {{ timeline.description }}
                  </p>
                </div>

                <!-- 分类和标签 -->
                <div class="flex flex-wrap gap-2 justify-between">
                  <div class="flex flex-wrap gap-2">
                    <span v-if="timeline.category"
                      class="px-3 py-1 bg-sky-900/50 text-sky-300 text-xs rounded-full border border-sky-800">
                      🏷️ {{ timeline.category }}
                    </span>

                    <span v-for="tag in parseTags(timeline.tags)" :key="tag"
                      class="px-3 py-1 bg-purple-900/50 text-purple-300 text-xs rounded-full border border-purple-800">
                      # {{ tag }}
                    </span>
                  </div>

                  <div class="flex items-center gap-2">
                    <span v-if="timeline.isPublic" class="flex items-center gap-1 text-green-400">
                      🌐 公开
                    </span>
                    <span v-if="timeline.attachments" class="flex items-center gap-1 text-blue-400">
                      📎 附件
                    </span>
                  </div>
                </div>

              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 返回按钮 -->
      <div class="absolute top-8 right-20">
        <BackToHome />
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-gray-800 rounded-2xl p-8 text-center">
        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-sky-500 mx-auto mb-4"></div>
        <p class="text-white">加载中...</p>
      </div>
    </div>

    <!-- 详情模态框 -->
    <FilePreview :visible="showDetailModal" :fileIds="parseAttachments(selectedTimelineData)"
      @close="closeDetailModal" />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { TimelineEntity, TimelineStatistics } from '../../interface/timeline';
import { getTimelineList, getTimelineStatistics, getTimelineCategories } from '../../api/timelineApi';
import BackToHome from '../../components/BackToHome.vue';
import router from '@/router';
import FilePreview from '@/components/FilePreview.vue';
import { useToast } from '@/components/toast';

// 响应式数据
const timelines = ref<TimelineEntity[]>([]);
const years = ref<string[]>([]);
const categories = ref<string[]>([]);
const statistics = ref<TimelineStatistics | null>(null);
const loading = ref(false);
const selectedYear = ref('');
const selectedCategory = ref('');
const selectedPriority = ref('');
const sortBy = ref('eventDate');
const sortDirection = ref<'asc' | 'desc'>('desc');
const showDetailModal = ref(false);
const selectedTimelineData = ref<TimelineEntity>({});

// 计算属性
const filteredAndSortedTimelines = computed(() => {
  let filtered = timelines.value;

  // 过滤年份
  if (selectedYear.value) {
    filtered = filtered.filter(t => t.eventDate?.substr(0, 4) === selectedYear.value);
  }

  // 过滤分类
  if (selectedCategory.value) {
    filtered = filtered.filter(t => t.category === selectedCategory.value);
  }

  // 过滤优先级
  if (selectedPriority.value) {
    filtered = filtered.filter(t => t.priority === selectedPriority.value);
  }

  // 排序
  filtered = filtered.sort((a, b) => {
    let aValue, bValue;

    switch (sortBy.value) {
      case 'eventDate':
        aValue = new Date(a.eventDate || '').getTime();
        bValue = new Date(b.eventDate || '').getTime();
        break;
      case 'createTime':
        aValue = new Date(a.createTime || '').getTime();
        bValue = new Date(b.createTime || '').getTime();
        break;
      case 'priority':
        const priorityOrder = { high: 3, medium: 2, low: 1 };
        aValue = priorityOrder[a.priority as keyof typeof priorityOrder] || 0;
        bValue = priorityOrder[b.priority as keyof typeof priorityOrder] || 0;
        break;
      default:
        aValue = 0;
        bValue = 0;
    }

    return sortDirection.value === 'asc' ? aValue - bValue : bValue - aValue;
  });

  return filtered;
});

const completedCount = computed(() => {
  return timelines.value.filter(t => t.priority === 'low').length;
});

// 方法
const fetchTimelines = async () => {
  loading.value = true;
  try {
    const result = await getTimelineList();
    if (result.success) {
      timelines.value = result.data || [];
      const yearsSet = Array.from(new Set(timelines.value.map(t => t.eventDate?.substr(0, 4) || '未知'))).sort();
      yearsSet?.reverse();
      years.value = yearsSet;
    }
  } catch (error) {
    console.error('获取时间线列表失败:', error);
  } finally {
    loading.value = false;
  }
};

const fetchCategories = async () => {
  try {
    const result = await getTimelineCategories();
    if (result.success) {
      categories.value = result.data || [];
    }
  } catch (error) {
    console.error('获取分类列表失败:', error);
  }
};

const fetchStatistics = async () => {
  try {
    const result = await getTimelineStatistics();
    if (result.success) {
      statistics.value = result.data;
    }
  } catch (error) {
    console.error('获取统计信息失败:', error);
  }
};

const toggleSortDirection = () => {
  sortDirection.value = sortDirection.value === 'asc' ? 'desc' : 'asc';
};

const toast = useToast();
const selectTimeline = (timeline: TimelineEntity) => {
  selectedTimelineData.value = timeline;
  if (selectedTimelineData.value.attachments) {
    showDetailModal.value = true;
  } else {
    toast?.warning("没有附件")
  }
};

const closeDetailModal = () => {
  showDetailModal.value = false;
  selectedTimelineData.value = {};
};

// 解析附件信息
const parseAttachments = (timeline?: TimelineEntity): number[] => {
  if (!timeline) return [];
  if (!timeline.attachments) return [];

  console.log("click card", timeline.attachments)
  // 假设附件格式为逗号分隔的文件名
  return timeline.attachments.split(',')
    .map(id => parseInt(id))
    .filter(id => !isNaN(id));
};

// 工具方法
const getPriorityNodeColor = (priority?: string) => {
  switch (priority) {
    case 'high': return 'bg-red-500';
    case 'medium': return 'bg-yellow-500';
    case 'low': return 'bg-green-500';
    default: return 'bg-gray-500';
  }
};

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

// const formatDateTime = (dateTime?: string) => {
//   if (!dateTime) return '';
//   return new Date(dateTime).toLocaleString('zh-CN');
// };

const showTimelineManager = () => {
  router.push('/timeline/manager');
}

// 生命周期
onMounted(() => {
  fetchTimelines();
  fetchCategories();
  fetchStatistics();
});
</script>

<style scoped>
.timeline-card {
  animation: fadeInUp 0.6s ease-out;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 响应式调整 */
@media (max-width: 768px) {
  .timeline-card {
    margin: 0 !important;
    text-align: left !important;
  }
}
</style>