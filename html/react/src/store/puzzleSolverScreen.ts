import { createActions, handleActions, Action } from "redux-actions";
import { JiggenThunkAction, Resource } from "../models";
import { downloadedImagesActions } from "./downloadedImages";
import gwtAdapter from "../gwtAdapter";
import { generatedTemplatesActions } from "./generatedTemplates";
import { backgroundsActions } from "./backgrounds";

export interface PuzzleSolverScreenState {
    selectedBackground: string | null;
    selectedGeneratedTemplate: string | null;

    isFreshPuzzle: Boolean;
}

const initialState: PuzzleSolverScreenState = {
    isFreshPuzzle: true,
    selectedBackground: null,
    selectedGeneratedTemplate: null
};

const {
	selectGeneratedTemplate,
    selectBackground,
    setPuzzleStatus
} = createActions({
	SELECT_GENERATED_TEMPLATE: (selectedLink) => ({selectedLink}),
	SELECT_BACKGROUND: (selectedLink) => ({selectedLink}),
	SET_PUZZLE_STATUS: (isFreshPuzzle) => ({isFreshPuzzle})
});

const reducers = handleActions({
        SELECT_GENERATED_TEMPLATE: (state, {payload}: Action<any>): Partial<PuzzleSolverScreenState> => {
            const oldLink = state.selectedBackground;
            const wasFreshPuzzle = state.isFreshPuzzle;
            const isFreshPuzzle = wasFreshPuzzle || oldLink != payload.link;
            return {
                ...state,
                selectedGeneratedTemplate: payload.selectedLink,
                isFreshPuzzle,
                isLoading: isFreshPuzzle
            }
        },
        SELECT_BACKGROUND: (state, {payload}: Action<any>): Partial<PuzzleSolverScreenState> => {
            const oldLink = state.selectedBackground;
            const wasFreshPuzzle = state.isFreshPuzzle;
            const isFreshPuzzle = wasFreshPuzzle || oldLink != payload.link;
            return {
                ...state,
                selectedBackground: payload.selectedLink,
                isFreshPuzzle,
                isLoading: isFreshPuzzle
            }
        },
        SET_PUZZLE_STATUS: (state, {payload}: Action<any>): Partial<PuzzleSolverScreenState> => {
            return {
                ...state,
                isFreshPuzzle: payload.isFreshPuzzle
            }
        },
    },
	initialState
);

const selectAndDownloadBackground = (link: string): JiggenThunkAction => {
    return async (dispatch, getState) => {
        dispatch(selectBackground(link));
        const background = await backgroundsActions.getOrDownloadBackground(link, dispatch, getState);
        const downloadedBackground = await downloadedImagesActions.getOrDownloadImage(background, dispatch, getState);
        gwtAdapter.setBackground(downloadedBackground);
	};
};

const selectAndDownloadGeneratedTemplate = (link: string): JiggenThunkAction => {
    return async (dispatch, getState) => {
        dispatch(selectGeneratedTemplate(link));
        const generatedTemplate = await generatedTemplatesActions.getOrDownloadGeneratedTemplate(link, dispatch, getState);
        gwtAdapter.setTemplate(generatedTemplate);
	};
};

const puzzleSolverActions = {
    selectAndDownloadBackground,
    selectAndDownloadGeneratedTemplate,
	selectGeneratedTemplate,
    selectBackground,
    setPuzzleStatus
}

export {
	puzzleSolverActions,
};
export default reducers;