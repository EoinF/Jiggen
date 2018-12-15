import axios from 'axios';
import { getOrFetchResourceLinks } from './resourceLinks';

const FETCH_PLAYABLE_PUZZLES = "FETCH_PLAYABLE_PUZZLES";
const SET_PLAYABLE_PUZZLES = "SET_PLAYABLE_PUZZLE";
const SELECT_PLAYABLE_PUZZLE = "SELECT_PLAYABLE_PUZZLE";


function startFetchingPuzzlesOfTheDay() {
	return {
		type: FETCH_PLAYABLE_PUZZLES
	}
}

function setPlayablePuzzles(playablePuzzles) {
	return {
		type: SET_PLAYABLE_PUZZLES,
		playablePuzzles
	}
};

function selectPlayablePuzzle(playablePuzzleId) {
	return {
		type: SELECT_PLAYABLE_PUZZLE,
		playablePuzzleId
	}
};

const fetchPuzzlesOfTheDay = (onFetchLinksResolver = () => {}) => {
	return async (dispatch, getState) => {
		const resourceLinks = await getOrFetchResourceLinks(dispatch, getState);
		dispatch(startFetchingPuzzlesOfTheDay());
		const result = await axios.get(resourceLinks.todaysPuzzles);
		onFetchLinksResolver(result.data);
		dispatch(setPlayablePuzzles(result.data));
	};
}


const playablePuzzlesActions = {
	fetchPuzzlesOfTheDay,
	selectPlayablePuzzle
}

export {
	playablePuzzlesActions,
	FETCH_PLAYABLE_PUZZLES,
	SET_PLAYABLE_PUZZLES,
	SELECT_PLAYABLE_PUZZLE
};