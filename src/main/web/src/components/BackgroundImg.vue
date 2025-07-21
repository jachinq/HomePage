<script setup lang="ts">
import { useGlobalConfigStore } from '@/stores/useGlobalConfigStore';
import { watch, ref } from 'vue';

const { state } = useGlobalConfigStore();

// 默认背景渐变
const bgImg = ref();
const isLoading = ref(false);

const loadSuccess = (url: string) => {
  bgImg.value.style.backgroundImage = `url(${url})`;
  setTimeout(() => {
    isLoading.value = false; // 延迟显示背景，防止闪屏+加载动画
  }, 100);
}

// 预加载并设置背景图片
const loadBackgroundImage = async (bgUrl: string) => {
  if (!bgUrl) {
    return;
  }

  isLoading.value = true;
  try {
    const image = new Image(); // 预加载图片
    image.onload = () => loadSuccess(bgUrl); // 图片加载成功
    image.onerror = () => {
      console.warn('背景图片加载失败，使用默认背景');
      isLoading.value = false;
    };
    image.src = bgUrl; // 开始加载图片
  } catch (error) {
    console.error('背景图片加载出错:', error);
    isLoading.value = false;
  }
};

// 监听配置变化
watch(() => state.config, (newVal, oldVal) => {
  console.log('配置变化，更新背景图片:', newVal, oldVal);
  newVal && loadBackgroundImage(newVal.bgUrl)
}, { immediate: true, deep: true });
</script>

<template>
  <div class="bg-img-container z-[-1]">
    <!-- 背景图片层 -->
    <div ref="bgImg"
      class="w-full h-full fixed top-0 left-0 bg-cover bg-center bg-no-repeat transition-all duration-500 ease-in-out" :class="{ 'scale-110': isLoading }">
      <div class="w-full h-full bg-gray-900/30" :class="{ 'backdrop-blur-sm': isLoading }"></div>
    </div>
  </div>
</template>