import type { ComponentType } from 'react';
import Provider from './components/ProviderComponent';

interface MenuItem {
  key: string;
  label: string;
  component: ComponentType;
}

interface RegisterArgs {
  registerMenu: (menu: MenuItem) => void;
}

export function register({ registerMenu }: RegisterArgs) {
  registerMenu({
    key: 'plugin',
    label: '插件页面',
    component: Provider,
  });
}

