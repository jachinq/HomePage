<template>

    <div class="w-full md:w-1/4 xl:w-1/6 mr-4 my-2">
        <div class="bg-gray-800 rounded-lg shadow-lg overflow-hidden">
            <div class="px-6 py-4 relative">
                <div class="font-bold text-xl mb-2 cursor-pointer hover:underline" @click="openApp(app)">{{
                    props.app.name
                    }}
                </div>
                <p class="text-gray-500 text-base">
                    {{ props.app.description }}
                </p>

                <div class="absolute top-1/5 right-4 z-50 mx-2">
                    <slot></slot>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { defineProps, ref, getCurrentInstance } from 'vue'

interface App {
    name: string
    description: string
    url: string,
    port: number,
    outerUrl: string,
}

const context = getCurrentInstance()?.appContext.config.globalProperties;
const toast = context?.$toast;

const configData = ref<any>({})

const props = defineProps<{
    app: App,
    innerMode: boolean,
}>()

const openApp = (app: App) => {
      console.log(app)
      if (props.innerMode) {
        let { innerDomain = '' } = configData.value;
        if (innerDomain.startsWith("http://") || innerDomain.startsWith("https://")) {
          innerDomain = innerDomain.replace("http://", "//").replace("https://", "//");
        } else if (!innerDomain.startsWith("//")) {
          innerDomain = `//${innerDomain}`;
        }
        if (innerDomain.endsWith("/")) {
          innerDomain = innerDomain.slice(0, -1);
        }
        if (configData.value, innerDomain && app.port) {

          window.open(`${innerDomain}:${app.port}`, "_blank");
        } else {
          toast.error("未配置内网地址或端口，无法使用内网模式打开")
        }
      } else {
        if (!app.outerUrl) {
          toast.error("未配置外网地址")
          return
        }
        window.open(app.outerUrl, "_blank");
      }
    }
</script>