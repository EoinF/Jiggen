import { 
	FETCH_BACKGROUNDS,
	SET_BACKGROUND,
	SET_BACKGROUNDS,
	SELECT_BACKGROUND,
	UPDATE_BACKGROUND
} from '../actions/backgrounds';

const initialState = {
  selectedId: null,
  backgroundsMap: {
  	// map of background ids to backgrounds
  },
  isFetching: false,
};

function selectBackground(state, {id}) {
	return {
		...state,
		selectedId: id
	}
}

function setBackground(state, {background}) {
	const {
		backgroundsMap
	} = state;

	const newMap = {...backgroundsMap};
	newMap[background.id] = JSON.parse(JSON.stringify(background));

	return {
		...state,
		selectedId: background.id,
		backgroundsMap: newMap,
		isFetching: false
	};
}

function setBackgrounds(state, {backgrounds}) {
	let backgroundsMap = {};

	backgrounds.forEach(background => {
		backgroundsMap[background.id] = background;
	});

	return {
		...state,
		backgroundsMap,
		isFetching: false
	};
}

function updateBackground(state, {id, updatedAttributes}) {
	const updatedBackground = {
		...state.backgroundsMap[id],
		...updatedAttributes
	};
	return setBackground(state, {background: updatedBackground});
}

function startFetchingBackgrounds(state, _) {
	return {
		...state,
		isFetching: true
	};
}


function backgroundReducers(state = initialState, action) {
	switch (action.type) {
		case FETCH_BACKGROUNDS:
			return startFetchingBackgrounds(state, action);
		case SET_BACKGROUND:
			return setBackground(state, action)
		case SET_BACKGROUNDS:
			return setBackgrounds(state, action)
		case SELECT_BACKGROUND:
			return selectBackground(state, action)
		case UPDATE_BACKGROUND:
			return updateBackground(state, action)
		default:
			return state;
	}
}

export default backgroundReducers;