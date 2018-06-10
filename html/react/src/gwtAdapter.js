const onGwtLoadedPromise = new Promise((resolve, reject) => {
	// Used by the gwt application to notify that the gwt app has been instantiated
	// (important because we need to wait for window.gwtAdapter to become available)
	window.setGwtLoaded = resolve;
});

function setGeneratedTemplate(generatedTemplate) {
	onGwtLoadedPromise.then(() => {
		// The gwtAdapter object is exported from java gwt code using JsInterop
		window.gwtAdapter.setGeneratedTemplate(generatedTemplate);
	});
}


function setTemplate(template) {
	onGwtLoadedPromise.then(() => {
		// The gwtAdapter object is exported from java gwt code using JsInterop
		window.gwtAdapter.setTemplate(template);
	});
}

export default {
	setGeneratedTemplate,
	setTemplate
};