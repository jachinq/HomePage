// stores/useUserStore.js
import {reactive} from 'vue'
import {GlobalConfig} from "@/interface/global.ts";
import {getGlobalConfig} from "@/api/globalConfigApi.ts";

const state = reactive<{
    config: GlobalConfig | null
}>({
    config: null
})

const init = async () => {
    const result: any = await getGlobalConfig();
    if (result.success) {
        state.config = result.data;
        console.log("init globalConfigStore", state.config)
    }
}
init()

export function useGlobalConfigStore() {
    return { state }
}

