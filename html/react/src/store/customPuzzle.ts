import { createActions, handleActions, Action } from "redux-actions";

export interface CustomPuzzleState {
    selectedBackground: string | null;
    selectedTemplate: string | null;
}

const initialState: CustomPuzzleState = {
    selectedBackground: null,
    selectedTemplate: null
};

const {
	customPuzzleSelectTemplate,
    customPuzzleSelectBackground
} = createActions({
	CUSTOM_PUZZLE_SELECT_TEMPLATE: (selectedLink) => ({selectedLink}),
	CUSTOM_PUZZLE_SELECT_BACKGROUND: (selectedLink) => ({selectedLink})
});

const reducers = handleActions({
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
    },
	initialState
);

const customPuzzleActions = {
    selectBackground: customPuzzleSelectBackground,
    selectTemplate: customPuzzleSelectTemplate
}

export {
	customPuzzleActions
};
export default reducers;