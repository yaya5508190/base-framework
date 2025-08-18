import React, { useEffect, useState } from 'react';
import './App.css';

interface MenuItem {
  key: string;
  label: string;
  component: React.ComponentType;
}

const App = () => {
  const [menus, setMenus] = useState<MenuItem[]>([]);
  const [activeKey, setActiveKey] = useState<string>();

  const registerMenu = (menu: MenuItem) => {
    setMenus((prev) => [...prev, menu]);
    setActiveKey((prev) => prev ?? menu.key);
  };

  useEffect(() => {
    import('plugin_project/plugin')
      .then((mod) => {
        if (mod.register) {
          mod.register({ registerMenu });
        }
      })
      .catch((err) => {
        console.error('Failed to load plugin', err);
      });
  }, []);

  const ActiveComponent = menus.find((m) => m.key === activeKey)?.component;

  return (
    <div className="content">
      <h1 className="title">我是主应用中的页面</h1>
      <nav className="menu">
        {menus.map((m) => (
          <button key={m.key} onClick={() => setActiveKey(m.key)}>
            {m.label}
          </button>
        ))}
      </nav>
      <div className="plugin-container">
        {ActiveComponent ? <ActiveComponent /> : null}
      </div>
    </div>
  );
};

export default App;

