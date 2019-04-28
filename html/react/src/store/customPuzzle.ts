import { createActions, handleActions, Action } from "redux-actions";
import { loadState } from "./localStorage";
import { StringMap, JiggenThunkAction, StateRoot } from "../models";
import uuid from 'uuid';
import { GeneratedTemplate, downloadedTemplatesActions } from "./downloadedTemplates";
import { Template, templatesActions } from "./templates";
import { Background, backgroundsActions } from "./backgrounds";
import { downloadedImagesActions } from "./downloadedImages";

export interface CustomPuzzle {
    id: string;
    background: string | null;
    template: string | null;
    name: string;
    isDownloaded: boolean;
    isDownloading: boolean;
}

export interface CustomPuzzleState {
    currentPuzzle: CustomPuzzle;
    puzzleMap: StringMap<CustomPuzzle>;
}

const newPuzzle = {
    id: uuid.v4(),
    name: "Custom Puzzle",
    background: null,
    template: null,
    isDownloaded: false,
    isDownloading: false
};

const initialState: CustomPuzzleState = {
    puzzleMap: loadState("customPuzzles") || {},
    currentPuzzle: {...newPuzzle}
};

const {
    customPuzzleSetName,
	customPuzzleSelectTemplate,
    customPuzzleSelectBackground,
    customPuzzleSelectPuzzle,
    customPuzzleSavePuzzle,
    customPuzzleDeletePuzzle,
    customPuzzleDownloadComplete,
    customPuzzleDownloadStart
} = createActions({
	CUSTOM_PUZZLE_SET_NAME: (name) => ({name}),
	CUSTOM_PUZZLE_SELECT_TEMPLATE: (selectedLink) => ({selectedLink}),
    CUSTOM_PUZZLE_SELECT_BACKGROUND: (selectedLink) => ({selectedLink}),
    CUSTOM_PUZZLE_SELECT_PUZZLE: (id) => ({id}),
    CUSTOM_PUZZLE_SAVE_PUZZLE: () => ({}),
    CUSTOM_PUZZLE_DELETE_PUZZLE: (customPuzzle: CustomPuzzle) => ({customPuzzle}),
    CUSTOM_PUZZLE_DOWNLOAD_COMPLETE: (id) => ({id}),
    CUSTOM_PUZZLE_DOWNLOAD_START: (id) => ({id})
});

