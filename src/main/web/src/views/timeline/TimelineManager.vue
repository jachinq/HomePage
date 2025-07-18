<script setup lang="ts">
import { ref, onMounted, getCurrentInstance, computed } from 'vue';
import { TimelineEntity, TimelineStatistics } from '../../interface/timeline';
import { getTimelineList, getTimelineStatistics, getTimelineCategories } from '../../api/timelineApi';
import BackToHome from '../../components/BackToHome.vue';
import Loading from '../../components/Loading.vue';
import TimelineCard from './TimelineCard.vue';
import EditTimelineModal from './EditTimelineModal.vue';
import SearchInput from '../../components/SearchInput.vue';

const context = getCurrentInstance()?.appContext.config.globalProperties;
const toast = context?.$toast;

const timelines = ref<TimelineEntity[]>([]);
const categories = ref<string[]>([]);
const statistics = ref<TimelineStatistics | null>(null);
const loading = ref(false);
const searchKeyword = ref('');
const selectedCategory = ref('');
const selectedPriority = ref('');
const openModal = ref({ add: false, set: false });
const activeTimeline = ref<TimelineEntity>({});

// 获取时间线列表
const fetchTimelines = async () => {
  loading.value = true;
  try {
    const result = await getTimelineList();
    if (result.success) {
      timelines.value = result.data || [];
    } else {
      toast?.error(result.message);
    }
  } catch (error) {
    toast?.error('获取时间线列表失败');
  } finally {
    loading.value = false;
  }
};

// 获取分类列表
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

// 获取统计信息
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

// 搜索时间线
const handleSearch = async (keyword: string) => {
  searchKeyword.value = keyword;
  if (keyword.trim()) {
    // 这里可以调用搜索API
    // await searchTimeline(keyword);
  } else {
    await fetchTimelines();
  }
};

// 过滤时间线
const filteredTimelines = computed(() => {
  let filtered = timelines.value;
  
  if (selectedCategory.value) {
    filtered = filtered.filter(t => t.category === selectedCategory.value);
  }
  
  if (selectedPriority.value) {
    filtered = filtered.filter(t => t.priority === selectedPriority.value);
  }
  
  if (searchKeyword.value) {
    filtered = filtered.filter(t => 
      t.title?.toLowerCase().includes(searchKeyword.value.toLowerCase()) ||
      t.description?.toLowerCase().includes(searchKeyword.value.toLowerCase())
    );
  }
  
  return filtered;
});

// 打开新增模态框
const openAddModal = () => {
  activeTimeline.value = {};
  openModal.value = { add: true, set: false };
};

// 打开编辑模态框
const openEditModal = (timeline: TimelineEntity) => {
  activeTimeline.value = timeline;
  openModal.value = { add: false, set: true };
};

// 处理模态框关闭
const handleModalClose = (success?: boolean) => {
  openModal.value = { add: false, set: false };
  if (success) {
    fetchTimelines();
    fetchStatistics();
    fetchCategories();
  }
};

// 处理删除成功
const handleDeleteSuccess = (success?: boolean) => {
  if (success) {
    fetchTimelines();
    fetchStatistics();
    fetchCategories();
  }
};

onMounted(() => {
  fetchTimelines();
  fetchCategories();
  fetchStatistics();
});
</script>

<template>
  <div class="flex flex-col w-full h-full">
    <!-- 头部区域 -->
    <div class="flex justify-between items-center mb-4">
      <span class="text-2xl font-bold">时间线</span>
      <BackToHome />
    </div>

    <!-- 统计信息 -->
    <div v-if="statistics" class="flex gap-4 mb-4 text-sm text-gray-400">
      <span>总事件: {{ statistics.total }}</span>
      <span>分类数: {{ statistics.categoryCount }}</span>
    </div>

    <!-- 操作区域 -->
    <div class="flex flex-col md:flex-row gap-4 mb-4">
      <!-- 搜索框 -->
      <div class="flex-1">
        <SearchInput @search="handleSearch" placeholder="搜索时间线事件..." />
      </div>
      
      <!-- 过滤器 -->
      <div class="flex gap-2">
        <select v-model="selectedCategory" class="px-3 py-2 bg-gray-800 text-white rounded border border-gray-600">
          <option value="">全部分类</option>
          <option v-for="category in categories" :key="category" :value="category">
            {{ category }}
          </option>
        </select>
        
        <select v-model="selectedPriority" class="px-3 py-2 bg-gray-800 text-white rounded border border-gray-600">
          <option value="">全部优先级</option>
          <option value="high">高</option>
          <option value="medium">中</option>
          <option value="low">低</option>
        </select>
        
        <button 
          @click="$router.push('/timeline/display')"
          class="bg-purple-600 hover:bg-purple-500 text-white px-4 py-2 rounded font-medium transition-colors"
        >
          现代化展示
        </button>
        
        <button 
          @click="openAddModal"
          class="bg-sky-600 hover:bg-sky-500 text-white px-4 py-2 rounded font-medium transition-colors"
        >
          新增事件
        </button>
      </div>
    </div>

    <!-- 时间线列表 -->
    <div class="flex-1 overflow-y-auto">
      <div v-if="filteredTimelines.length === 0" class="text-center text-gray-500 py-20">
        <div class="text-4xl mb-4">📅</div>
        <p>暂无时间线事件</p>
        <p class="text-sm mt-2">点击上方"新增事件"按钮开始记录重要时刻</p>
      </div>
      
      <div v-else class="flex flex-col md:flex-row gap-4 flex-wrap">
        <TimelineCard
          v-for="timeline in filteredTimelines"
          :key="timeline.id"
          :timeline="timeline"
          @edit="openEditModal"
          @delete="handleDeleteSuccess"
        />
      </div>
    </div>

    <!-- 加载状态 -->
    <Loading :loading="loading" />

    <!-- 编辑模态框 -->
    <EditTimelineModal
      :open-modal="openModal"
      :old-data="activeTimeline"
      @close="handleModalClose"
    />
  </div>
</template>

<style scoped>
/* 如果需要自定义样式 */
</style> 