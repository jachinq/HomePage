// stores/useUserStore.js
import { reactive } from 'vue'
import { validate } from "../api/AuthApi.ts";
import storage from "../utils/storage.ts";
import { User } from '@/interface/user.ts';
import { getUserInfo } from '@/api/userApi.ts';
import { useGlobalConfigStore } from './useGlobalConfigStore.ts';

const { resetConfig, initConfig } = useGlobalConfigStore()

const state = reactive<{
    user: User | null,
    isLogin: boolean
}>({
    user: null,
    isLogin: false
})

const validateToken = async () => {
    // 判断是否登录
    let token = storage.getToken();
    if (!token) {
        return
    }
    const result = await validate({ token });
    if (result.success) {
        state.isLogin = true
        const result = await getUserInfo({});
        if (result.success) {
            state.user = result.data
        }
    } else {
        state.user = null
        state.isLogin = false
    }
    // console.log(state)
}
validateToken();

export function useUserStore() {
    function login(result: {token: string, user: User}, router?: () => {}) {
        storage.setToken(result?.token || '') // 存储token
        state.user = result?.user || null
        state.isLogin = true
        initConfig()
        router && router()
    }

    function logout(router?: () => {}) {
        state.user = null
        state.isLogin = false
        storage.removeToken()
        router && router()
        resetConfig()
    }

    return {
        state,
        login,
        logout,
        validateToken
    }
}

