import axios from 'axios';
import { handleActions, createActions, Action } from 'redux-actions';
import generator from 'seedrandom'

import { Resource, BaseState, JiggenThunkAction } from '../models';
import base from './base';
import { getOrFetchResourceLinks } from '../actions/resourceLinks';

export interface PlayablePuzzle extends Resource {
}
export interface PlayablePuzzlesState extends BaseState<PlayablePuzzle> {}

const initialState: PlayablePuzzlesState = {
	...base.initialState as PlayablePuzzlesState
};

const {
	startFetchingPlayablePuzzles,
	setPlayablePuzzles,
	selectPlayablePuzzle
} = createActions({
	START_FETCHING_PLAYABLE_PUZZLES: () => ({isFetching: true}),
	SET_PLAYABLE_PUZZLES: (playablePuzzles: PlayablePuzzle[]) => ({resourceList: playablePuzzles}),
	SELECT_PLAYABLE_PUZZLE: (playablePuzzleId) => ({selectedId: playablePuzzleId})
})

function fetchPuzzlesOfTheDay (): JiggenThunkAction {
	return async (dispatch, getState) => {
		const resourceLinks = await getOrFetchResourceLinks(dispatch, getState);
		dispatch(startFetchingPlayablePuzzles());
		const result = await axios.get(resourceLinks.todaysPuzzles);
		if ((result.data as PlayablePuzzle[]).length > 0) {
			dispatch(setPlayablePuzzles(result.data));
		} else {
			const result = await axios.get(resourceLinks.playablePuzzles);
			
			const allPuzzles: PlayablePuzzle[] = result.data;
			const day = new Date().getMonth() * 30 + (new Date()).getDate();

			const rand = generator(day.toString());
			const chosenIndex = Math.floor(rand() * allPuzzles.length);

            const chosenPuzzles = allPuzzles.filter((puzzle) =>
                puzzle.links.background === allPuzzles[chosenIndex].links.background
			);
			dispatch(setPlayablePuzzles(chosenPuzzles));
		}
	};
}

const reducers = handleActions({
		START_FETCHING_PLAYABLE_PUZZLES: (state, {payload}: Action<any>) => base.setIsFetching(state, payload),
		SET_PLAYABLE_PUZZLES: (state, {payload}: Action<any>) => base.setResources(state, payload),
		SELECT_PLAYABLE_PUZZLE: (state, {payload}: Action<any>) => base.selectResource(state, payload),
	},
	initialState
);

const playablePuzzlesActions = {
	fetchPuzzlesOfTheDay,
	selectPlayablePuzzle
}

export {
	playablePuzzlesActions,
};
export default reducers;