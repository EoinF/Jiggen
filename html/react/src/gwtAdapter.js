import { toggleFullScreen } from './utils/fullScreen';
import cachedImageDownload from './utils/cachedImageDownload';
import { Subject } from 'rxjs';
import { filter, shareReplay, first } from 'rxjs/operators';

const appState$ = new Subject();
window.nextAppState = (val) => appState$.next(val);

const gwtAppLoaded$ = appState$.pipe(
	filter(value => value === "LOADED"),
	first(),
	shareReplay(1)
);

// Set up cached image download for gdx app to use
window.downloadImage = cachedImageDownload;

function setTemplate(generatedTemplate) {
	gwtAppLoaded$.subscribe(() => {
		// The gwtAdapter object is exported from java gwt code using JsInterop
		window.gwtAdapter.setTemplate(generatedTemplate);
	});
}

function setBackground(background) {
	gwtAppLoaded$.subscribe(() => {
		// The gwtAdapter object is exported from java gwt code using JsInterop
		window.gwtAdapter.setBackground(background);
	});
}

function startDemo() {
	gwtAppLoaded$.subscribe(() => {
		// The gwtAdapter object is exported from java gwt code using JsInterop
		window.gwtAdapter.startDemo();
	});
}
function pauseGame() {
	gwtAppLoaded$.subscribe(() => {
		// The gwtAdapter object is exported from java gwt code using JsInterop
		window.gwtAdapter.pauseGame();
	});
}
function resumeGame() {
	gwtAppLoaded$.subscribe(() => {
		// The gwtAdapter object is exported from java gwt code using JsInterop
		window.gwtAdapter.resumeGame();
	});
}

function shuffle() {
	gwtAppLoaded$.subscribe(() => {
		// The gwtAdapter object is exported from java gwt code using JsInterop
		window.gwtAdapter.shuffle();
	});
}

function resizeGameContainer(width, height) {
	gwtAppLoaded$.subscribe(() => {
		window.gwtAdapter.resizeGameContainer(width, height);
	});
}

function toggleGwtFullScreen () {
	toggleFullScreen(document.getElementById('embed-html'));
};

gwtAppLoaded$.subscribe(() => {
	window.gwtAdapter.toggleFullScreen = toggleGwtFullScreen;
});

export {gwtAppLoaded$};

export default {
	onGwtLoadedPromise: gwtAppLoaded$.pipe(first()).toPromise(),
	gwtAppLoaded$,
	appState$,
	setBackground,
	setTemplate,
	startDemo,
	pauseGame,
	resumeGame,
	shuffle,
	resizeGameContainer,
	toggleGwtFullScreen
};