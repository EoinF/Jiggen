import React from 'react';
import ReactDOM from 'react-dom';
import 'normalize.css';
import registerServiceWorker, {unregister} from './registerServiceWorker';
import { displayOptionsActions } from './store/displayOptions';

import store from './store';

import App from './App';

window.addEventListener('popstate', function (e) {
	store.dispatch(displayOptionsActions.disableFullScreenFallback());
});

ReactDOM.render(
  	<App store={store}/>
  , document.getElementById('react-root'));

registerServiceWorker();
