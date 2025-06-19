<script setup>
import SearchInput from "../components/SearchInput.vue";
import TimeDisplay from "../components/TimeDisplay.vue";
import UserAvatar from "../components/UserAvatar.vue";
</script>
<script>
import  { useUserStore } from '../stores/useUserStore.js'
const { state, login, logout } = useUserStore()
console.log('home', state)
export default {
  name: 'Home',
  data() {
    return {
      searchValue: '',
      searchUrl: 'https://www.bing.com/search?&form=QBLH&q=##SEARCH_VALUE##'
    }
  },
  methods: {
    routeTo(url) {
      this.$router?.push(url)
    },
    handleSearch(searchValue) {
      this.searchValue = searchValue?.trim() || ''
      if (searchValue) {
        this.searchUrl = this.searchUrl.replace('##SEARCH_VALUE##', searchValue);
        window.open(this.searchUrl, '_blank');
      }
    },
    handleUser() {
      // 判断状态，登录的话就是退出，退出的话就是登录
      if (state.isLogin) {
        logout()
      } else {
        this.routeTo('/login')
      }
    }
  }
}
</script>

<template>
  <div class="min-h-full min-w-full flex flex-col px-16 relativize">
<!--    <div class="absolute top-0 left-0 right-0 z-10 flex justify-between items-center">-->
<!--      <div v-if="state.isLogin">-->
<!--        <div class="px-4 py-2 bg-gray-200 rounded-lg hover:bg-gray-300 cursor-pointer select-none" @click="routeTo('/app')">-->
<!--          App-->
<!--        </div>-->
<!--      </div>-->
<!--    </div>-->

    <div class="clock">
      <TimeDisplay />
    </div>
    <div class="my-2 flex w-full h-full text-center justify-center items-center">
      <div>
        <SearchInput @enter="handleSearch" custom-class="sm:min-w-[400px] md:min-w-[500px] lg:min-w-[600px]"/>
      </div>
    </div>

    <template v-if="state.isLogin">
      <span class="text-center text-sm text-gray-400 cursor-pointer hover:underline" @click="$router.push('/app')">app</span>
    </template>


    <UserAvatar :login="state.isLogin" :avatar-url="state.user?.avatarUrl" @click="handleUser"/>
  </div>
</template>
