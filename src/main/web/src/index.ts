import { createApp } from 'vue';
import App from './App.vue';
import './styles/index.css';
import router from './router'
import toast from './components/toast'

const app = createApp(App);
app.use(router)
app.use(toast)
app.mount('#root');


// 定义局部自定义指令，这里是在 setup 标签下编写，指令名称以 v 开头，无需额外注册逻辑
const vClickOutside = {
    mounted(el: any, binding: any) {
        function eventHandler(e: MouseEvent) {
            if (el.contains(e.target)) {
                return false
            }
            // 如果绑定的参数是函数，正常情况也应该是函数，执行
            if (binding.value && typeof binding.value === 'function') {
                binding.value(e)
            }
        }
        // 用于销毁前注销事件监听
        el.__click_outside__ = eventHandler
        // 添加事件监听
        document.addEventListener('click', eventHandler)
    },
    beforeUnmount(el: any) {
        // 移除事件监听
        document.removeEventListener('click', el.__click_outside__)
        // 删除无用属性
        delete el.__click_outside__
    }
}
app.directive('click-outside', vClickOutside)