const reducers = handleActions({
        CUSTOM_PUZZLE_SET_NAME: (state, {payload}: Action<any>): Partial<CustomPuzzleState> => {
            return {
                ...state,
                currentPuzzle: {
                    ...state.currentPuzzle,
                    name: payload.name
                }
            }
        },
        CUSTOM_PUZZLE_SELECT_TEMPLATE: (state, {payload}: Action<any>): Partial<CustomPuzzleState> => {
            return {
                ...state,
                currentPuzzle: {
                    ...state.currentPuzzle,
                    template: payload.selectedLink
                }
            }
        },
        CUSTOM_PUZZLE_SELECT_BACKGROUND: (state, {payload}: Action<any>): Partial<CustomPuzzleState> => {
            return {
                ...state,
                currentPuzzle: {
                    ...state.currentPuzzle,
                    background: payload.selectedLink
                }
            }
        },
        CUSTOM_PUZZLE_SELECT_PUZZLE: (state, {payload}: Action<any>): Partial<CustomPuzzleState> => {
            const selectedPuzzle = (state as CustomPuzzleState).puzzleMap[payload.id] || {...newPuzzle};
            return {
                ...state,
                currentPuzzle: selectedPuzzle
            }
        },
        CUSTOM_PUZZLE_SAVE_PUZZLE: (state, {payload}: Action<any>): Partial<CustomPuzzleState> => {
            return {
                ...state,
                puzzleMap: {
                    ...state.puzzleMap,
                    [state.currentPuzzle.id]: {
                        ...state.currentPuzzle,
                        isDownloaded: false,
                        isDownloading: true
                    }
                }
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
        CUSTOM_PUZZLE_DOWNLOAD_COMPLETE: (state, {payload}: Action<any>): Partial<CustomPuzzleState> => {
            const updatedCustomPuzzle = {...state.puzzleMap[payload.id]};
            updatedCustomPuzzle.isDownloaded = true;
            updatedCustomPuzzle.isDownloading = false;
            
            return {
                ...state,
                puzzleMap: {
                    ...state.puzzleMap,
                    [payload.id]: updatedCustomPuzzle
                }
            }
        },
        CUSTOM_PUZZLE_DOWNLOAD_START: (state, {payload}: Action<any>): Partial<CustomPuzzleState> => {
            const updatedCustomPuzzle = {...state.puzzleMap[payload.id]};
            updatedCustomPuzzle.isDownloading = true;
            
            return {
                ...state,
                puzzleMap: {
                    ...state.puzzleMap,
                    [payload.id]: updatedCustomPuzzle
                }
            }
        }
    },
	initialState
);


function savePuzzle(existingPuzzle: CustomPuzzle | undefined = undefined): JiggenThunkAction {
    return async (dispatch, getState) => {
        const puzzle: CustomPuzzle = existingPuzzle || getState().customPuzzle.currentPuzzle;
        if (existingPuzzle == null) { // Only dispatch a new event when it's a new puzzle being saved
            dispatch(customPuzzleSavePuzzle());
        } else {
            dispatch(customPuzzleDownloadStart(puzzle.id));
        }

        if (puzzle.background != null && puzzle.template != null) {
            const templateLink = puzzle.template;
            const backgroundLink = puzzle.background;

            /*
                Template
            */
            await templatesActions.getOrDownloadTemplate(templateLink, dispatch, getState);
            const templateResponse = await fetch(templateLink);
            const template = await templateResponse.clone().json() as Template;

            const templateImageLink = template.links.image;
            const templateImageResponse = await fetch(templateImageLink);

            // Wait for the downloaded template to download first (avoids duplicate downloads)
            await downloadedTemplatesActions.getOrDownloadTemplate(template, dispatch, getState);

            const generatedTemplateLink = template.links.generatedTemplate;
            const generatedTemplateResponse = await fetch(generatedTemplateLink);
            const generatedTemplate = await generatedTemplateResponse.clone().json() as GeneratedTemplate;

            const atlasLink = generatedTemplate.links.atlas;
            const atlasResponse = await fetch(atlasLink);

            const atlasImageResponses = await Promise.all<any>(generatedTemplate.links.images.map(async link => {
                    return {
                        link,
                        response: await fetch(link)
                    }
                }
            ));
            
            /*
                Background
            */
            await backgroundsActions.getOrDownloadBackground(backgroundLink, dispatch, getState);
            const backgroundResponse = await fetch(backgroundLink);
            const background = await backgroundResponse.clone().json() as Background;

            const backgroundImageCompressedLink = background.links['image-compressed'];
            const backgroundImageCompressedResponse = await fetch(backgroundImageCompressedLink);
            
            // Wait for the background image to download (avoids duplicate downloads)
            const backgroundImageLink = background.links.image;
            const backgroundImageResponse = await new Promise<Response>((resolve => {
                dispatch(downloadedImagesActions.downloadImage(background, resolve));
            }));
            
            await caches.open("customPuzzles").then((cache) => {
                return Promise.all([
                    cache.put(templateLink, templateResponse),
                    cache.put(templateImageLink, templateImageResponse),
                    cache.put(backgroundLink, backgroundResponse),
                    cache.put(backgroundImageLink, backgroundImageResponse),
                    cache.put(backgroundImageCompressedLink, backgroundImageCompressedResponse),
                    cache.put(generatedTemplateLink, generatedTemplateResponse),
                    cache.put(atlasLink, atlasResponse),
                    ...atlasImageResponses.map(({link, response}) => 
                        cache.put(link, response)
                    )
                ]);
            });
            
            dispatch(customPuzzleDownloadComplete(puzzle.id));
        }
    };
}

function deletePuzzle(puzzle: CustomPuzzle): JiggenThunkAction {
    return async (dispatch, getState) => {
        const templateLink = puzzle.template!;
        const backgroundLink = puzzle.background!;

        /*
            Template
        */
        const template = await templatesActions.getOrDownloadTemplate(templateLink, dispatch, getState);
        const templateImageLink = template.links.image;

        const downloadedTemplate = await downloadedTemplatesActions.getOrDownloadTemplate(template, dispatch, getState);

        const generatedTemplateLink = template.links.generatedTemplate;
        const generatedTemplate = downloadedTemplate.generatedTemplate!;

        const atlasLink = generatedTemplate.links.atlas;
        
        /*
            Background
        */
        const background = await backgroundsActions.getOrDownloadBackground(backgroundLink, dispatch, getState);
        const backgroundImageCompressedLink = background.links['image-compressed'];
        const backgroundImageLink = background.links.image;

        function onError(err: Error) {
            console.log(err);
        }

        await caches.open("customPuzzles").then((cache) => {
            return Promise.all([
                cache.delete(templateLink).catch(onError),
                cache.delete(templateImageLink).catch(onError),
                cache.delete(backgroundLink).catch(onError),
                cache.delete(backgroundImageLink).catch(onError),
                cache.delete(backgroundImageCompressedLink).catch(onError),
                cache.delete(generatedTemplateLink).catch(onError),
                cache.delete(atlasLink).catch(onError),
                ...generatedTemplate.links.images.map(link => 
                    cache.delete(link).catch(onError)
                )
            ]);
        });
        
        dispatch(customPuzzleDeletePuzzle(puzzle));
    }
};

const customPuzzleActions = {
    selectBackground: customPuzzleSelectBackground,
    selectTemplate: customPuzzleSelectTemplate,
    setName: customPuzzleSetName,
    selectPuzzle: customPuzzleSelectPuzzle,
    savePuzzle,
    deletePuzzle
}

export {
	customPuzzleActions
};
export default reducers;