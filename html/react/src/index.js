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

const puzzleMap$ = new Subject();
puzzleMap$
	.pipe(throttleTime(500))
	.subscribe((value) => saveState("customPuzzles", value));

store.subscribe(() => {
	const puzzleMap = store.getState().customPuzzle.puzzleMap;
	puzzleMap$.next(puzzleMap);
});

window.addEventListener('popstate', function (e) {
	store.dispatch(displayOptionsActions.disableFullScreenFallback());
});

ReactDOM.render(
  	<App store={store}/>
  , document.getElementById('react-root'));

registerServiceWorker();
