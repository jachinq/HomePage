<template>
  <div class="min-h-[100vh] px-16 py-4">
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
