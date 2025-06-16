import { createApp } from 'vue';
import App from './App.vue';
import './styles/index.css';
import ToastPlugin from './components/Toast';

const app = createApp(App);
app.use(ToastPlugin);
app.mount('#root');
