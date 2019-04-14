import { createActions, handleActions, Action } from "redux-actions";
import { JiggenThunkAction, Resource, StateRoot } from "../models";
import { downloadedImagesActions } from "./downloadedImages";
import gwtAdapter from "../gwtAdapter";
import { generatedTemplatesActions } from "./generatedTemplates";
import { backgroundsActions } from "./backgrounds";
import { templatesActions } from "./templates";
import { CustomPuzzle } from "./customPuzzle";

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
	selectTemplate,
    selectBackground,
    setPuzzleStatus,
    setIsActive,
    setTemplateReady,
    setBackgroundReady
} = createActions({
	SELECT_TEMPLATE: (selectedLink) => ({selectedLink}),
	SELECT_BACKGROUND: (selectedLink) => ({selectedLink}),
    SET_PUZZLE_STATUS: (isFreshPuzzle) => ({isFreshPuzzle}),
    SET_IS_ACTIVE: (isActive) => ({isActive}),
    SET_TEMPLATE_READY: () => ({}),
    SET_BACKGROUND_READY: () => ({}),
});

const reducers = handleActions({
        SELECT_TEMPLATE: (state, {payload}: Action<any>): Partial<PuzzleSolverScreenState> => {
            const oldLink = state.selectedBackground;
            const wasFreshPuzzle = state.isFreshPuzzle;
            const isFreshPuzzle = wasFreshPuzzle || oldLink != payload.link;
            return {
                ...state,
                selectedTemplate: payload.selectedLink,
                isFreshPuzzle,
                isTemplateReady: false
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
                isBackgroundReady: false
            }
        },
        SET_PUZZLE_STATUS: (state, {payload}: Action<any>): Partial<PuzzleSolverScreenState> => {
            return {
                ...state,
                isFreshPuzzle: payload.isFreshPuzzle
            }
        },
        SET_IS_ACTIVE: (state, {payload}: Action<any>): Partial<PuzzleSolverScreenState> => {
            return {
                ...state,
                isActive: payload.isActive
            }
        },
        SET_TEMPLATE_READY: (state, {payload}: Action<any>): Partial<PuzzleSolverScreenState> => {
            return {
                ...state,
                isTemplateReady: true
            }
        },
        SET_BACKGROUND_READY: (state, {payload}: Action<any>): Partial<PuzzleSolverScreenState> => {
            return {
                ...state,
                isBackgroundReady: true
            }
        },
    },
	initialState
);

const selectAndDownloadBackground = (link: string): JiggenThunkAction => {
    return async (dispatch, getState) => {
        const state = getState() as StateRoot;
        if (state.puzzleSolverScreen.selectedBackground !== link) {
            dispatch(selectBackground(link));
            const background = await backgroundsActions.getOrDownloadBackground(link, dispatch, getState);
            const downloadedBackground = await downloadedImagesActions.getOrDownloadImage(background, dispatch, getState);
            gwtAdapter.setBackground(downloadedBackground);
            dispatch(setBackgroundReady());
        }
	};
};

const selectAndDownloadTemplate = (link: string): JiggenThunkAction => {
    return async (dispatch, getState) => {
        const state = getState() as StateRoot;
        if (state.puzzleSolverScreen.selectedTemplate !== link) {
            dispatch(selectTemplate(link));
            const template = await templatesActions.getOrDownloadTemplate(link, dispatch, getState);
            const generatedTemplate = await generatedTemplatesActions.getOrDownloadGeneratedTemplate(template.links.generatedTemplate, dispatch, getState);
            gwtAdapter.setTemplate(generatedTemplate);
            dispatch(setTemplateReady());
        }
	};
};

const puzzleSolverActions = {
    selectAndDownloadBackground,
    selectAndDownloadTemplate,
    setPuzzleStatus,
    setIsActive
}

export {
	puzzleSolverActions,
};
export default reducers;