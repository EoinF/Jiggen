import React from 'react';
import ReactDOM from 'react-dom';
import 'normalize.css';
//import registerServiceWorker, {unregister} from './registerServiceWorker';
import { displayOptionsActions } from './store/displayOptions';

import store from './store';

import App from './App';

// If not in production, enable gwt dev tools
if (!!process.env.REACT_APP_SHOW_SUPERDEV === true) {
	document.getElementById('superdev-reload').classList.remove('hidden');
}

window.addEventListener('popstate', function (e) {
	store.dispatch(displayOptionsActions.disableFullScreenFallback());
});

ReactDOM.render(
  	<App store={store}/>
  , document.getElementById('react-root'));

// registerServiceWorker();
