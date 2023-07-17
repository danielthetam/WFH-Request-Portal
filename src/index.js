import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css'
import Form from './form.js'
import AdminView from './admin.js'

function Route() {
  if (window.location.pathname === '/') {
    return React.createElement(Form);
  } else if (window.location.pathname === '/admin') {
    return React.createElement(AdminView);
  } else {
    window.location.pathname = '/';
    return React.createElement(Form);
  }
}

function App() {
  return (
    <Route />
  );
}
const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <App />    
);