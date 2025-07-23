<template>
  <div class="my-2 flex w-full h-full text-center justify-center items-center">

    <div class="relative">

      <!-- 搜索输入框 -->
      <div class="relative">

        <!-- 当前搜索引擎图标 -->
        <div class="absolute left-2 top-1 z-10" v-if="inputFocus">
            <div @click.stop="showEngineSelect=!showEngineSelect" class="p-1">
              <div v-html="getCurrentEngine().icon" class="icon w-6 h-6 overflow-hidden middle-0 md:block cursor-pointer select-none"></div>
            </div>
        </div>
        <!-- 搜索引擎选择器 -->
        <div class="absolute left-0 top-12 z-10 rounded-lg bg-slate-700/50 backdrop-blur-sm p-2 flex flex-col gap-2" v-if="showEngineSelect" v-click-outside="() => showEngineSelect = false">
          <div v-for="engine in searchEngines" class="flex items-center px-1 hover:bg-gray-600 rounded">
            <div v-html="engine.icon" class="icon w-6 h-6 overflow-hidden middle-0 md:block cursor-pointer select-none"></div>
            <span @click="setEngine(engine.key)" class="cursor-pointer select-none text-gray-400">
              {{ engine.name }}
            </span>
          </div>
        </div>

        <input type="text" v-model="searchQuery" @input="handleRecommend(searchQuery)"  v-click-outside="() => inputFocus = false" @focus="inputFocus = true"
          @compositionstart="handleCompositionStart" @compositionend="handleCompositionEnd" @keydown="handleKeydown"
          :placeholder="inputFocus ? '' : '搜索'"
          class="group w-full px-16 py-2 text-gray-400 bg-gray-800/50 border rounded-lg focus:outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-500 backdrop-blur-sm text-center"
          :class="customClass" />

        <!-- 搜索按钮 -->
        <div class="absolute inset-y-0 right-0 flex items-center pr-3 cursor-pointer" v-if="inputFocus">

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
        <div v-for="(item, index) in searchTexts" :key="`${item.Txt}-${index}`" @click="handleSearch(item.Txt)"
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
import storage from '@/utils/storage.js';

interface SearchText {
  Txt: string
}

interface SearchEngine {
  key: string
  name: string
  searchUrl: string
  icon: string
}

