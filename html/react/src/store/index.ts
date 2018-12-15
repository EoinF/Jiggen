
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
} from '../reducers';

import { ReducersRoot } from './models';

import generatedTemplates from './generatedTemplates';
import backgrounds from './backgrounds';

import thunk from 'redux-thunk';

declare global {
    interface Window { __REDUX_DEVTOOLS_EXTENSION_COMPOSE__: any; }
}
const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

const reducersMap: ReducersRoot = {
    templates: templateReducers,
    backgrounds,
    generatedTemplates,
    resourceLinks: resourceLinkReducers,
    displayOptions: displayOptionsReducers,
    playablePuzzles: playablePuzzlesReducers
};

const store = createStore(
	combineReducers(reducersMap),
	composeEnhancers(
		applyMiddleware(thunk)
	)
);

export default store;