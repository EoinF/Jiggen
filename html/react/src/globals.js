import React from 'react';
import ReactDOM from 'react-dom'
import App from './App';

function renderReact(selector) {
    ReactDOM.render( <App />, document.getElementById(selector) );
}

window.renderReact = renderReact;
