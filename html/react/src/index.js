import React from 'react';
import ReactDOM from 'react-dom';
import 'normalize.css';
import registerServiceWorker from './registerServiceWorker';

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

ReactDOM.render(
  	<App store={store}/>
  , document.getElementById('react-root'));

registerServiceWorker();
