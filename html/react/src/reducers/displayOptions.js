import { 
	SET_FULLSCREEN_FALLBACK,
	SHOW_MODAL
} from '../actions/displayOptions';

const initialState = {
	showFullScreenFallback: false,
	modalType: null,
	showModal: false
};

function setFullScreenFallback(state, {isFullScreen}) {
	return {
		...state,
		showFullScreenFallback: isFullScreen
	};
}

function showModal(state, {modalType}) {
	return {
		...state,
		modalType,
		showModal: true
	}
}


function displayOptionsReducers(state = initialState, action) {
	switch (action.type) {
		case SET_FULLSCREEN_FALLBACK:
			return setFullScreenFallback(state, action);
		case SHOW_MODAL:
			return showModal(state, action);
		default:
			return state;
	}
}

export default displayOptionsReducers;