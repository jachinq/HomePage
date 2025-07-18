import {createRouter, createWebHistory} from 'vue-router'
import Home from '../views/Home.vue'
import AppSet from '../views/appset/AppSet.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Habit from '../views/habit/Habit.vue'
import Timeline from '../views/timeline/Timeline.vue'
import TimelineDisplay from '../views/timeline/TimelineDisplay.vue'
import User from '@/views/User.vue'
import {useGlobalConfigStore} from "@/stores/useGlobalConfigStore.ts";
import {watch} from "vue";

const routes = [
    {path: '/', name: '主页', component: Home},
    {path: '/login', name: '登录', component: Login},
    {path: '/register', name: '注册', component: Register},
    {path: '/app', name: '应用', component: AppSet},
    {path: '/habit', name: '习惯', component: Habit},
    {path: '/timeline', name: '时间线', component: Timeline},
    {path: '/timeline/display', name: '时间线展示', component: TimelineDisplay},
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

const setBgUrl = (pathname: string = window.location.pathname) => {
    const defaultBg = () => {
        document.body.style.backgroundImage = 'linear-gradient(to bottom, #020917, #101725)'
    }
    // 判断一下地址是否为内网地址
    if (pathname !== '/') {
        defaultBg();
    } else if (state.config?.bgUrl) {
        document.body.style.backgroundImage = `url(${state.config?.bgUrl})`
    } else {
        defaultBg();
    }
}

router.beforeEach((to, _from, next) => {
    if (typeof to.name === "string") document.title = to.name;
    setBgUrl(to.path);
    next()
})

export default router