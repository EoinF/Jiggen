import { 
	FETCH_BACKGROUNDS,
	SET_BACKGROUND,
	SET_BACKGROUNDS,
	SELECT_BACKGROUND
} from '../actions';

const initialState = {
  selectedId: null,
  backgrounds: [],
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
		backgroundsMap,
		backgrounds
	} = state;

	const newMap = {...backgroundsMap};
	newMap[background.id] = background;

	return {
		...state,
		selectedId: background.id,
		backgrounds: [...backgrounds, background],
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
		backgrounds,
		backgroundsMap,
		isFetching: false
	};
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
		default:
			return state;
	}
}

export default backgroundReducers;