<script setup>
import { bing } from "../api/corsApi.js";
import SearchInput from "../components/SearchInput.vue";
import TimeDisplay from "../components/TimeDisplay.vue";
import UserAvatar from "../components/UserAvatar.vue";


</script>
<script>
import { useUserStore } from '../stores/useUserStore.js'
import fetchUtil from "../utils/fetchUtil.js";
const { state, login, logout } = useUserStore()
console.log('home', state)
export default {
  name: 'Home',
  data() {
    return {
      searchValue: '',
      searchUrl: 'https://www.bing.com/search?&form=QBLH&q=##SEARCH_VALUE##',
      showBingList: false,
      bingTxts: [],
      selectedBingIndex: -1,
    }
  },
  methods: {
    routeTo(url) {
      this.$router?.push(url)
    },
    handleSearch(searchValue1) {
      console.log('handleSearch', searchValue1)
      // 优先拿入参
      let searchValue = searchValue1 || this.searchValue
      searchValue = searchValue?.trim() || ''
      if (this.selectedBingIndex > -1) {
        // 如果选择了提示词，则用提示词替换搜索文本
        const item = this.bingTxts[this.selectedBingIndex]
        searchValue = item.Txt
      }

      if (searchValue) {
        let searchUrl = this.searchUrl.replace('##SEARCH_VALUE##', searchValue);
        window.open(searchUrl, '_blank');
      }
    },
    handleUser() {
      // 判断状态，登录的话就是退出，退出的话就是登录
      if (state.isLogin) {
        logout()
      } else {
        this.routeTo('/login')
      }
    },
    async handleBingRecommend(keyword) {
      // const url = "https://api.bing.com/qsonhs.aspx?q=" + key;
      this.searchValue = keyword // input事件刷新的搜索文本
      this.selectedBingIndex = -1 // 重新输入的话需要清空选择
      console.log('handleBingRecommend', keyword, this.searchValue)
      if (!keyword) {
        this.bingTxts = []
        return
      }

      const result = await bing(keyword)
      if (result.success) {
        const data = JSON.parse(result.data)
        this.bingTxts = data?.AS?.Results[0]?.Suggests || []
        this.showBingList = this.bingTxts.length > 0
        console.log('bingTxts', this.bingTxts.length, this.showBingList)
      }
    },
    handleKeydown(event) {
      const keyDown = event.key == "ArrowDown";
      const keyUp = event.key == "ArrowUp";
      if (keyDown || keyUp) {
        // 处理上下键，选择提示词
        if (this.bingTxts && this.bingTxts.length > 0) {
          if (keyDown) {
            this.selectedBingIndex = (this.selectedBingIndex + 1) % this.bingTxts.length
          } else {
            this.selectedBingIndex = (this.selectedBingIndex - 1 + this.bingTxts.length) % this.bingTxts.length
          }
        }
        
      }
    },
    handleClickOutside() {
      console.log('handleClickOutside')
      this.showBingList = false
    },
    selectedClass(index) {
      let className = this.selectedBingIndex == index ? 'bg-gray-600 ' : 'bg-gray-700 ';
      if (index == 0 && this.bingTxts.length == 1) {
        className +='rounded-lg'
      }
      else if (index == 0) {
        className +='rounded-tl-lg rounded-tr-lg'
      }
      else if (index == this.bingTxts.length - 1) {
        className +='rounded-bl-lg rounded-br-lg'
      }
      
      return className
    }
  }
}
</script>

<template>
  <div class="min-h-[calc(100vh-140px)] min-w-full flex flex-col items-center justify-center px-16 relativize">
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
    
    <template v-if="state.isLogin">
      <span class="text-center text-sm text-gray-400 cursor-pointer hover:underline"
        @click="$router.push('/app')">app</span>
    </template>
    
    <div class="my-2 flex w-full h-full text-center justify-center items-center">
      <div class="relative">
        <SearchInput @search="handleBingRecommend" @enter="handleSearch" @keydown="handleKeydown"
          custom-class="sm:min-w-[400px] md:min-w-[500px] lg:min-w-[600px]" />
          <div v-if="showBingList" class="absolute top-12 left-0 right-0 z-10 flex flex-col justify-between rounded-lg" v-click-outside="handleClickOutside">
            <div v-for="(item, index) in bingTxts" @click="handleSearch(item.Txt)" 
              class="px-4 py-2 hover:bg-gray-600 cursor-pointer select-none" 
              :class="selectedClass(index)">
              <span>{{ item.Txt }}</span>
            </div>
          </div>
      </div>
    </div>



    <UserAvatar :login="state.isLogin" :avatar-url="state.user?.avatarUrl" @click="handleUser" />
  </div>
</template>
