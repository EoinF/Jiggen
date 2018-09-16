import React from 'react';
import ReactDOM from 'react-dom';
import 'normalize.css';
import './index.css';
//import registerServiceWorker, {unregister} from './registerServiceWorker';
import { createStore, applyMiddleware, combineReducers, compose } from 'redux';
import {
	templateReducers,
	backgroundReducers,
	generatedTemplateReducers,
	resourceLinkReducers
} from './reducers';
import thunk from 'redux-thunk';

import App from './App';

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

// If not in production, enable gwt dev tools
if (!!process.env.REACT_APP_SHOW_SUPERDEV === true) {
	document.getElementById('superdev-reload').classList.remove('hidden');
}

const store = createStore(
	combineReducers({
		templates: templateReducers,
		backgrounds: backgroundReducers,
		generatedTemplates: generatedTemplateReducers,
		resourceLinks: resourceLinkReducers
	}),
	composeEnhancers(
		applyMiddleware(thunk)
	)
)

ReactDOM.render(
  	<App store={store}/>
  , document.getElementById('react-root'));

// registerServiceWorker();
