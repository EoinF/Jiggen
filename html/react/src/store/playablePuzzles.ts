import axios from 'axios';
import { handleActions, createActions, Action } from 'redux-actions';

import { Resource, ReducersRoot, BaseState, JiggenThunkAction } from '../models';
import base from './base';
import { getOrFetchResourceLinks } from '../actions/resourceLinks';
import { Background } from './backgrounds';
import { GeneratedTemplate } from './generatedTemplates';

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
		dispatch(setPlayablePuzzles(result.data));
	};
}


const reducers = handleActions({
		FETCH_PLAYABLE_PUZZLES: (state, {payload}: Action<any>) => base.setIsFetching(state, payload),
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