import React from 'react';
import ReactDOM from 'react-dom';
import 'normalize.css';
import registerServiceWorker from './registerServiceWorker';

import store from './store';

import App from './App';

import { saveState } from './store/localStorage';
import { Subject } from 'rxjs';
import { first, debounceTime } from 'rxjs/operators';
import { customPuzzleActions } from './store/customPuzzle';
import { customPuzzlesStateKey } from './constants';

const puzzleMap$ = new Subject();
puzzleMap$
	.pipe(debounceTime(500))
	.subscribe((value) => saveState(customPuzzlesStateKey, value));

store.subscribe(() => {
	const puzzleMap = store.getState().customPuzzle.puzzleMap;
	puzzleMap$.next(puzzleMap);
});

puzzleMap$.pipe(
	first()
).subscribe((puzzles) => {
	for (const key in puzzles) {
		store.dispatch(customPuzzleActions.savePuzzle(puzzles[key]));
	}
});

ReactDOM.render(
  	<App store={store}/>
  , document.getElementById('react-root'));

registerServiceWorker();
