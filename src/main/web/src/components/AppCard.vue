
<script setup lang="ts">
import {useToast} from "@/components/toast";
import {AppConfig, AppSet} from '@/interface/appset';
import appIcon from "@/assets/unknow.svg";

const toast = useToast()

const props = defineProps<{
  app: AppSet,
  configData: AppConfig,
}>()

const innerDomainProc = (innerDomain: String | undefined): String => {
  if (!innerDomain) return "";
  if (innerDomain.startsWith("http://") || innerDomain.startsWith("https://")) {
    innerDomain = innerDomain.replace("http://", "//").replace("https://", "//");
  } else if (!innerDomain.startsWith("//")) {
    innerDomain = `//${innerDomain}`;
  }
  if (innerDomain.endsWith("/")) {
    innerDomain = innerDomain.slice(0, -1);
  }
  return innerDomain;
}

const outerDomainProc = (outerUrl: string): string => {
  if (!outerUrl) return "";
  if (outerUrl.endsWith("/")) {
    outerUrl = outerUrl.slice(0, -1);
  }
  if (outerUrl.startsWith("http://") || outerUrl.startsWith("https://")) {
    return outerUrl;
  }
  // 没有带 http:// 或 https:// 则认为是相对路径
  return `//${outerUrl}.${props.configData.outerDomain}`;
};

const openApp = (app: AppSet) => {
  // console.log(configData.value)
  if (openWithInnerMode(app)){
    return;
  } 
  if (openWithOuterMode(app)) {
    return
  }
  toast.error("未正确配置端口或内网域名或外网地址，无法正常打开")
  // console.log(app)
}

const openWithInnerMode = (app: AppSet) => {
  if (props.configData.innerMode && app.port) {
    let innerDomain = innerDomainProc(props.configData.innerDomain);
    if (innerDomain && app.port) {
      window.open(`${innerDomain}:${app.port}`, "_blank");
      return true;
    } else {
      return false;
    }
  }
  // console.log("innerMode is false")
  return false;
}

const openWithOuterMode = (app: AppSet) => {
  if (!app.outerUrl || !props.configData.outerDomain) {
    // toast.error("未配置外网地址")
    return false;
  }
  // console.log(outerDomainProc(app.outerUrl))
  window.open(outerDomainProc(app.outerUrl), "_blank");
  return true;
}
</script>

<template>

  <div class="w-full sm:w-1/3 md:w-1/4 lg:w-1/6 xl:w-1/7 2xl:w-1/9 min-w-[200px]">
    <div class="group bg-gray-800 rounded-lg shadow-lg overflow-hidden hover:bg-slate-700 cursor-pointer"
         @click="openApp(app)">
      <div class="px-6 py-4 relative">
        <div class="flex items-center mb-2 gap-2">
          <template v-if="props.app.icon">
            <div v-if="props.app.icon.startsWith('http://') || props.app.icon.startsWith('https://') || props.app.icon.startsWith('data:image/')">
                <img :src="props.app.icon" class="icon w-12 h-12 overflow-hidden middle-0 md:block" alt="">
            </div>
            <div v-else v-html="props.app.icon" class="icon w-12 h-12 overflow-hidden middle-0 md:block"></div>
          </template>
          <template v-else>
            <div class="icon w-12 h-12 overflow-hidden middle-0 md:block">
              <img :src="appIcon" alt="">
            </div>
          </template>

          <span class="font-bold text-xl group-hover:underline max-w-full overflow-hidden whitespace-nowrap">{{ props.app.name }}</span>
        </div>
        <span class="text-gray-400 text-sm max-w-full overflow-hidden whitespace-nowrap" :title="props.app.description">
                    {{ props.app.description }}
                </span>

        <div class="absolute top-1 right-[-4px] z-50 mx-2">
          <slot></slot>
        </div>
      </div>
    </div>
  </div>
</template>

<style>
.icon svg {
  width: 100%;
  height: 100%;
  object-fit: contain;
}
</style>