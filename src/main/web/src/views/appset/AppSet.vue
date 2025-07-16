<script setup lang="ts">
import { ref } from 'vue';
import { Switch } from "@headlessui/vue";
import BackToHome from "@/components/BackToHome.vue";
import SearchInput from "@/components/SearchInput.vue";
import ConfigModal from "./ConfigModal.vue";
import { getAppConfig } from "@/api/appConfigApi";
import { AppConfig, AppType } from "@/interface/appset";
import AppCardList from "@/views/appset/AppCardList.vue";

const searchValue = ref("");
const openConfig = ref(false);
const configData = ref<AppConfig>({});

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

const getConfig = async () => {
  const result = await getAppConfig();
  if (result.success) {
    configData.value = result.data || {};

    // 判断一下地址是否为内网地址
    const url = new URL(window.location.href);
    const hostname = url.hostname;
    if (hostname.includes("192.168")
      || hostname.includes("localhost")
      || hostname.includes("0.0.0.0")
      || hostname.includes("127.0.0.1")) {
      configData.value.innerMode = true;
    }
    console.log('configData', configData.value)
  }
}

const refresh = () => {
  getConfig();
}
refresh();
</script>

<template>
  <div class="min-h-full min-w-lg">

    <div class="mb-2 flex items-center justify-between">
      <div class="flex mt-2 items-center gap-4">
        <span class="text-2xl font-bold">App Set</span>
        <span class="hover:text-blue-400 cursor-pointer select-none underline" @click="openConfigModal()">配置</span>
        <div class=" flex items-center gap-2">
          <span class="text-gray-400">{{ configData?.innerMode ? '内网模式' : '外网模式' }}</span>
          <Switch v-model="configData.innerMode" :class="configData?.innerMode ? 'bg-blue-400' : 'bg-gray-500'"
            class="relative inline-flex h-4 w-8 items-center rounded-full">
            <span class="sr-only">Inner Mode</span>
            <span :class="configData?.innerMode ? 'translate-x-4 h-5 w-5 bg-blue-500' : 'translate-x-0 h-4 w-4 bg-gray-700 '"
              class="inline-block transform rounded-full transition"></span>
          </Switch>
        </div>
      </div>
      <BackToHome />
    </div>

    <div class="my-2">
      <SearchInput @search="(value) => searchValue = value" />
    </div>

    <AppCardList :search-value="searchValue" :config-data="configData" :type="AppType.APP" />
    <AppCardList :search-value="searchValue" :config-data="configData" :type="AppType.Bookmark" />

    <ConfigModal :open-modal="openConfig" :old-data="configData" @close="closeConfigModal" />
  </div>
</template>