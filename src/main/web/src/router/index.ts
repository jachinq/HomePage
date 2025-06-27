import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import AppSet from '../views/appset/AppSet.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Habit from '../views/habit/Habit.vue'
import User from '@/views/User.vue'

const routes = [
    { path: '/', name: 'Home', component: Home, meta: { background: 'http://192.168.2.128:10016/res/202408/9104f3c4-f3bc-4d5e-b3de-466324ae3beb.webp' } },
    { path: '/login', name: 'Login', component: Login },
    { path: '/register', name: 'Register', component: Register },
    { path: '/app', name: 'App', component: AppSet },
    { path: '/habit', name: 'Habit', component: Habit },
    { path: '/user', name: 'User', component: User },
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, _from, next) => {
    console.log(to)
    if (to.meta.background) {
        document.body.style.backgroundImage = `url(${to.meta.background})`
    } else {
        document.body.style.backgroundImage = 'linear-gradient(to bottom, #020917, #101725)'
    }
    next()
})

export default router