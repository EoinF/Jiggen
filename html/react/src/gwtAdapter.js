import {setFullScreen} from './utils/fullScreen';
import cachedImageDownload from './utils/cachedImageDownload';

const onGwtLoadedPromise = new Promise((resolve, reject) => {
	// Used by the gwt application to notify that the gwt app has been instantiated
	// (important because we need to wait for window.gwtAdapter to become available)
	window.setGwtLoaded = resolve;
});

// Set up cached image download for gdx app to use
window.downloadImage = cachedImageDownload;


function setTemplate(generatedTemplate) {
	onGwtLoadedPromise.then(() => {
		// The gwtAdapter object is exported from java gwt code using JsInterop
		window.gwtAdapter.setTemplate(generatedTemplate);
	});
}

function setBackground(background) {
	onGwtLoadedPromise.then(() => {
		// The gwtAdapter object is exported from java gwt code using JsInterop
		window.gwtAdapter.setBackground(background);
	});
}

function startDemo() {
	onGwtLoadedPromise.then(() => {
		// The gwtAdapter object is exported from java gwt code using JsInterop
		window.gwtAdapter.startDemo();
	});
}

function shuffle() {
	onGwtLoadedPromise.then(() => {
		// The gwtAdapter object is exported from java gwt code using JsInterop
		window.gwtAdapter.shuffle();
	});
}

function resizeGameContainer(width, height) {
	onGwtLoadedPromise.then(() => {
		window.gwtAdapter.resizeGameContainer(width, height);
	});
}

function setGwtFullScreen (isFullScreen) {
	setFullScreen(isFullScreen, document.getElementById('embed-html'));
};

onGwtLoadedPromise.then(() => {
	window.gwtAdapter.setFullScreen = setGwtFullScreen;
});

export default {
	onGwtLoadedPromise,
	setBackground,
	setTemplate,
	startDemo,
	shuffle,
	resizeGameContainer,
	setGwtFullScreen
};