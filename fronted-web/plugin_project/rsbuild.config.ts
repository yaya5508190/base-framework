import { defineConfig } from '@rsbuild/core';
import { pluginReact } from '@rsbuild/plugin-react';
import { pluginModuleFederation } from '@module-federation/rsbuild-plugin';
import moduleFederationConfig from './module-federation.config';

export default defineConfig({
  plugins: [pluginReact(), pluginModuleFederation(moduleFederationConfig)],
  server: {
    port: 3001,
  },
  output: {
    // 让入口 html 里引用的 JS/CSS 以及动态分包都用相对路径
    assetPrefix: '943a6b1b-222a-4e99-b900-3744270480e6'
  }
});
