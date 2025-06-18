// stores/useUserStore.js
import { reactive } from 'vue'

const state = reactive({
    user: null,
    isLogin: false
})

export function useUserStore() {
    function login(userData: any) {
        state.user = userData
        state.isLogin = true
    }

    function logout() {
        state.user = null
        state.isLogin = false
        console.log('logout', state)
    }

    return {
        state,
        login,
        logout
    }
}

