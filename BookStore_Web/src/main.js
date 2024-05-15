import './assets/main.css'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap/dist/js/bootstrap'
import Notiflix from "notiflix";

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(createPinia())
app.use(router)

app.mount('#app')
Notiflix.Notify.init({
    position: 'right-bottom',
    width: '370px',
    distance: '30px',
    fontSize: '15px',
    timeout: 7000,
    messageMaxLength: 500
    // closeButton: true
})
