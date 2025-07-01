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
    }
}
init()

export function useGlobalConfigStore() {
    const refresh = () => {
        init()
    }

    return {
        state,
        refresh,
    }
}

