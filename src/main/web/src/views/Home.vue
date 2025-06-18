<script setup>
import SearchInput from "../components/SearchInput.vue";
</script>
<script>
import  { useUserStore } from '../stores/useUserStore.js'
const { state, login, logout } = useUserStore()
console.log('home', state)
export default {
  name: 'Home',
  data() {
    return {
    }
  },
  methods: {
    routeTo(url) {
      this.$router?.push(url)
    }
  }
}
</script>

<template>
  <div class="min-h-full min-w-full flex flex-col px-16 relativize">
    <div class="absolute top-0 left-0 right-0 z-10 flex justify-between items-center">
      <div v-if="state.isLogin">
        <div class="px-4 py-2 bg-gray-200 rounded-lg hover:bg-gray-300 cursor-pointer select-none" @click="routeTo('/app')">
          App
        </div>
        <div class="px-4 py-2 bg-gray-200 rounded-lg hover:bg-gray-300 cursor-pointer select-none" @click="logout">
          Logout
        </div>
      </div>
      <div v-else class="px-4 py-2 bg-gray-200 rounded-lg hover:bg-gray-300 cursor-pointer select-none" @click="routeTo('/login')">
        login
      </div>
    </div>

    <div class="clock"></div>
    <div class="my-2">
      <SearchInput/>
    </div>
  </div>
</template>
