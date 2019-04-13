import { createActions, handleActions, Action } from "redux-actions";
import { loadState } from "./localStorage";

export interface CustomPuzzle {
    id: string;
    background: string;
    template: string;
    name: string;
}

export interface CustomPuzzleState {
    selectedBackground: string | null;
    selectedTemplate: string | null;
    name: string;
    puzzleList: CustomPuzzle[];
}

const initialState: CustomPuzzleState = {
    puzzleList: loadState("customPuzzles") || [],
    selectedBackground: null,
    selectedTemplate: null,
    name: "Custom Puzzle"
};

const {
    customPuzzleSetName,
	customPuzzleSelectTemplate,
    customPuzzleSelectBackground,
    customPuzzleAddPuzzle,
    customPuzzleDeletePuzzle
} = createActions({
	CUSTOM_PUZZLE_SET_NAME: (name) => ({name}),
	CUSTOM_PUZZLE_SELECT_TEMPLATE: (selectedLink) => ({selectedLink}),
    CUSTOM_PUZZLE_SELECT_BACKGROUND: (selectedLink) => ({selectedLink}),
    CUSTOM_PUZZLE_ADD_PUZZLE: (customPuzzle: CustomPuzzle) => ({customPuzzle}),
    CUSTOM_PUZZLE_DELETE_PUZZLE: (customPuzzle: CustomPuzzle) => ({customPuzzle})
});

const reducers = handleActions({
        CUSTOM_PUZZLE_SET_NAME: (state, {payload}: Action<any>): Partial<CustomPuzzleState> => {
            return {
                ...state,
                name: payload.name
            }
        },
        CUSTOM_PUZZLE_SELECT_TEMPLATE: (state, {payload}: Action<any>): Partial<CustomPuzzleState> => {
            return {
                ...state,
                selectedTemplate: payload.selectedLink
            }
        },
        CUSTOM_PUZZLE_SELECT_BACKGROUND: (state, {payload}: Action<any>): Partial<CustomPuzzleState> => {
            return {
                ...state,
                selectedBackground: payload.selectedLink
            }
        },
        CUSTOM_PUZZLE_ADD_PUZZLE: (state, {payload}: Action<any>): Partial<CustomPuzzleState> => {
            return {
                ...state,
                puzzleList: [...state.puzzleList, payload.customPuzzle]
            }
        },
        CUSTOM_PUZZLE_DELETE_PUZZLE: (state, {payload}: Action<any>): Partial<CustomPuzzleState> => {
            return {
                ...state,
                puzzleList: [...state.puzzleList].filter(puzzle => puzzle.id != payload.customPuzzle.id)
            }
        },
    },
	initialState
);

const customPuzzleActions = {
    selectBackground: customPuzzleSelectBackground,
    selectTemplate: customPuzzleSelectTemplate,
    setName: customPuzzleSetName,
    addPuzzle: customPuzzleAddPuzzle,
    deletePuzzle: customPuzzleDeletePuzzle
}

export {
	customPuzzleActions
};
export default reducers;