declare global {
    interface Document { 
		webkitIsFullScreen: Boolean;
		mozFullScreen: Boolean;

		exitFullScreen(): void;
		webkitExitFullscreen(): void;
		webkitCancelFullScreen(): void;
		mozCancelFullScreen(): void;
		msExitFullscreen(): void;
		
	}
	interface Element {
		webkitRequestFullscreen(): void;
		mozRequestFullScreen(): void;
		msRequestFullscreen(): void;
	}
}

function isFullScreen() {
	 return document.fullscreen || document.webkitIsFullScreen || document.mozFullScreen;
}

function onFullScreenChange(callback: EventListenerOrEventListenerObject) {
    document.addEventListener('webkitfullscreenchange', callback, false);
    document.addEventListener('mozfullscreenchange', callback, false);
    document.addEventListener('fullscreenchange', callback, false);
	document.addEventListener('MSFullscreenChange', callback, false);
}

function unsetOnFullScreenChange(callback: EventListenerOrEventListenerObject) {
    document.removeEventListener('webkitfullscreenchange', callback, false);
	document.removeEventListener('mozfullscreenchange', callback, false);
    document.removeEventListener('fullscreenchange', callback, false);
	document.removeEventListener('MSFullscreenChange', callback, false);
}

function toggleFullScreen(element: Element = {} as Element) {
    setFullScreen(!isFullScreen(), element);
}

function setFullScreen(isFullScreen: Boolean, element: Element = {} as Element) {
	if (isFullScreen) {
		// for newer Webkit and Firefox
		const requestFullScreen = element.requestFullscreen
			|| element.webkitRequestFullscreen // Chrome, Safari and Edge
			|| element.mozRequestFullScreen // Firefox
			|| element.msRequestFullscreen; // IE 11
		if (requestFullScreen != null) {
			requestFullScreen.call(element);
		}
	} else {
		const cancelFullScreen = document.exitFullScreen
			|| document.webkitExitFullscreen // Edge and Safari
			|| document.webkitCancelFullScreen // Chrome
			|| document.mozCancelFullScreen // Firefox
			|| document.msExitFullscreen; // IE 11
		if (cancelFullScreen != null) {
			cancelFullScreen.call(document);
		}
	}
}

export {setFullScreen, onFullScreenChange, unsetOnFullScreenChange, isFullScreen, toggleFullScreen};
