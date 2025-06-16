import { createRouter, createWebHistory } from 'vue-router'
import AppSet from '../views/AppSet.vue'

const routes = [
    {
        path: '/',
        name: 'Home',
        component: AppSet
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router