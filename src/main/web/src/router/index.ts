import {createRouter, createWebHistory} from 'vue-router'
import Home from '../views/Home.vue'
import AppSet from '../views/appset/AppSet.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Habit from '../views/habit/Habit.vue'
import TimelineManager from '../views/timeline/TimelineManager.vue'
import TimelineDisplay from '../views/timeline/TimelineDisplay.vue'
import User from '@/views/User.vue'

const routes = [
    {path: '/', name: '主页', component: Home},
    {path: '/login', name: '登录', component: Login},
    {path: '/register', name: '注册', component: Register},
    {path: '/app', name: '应用', component: AppSet},
    {path: '/habit', name: '习惯', component: Habit},
    {path: '/timeline/manager', name: '时间线', component: TimelineManager},
    {path: '/timeline/', name: '时间线展示', component: TimelineDisplay},
    {path: '/user', name: '用户', component: User},
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, _from, next) => {
    if (typeof to.name === "string") document.title = to.name;
    next()
})

export default router