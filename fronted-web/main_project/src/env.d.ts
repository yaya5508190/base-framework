/// <reference types="@rsbuild/core/types" />
declare module 'plugin_project/plugin' {
  import type { ComponentType } from 'react';
  export function register(args: {
    registerMenu: (menu: { key: string; label: string; component: ComponentType }) => void;
  }): void;
}
