<script setup lang="ts">
import BackToTop from './components/BackToTop.vue'
</script>

<template>
  <div class="min-h-[100vh] px-16 py-4">
    <router-view/>
    <BackToTop />
  </div>
</template>