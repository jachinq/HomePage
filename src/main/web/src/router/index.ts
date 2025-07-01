import {createRouter, createWebHistory} from 'vue-router'
import Home from '../views/Home.vue'
import AppSet from '../views/appset/AppSet.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Habit from '../views/habit/Habit.vue'
import User from '@/views/User.vue'
import {useGlobalConfigStore} from "@/stores/useGlobalConfigStore.ts";
import {watch} from "vue";

const routes = [
    {path: '/', name: '主页', component: Home},
    {path: '/login', name: '登录', component: Login},
    {path: '/register', name: '注册', component: Register},
    {path: '/app', name: '应用', component: AppSet},
    {path: '/habit', name: '习惯', component: Habit},
    {path: '/user', name: '用户', component: User},
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

const {state} = useGlobalConfigStore();
watch(()=> state.config, (value)=>{
    if (value) setBgUrl()
})

const setBgUrl = () => {
    // 判断一下地址是否为内网地址
    const url = new URL(window.location.href);
    console.log(router.currentRoute.value);

    if (url.pathname !== '/') {
        document.body.style.backgroundImage = 'linear-gradient(to bottom, #020917, #101725)'
    } else if (state.config?.bgUrl){
        document.body.style.backgroundImage = `url(${state.config?.bgUrl})`
        console.log("$$$$$$$$ ", {
            bgUrl: state.config?.bgUrl,
            url
        })
    }
}

router.beforeEach((to, _from, next) => {
    if (typeof to.name === "string") document.title = to.name;
    if (to.path !== '/') {
        document.body.style.backgroundImage = 'linear-gradient(to bottom, #020917, #101725)'
    } else if (state.config?.bgUrl){
        document.body.style.backgroundImage = `url(${state.config?.bgUrl})`
    }
    next()
})

export default router