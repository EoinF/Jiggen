import { createActions, handleActions, Action } from "redux-actions";
import { JiggenThunkAction, StateRoot } from "../models";
import { downloadedImagesActions } from "./downloadedImages";
import gwtAdapter from "../gwtAdapter";
import { downloadedTemplatesActions } from "./downloadedTemplates";
import { backgroundsActions, Background } from "./backgrounds";
import { templatesActions } from "./templates";

export interface PuzzleSolverScreenState {
    isActive: boolean;

    selectedBackground: string | null;
    selectedTemplate: string | null;

    isTemplateReady: boolean;
    isBackgroundReady: boolean;

    isFreshPuzzle: boolean;
}

const initialState: PuzzleSolverScreenState = {
    isActive: false,
    isFreshPuzzle: true,
    selectedBackground: null,
    selectedTemplate: null,
    isTemplateReady: false,
    isBackgroundReady: false
};

const {
	puzzleSolverSelectTemplate,
    puzzleSolverSelectBackground,
    puzzleSolverSetPuzzleStatus,
    puzzleSolverSetIsActive,
    puzzleSolverSetTemplateReady,
    puzzleSolverSetBackgroundReady
} = createActions({
	PUZZLE_SOLVER_SELECT_TEMPLATE: (selectedLink) => ({selectedLink}),
	PUZZLE_SOLVER_SELECT_BACKGROUND: (selectedLink) => ({selectedLink}),
    PUZZLE_SOLVER_SET_PUZZLE_STATUS: (isFreshPuzzle) => ({isFreshPuzzle}),
    PUZZLE_SOLVER_SET_IS_ACTIVE: (isActive) => ({isActive}),
    PUZZLE_SOLVER_SET_TEMPLATE_READY: () => ({}),
    PUZZLE_SOLVER_SET_BACKGROUND_READY: () => ({}),
});

const reducers = handleActions({
        PUZZLE_SOLVER_SELECT_TEMPLATE: (state, {payload}: Action<any>): Partial<PuzzleSolverScreenState> => {
            const oldLink = state.selectedBackground;
            const wasFreshPuzzle = state.isFreshPuzzle;
            const isFreshPuzzle = wasFreshPuzzle || oldLink !== payload.link;
            return {
                ...state,
                selectedTemplate: payload.selectedLink,
                isFreshPuzzle,
                isTemplateReady: false
            }
        },
        PUZZLE_SOLVER_SELECT_BACKGROUND: (state, {payload}: Action<any>): Partial<PuzzleSolverScreenState> => {
            const oldLink = state.selectedBackground;
            const wasFreshPuzzle = state.isFreshPuzzle;
            const isFreshPuzzle = wasFreshPuzzle || oldLink !== payload.link;
            return {
                ...state,
                selectedBackground: payload.selectedLink,
                isFreshPuzzle,
                isBackgroundReady: false
            }
        },
        PUZZLE_SOLVER_SET_PUZZLE_STATUS: (state, {payload}: Action<any>): Partial<PuzzleSolverScreenState> => {
            return {
                ...state,
                isFreshPuzzle: payload.isFreshPuzzle
            }
        },
        PUZZLE_SOLVER_SET_IS_ACTIVE: (state, {payload}: Action<any>): Partial<PuzzleSolverScreenState> => {
            return {
                ...state,
                isActive: payload.isActive
            }
        },
        PUZZLE_SOLVER_SET_TEMPLATE_READY: (state, {payload}: Action<any>): Partial<PuzzleSolverScreenState> => {
            return {
                ...state,
                isTemplateReady: true
            }
        },
        PUZZLE_SOLVER_SET_BACKGROUND_READY: (state, {payload}: Action<any>): Partial<PuzzleSolverScreenState> => {
            return {
                ...state,
                isBackgroundReady: true
            }
        },
    },
	initialState
);

const selectAndDownloadBackground = (link: string, isCustom: boolean): JiggenThunkAction => {
    return async (dispatch, getState) => {
        const state = getState() as StateRoot;
        if (state.puzzleSolverScreen.selectedBackground !== link) {
            dispatch(puzzleSolverSelectBackground(link));
            let background: Background;
            if (isCustom) {
                background = new Background(link, true);
                backgroundsActions.setBackground(background);
            } else {
                background = await backgroundsActions.getOrDownloadBackground(link, dispatch, getState);
            }
            const downloadedBackground = await downloadedImagesActions.getOrDownloadImage(background, dispatch, getState);
            gwtAdapter.setBackground(downloadedBackground);
            dispatch(puzzleSolverSetBackgroundReady());
        }
	};
};

const selectAndDownloadTemplate = (link: string): JiggenThunkAction => {
    return async (dispatch, getState) => {
        const state = getState() as StateRoot;
        if (state.puzzleSolverScreen.selectedTemplate !== link) {
            dispatch(puzzleSolverSelectTemplate(link));
            const template = await templatesActions.getOrDownloadTemplate(link, dispatch, getState);
            const downloadedTemplate = await downloadedTemplatesActions.getOrDownloadTemplate(template, dispatch, getState);
            gwtAdapter.setTemplate(downloadedTemplate);
            dispatch(puzzleSolverSetTemplateReady());
        }
	};
};

const setIsActive = (isActive: boolean) => {
    if (isActive) {
        gwtAdapter.resumeGame();
    } else {
        gwtAdapter.pauseGame();
    }
    return puzzleSolverSetIsActive(isActive);
} 

const puzzleSolverActions = {
    selectAndDownloadBackground,
    selectAndDownloadTemplate,
    setPuzzleStatus: puzzleSolverSetPuzzleStatus,
    setIsActive
}

export {
	puzzleSolverActions,
};
export default reducers;