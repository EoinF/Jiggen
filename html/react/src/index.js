import React from 'react';
import ReactDOM from 'react-dom';
import 'normalize.css';
import registerServiceWorker, {unregister} from './registerServiceWorker';
import { displayOptionsActions } from './store/displayOptions';

import store from './store';

import App from './App';

import { saveState } from './store/localStorage';
import { Subject } from 'rxjs';
import { throttleTime } from 'rxjs/operators';

const puzzleList$ = new Subject();
puzzleList$
	.pipe(throttleTime(500))
	.subscribe((value) => saveState("customPuzzles", value));

store.subscribe(() => {
	const puzzleList = store.getState().customPuzzle.puzzleList;
	puzzleList$.next(puzzleList);
});

window.addEventListener('popstate', function (e) {
	store.dispatch(displayOptionsActions.disableFullScreenFallback());
});

ReactDOM.render(
  	<App store={store}/>
  , document.getElementById('react-root'));

registerServiceWorker();
