import { createApp } from 'vue';
import App from './App.vue';
import './styles/index.css';
import router from './router'
import toast from './components/toast'

const app = createApp(App);
app.use(router)
app.use(toast)
app.mount('#root');
