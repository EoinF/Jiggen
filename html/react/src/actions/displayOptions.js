const SET_FULLSCREEN_FALLBACK = 'SET_FULLSCREEN_FALLBACK';
const SHOW_MODAL = 'SHOW_MODAL';

const MODAL_TYPE_BACKGROUND_SELECTION = 'BACKGROUND_SELECTION';

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

const showBackgroundsModal = () => {
	return {
		type: SHOW_MODAL,
		modalType: MODAL_TYPE_BACKGROUND_SELECTION
	}
}

const displayOptionsActions = {
	showBackgroundsModal,
	disableFullScreenFallback,
	enableFullScreenFallback
}

export {
	displayOptionsActions,
	SET_FULLSCREEN_FALLBACK,
	SHOW_MODAL
}