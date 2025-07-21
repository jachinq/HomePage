<script setup lang="ts">
import BackToHome from '@/components/BackToHome.vue';
import { AppSet, AppType } from '@/interface/appset';
import { ref } from 'vue';

const appSet = ref<AppSet[]>([
  { type: AppType.SYS, id: 2, name: '时间线', icon: '📅', outerUrl: '/timeline' },
  { type: AppType.SYS, id: 1, name: '习惯', icon: '📒', outerUrl: '/habit' },
  { type: AppType.SYS, id: 3, name: '用户', icon: '👤', outerUrl: '/user' },
  { type: AppType.SYS, id: 4, name: '设置', icon: '⚙️', outerUrl: '/setting' },
]);

</script>

<template>
  <div class="flex-grow mt-2">
    <div class="mb-2 flex items-center justify-between">

      <div class="flex flex-wrap gap-3 items-center">
        <div v-for="app in appSet" :key="app.id" @click="$router.push(app.outerUrl || '')"
          class="group flex items-center gap-2 text-xl cursor-pointer hover:text-sky-300">
          <div class="group-hover:scale-120 transition-all duration-300">
            {{ app.icon }}
          </div>
          <span class="group-hover:scale-110 transition-all duration-300">{{ app.name }}</span>
        </div>
      </div>

      <BackToHome />
    </div>

  </div>
</template>