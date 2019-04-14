import { createActions, handleActions, Action } from "redux-actions";
import { loadState } from "./localStorage";
import { StringMap } from "../models";
import uuid from 'uuid';

export interface CustomPuzzle {
    id: string;
    background: string;
    template: string;
    name: string;
}

export interface CustomPuzzleState {
    id: string;
    selectedBackground: string | null;
    selectedTemplate: string | null;
    name: string;
    puzzleMap: StringMap<CustomPuzzle>;
}

const initialState: CustomPuzzleState = {
    id: uuid.v4(),
    puzzleMap: loadState("customPuzzles") || {},
    selectedBackground: null,
    selectedTemplate: null,
    name: "Custom Puzzle"
};

const {
    customPuzzleSetName,
	customPuzzleSelectTemplate,
    customPuzzleSelectBackground,
    customPuzzleSelectPuzzle,
    customPuzzleSavePuzzle,
    customPuzzleDeletePuzzle
} = createActions({
	CUSTOM_PUZZLE_SET_NAME: (name) => ({name}),
	CUSTOM_PUZZLE_SELECT_TEMPLATE: (selectedLink) => ({selectedLink}),
    CUSTOM_PUZZLE_SELECT_BACKGROUND: (selectedLink) => ({selectedLink}),
    CUSTOM_PUZZLE_SELECT_PUZZLE: (id) => ({id}),
    CUSTOM_PUZZLE_SAVE_PUZZLE: () => ({}),
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
        CUSTOM_PUZZLE_SELECT_PUZZLE: (state, {payload}: Action<any>): Partial<CustomPuzzleState> => {
            const selectedPuzzle = (state as CustomPuzzleState).puzzleMap[payload.id] || {} as CustomPuzzle;
            return {
                ...state,
                selectedBackground: selectedPuzzle.background || null,
                selectedTemplate: selectedPuzzle.template || null,
                name: selectedPuzzle.name || 'Custom Puzzle',
                id: payload.id || uuid.v4()
            }
        },
        CUSTOM_PUZZLE_SAVE_PUZZLE: (state, {payload}: Action<any>): Partial<CustomPuzzleState> => {
            const customPuzzle: CustomPuzzle = {
                background: state.selectedBackground,
                template: state.selectedTemplate,
                name: state.name,
                id: state.id
            };
            const updatedPuzzleMap = {...state.puzzleMap};
            updatedPuzzleMap[customPuzzle.id] = customPuzzle;
            
            return {
                ...state,
                puzzleMap: updatedPuzzleMap
            }
        },
        CUSTOM_PUZZLE_DELETE_PUZZLE: (state, {payload}: Action<any>): Partial<CustomPuzzleState> => {
            const updatedPuzzleMap = {...state.puzzleMap};
            delete updatedPuzzleMap[payload.customPuzzle.id];
            return {
                ...state,
                puzzleMap: updatedPuzzleMap
            }
        },
    },
	initialState
);

const customPuzzleActions = {
    selectBackground: customPuzzleSelectBackground,
    selectTemplate: customPuzzleSelectTemplate,
    setName: customPuzzleSetName,
    selectPuzzle: customPuzzleSelectPuzzle,
    savePuzzle: customPuzzleSavePuzzle,
    deletePuzzle: customPuzzleDeletePuzzle
}

export {
	customPuzzleActions
};
export default reducers;