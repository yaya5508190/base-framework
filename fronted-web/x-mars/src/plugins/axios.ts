import type { App } from 'vue'
import axios from 'axios'

const instance = axios.create({
  baseURL: './943a6b1b-222a-4a99-b900-3744271480e6',
  timeout: 5000,
})

export default {
  install (app: App) {
    app.config.globalProperties.$axios = instance
  },
}

export { instance as axiosInstance }
