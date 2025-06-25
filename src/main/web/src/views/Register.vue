<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import {register} from "@/api/AuthApi.js";
import { getCurrentInstance } from "vue";
const context = getCurrentInstance()?.appContext.config.globalProperties;
const toast = context?.$toast;

const router = useRouter()
const username = ref('')
const password = ref('')
const confirmPassword = ref('')

const handleRegister = async () => {
  if (password.value !== confirmPassword.value) {
    toast.error('两次输入的密码不一致')
    return
  }
  let result = await register({
    username: username.value,
    password: password.value
  });
  if (!result.success) {
    toast.error(result.message)
    return
  }
  // 注册成功，返回登录页面
  await router.push('/login')
  console.log('注册:', username.value, password.value)
}
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-800 py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-8">
      <div>
        <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-400">
          注册新账户
        </h2>
        <p class="mt-2 text-center text-sm text-gray-500">
          或
          <router-link to="/login" class="font-medium text-blue-500 hover:text-blue-400">
            返回登录
          </router-link>
        </p>
      </div>
      <form class="mt-8 space-y-6" @submit.prevent="handleRegister">
        <div class="rounded-md shadow-sm -space-y-px">
          <div>
            <label for="username" class="sr-only">用户名</label>
            <input
                id="username"
                v-model="username"
                name="username"
                type="text"
                required
                class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-600 placeholder-gray-500 text-gray-900 rounded-t-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 focus:z-10 sm:text-sm"
                placeholder="用户名"
            />
          </div>
          <div>
            <label for="password" class="sr-only">密码</label>
            <input
                id="password"
                v-model="password"
                name="password"
                type="password"
                required
                class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-600 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-blue-500 focus:border-blue-500 focus:z-10 sm:text-sm"
                placeholder="密码"
            />
          </div>
          <div>
            <label for="confirm-password" class="sr-only">确认密码</label>
            <input
                id="confirm-password"
                v-model="confirmPassword"
                name="confirm-password"
                type="password"
                required
                class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-600 placeholder-gray-500 text-gray-900 rounded-b-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 focus:z-10 sm:text-sm"
                placeholder="确认密码"
            />
          </div>
        </div>

        <div>
          <button
              type="submit"
              class="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-gray-200 bg-blue-700 hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
          >
            注册
          </button>
        </div>
      </form>
    </div>
  </div>
</template> 