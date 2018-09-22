const SET_FULLSCREEN_FALLBACK = 'SET_FULLSCREEN_FALLBACK'; 

const enableFullScreenFallback = () => {
	return setFullScreenFallback(true);
}
const disableFullScreenFallback = () => {
	return setFullScreenFallback(false);
}

const setFullScreenFallback = (isFullScreen) => {
	return {
		type: SET_FULLSCREEN_FALLBACK,
		isFullScreen
	}
}

const displayOptionsActions = {
	disableFullScreenFallback,
	enableFullScreenFallback
}

export {
	displayOptionsActions,
	SET_FULLSCREEN_FALLBACK
}