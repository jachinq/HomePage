<script setup lang="ts">
import { ref, watch } from 'vue';
import { VueDraggableNext as Draggable } from 'vue-draggable-next'

import AppCard from "@/components/AppCard.vue";
import Pagination from "@/components/Pagination.vue";
import { batchSaveAppSet, getAppSetList } from "@/api/appSetApi";
import { AppSet, AppType } from "@/interface/appset";
import { useToast } from '@/components/toast';
import EditModal from "@/views/appset/EditModal.vue";
import { useScreenSizeStore } from '@/stores/screenSizeStore';

const toast = useToast();
const screenSizeStore = useScreenSizeStore();

const appSet = ref<AppSet[]>([]);
const total = ref(0);
const totalPages = ref(0);
const pageNum = ref(1);
const pageSize = ref(32);
const openModal = ref({ add: false, set: false });
const appSetData = ref<AppSet | null>();
const changeAppSort = ref(false)

const openSaveModal = (app: AppSet | null) => {
  appSetData.value = app;
  if (appSetData.value) {
    openModal.value = { add: false, set: true };
  } else {
    const findMaxSort = (list: AppSet[]): number => {
      let maxSort = 0;
      list.forEach(app => {
        if (app.sort ?? 0 > maxSort) {
          maxSort = app.sort ?? 0;
        }
      });
      return maxSort;
    }

    openModal.value = { add: true, set: false };
    appSetData.value = {
      type: props.type,
      sort: Math.max(findMaxSort(appSet.value), total.value) + 1,
    }
  }
}

const packageArgs = (args: any = "", params: any = {}) => {
  if (args && args !== "" && typeof args === "string") {
    params.name = args;
  }
  return params;
}

const getAppList = async (args: any = "") => {
  const result = await getAppSetList(packageArgs(args, {
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    type: props.type,
  }));
  if (!result.success) {
    toast?.error("获取数据失败")
    return
  }
  appSet.value = result.data;
  total.value = result.page.total;
  totalPages.value = result.page.totalPages;
}

const closeSaveModal = (success: any) => {
  openModal.value = { add: false, set: false };
  if (success === true) {
    getAppList();
  }
}

const saveAppSort = async () => {
  let appList: AppSet[] = JSON.parse(JSON.stringify(appSet.value))

  const findMinSort = (list: AppSet[]): number => {
    let minSort = 1;
    list.forEach(app => {
      if (app.sort ?? 0 < minSort) {
        minSort = app.sort ?? 0;
      }
    });
    return minSort;
  }

  const newAppList: AppSet[] = [];
  const appMinSort = findMinSort(appList);
  appList.forEach((app, index) => {
    app.sort = index + 1 + appMinSort;
    newAppList.push(app);
  });

  console.log('newAppList', newAppList)

  const result = await batchSaveAppSet(newAppList);
  if (result.success) {
    toast?.success("保存排序成功");
    changeAppSort.value = false;
  } else {
    toast?.error("保存排序失败");
  }
}

const props = defineProps<{
  configData: any,
  type: AppType,
  searchValue: string,
}>()
const emit = defineEmits(['onSort']);

const refresh = () => {
  changeAppSort.value = false;
  getAppList(props.searchValue);
}
refresh();

watch(() => props.searchValue, () => {
  refresh();
})

</script>

<template>
  <div class="flex-grow mt-2">
    <div class="flex mt-2 items-center gap-4">
      <span class="text-2xl font-bold">{{ props.type === AppType.SYS ? '系统应用' : props.type === AppType.APP ? '应用' : '书签'
      }}</span>
      <span class="hover:text-blue-400 cursor-pointer select-none underline" @click="openSaveModal(null)">添加</span>
      <span class="hover:text-blue-400 cursor-pointer select-none underline" @click="refresh">刷新</span>
      <span v-if="changeAppSort" class="hover:text-blue-400 cursor-pointer select-none underline"
        @click="saveAppSort">保存排序</span>
    </div>

    <div class="flex flex-wrap gap-2 items-center">
      <template v-if="!screenSizeStore.isSmall">
        <Draggable :list="appSet" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 xl:grid-cols-6 2xl:grid-cols-8 gap-4 items-center w-full" @change="changeAppSort = true">
          <transition-group>
            <AppCard v-for="app in appSet" :app="app" :config-data="props.configData" :key="app.id">
              <span
                class="hidden group-hover:block text-sm text-gray-400 hover:underline hover:cursor-pointer hover:text-blue-300 bg-slate-800 px-2 rounded-2xl"
                @click.prevent.stop="openSaveModal(app)">
                修改
              </span>
            </AppCard>
          </transition-group>
        </Draggable>

      </template>

      <!-- 小屏幕禁止拖拽排序 -->
      <template v-else>
        <div class="grid grid-cols-1 gap-2 items-center w-full">
          <AppCard v-for="app in appSet" :app="app" :config-data="props.configData" :key="app.id">
            <span
              class="hidden group-hover:block text-sm text-gray-400 hover:underline hover:cursor-pointer hover:text-blue-300 bg-slate-800 px-2 rounded-2xl"
              @click.prevent.stop="openSaveModal(app)">
              修改
            </span>
          </AppCard>
        </div>
      </template>


      <div v-if="appSet.length === 0">
        暂无数据
      </div>
    </div>
    <div v-if="totalPages > 0" class="mt-2">
      <Pagination :current-page="pageNum" :total-pages="totalPages"
        @update:current-page="pageNum = $event; getAppList()" />
    </div>
  </div>

  <EditModal :open-modal="openModal" :old-data="appSetData" @close="closeSaveModal" />

</template>