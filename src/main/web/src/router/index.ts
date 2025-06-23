import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import AppSet from '../views/AppSet.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Habit from '../views/habit/Habit.vue'

const routes = [
    { path: '/', name: 'Home', component: Home },
    { path: '/login', name: 'Login', component: Login },
    { path: '/register', name: 'Register', component: Register },
    { path: '/app', name: 'App', component: AppSet },
    { path: '/habit', name: 'Habit', component: Habit },
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router