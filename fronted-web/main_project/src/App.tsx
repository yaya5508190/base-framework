import './App.css';
import Provider from 'provider';

const App = () => {
  return (
    <div className="content">
        <h1 className="title">我是主应用中的页面</h1>
      <Provider />
    </div>
  );
};

export default App;
