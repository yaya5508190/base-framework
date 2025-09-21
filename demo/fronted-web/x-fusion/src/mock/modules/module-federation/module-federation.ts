import type { MFConfig } from '@/types/module-federation.ts'
import Mock from 'mockjs'

export default function setup () {
  Mock.mock('./api/plugin/module-federation', 'get', () => {
    return {
      remotes: [
        {
          alias: 'remote',
          name: 'remote',
          entry: 'http://localhost:3001/943a6b1b-222a-4a99-b900-3744271480e6/mf-manifest.json',
        },
        // { alias: 'vueViteRemote', name: 'vueViteRemote', entry: 'http://localhost:5174/remoteEntry.js' },
      ],
      menus: [
        { name: '微应用', path: '/remote', component: 'remote/App', parent: false, children: [] },
        {
          name: '微应用页面', path: '', component: '', parent: true, children: [
            { name: '微应用页面1', path: '/remote1', component: 'remote/App', parent: false, children: [] },
            { name: '微应用页面2', path: '/remote2', component: 'remote/App1', parent: false, children: [] },
          ],
        },
      ],
      components: [],
    } satisfies MFConfig
  })
}
