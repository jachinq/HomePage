<script setup lang="ts">
import { ref } from 'vue';
import { bing } from "../api/corsApi.js";
import SearchInput from "../components/SearchInput.vue";
import TimeDisplay from "../components/TimeDisplay.vue";
import UserAvatar from "../components/UserAvatar.vue";
import { useUserStore } from '../stores/useUserStore.js'
import { useRouter } from 'vue-router';

interface SearchText {
  Txt: string
}

const router = useRouter();
const { state, logout } = useUserStore()

const keyWordTemplate = "##SEARCH_VALUE##";
const searchUrl = 'https://www.bing.com/search?&form=QBLH&q=' + keyWordTemplate;
const searchValue = ref('');
const showTextSelect = ref(false);
const searchTexts = ref<SearchText[]>([]);
const selectedTextIndex = ref(-1);

const routeTo = (url: string) => router?.push(url);
const handleSearch = (searchValueTmp: string) => {
  // 优先拿入参
  let searchText = searchValueTmp || searchValue.value
  searchText = searchText?.trim() || ''
  if (selectedTextIndex.value > -1) {
    // 如果选择了提示词，则用提示词替换搜索文本
    const item = searchTexts.value[selectedTextIndex.value]
    searchText = item?.Txt
  }

  if (searchText) {
    window.open(searchUrl.replace(keyWordTemplate, searchText), '_blank');
  }
}
const handleUser = () => {
  // 判断状态，登录的话就是退出，退出的话就是登录
  if (state.isLogin) {
    logout()
  } else {
    routeTo('/login')
  }
}

const handleBingRecommend = async (keyword: string) => {
  // const url = "https://api.bing.com/qsonhs.aspx?q=" + key;
  searchValue.value = keyword // input事件刷新的搜索文本
  selectedTextIndex.value = -1 // 重新输入的话需要清空选择
  // console.log('handleBingRecommend', keyword, searchValue.value)
  if (!keyword) {
    searchTexts.value = []
    return
  }

  const result = await bing(keyword)
  if (result.success) {
    const data = JSON.parse(result.data)
    searchTexts.value = data?.AS?.Results[0]?.Suggests || []
    showTextSelect.value = searchTexts.value.length > 0
    // console.log('searchTexts', searchTexts.value.length, showTextSelect.value)
  }
}

const handleKeydown = (event: KeyboardEvent) => {
  const keyDown = event.key == "ArrowDown";
  const keyUp = event.key == "ArrowUp";
  if (keyDown || keyUp) {
    let length = searchTexts.value ? searchTexts.value.length : 0;
    let currentIndex = selectedTextIndex.value;
    // 处理上下键，选择提示词
    if (length > 0) {
      if (keyDown) {
        selectedTextIndex.value = (currentIndex + 1) % length
      } else {
        selectedTextIndex.value = (currentIndex - 1 + length) % length
      }
    }

  }
}

const selectedClass = (index: number) => {
  let className = selectedTextIndex.value == index ? 'bg-gray-600 ' : 'bg-gray-700 ';
  if (index == 0 && searchTexts.value.length == 1) {
    className += 'rounded-lg'
  } else if (index == 0) {
    className += 'rounded-tl-lg rounded-tr-lg'
  } else if (index == searchTexts.value.length - 1) {
    className += 'rounded-bl-lg rounded-br-lg'
  }

  return className
}
</script>

<template>
  <div class="min-h-[calc(100vh-140px)] min-w-full flex flex-col items-center justify-center px-16 relativize">
    <div class="mask w-full h-full absolute top-0 left-0 bg-gray-800 opacity-30"></div>

    <div class="z-10">
      <TimeDisplay/>
    </div>

    <div v-if="state.isLogin" class="flex gap-2 mt-1 z-10">
      <span class="text-center text-sm text-gray-400 cursor-pointer hover:underline"
            @click="$router.push('/app')">应用</span>
      <span class="text-center text-sm text-gray-400 cursor-pointer hover:underline"
            @click="$router.push('/habit')">习惯</span>
    </div>

    <div class="my-2 flex w-full h-full text-center justify-center items-center">
      <div class="relative">
        <SearchInput @search="handleBingRecommend" @enter="handleSearch" @keydown="handleKeydown"
                     custom-class="sm:min-w-[400px] md:min-w-[500px] lg:min-w-[600px]"/>
        <div v-if="showTextSelect" class="absolute top-12 left-0 right-0 z-10 flex flex-col justify-between rounded-lg"
             v-click-outside="()=>showTextSelect=false">
          <div v-for="(item, index) in searchTexts" @click="handleSearch(item.Txt)"
               class="px-4 py-2 hover:bg-gray-600 cursor-pointer select-none"
               :class="selectedClass(index)">
            <span>{{ item.Txt }}</span>
          </div>
        </div>
      </div>
    </div>


    <UserAvatar :login="state.isLogin" :avatar-url="state.user?.avatarUrl" @click="handleUser"/>
    
  </div>
</template>
