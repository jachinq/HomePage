<template>
  <div class="fixed top-16 right-16 z-50 flex items-center justify-center cursor-pointer" :class="customClass"
    @click="onClick">
    <template v-if="avatarUrl">
      <img :src="avatarUrl" alt="用户头像"
        class="w-12 h-12 rounded-full border-2 border-gray-200 shadow object-cover bg-gray-800 hover:border-blue-500 hover:shadow-lg" />
    </template>
    <template v-else>
      <div
        class="w-16 h-16 rounded-full flex items-center hover:bg-sky-700 justify-center border-2 border-gray-400 shadow text-2xl font-bold select-none"
        :class="login ? 'bg-sky-800 text-gray-200' : 'bg-gray-600 text-gray-200'">
        <template v-if="login">
          <Tooltips text="点击退出登录">
            <img :src="whitecat" alt="白猫" class="" />
          </Tooltips>
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
import Tooltips from './Tooltips.vue';
import whitecat from '@/assets/whitecat.svg'
defineProps({
  login: {
    type: Boolean,
    default: false,
  },
  avatarUrl: {
    type: [String, null],
    default: '',
  },
  customClass: {
    type: String,
    default: '',
  },
})

const emits = defineEmits(['click'])

function onClick() {
  emits('click')
}
</script>

<style scoped></style>