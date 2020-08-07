import React from 'react';
import ReactDOM from 'react-dom';
import 'normalize.css';
import registerServiceWorker from './registerServiceWorker';

import store from './store';

import App from './App';

import { saveState } from './store/localStorage';
import { Subject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { customPuzzleActions } from './store/customPuzzle';
import { customPuzzlesStateKey } from './constants';

const puzzleMap$ = new Subject();
puzzleMap$
	.pipe(debounceTime(500))
	.subscribe((value) => saveState(customPuzzlesStateKey, value));

store.subscribe(() => {
	console.log("on sub")
	const puzzleMap = store.getState().customPuzzle.puzzleMap;
	puzzleMap$.next(puzzleMap);
});

const puzzleMap = store.getState().customPuzzle.puzzleMap;
for (const key in store.getState().customPuzzle.puzzleMap) {
	store.dispatch(customPuzzleActions.savePuzzle(puzzleMap[key]));
}


ReactDOM.render(
  	<App store={store}/>
  , document.getElementById('react-root'));

registerServiceWorker();
