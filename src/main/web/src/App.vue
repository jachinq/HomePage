<template>
  <div class="flex flex-col items-center justify-center min-h-[100vh]">
<!--    <div class="flex flex-col items-center justify-center">-->
<!--      <button class="bg-blue-500 hover:cursor-pointer hover:bg-blue-700 text-white font-bold py-2 px-4 rounded" @click="getList">getList</button>-->
<!--      <button class="bg-blue-500 hover:cursor-pointer hover:bg-blue-700 text-white font-bold py-2 px-4 rounded" @click="addSex">addSex</button>-->
<!--    </div>-->
    <AppSet />
  </div>
</template>

<script>
import FetchUtil from "./utils/fetchUtil.ts";
import AppSet from "./views/AppSet.vue";

export default {
  name: "App",
  components: {
    AppSet
  },
  data() {
    return {
      message: "Hello, World!"
    };
  },
  methods: {
    getList() {
      // 发送请求
      FetchUtil.get("/sex-daily/list").then(response => {
        console.log(response);
        return response.json()
      }).then(data => {
        console.log(data);
      }).catch(error => {
        console.error(error);
      });
    },
    addSex() {
      const data = {
        des: "今天有点早",
        duration: 10,
        createTime: new Date().getTime(),
      };
      // 发送请求
      FetchUtil.post("/sex-daily/add", data).then(response => {
        console.log(response);
        return response.json()
      }).then(data => {
        console.log(data);
      }).catch(error => {
        console.error(error);
      });
    }
  }
};
</script>

<style scoped>
</style>
