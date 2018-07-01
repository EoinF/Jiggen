const onGwtLoadedPromise = new Promise((resolve, reject) => {
	// Used by the gwt application to notify that the gwt app has been instantiated
	// (important because we need to wait for window.gwtAdapter to become available)
	window.setGwtLoaded = resolve;
});

function resize() {
	onGwtLoadedPromise.then(() => {
	    const width = window.innerWidth || document.body.clientWidth;
	    const height = window.innerHeight || document.body.clientHeight;
		window.gwtAdapter.resize(width, height);
	});
}

document.addEventListener("DOMContentLoaded", () => {
	onGwtLoadedPromise.then(() => {
		resize();
		window.addEventListener('resize', resize, true);
	});
});

function startPuzzle(generatedTemplate, background) {
	onGwtLoadedPromise.then(() => {
		// The gwtAdapter object is exported from java gwt code using JsInterop
		window.gwtAdapter.startPuzzle(generatedTemplate, background);
	});
}


function setTemplate(template) {
	onGwtLoadedPromise.then(() => {
		// The gwtAdapter object is exported from java gwt code using JsInterop
		window.gwtAdapter.setTemplate(template);
	});
}

export default {
	startPuzzle,
	setTemplate
};