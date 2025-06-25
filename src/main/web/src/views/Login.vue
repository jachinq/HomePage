<script setup lang="ts">
import { ref, getCurrentInstance } from "vue";
import { login } from "../api/AuthApi.js";
import { useUserStore } from '../stores/useUserStore.ts'
import storage from "../utils/storage.js";
import { useRouter } from "vue-router";

const context = getCurrentInstance()?.appContext.config.globalProperties;
const $toast = context?.$toast;

const userStore = useUserStore()
const $router = useRouter()
const username = ref('')
const password = ref('')

const handleLogin = async () => {
  const result = await login({
    username: username.value,
    password: password.value
  });
  if (result.success) {
    userStore.login(result.data)
    // token写入storage
    storage.setToken(result.data?.token || '')
    username.value = result.data?.user?.username || ''
    $toast.success('欢迎回来~' + username.value)
    $router.push('/')
  } else {
    $toast.error(result.message)
  }
}

const created = async () => {
  if (userStore.state.isLogin) {
    $router.push('/')
    return
  }
  await userStore.validateToken();
  if (userStore.state.isLogin) {
    $router.push('/')
  }
}
created()
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-800 py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-8">
      <div>
        <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-400">
          登录账户
        </h2>
        <p class="mt-2 text-center text-sm text-gray-500">
          或
          <router-link to="/register" class="font-medium text-blue-500 hover:text-blue-400">
            注册新账户
          </router-link>
        </p>
      </div>
      <form class="mt-8 space-y-6" @submit.prevent="handleLogin">
        <div class="rounded-md shadow-sm -space-y-px">
          <div>
            <label for="username" class="sr-only">用户名</label>
            <input id="username" v-model="username" name="username" type="text" required
              class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-600 placeholder-gray-500 text-gray-400 rounded-t-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 focus:z-10 sm:text-sm"
              placeholder="用户名" />
          </div>
          <div>
            <label for="password" class="sr-only">密码</label>
            <input id="password" v-model="password" name="password" type="password" required
              class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-600 placeholder-gray-500 text-gray-400 rounded-b-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 focus:z-10 sm:text-sm"
              placeholder="密码" />
          </div>
        </div>

        <div>
          <button type="submit"
            class="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-gray-200 bg-blue-700 hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
            登录
          </button>
        </div>
      </form>
    </div>
  </div>
</template>