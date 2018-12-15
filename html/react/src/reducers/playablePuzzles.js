import {
	FETCH_PLAYABLE_PUZZLES,
	SET_PLAYABLE_PUZZLES,
	SELECT_PLAYABLE_PUZZLE
} from '../actions/playablePuzzles';

const initialState = {
  selectedId: null,
  playablePuzzles: [],
  playablePuzzlesMap: {
  	// map of playable puzzle ids to playable puzzles
  },
  isFetching: false,
};

function setPlayablePuzzles(state, {playablePuzzles}) {
	let playablePuzzlesMap = {};

	playablePuzzles.forEach(puzzle => {
		playablePuzzlesMap[puzzle.id] = puzzle;
	});

	return {
		...state,
		playablePuzzles,
		playablePuzzlesMap,
		isFetching: false
	};
}

function startFetchingPlayablePuzzles(state, _) {
	return {
		...state,
		isFetching: true
	};
}

function selectPlayablePuzzle(state, {playablePuzzleId}) {
	return {
		...state,
		selectedId: playablePuzzleId
	};
}


function playablePuzzlesReducers(state = initialState, action) {
	switch (action.type) {
		case FETCH_PLAYABLE_PUZZLES:
			return startFetchingPlayablePuzzles(state, action);
		case SET_PLAYABLE_PUZZLES:
			return setPlayablePuzzles(state, action);
		case SELECT_PLAYABLE_PUZZLE:
			return selectPlayablePuzzle(state, action);
		default:
			return state;
	}
}

export default playablePuzzlesReducers;