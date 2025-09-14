// Plugins
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import Vue from '@vitejs/plugin-vue'
import Vuetify, { transformAssetUrls } from 'vite-plugin-vuetify'
import Fonts from 'unplugin-fonts/vite'

// Utilities
import { defineConfig } from 'vite'
import { fileURLToPath, URL } from 'node:url'
import {federation} from "@module-federation/vite";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    Vue({
      template: { transformAssetUrls },
    }),
    // https://github.com/vuetifyjs/vuetify-loader/tree/master/packages/vite-plugin#readme
    Vuetify(),
    Components(),
    AutoImport({
      imports: [
        'vue',
        {
          '@/plugins/axios.ts': ['axiosInstance'],
        },
      ],
      dts: 'src/auto-imports.d.ts',
      eslintrc: {
        enabled: true,
      },
      vueTemplate: true,
    }),
    Fonts({
      fontsource: {
        families: [
          {
            name: 'Roboto',
            weights: [100, 300, 400, 500, 700, 900],
            styles: ['normal', 'italic'],
          },
        ],
      },
    }),
    federation({
      name: 'x-mars',
      exposes: {
        './App': './src/exposes/App/mount.ts',
        './App1': './src/exposes/App1/mount.ts',
      },
      filename: 'remoteEntry.js',
      manifest: true,
      shared: {
        vue: { singleton: true, requiredVersion: '^3.4.0' },
      },
    }),
  ],
  optimizeDeps: {
    exclude: ['vuetify'],
  },
  define: { 'process.env': {} },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('src', import.meta.url)),
    },
    extensions: [
      '.js',
      '.json',
      '.jsx',
      '.mjs',
      '.ts',
      '.tsx',
      '.vue',
    ],
  },
  server: {
    port: 3001,
    origin: 'http://localhost:3001',
    proxy: {
      // 以 /api 开头的请求转发到后端
      '/943a6b1b-222a-4a99-b900-3744271480e6/api': {
        target: 'http://127.0.0.1:8080/', // 你的后端
        changeOrigin: true,               // 修改 Origin 为 target
      },
    },
  },
  css: {
    preprocessorOptions: {
      sass: {
        api: 'modern-compiler',
      },
      scss: {
        api: 'modern-compiler',
      },
    },
  },
  base: '/943a6b1b-222a-4a99-b900-3744271480e6/',
  // base: '/',
  build: {
    target: 'esnext' ,
    outDir: 'dist',
    assetsDir: '',   // 👈 静态资源直接输出在 dist 根目录
  },
})
