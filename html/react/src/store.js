import { 
	createStore, 
	applyMiddleware, 
	combineReducers, 
	compose 
} from 'redux';

import {
	templateReducers,
	backgroundReducers,
	generatedTemplateReducers,
	resourceLinkReducers,
	displayOptionsReducers
} from './reducers';

import thunk from 'redux-thunk';

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

const store = createStore(
	combineReducers({
		templates: templateReducers,
		backgrounds: backgroundReducers,
		generatedTemplates: generatedTemplateReducers,
		resourceLinks: resourceLinkReducers,
		displayOptions: displayOptionsReducers
	}),
	composeEnhancers(
		applyMiddleware(thunk)
	)
);

export default store;