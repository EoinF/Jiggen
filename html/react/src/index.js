import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import registerServiceWorker, {unregister} from './registerServiceWorker';
import { createStore, applyMiddleware, combineReducers, compose } from 'redux';
import {
	templateReducers,
	generatedTemplateReducers,
	resourceLinkReducers,
	puzzleSolverReducers
} from './reducers';
import thunk from 'redux-thunk';

import App from './App';

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

const store = createStore(
	combineReducers({
		templates: templateReducers,
		generatedTemplates: generatedTemplateReducers,
		resourceLinks: resourceLinkReducers,
		puzzle: puzzleSolverReducers
	}),
	composeEnhancers(
		applyMiddleware(thunk)
	)
)

ReactDOM.render(
  	<App store={store}/>
  , document.getElementById('react-root'));

// registerServiceWorker();
