import { 
	SET_RESOURCE_LINKS
} from '../actions';

const initialState = {
	isLoaded: false
};


function setResourceLinks(state, {resourceLinks}) {
	return {
		isLoaded: true,
		resourceLinks
	};
}


function resourceLinkReducers(state = initialState, action) {
	switch (action.type) {
		case SET_RESOURCE_LINKS:
			return setResourceLinks(state, action);
		default:
			return state;
	}
}

export default resourceLinkReducers;