// 搜索引擎配置
const keyWordTemplate = "##SEARCH_VALUE##";
const searchEngines: SearchEngine[] = [
  {
    key: 'bing',
    name: '必应',
    searchUrl: `https://www.bing.com/search?&form=QBLH&q=${keyWordTemplate}`,
    icon: '<svg t="1753095797159" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="48" height="48"><path d="M213.333333 770.986667 626.346667 546.986667 512 491.946667 442.453333 332.8 810.666667 452.266667 810.666667 631.466667 382.72 890.453333 213.333333 770.986667 213.333333 133.546667 384 192 384 618.666667 213.333333 770.986667Z" fill="#8a8a8a"></path></svg>'
  },
  {
    key: 'google',
    name: '谷歌',
    searchUrl: `https://www.google.com/search?q=${keyWordTemplate}`,
    icon: '<svg viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="48" height="48"><path d="M832.673684 524.638316a274.863158 274.863158 0 0 0-4.554105-50.472421h-304.505263v118.568421h177.717895a194.425263 194.425263 0 0 1-67.368422 87.066947l84.48 84.992a307.658105 307.658105 0 0 0 114.229895-240.154947z" fill="#8a8a8a"></path><path d="M523.614316 833.724632a307.577263 307.577263 0 0 0 222.019368-94.31579 316.820211 316.820211 0 0 1-27.297684 25.249684l-84.48-84.965052a207.683368 207.683368 0 0 0 16.491789-13.473685 189.386105 189.386105 0 0 1-298.792421-62.248421l-99.408842 66.964211a307.388632 307.388632 0 0 0 271.46779 162.789053z" fill="#8a8a8a"></path><path d="M351.043368 604.483368a188.469895 188.469895 0 0 1-6.170947-143.710315l-97.872842-72.111158A308.035368 308.035368 0 0 0 215.578947 524.126316a311.349895 311.349895 0 0 0 37.079579 146.297263z" fill="#8a8a8a"></path><path d="M344.360421 460.8a190.356211 190.356211 0 0 1 303.912421-80.357053l73.647158-92.725894A305.178947 305.178947 0 0 0 523.533474 215.578947a308.655158 308.655158 0 0 0-277.557895 173.594948z" fill="#8a8a8a"></path></svg>'
  },
  {
    key: 'baidu',
    name: '百度',
    searchUrl: `https://www.baidu.com/s?wd=${keyWordTemplate}`,
    icon: '<svg class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="48" height="48"><path d="M206.5777475 584.50443167c-52.79593594 0-94.10316656-62.16020344-94.10316656-141.57764625S153.78343812 301.34913917 206.5777475 301.34913917s94.0991025 62.15979656 94.0991025 141.57764625-41.31007594 141.57764625-94.0991025 141.57764625z m188.10553781-188.07912c-52.69310719 0-94.00643531-61.97324344-94.00643531-141.06025406S341.99017812 114.30764886 394.68328531 114.30764886s94.01171906 61.97324344 94.01171907 141.05740875-41.31414094 141.06025406-94.01171907 141.06025406z m235.34956782 0c-52.78780688 0-94.10194781-61.97324344-94.10194688-141.06025406S577.24504625 114.30764886 630.03285312 114.30764886s94.10194781 61.97324344 94.10194782 141.05740875-41.31414094 141.06025406-94.10194782 141.06025406zM818.23674875 584.50443167c-52.79187094 0-94.10194781-62.16020344-94.10194781-141.57764625s41.31007594-141.57764625 94.10194781-141.57764625S912.33869563 363.50893573 912.33869563 442.92678542s-41.31007594 141.57764625-94.10194688 141.57764625zM677.08585906 866.61924917c-40.84267594 0-68.45994937-13.82286188-92.78916093-25.99966125-22.58154094-11.28670594-42.06198094-21.02082938-71.89026282-21.02082938-29.69009344 0-49.16646937 9.73412344-71.75207437 21.02082938-24.42106688 12.17679937-52.08873844 25.99966125-92.92613063 25.99966125-26.16142219 0-50.76660469-14.24555438-67.61375062-39.07468219-29.49744281-43.54140563-29.968095-109.5993225-1.31725688-181.25791687 51.61727344-128.92938187 136.72968656-202.83962719 233.564505-202.83962813s181.98950062 73.91024531 233.55800157 202.83962813c28.65368344 71.65859438 28.18221844 137.71651125-1.31685 181.25791687-16.7979675 24.82912781-41.45232844 39.07468219-67.61456438 39.07468219h0.09754406zM512.40643531 772.57826698" fill="#8a8a8a"></path><path d="M434.34282781 649.18046886q23.81710406 2.07282 25.74320063 44.30550375-1.9265025 41.224725-25.74320063 43.27722281-27.66279469 0-26.69507156-43.27722281-0.96081281-44.29127813 26.69507156-44.30550375z m-7.62716343-25.75986469q-53.40558844 3.09094031-57.20331844 70.06536844 3.79813687 65.95224375 53.38973719 68.00474156 26.68328531 0 37.18394531-20.60626594 0 3.08890781 0.95105906 9.2707875v9.27485157h37.18475813q-0.96325125-12.36375938-0.95512219-26.78814563v-163.83403875h-37.180695v73.15834031q-12.40643531-17.50922906-33.37036406-18.54563906z m228.2397975 136.98086344v-12.36375938q-1.03641-7.2-1.02828188-11.33547844v-109.2213375h-40.18425v80.36850094q-1.04250656 27.82455562-23.69923781 29.8811175-22.67908594 0-23.69923875-29.8811175v-80.36850094h-39.15596907v86.55444469q0 12.36375938 2.06062688 20.60626594 8.23641 24.72751875 44.30550375 27.82049156 26.78408156 0 40.188315-18.54563906l2.06062594 16.48501219h39.15190593z" fill="#8a8a8a"></path></svg>'
  },
  {
    key: 'bilibili',
    name: '哔哩哔哩',
    searchUrl: `https://search.bilibili.com/all?keyword=${keyWordTemplate}`,
    icon: '<svg class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="200" height="200"><path d="M900.8 388.544c-0.384-68.304-57.6-124.224-122.832-122.928-44.064 0.864-85.584 0-135.6 0 21.36-22.464 41.424-41.856 59.856-60.576 11.472-11.664 10.128-22.944-0.336-33.6s-21.024-17.232-34.464-2.736-90.816 96.816-90.816 96.816H472.688s-70.512-68.496-97.296-96c-12.96-13.296-24-15.888-37.824-1.584s-11.232 25.488 2.016 38.688c17.808 17.712 34.032 37.056 53.472 58.512h-148.8a80.16 80.16 0 0 0-36.768 8.4c-46.416 22.944-68.976 61.392-69.552 111.408C136.784 488 137.6 591.296 137.6 694.4c0 91.968 44.496 139.2 136.032 143.664 8.064 0.432 19.2 0 19.2 0s4.224 40.944 40.992 40.992c34.08 0 40.752-41.616 40.752-41.616l298.656 1.584s7.776 40.992 38.4 40.992c48 0 43.968-40.992 43.968-40.992s62.88 5.712 95.184-24c30.912-28.464 48.336-62.4 49.008-102.48 2.16-108 1.44-216 1.008-324z m-79.584 336c0 28.8-7.968 35.904-37.92 35.904H262.592c-29.52 0-38.16-8.592-38.4-38.4V383.792c0-27.936 9.264-37.584 37.152-37.632q261.504-0.288 523.2 0c28.416 0 36.432 7.536 36.528 35.376q0.432 171.696-0.048 343.248z" fill="#8a8a8a"></path><path d="M599.936 627.2s-27.312 26.112-46.176 14.4c-14.4-8.64-21.36-23.184-29.568-38.976l-0.288 0.48-0.288-0.48c-8.256 15.792-15.408 30.336-29.568 38.976-18.864 11.52-46.176-14.4-46.176-14.4l-22.656 15.648s-0.528 0.384 2.832 4.8c21.648 28.464 59.184 40.272 87.216 21.36a24.432 24.432 0 0 1 8.688-4.272 24.432 24.432 0 0 1 8.688 4.272c28.032 18.96 65.856 6.24 87.216-21.36 3.408-4.416 2.832-4.8 2.832-4.8zM280.1408 476.048l160.0176-30.0624 11.6352 61.9392-160.0176 30.0624zM763.5392 537.92l-160.0176-30.0624 11.6352-61.9392 160.0176 30.0624z" fill="#8a8a8a"></path></svg>'
  }
]
const currentSearchEngine = ref(storage.getItem('searchEngine') || 'bing'); // 默认使用必应
const searchValue = ref('');
const showTextSelect = ref(false);
const searchTexts = ref<SearchText[]>([]);
const selectedTextIndex = ref(-1);
const inputFocus = ref(false); // 是否显示搜索引擎图标
const showEngineSelect = ref(false); // 是否显示搜索引擎选择

