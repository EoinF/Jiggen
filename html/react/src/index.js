import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import registerServiceWorker from './registerServiceWorker';
import { createStore, applyMiddleware, combineReducers } from 'redux';
import {
	templateReducers,
	generatedTemplateReducers,
	resourceLinkReducers
} from './reducers';
import thunk from 'redux-thunk';

import App from './App';

import './globals.js'

const store = createStore(
	combineReducers({ 
		templates: templateReducers,
		generatedTemplates: generatedTemplateReducers,
		resourceLinks: resourceLinkReducers
	}),
	applyMiddleware(thunk)
)

ReactDOM.render(
  	<App store={store}/>
  , document.getElementById('react-root'));

registerServiceWorker();
