const onGwtLoadedPromise = new Promise((resolve, reject) => {
	// Used by the gwt application to notify that the gwt app has been instantiated
	// (important because we need to wait for window.gwtAdapter to become available)
	window.setGwtLoaded = resolve;
});

function startPuzzle(generatedTemplate, background) {
	onGwtLoadedPromise.then(() => {
		// The gwtAdapter object is exported from java gwt code using JsInterop
		window.gwtAdapter.startPuzzle(generatedTemplate, background);
	});
}

function startDemo() {
	onGwtLoadedPromise.then(() => {
		// The gwtAdapter object is exported from java gwt code using JsInterop
		window.gwtAdapter.startDemo();
	});
}


function setTemplate(template) {
	onGwtLoadedPromise.then(() => {
		// The gwtAdapter object is exported from java gwt code using JsInterop
		window.gwtAdapter.setTemplate(template);
	});
}

function setFullScreen(isFullScreen) {
	const el = document.querySelector('canvas');

	if (el) {
		if (isFullScreen) {
			// for newer Webkit and Firefox
			const requestFullScreen = el.requestFullscreen
				|| el.webkitRequestFullscreen // Chrome, Safari and Edge
				|| el.mozRequestFullScreen // Firefox
				|| el.msRequestFullscreen; // IE 11
			if (typeof requestFullScreen != "undefined" && requestFullScreen) {
				requestFullScreen.call(el);
			}
		} else {
			const cancelFullScreen = document.exitFullScreen
				|| document.webkitExitFullscreen // Edge and Safari
				|| document.webkitCancelFullScreen // Chrome
				|| document.mozCancelFullScreen // Firefox
				|| document.msExitFullscreen; // IE 11
			if (typeof cancelFullScreen != "undefined" && cancelFullScreen) {
				cancelFullScreen.call(document);
			}
		}
	}
}

onGwtLoadedPromise.then(() => {
	window.gwtAdapter.setFullScreen = setFullScreen;
});

export default {
	startPuzzle,
	setTemplate,
	startDemo,
	setFullScreen
};