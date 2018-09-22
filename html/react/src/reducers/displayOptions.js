import { 
	SET_FULLSCREEN_FALLBACK
} from '../actions/displayOptions';

const initialState = {
	showFullScreenFallback: false
};

function setFullScreenFallback(state, {isFullScreen}) {
	return {
		showFullScreenFallback: isFullScreen
	};
}


function displayOptionsReducers(state = initialState, action) {
	switch (action.type) {
		case SET_FULLSCREEN_FALLBACK:
			return setFullScreenFallback(state, action);
		default:
			return state;
	}
}

export default displayOptionsReducers;