// 获取当前搜索引擎配置
const getCurrentEngine = () => {
  return searchEngines.find(engine => engine.key === currentSearchEngine.value) || searchEngines[0]
}
const setEngine = (key: string) => {
  currentSearchEngine.value = key
  showEngineSelect.value = false
  storage.setItem('searchEngine', key)
}

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
    const currentEngine = getCurrentEngine()
    const searchUrl = currentEngine.searchUrl.replace(keyWordTemplate, encodeURIComponent(searchText))
    window.open(searchUrl, '_blank');

    // 隐藏提示词列表
    showTextSelect.value = false
    selectedTextIndex.value = -1
  }
}

const handleRecommend = async (keyword: string) => {
  searchValue.value = keyword // input事件刷新的搜索文本
  selectedTextIndex.value = -1 // 重新输入的话需要清空选择

  if (!keyword) {
    searchTexts.value = []
    showTextSelect.value = false
    return
  }

  try {
    const result = await bing(keyword)
    if (result.success) {
      const data = JSON.parse(result.data)
      searchTexts.value = data?.AS?.Results[0]?.Suggests || []
      showTextSelect.value = searchTexts.value.length > 0
    }
  } catch (error) {
    console.error('获取搜索建议失败:', error)
    searchTexts.value = []
    showTextSelect.value = false
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