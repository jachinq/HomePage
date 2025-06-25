// stores/useUserStore.js
import {reactive} from 'vue'
import {validate} from "../api/AuthApi.ts";
import storage from "../utils/storage.ts";

interface User {
    avatarUrl: string
}

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
    const r = await validate({
        token, user: {}
    });
    if (r.success) {
        state.user = r.data
        state.isLogin = true
    } else {
        state.user = null
        state.isLogin = false
    }
}
validateToken();

export function useUserStore() {
    function login(userData: User) {
        state.user = userData
        state.isLogin = true
    }

    function logout() {
        state.user = null
        state.isLogin = false
        storage.removeToken()
    }

    return {
        state,
        login,
        logout,
        validateToken
    }
}

