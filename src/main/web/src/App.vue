<template>
  <div class="min-h-[100vh] px-16 py-4">
<!--    <div class="flex flex-col items-center justify-center">-->
<!--      <button class="bg-blue-500 hover:cursor-pointer hover:bg-blue-700 text-white font-bold py-2 px-4 rounded" @click="getList">getList</button>-->
<!--      <button class="bg-blue-500 hover:cursor-pointer hover:bg-blue-700 text-white font-bold py-2 px-4 rounded" @click="addSex">addSex</button>-->
<!--    </div>-->
<!--    <AppSet />-->
<!--    <TabGroup @change="changeTab">-->
<!--      <TabList>-->
<!--        <Tab as="template" v-slot="{ selected }" v-for="tab in tabList" >-->
<!--          <button :class="{ 'bg-blue-500 text-white': selected, 'bg-gray-800 text-black': !selected }">-->
<!--            {{ tab.name }}-->
<!--          </button>-->
<!--        </Tab>-->
<!--      </TabList>-->
<!--      <TabPanels>-->
<!--        <TabPanel>-->
<!--          <router-view />-->
<!--        </TabPanel>-->
<!--      </TabPanels>-->
<!--    </TabGroup>-->
    <router-view />
  </div>
</template>

<script setup>
import {TabGroup, Tab, TabList, TabPanels, TabPanel} from "@headlessui/vue";
</script>

<script>
import AppSet from "./views/AppSet.vue";
import {Tab, TabGroup, TabList, TabPanel, TabPanels} from "@headlessui/vue";

export default {
  name: "App",
  data() {
    return {
      selected: 0,
      tabList: [
        { name:"Home", router: "/" },
        { name:"AppSet", router: "/app" },
        { name: "Login", router: "/login" },
        { name: "Register", router: "/register" }
      ]
    };
  },
  methods: {
    changeTab(index) {
      this.selected = index;
      console.log('goto', this.tabList[index].router);
      this.$router.push(this.tabList[index].router);
    }
  }
};
</script>
