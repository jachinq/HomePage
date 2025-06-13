<template>
  <div class="content">
<!--    <h1>Rsbuild with Crm</h1>-->
<!--    <p>Start building amazing things with Rsbuild.</p>-->
<!--    <p>This is a sample app created using Rsbuild and Crm.</p>-->
    <div class="button_flex">
      <button @click="getList">getList</button>
      <button @click="addSex">addSex</button>
    </div>
  </div>
</template>

<script>
import * as http from "./utils/http.js";

export default {
  name: "App",
  data() {
    return {
      message: "Hello, World!"
    };
  },
  methods: {
    getList() {
      // 发送请求
      http.get("/sex-daily/list").then(response => {
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
      http.post("/sex-daily/add", data).then(response => {
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
.content {
  display: flex;
  min-height: 100vh;
  line-height: 1.1;
  text-align: center;
  flex-direction: column;
  justify-content: center;
}

.content h1 {
  font-size: 3.6rem;
  font-weight: 700;
}

.content p {
  font-size: 1.2rem;
  font-weight: 400;
  opacity: 0.5;
}

.button_flex {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  gap: 10px;
}
</style>
