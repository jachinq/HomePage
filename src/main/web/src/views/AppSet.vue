<script setup>
import {Switch} from "@headlessui/vue";
import SearchInput from "../components/SearchInput.vue";
import Pagination from "../components/Pagination.vue";
import EditModal from "./appset/EditModal.vue";
import ConfigModal from "./appset/ConfigModal.vue";
import AppCard from "../components/AppCard.vue";

</script>
<script>
import {getAppSetList} from "../api/appSetApi.js";
import {getAppConfig} from "../api/appConfigApi.js";

export default {
  name: 'AppSet',
  data() {
    return {
      appSet: [],
      totalPages: 0,
      pageNum: 1,
      pageSize: 10,
      searchValue: "",
      innerMode: false,
      openModal: false,
      appSetData: {},
      openConfig: false,
      configData: {}
    }
  },
  methods: {
    openConfigModal() {
      console.log("config")
      this.openConfig = true;
    },
    closeConfigModal(data = {}) {
      const {open = false, success = false} = data;
      this.openConfig = open;
      if (success) {
        this.getConfig();
      }
    },
    openSaveModal(app) {
      this.appSetData = app;
      this.openModal = true;
    },
    closeSaveModal(data = {}) {
      const {open = false, success = false} = data;
      this.openModal = open;
      if (success) {
        this.searchList();
      }
    },
    async searchList(args) {
      let params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      };
      if (args || args === "") {
        this.searchValue = args;
      }
      if (this.searchValue && this.searchValue !== "") {
        params.name = this.searchValue;
      }
      console.log(params, args)

      const result = await getAppSetList(params);
      if (!result.success) {
        this.$toast.error("获取数据失败")
        return
      }
      this.appSet = result.data.content;
      this.totalPages = result.data.totalPages;
    },
    async getConfig() {
      const result = await getAppConfig();
      if (result.success) {
        this.configData = result.data;
        console.log('configData', this.configData)
      }
    },

  },
  created() {
    this.searchList();
    this.getConfig();
    // 判断一下地址是否为内网地址
    const url = new URL(window.location.href);
    const hostname = url.hostname;
    if (hostname.includes("192.168")
        || hostname.includes("localhost")
        || hostname.includes("127.0.0.1")) {
      this.innerMode = true;
    }
  }
}
</script>

<template>
  <div class="min-h-full min-w-full">

    <div class="mb-2 flex items-center justify-between">
      <span class="text-2xl font-bold">App Set</span>
      <span class="text-sm text-gray-400 cursor-pointer hover:underline" @click="$router.push('/')">home</span>
    </div>

    <div class="my-2">
      <SearchInput @search="searchList"/>
    </div>

    <div class="flex mt-2 items-center gap-4">
      <span class="hover:text-blue-400 cursor-pointer select-none underline" @click="openConfigModal()">配置</span>
      <span class="hover:text-blue-400 cursor-pointer select-none underline" @click="openSaveModal()">添加</span>
      <span class="hover:text-blue-400 cursor-pointer select-none underline" @click="searchList()">刷新</span>

      <div class=" flex items-center gap-2">
        <span class="text-gray-400">内网模式</span>
        <Switch v-model="innerMode" :class="innerMode ? 'bg-blue-400' : 'bg-gray-500'"
                class="relative inline-flex h-4 w-8 items-center rounded-full">
          <span class="sr-only">Inner Mode</span>
          <span :class="innerMode ? 'translate-x-4 h-5 w-5 bg-blue-500' : 'translate-x-0 h-4 w-4 bg-gray-700 '"
                class="inline-block transform rounded-full transition"></span>
        </Switch>
      </div>
    </div>

    <div class="flex-grow mt-2">
      <div class="flex flex-wrap">
        <template v-for="app in appSet">
          <AppCard :app="app" :inner-mode="innerMode">
              <span class="text-sm text-gray-400 hover:underline hover:cursor-pointer hover:text-blue-300" 
                @click="openSaveModal(app)">
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
                    @update:current-page="pageNum = $event; searchList()"/>
      </div>
    </div>


    <EditModal :open-modal="openModal" :old-data="appSetData" @close="closeSaveModal"/>
    <ConfigModal :open-modal="openConfig" :old-data="configData" @close="closeConfigModal"/>
  </div>
</template>