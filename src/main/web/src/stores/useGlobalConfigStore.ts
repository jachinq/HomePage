// stores/useUserStore.js
import { reactive } from 'vue'
import { GlobalConfig } from "@/interface/global.ts";
import { getGlobalConfig } from "@/api/globalConfigApi.ts";
import defaultBgUrl from "@/assets/defaultBg.webp";

const defaultConfig = {
    id: 0,
    userId: 0,
    bgUrl: defaultBgUrl
}
const state = reactive<{
    config: GlobalConfig
}>({
    config: defaultConfig
})

const reset = () => {
    state.config = defaultConfig
}

const init = async () => {
    const result: any = await getGlobalConfig();
    if (result.success) {
        if (result.data) {
            state.config = result.data;
            if (!state.config.bgUrl || state.config.bgUrl === "") {
                state.config.bgUrl = defaultConfig.bgUrl
            }
        } else {
            reset()
        }
        // console.log("init globalConfigStore", state.config)
    } else {
        reset()
    }
}
init()

export function useGlobalConfigStore() {
    return { golabalConfig: state, initConfig: init, resetConfig: reset }
}

