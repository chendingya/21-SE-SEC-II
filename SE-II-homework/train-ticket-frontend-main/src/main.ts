import {createApp} from 'vue'
import { createPinia } from 'pinia'
import { createRouter, createWebHistory } from 'vue-router'
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
// import 'element-plus/dist/index.css'
import "~/styles/index.scss";
import "uno.css";
// @ts-ignore
import routes from '~pages'
import App from './App.vue'


// routes.push( { path: '/order/:orderId', component: order },)

const router = createRouter({
    history: createWebHistory(),
    routes,
})




createApp(App).use(createPinia()).use(ElementPlus, {
    locale: zhCn,
}).use(router).mount('#app')
