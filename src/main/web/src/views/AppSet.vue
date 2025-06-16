<script setup>
import {Menu, MenuButton, MenuItems, MenuItem} from "@headlessui/vue";
import SearchInput from "../components/SearchInput.vue";
</script>
<script>
import {addAppSet, deleteAppSet, getAppSetList, updateAppSet} from "../api/appSetApi.js";
import {randomString} from "../utils/commUtil.js";

export default {
  name: 'AppSet',
  data() {
    return {
      appSet: []
    }
  },
  methods: {
    async add() {
      if ((await addAppSet({
        name: "test",
        description: "safa",
      })).success) {
        await this.getAppSetList();
      }
    },
    async update(data) {
      data.name = randomString(6);
      if ((await updateAppSet(data)).success) {
        this.$toast.success("修改成功✌️")
        await this.getAppSetList();
      }
    },
    async del(data) {
      if ((await deleteAppSet(data)).success) {
        this.$toast.success("删除成功✌️")
        await this.getAppSetList();
      }
    },
    async getAppSetList() {
      const result = await getAppSetList();
      this.appSet = result.data;
      console.log(this.appSet);
    },
    async searchList(args) {
      let params;
      if (args && args !== "") {
        params = {
          name: args
        }
      }
      const result = await getAppSetList(params);
      this.appSet = result.data;
      console.log(this.appSet);
    }
  },
  created() {
    this.getAppSetList();
  }
}
</script>

<template>
  <div class="min-h-full min-w-full flex flex-col px-16">

    <div class="flex-shrink-0">
      <h1 class="text-3xl font-bold">App Set</h1>
    </div>

    <div>
      <SearchInput @search="searchList"/>
    </div>

    <div class="flex my-4">
      <div
          class="px-4 py-2 bg-gray-200 rounded-lg flex justify-between items-center hover:bg-gray-300 cursor-pointer select-none"
          @click="add()">
        添加
      </div>

      <div class="px-4 py-2 bg-gray-200 rounded-lg flex justify-between items-center hover:bg-gray-300 cursor-pointer"
           @click="getAppSetList()">
        刷新
      </div>
    </div>

    <div class="flex-grow">
      <div class="flex flex-wrap">
        <div v-for="app in appSet" class="w-full md:w-1/4 xl:w-1/6 mr-4 my-2">
          <div class="bg-white rounded-lg shadow-lg overflow-hidden">
            <div class="px-6 py-4 relative">
              <div class="font-bold text-xl mb-2">{{ app.name }}</div>
              <p class="text-gray-700 text-base">
                {{ app.description }}
              </p>

              <div class="absolute top-1/4 right-2 z-50">
                <Menu as="div" class="relative inline-block text-left">
                  <div>
                    <MenuButton
                        class="inline-flex w-full justify-center rounded-md bg-black/20 px-4 py-2 text-sm font-medium text-white hover:bg-black/30 focus:outline-none focus-visible:ring-2 focus-visible:ring-white/75">
                      More
                    </MenuButton>
                  </div>
                  <transition
                      enter-active-class="transition duration-100 ease-out"
                      enter-from-class="transform scale-95 opacity-0"
                      enter-to-class="transform scale-100 opacity-100"
                      leave-active-class="transition duration-75 ease-in"
                      leave-from-class="transform scale-100 opacity-100"
                      leave-to-class="transform scale-95 opacity-0"
                  >
                    <MenuItems>
                      <MenuItem>
                        <div
                            class="px-4 py-2 bg-gray-200 rounded-lg flex justify-between items-center hover:bg-gray-300 cursor-pointer"
                            @click="update(app)">
                          修改
                        </div>

                      </MenuItem>
                      <MenuItem>
                        <div
                            class="px-4 py-2 bg-gray-200 rounded-lg flex justify-between items-center hover:bg-gray-300 cursor-pointer"
                            @click="del(app)">
                          删除
                        </div>
                      </MenuItem>
                    </MenuItems>
                  </transition>
                </Menu>
              </div>
            </div>
          </div>
        </div>
        <div v-if="appSet.length === 0">
          暂无数据
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>