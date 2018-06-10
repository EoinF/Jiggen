import { getOrFetchGeneratedTemplateById } from './generatedTemplates';

const START_LOADING_PUZZLE_SOLVER = "LOAD_PUZZLE_SOLVER";
const SET_PUZZLE_SOLVER_DATA = "SET_PUZZLE_SOLVER_DATA";

function startLoadingPuzzleSolver() {
	return {
		type: START_LOADING_PUZZLE_SOLVER,
		isLoading: true
	};
}

function setPuzzleSolverData(generatedTemplate) {
	return {
		type: SET_PUZZLE_SOLVER_DATA,
		generatedTemplate
	};
}

function loadPuzzleSolver(id) {
	return (dispatch, getState) => {
		dispatch(startLoadingPuzzleSolver());

		getOrFetchGeneratedTemplateById(id, dispatch, getState).then((generatedTemplate) => {
			dispatch(setPuzzleSolverData(generatedTemplate));
		});
	}
}

export {
	loadPuzzleSolver,
	START_LOADING_PUZZLE_SOLVER,
	SET_PUZZLE_SOLVER_DATA
}