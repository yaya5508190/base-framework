import { createModuleFederationConfig } from '@module-federation/rsbuild-plugin';

export default createModuleFederationConfig({
  name: 'plugin_project',
  exposes: {
    './plugin': './src/plugin.ts',
  },
  shared: {
    react: { singleton: true },
    'react-dom': { singleton: true },
  },
});
