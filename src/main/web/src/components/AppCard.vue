<template>

    <div class="w-full sm:1/2 md:w-1/4 xl:w-1/6 2xl:w-1/8 min-w-[250px]">
        <div class="bg-gray-800 rounded-lg shadow-lg overflow-hidden hover:bg-slate-700 cursor-pointer"
            @click="openApp(app)">
            <div class="px-6 py-4 relative">
                <div class="flex items-center mb-2 gap-2">
                    <template v-if="props.app.icon">
                        <div v-html="props.app.icon" class="icon w-6 h-6 overflow-hidden middle-0 md:block"></div>
                    </template>

                    <div class="font-bold text-xl hover:underline">{{ props.app.name }}</div>
                </div>
                <span class="text-gray-400 text-sm" :title="props.app.description">
                    {{ props.app.description }}
                </span>

                <div class="absolute top-1/5 right-4 z-50 mx-2">
                    <slot></slot>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useToast } from "@/components/toast";
import { AppSet } from '@/interface/appset';

const toast = useToast()

const configData = ref<any>({})

const props = defineProps<{
    app: AppSet,
    innerMode: boolean,
    innerDomain: String,
    outerDomain: String,
}>()

const innerDomainProc = (innerDomain: String): String => {
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
    return `//${outerUrl}.${props.outerDomain}`;
};

const openApp = (app: AppSet) => {
    // console.log(configData.value)
    if (!openWithInnerMode(app) && !openWithOuterMode(app)) {
        toast.error("未配置内网地址或端口，无法使用内网模式打开")
    }
}

const openWithInnerMode = (app: AppSet) => {
    if (props.innerMode) {
        let innerDomain = innerDomainProc(props.innerDomain);
        if (innerDomain && app.port) {
            window.open(`${innerDomain}:${app.port}`, "_blank");
            return true;
        } else {
            return false;
        }
    }
    return true;
}

const openWithOuterMode = (app: AppSet) => {
    if (!app.outerUrl || !props.outerDomain) {
        toast.error("未配置外网地址")
        return false;
    }
    console.log(outerDomainProc(app.outerUrl))
    window.open(outerDomainProc(app.outerUrl), "_blank");
    return true;
}
</script>

<style>
.icon svg {
    width: 100%;
    height: 100%;
    object-fit: contain;
}
</style>