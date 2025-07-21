<template>
  <div class="my-2 flex w-full h-full text-center justify-center items-center">

    <div class="relative">
      <!-- 搜索输入框 -->
      <div class="relative">
        <input type="text" v-model="searchQuery" @input="handleBingRecommend(searchQuery)"
          @compositionstart="handleCompositionStart" @compositionend="handleCompositionEnd" @keydown="handleKeydown"
          placeholder="搜索..."
          class="group w-full px-4 py-2 text-gray-400 bg-gray-800/50 border rounded-lg focus:outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-500 backdrop-blur-sm"
          :class="customClass" />

        <!-- 搜索按钮 -->
        <div class="absolute inset-y-0 right-0 flex items-center pr-3 cursor-pointer">

          <svg @click="searchQuery = ''" v-show="searchQuery.length > 0" class="w-5 h-5 fill-gray-400 mr-2"
            viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg">
            <path
              d="M556.8 512l262.4-262.4c12.8-12.8 12.8-32 0-44.8s-32-12.8-44.8 0L512 467.2 249.6 198.4c-12.8-6.4-38.4-6.4-51.2 0s-6.4 38.4 0 51.2L467.2 512l-262.4 262.4c-12.8 12.8-12.8 32 0 44.8 0 12.8 12.8 12.8 19.2 12.8s19.2 0 25.6-6.4L512 556.8l262.4 262.4c6.4 6.4 12.8 6.4 25.6 6.4s19.2 0 25.6-6.4c12.8-12.8 12.8-32 0-44.8L556.8 512z">
            </path>
          </svg>

          <svg @click="handleSearch()" class="w-5 h-5 text-gray-400" fill="none" stroke="currentColor"
            viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
          </svg>
        </div>
      </div>

      <!-- 提示词 -->
      <div v-if="showTextSelect" class="absolute top-12 left-0 right-0 z-10 flex flex-col justify-between rounded-lg"
        v-click-outside="() => showTextSelect = false">
        <div v-for="(item, index) in searchTexts" @click="handleSearch(item.Txt)"
          class="px-4 py-2 hover:bg-gray-600 cursor-pointer select-none backdrop-blur-sm" :class="selectedClass(index)">
          <span>{{ item.Txt }}</span>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { bing } from "../api/corsApi.js";

interface SearchText {
  Txt: string
}

const keyWordTemplate = "##SEARCH_VALUE##";
const searchUrl = 'https://www.bing.com/search?&form=QBLH&q=' + keyWordTemplate; // 搜索引擎
const searchValue = ref('');
const showTextSelect = ref(false);
const searchTexts = ref<SearchText[]>([]);
const selectedTextIndex = ref(-1);

const selectedClass = (index: number) => {
  let className = selectedTextIndex.value == index ? 'bg-gray-600/50 ' : 'bg-gray-700/50 ';
  if (index == 0 && searchTexts.value.length == 1) {
    className += 'rounded-lg'
  } else if (index == 0) {
    className += 'rounded-tl-lg rounded-tr-lg'
  } else if (index == searchTexts.value.length - 1) {
    className += 'rounded-bl-lg rounded-br-lg'
  }

  return className
}

defineProps({
  customClass: {
    type: [String, null],
    default: () => '',
  },
})

const searchQuery = ref('')
const isComposing = ref(false)

const handleCompositionStart = () => {
  isComposing.value = true
}

const handleCompositionEnd = () => {
  isComposing.value = false
  handleSearch()
}

const handleSearch = (searchValueTmp: string = '') => {
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
  // 处理回车
  if (event.key === 'Enter') {
    handleSearch(searchQuery.value)
    return
  }

  // 处理上下键
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
</script>