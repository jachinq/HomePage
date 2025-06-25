<script setup lang="ts">
import { getCurrentInstance, ref } from 'vue';
import { Switch } from "@headlessui/vue";

import AppCard from "@/components/AppCard.vue";
import BackToHome from "@/components/BackToHome.vue";
import Pagination from "@/components/Pagination.vue";
import SearchInput from "@/components/SearchInput.vue";
import EditModal from "./EditModal.vue";
import ConfigModal from "./ConfigModal.vue";
import { getAppSetList } from "@/api/appSetApi";
import { getAppConfig } from "@/api/appConfigApi";
import { AppSet, AppType } from "@/interface/appset";

const context = getCurrentInstance()?.appContext.config.globalProperties;
const toast = context?.$toast;

const appSet = ref([]);
const bookMarkSet = ref([]);
const totalPages = ref(0);
const pageNum = ref(1);
const pageSize = ref(10);
const searchValue = ref("");
const innerMode = ref(false);
const openModal = ref({ add: false, set: false });
const appSetData = ref();
const openConfig = ref(false);
const configData = ref();

const openConfigModal = () => {
  console.log("config")
  openConfig.value = true;
}

const closeConfigModal = (data: any = {}) => {
  const { open = false, success = false } = data;
  openConfig.value = open;
  if (success) {
    getConfig();
  }
}

const openSaveModal = (app: AppSet | null) => {
  appSetData.value = app;
  if (appSetData.value) {
    openModal.value = { add: false, set: true };
  } else {
    openModal.value = { add: true, set: false };
  }
}

const closeSaveModal = (success: any) => {
  openModal.value = { add: false, set: false };
  if (success === true) {
    getAppList();
  }
}

const packageArgs = (args: any = "", params: any = {}) => {
  if (args || args === "") {
    searchValue.value = args;
  }
  if (searchValue.value && searchValue.value !== "") {
    params.name = searchValue.value;
  }
  return params;
}

const getAppList = async (args: any = "") => {
  const result = await getAppSetList(packageArgs(args, {
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    type: AppType.APP,
  }));
  if (!result.success) {
    toast?.error("获取数据失败")
    return
  }
  appSet.value = result.data;
  totalPages.value = result.page.totalPages;
}

const getBookMarkList = async (args: any = "") => {
  const result = await getAppSetList(packageArgs(args, {
    pageNum: 1,
    pageSize: -1,
    type: AppType.Bookmark,
  }));
  if (!result.success) {
    toast?.error("获取数据失败")
    return
  }
  bookMarkSet.value = result.data;
}

const getConfig = async () => {
  const result = await getAppConfig();
  if (result.success) {
    configData.value = result.data;
    console.log('configData', configData.value)
  }
}

const refresh = (args: any = "") => {
  getConfig();
  getAppList(args);
  getBookMarkList(args);
}
refresh();

// 判断一下地址是否为内网地址
const url = new URL(window.location.href);
const hostname = url.hostname;
if (hostname.includes("192.168")
  || hostname.includes("localhost")
  || hostname.includes("0.0.0.0")
  || hostname.includes("127.0.0.1")) {
  innerMode.value = true;
}
</script>

<template>
  <div class="min-h-full min-w-lg">

    <div class="mb-2 flex items-center justify-between">
      <div class="flex mt-2 items-center gap-4">
        <span class="text-2xl font-bold">App Set</span>
        <span class="hover:text-blue-400 cursor-pointer select-none underline" @click="openConfigModal()">配置</span>
        <span class="hover:text-blue-400 cursor-pointer select-none underline" @click="openSaveModal(null)">添加</span>
        <span class="hover:text-blue-400 cursor-pointer select-none underline" @click="refresh()">刷新</span>

        <div class=" flex items-center gap-2">
          <span class="text-gray-400">{{ innerMode ? '内网模式' : '外网模式' }}</span>
          <Switch v-model="innerMode" :class="innerMode ? 'bg-blue-400' : 'bg-gray-500'"
            class="relative inline-flex h-4 w-8 items-center rounded-full">
            <span class="sr-only">Inner Mode</span>
            <span :class="innerMode ? 'translate-x-4 h-5 w-5 bg-blue-500' : 'translate-x-0 h-4 w-4 bg-gray-700 '"
              class="inline-block transform rounded-full transition"></span>
          </Switch>
        </div>
      </div>
      <BackToHome />
    </div>

    <div class="my-2">
      <SearchInput @search="refresh" />
    </div>



    <div class="flex-grow mt-2">
      <div class="flex flex-wrap gap-2 items-center">
        <template v-for="app in appSet">
          <AppCard :app="app" :inner-mode="innerMode" :inner-domain="configData?.innerDomain"
            :outer-domain="configData?.outerDomain">
            <span class="text-sm text-gray-400 hover:underline hover:cursor-pointer hover:text-blue-300"
              @click.prevent.stop="openSaveModal(app)">
              修改
            </span>
          </AppCard>
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

    <div>
      <div v-if="bookMarkSet.length > 0" class="mt-2">
        <h2 class="text-lg font-bold">收藏夹</h2>
        <div class="flex flex-wrap gap-2 items-center">
          <template v-for="app in bookMarkSet">
            <AppCard :app="app" :inner-mode="innerMode" :inner-domain="configData?.innerDomain"
              :outer-domain="configData?.outerDomain">
              <span class="text-sm text-gray-400 hover:underline hover:cursor-pointer hover:text-blue-300"
                @click.prevent.stop="openSaveModal(app)">
                修改
              </span>
            </AppCard>
          </template>
        </div>
      </div>
    </div>

    <EditModal :open-modal="openModal" :old-data="appSetData" @close="closeSaveModal" />
    <ConfigModal :open-modal="openConfig" :old-data="configData" @close="closeConfigModal" />
  </div>
</template>