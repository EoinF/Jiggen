import React from 'react';
import ReactDOM from 'react-dom';
import 'normalize.css';
import './index.css';
//import registerServiceWorker, {unregister} from './registerServiceWorker';

import store from './store';

import App from './App';

// If not in production, enable gwt dev tools
if (!!process.env.REACT_APP_SHOW_SUPERDEV === true) {
	document.getElementById('superdev-reload').classList.remove('hidden');
}

ReactDOM.render(
  	<App store={store}/>
  , document.getElementById('react-root'));

// registerServiceWorker();
