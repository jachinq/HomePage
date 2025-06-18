<script setup>
import {Menu, MenuButton, MenuItems, MenuItem, Switch} from "@headlessui/vue";
import SearchInput from "../components/SearchInput.vue";
import Pagination from "../components/Pagination.vue";
import EditModal from "./appset/EditModal.vue";
</script>
<script>
import {deleteAppSet, getAppSetList, updateAppSet} from "../api/appSetApi.js";
import {randomString} from "../utils/commUtil.js";

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
      oldData: {},
    }
  },
  methods: {
    edit(app) {
      this.oldData = app;
      this.openModal = true;
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
    openApp(app) {
      console.log(app)
      if (this.innerMode) {
        if (app.innerUrl && app.port) {
          window.open(`${app.innerUrl}:${app.port}`, "_blank");
        } else {
          this.$toast.error("未配置内网地址或端口，无法使用内网模式打开")
        }
      } else {
        if (!app.outerUrl) {
          this.$toast.error("未配置外网地址")
          return
        }
        window.open(app.outerUrl, "_blank");
      }
    },
    closeModal(data = {}) {
      const {open = false, success = false} = data.open || {};
      this.openModal = open;
      if (success) {
        this.searchList();
      }
    }
  },
  created() {
    this.searchList();

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
      <div
          class="px-4 py-2 bg-gray-200 rounded-lg flex justify-between items-center hover:bg-gray-300 cursor-pointer select-none"
          @click="edit()">
        添加
      </div>

      <div class="px-4 py-2 bg-gray-200 rounded-lg flex justify-between items-center hover:bg-gray-300 cursor-pointer"
           @click="searchList()">
        刷新
      </div>

      <div class=" flex items-center gap-2">
        <span class="text-gray-400">内网模式</span>
        <Switch v-model="innerMode"
                :class="innerMode ? 'bg-blue-600' : 'bg-gray-200'"
                class="relative inline-flex h-6 w-11 items-center rounded-full">
          <span class="sr-only">Inner Mode</span>
          <span
              :class="innerMode ? 'translate-x-6' : 'translate-x-1'"
              class="inline-block h-4 w-4 transform rounded-full bg-gray-800 transition"
          />
        </Switch>
      </div>
    </div>

    <div class="flex-grow mt-2">
      <div class="flex flex-wrap">
        <div v-for="app in appSet" class="w-full md:w-1/4 xl:w-1/6 mr-4 my-2">
          <div class="bg-gray-800 rounded-lg shadow-lg overflow-hidden">
            <div class="px-6 py-4 relative">
              <div class="font-bold text-xl mb-2 cursor-pointer hover:underline" @click="openApp(app)">{{
                  app.name
                }}
              </div>
              <p class="text-gray-700 text-base">
                {{ app.description }}
              </p>

              <div class="absolute top-1/8 right-2 z-50 mx-2">
                <button class="bg-gray-200 rounded-lg px-4 py-2 text-sm text-gray-700 hover:bg-gray-300"
                        @click="edit(app)">
                  修改
                </button>
              </div>
            </div>
          </div>
        </div>
        <div v-if="appSet.length === 0">
          暂无数据
        </div>
      </div>
      <div v-if="totalPages > 0" class="mt-2">
        <Pagination :current-page="pageNum" :total-pages="totalPages"
                    @update:current-page="pageNum = $event; searchList()"/>
      </div>
    </div>


    <EditModal :open-modal="openModal" :old-data="oldData" @close="closeModal"/>
  </div>
</template>

<style scoped>

</style>