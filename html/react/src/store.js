import { 
	createStore,
	applyMiddleware,
	combineReducers,
	compose
} from 'redux';

import {
	templateReducers,
	resourceLinkReducers,
	displayOptionsReducers,
	playablePuzzlesReducers
} from './reducers';

import generatedTemplates from './store/generatedTemplates';
import backgrounds from './store/backgrounds';

import thunk from 'redux-thunk';

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

const store = createStore(
	combineReducers({
		templates: templateReducers,
		backgrounds,
		generatedTemplates,
		resourceLinks: resourceLinkReducers,
		displayOptions: displayOptionsReducers,
		playablePuzzles: playablePuzzlesReducers
	}),
	composeEnhancers(
		applyMiddleware(thunk)
	)
);

export default store;