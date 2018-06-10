import {
	START_LOADING_PUZZLE_SOLVER,
	SET_PUZZLE_SOLVER_DATA
} from '../actions';

const initialState = {
  generatedTemplate: null,
  isLoading: false
};

function startLoadingPuzzleSolver(state, {id}) {
	return {
		...state,
		isLoading: true
	}
}

function setPuzzleSolverData(state, {generatedTemplate}) {
	return {
		...state,
		generatedTemplate,
		selectedId: generatedTemplate.id,
		isLoading: false
	};
}


function puzzleSolverReducers(state = initialState, action) {
	switch (action.type) {
		case START_LOADING_PUZZLE_SOLVER:
			return startLoadingPuzzleSolver(state, action);
		case SET_PUZZLE_SOLVER_DATA:
			return setPuzzleSolverData(state, action)
		default:
			return state;
	}
}

export default puzzleSolverReducers;