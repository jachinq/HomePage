<template>
  <div class="cursor-pointer" :class="customClass"
    @click="onClick">
    <template v-if="avatar">
      <img :src="avatar" alt="用户头像"
        class="w-12 h-12 rounded-full border-4 border-gray-200 shadow object-cover hover:border-blue-500 hover:shadow-lg" 
        :class="{
          'w-48 h-48': size === 'large',
          'w-24 h-24': size === 'default',
          'w-12 h-12': size === 'small',
          'bg-sky-800 text-gray-200' : state?.isLogin,
          'bg-gray-600 text-gray-200': !state?.isLogin
        }"
        />
        
    </template>
    <template v-else>
      <div
        class="rounded-full flex items-center hover:bg-sky-700 justify-center border-4 border-gray-400 shadow text-2xl font-bold select-none"
        :class="{
          'w-48 h-48': size === 'large',
          'w-24 h-24': size === 'default',
          'w-12 h-12': size === 'small',
          'bg-sky-800 text-gray-200' : state?.isLogin,
          'bg-gray-600 text-gray-200': !state?.isLogin
        }">
        <template v-if="state?.isLogin">
          <!-- <Tooltips text="点击退出登录"> -->
            <img :src="whitecat" alt="白猫" class="" />
          <!-- </Tooltips> -->
        </template>
        <template v-else>
          <Tooltips text="点击登录">
            <span class="text-4xl">U</span>
          </Tooltips>
        </template>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/useUserStore';
import Tooltips from './Tooltips.vue';
import whitecat from '@/assets/whitecat.svg'

const { state } = useUserStore()

defineProps({
  avatar: {
    type: [String, null],
    default: '',
  },
  customClass: {
    type: String,
    default: '',
  },
  size: {
    type: [String, null],
    default: 'default',
  }
})

const emits = defineEmits(['click'])

function onClick() {
  emits('click')
}
</script>

<style scoped></style>