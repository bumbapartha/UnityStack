import { createApp } from 'vue'
import App from './core/App.vue'
import { createRouter } from './core/router.js'
import * as requirements from './modules/requirements/index.js'
import './core/ui/style.css'

const router = createRouter([requirements])

createApp(App).use(router).mount('#app')
