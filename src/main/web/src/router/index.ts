import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import AppSet from '../views/AppSet.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'

const routes = [
    { path: '/', name: 'Home', component: Home },
    { path: '/app', name: 'App', component: AppSet },
    { path: '/login', name: 'Login', component: Login },
    { path: '/register', name: 'Register', component: Register },
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router