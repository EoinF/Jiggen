
import { 
	createStore,
	applyMiddleware,
	combineReducers,
	compose
} from 'redux';

import {
	templateReducers,
	resourceLinkReducers
} from '../reducers';

import { ReducersRoot } from '../models';

import generatedTemplates from './generatedTemplates';
import backgrounds from './backgrounds';
import playablePuzzles from './playablePuzzles';
import displayOptions from './displayOptions';
import downloadedImages from './downloadedImages';

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
    displayOptions,
	playablePuzzles,
	downloadedImages
};

const store = createStore(
	combineReducers(reducersMap),
	composeEnhancers(
		applyMiddleware(thunk)
	)
);

export default store;