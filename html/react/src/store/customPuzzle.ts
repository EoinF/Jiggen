import { createActions, handleActions, Action } from "redux-actions";
import { loadState } from "./localStorage";
import { StringMap, JiggenThunkAction, StateRoot } from "../models";
import uuid from 'uuid';
import { GeneratedTemplate, downloadedTemplatesActions } from "./downloadedTemplates";
import { Template, templatesActions } from "./templates";
import { Background, backgroundsActions } from "./backgrounds";
import { downloadedImagesActions } from "./downloadedImages";
import { Dispatch } from "redux";

export interface CustomPuzzle {
    id: string;
    background: string | null;
    template: string | null;
    name: string;
    isDownloaded: boolean;
}

export interface CustomPuzzleState {
    currentPuzzle: CustomPuzzle;
    puzzlesDownloading: string[];
    puzzleMap: StringMap<CustomPuzzle>;
}

const newPuzzle = (): CustomPuzzle => ({
    id: uuid.v4(),
    name: "Custom Puzzle",
    background: null,
    template: null,
    isDownloaded: false
});

const initialState: CustomPuzzleState = {
    puzzleMap: loadState("customPuzzles") || {},
    puzzlesDownloading: [],
    currentPuzzle: newPuzzle()
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
            const selectedPuzzle = (state as CustomPuzzleState).puzzleMap[payload.id] || newPuzzle();
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
                        isDownloaded: false
                    }
                },
                puzzlesDownloading: [
                    ...state.puzzlesDownloading,
                    state.currentPuzzle.id
                ]
            }
        },
        CUSTOM_PUZZLE_DELETE_PUZZLE: (state, {payload}: Action<any>): Partial<CustomPuzzleState> => {
            const updatedPuzzleMap = {...state.puzzleMap};
            delete updatedPuzzleMap[payload.customPuzzle.id];
            return {
                ...state,
                puzzleMap: updatedPuzzleMap,
                puzzlesDownloading: [...state.puzzlesDownloading].filter(id => id != payload.customPuzzle.id)
            }
        },
        CUSTOM_PUZZLE_DOWNLOAD_COMPLETE: (state, {payload}: Action<any>): Partial<CustomPuzzleState> => {
            return {
                ...state,
                puzzleMap: {
                    ...state.puzzleMap,
                    [payload.id]: {
                        ...state.puzzleMap[payload.id],
                        isDownloaded: true
                    }
                },
                puzzlesDownloading: [
                    ...state.puzzlesDownloading
                ].filter(id => id !== payload.id)
            }
        },
        CUSTOM_PUZZLE_DOWNLOAD_START: (state, {payload}: Action<any>): Partial<CustomPuzzleState> => {
            const updatedCustomPuzzle = {...state.puzzleMap[payload.id]};
            
            return {
                ...state,
                puzzleMap: {
                    ...state.puzzleMap,
                    [payload.id]: updatedCustomPuzzle
                },
                puzzlesDownloading: [
                    ...state.puzzlesDownloading,
                    payload.id
                ]
            }
        }
    },
	initialState
);


function savePuzzle(existingPuzzle: CustomPuzzle | undefined = undefined): JiggenThunkAction {
    return async (dispatch, getState) => {
        const customPuzzleState = (getState() as StateRoot).customPuzzle;
        const puzzle: CustomPuzzle = existingPuzzle || customPuzzleState.currentPuzzle;

        if (puzzle.background != null && puzzle.template != null && !customPuzzleState.puzzlesDownloading.includes(puzzle.id)) {
            if (existingPuzzle == null) { // Only dispatch a save event when it's a new puzzle being saved
                dispatch(customPuzzleSavePuzzle());
            } else {
                dispatch(customPuzzleDownloadStart(puzzle.id));
                const puzzles = Object.values(customPuzzleState.puzzleMap);
                // Delete existing resources if no longer needed
                await deleteCachedLinks(puzzle.template, puzzle.background, puzzles, dispatch, getState);
                // dispatch(customPuzzleUpdatePuzzle(puzzle));
            }

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
        const puzzles = Object.values((getState() as StateRoot).customPuzzle.puzzleMap)
            .filter(p => p.id != puzzle.id);
        const templateLink = puzzle.template!;
        const backgroundLink = puzzle.background!;

        await deleteCachedLinks(templateLink, backgroundLink, puzzles, dispatch, getState);
        dispatch(customPuzzleDeletePuzzle(puzzle));
        
    }
};

async function deleteCachedLinks (templateLink: string, backgroundLink: string, puzzles: CustomPuzzle[], dispatch: Dispatch, getState: Function): Promise<any> {
    const linksToDelete: string[] = [];

    /*
        Template
    */
    if (templateLink != null && puzzles.every(p => p.template !== templateLink)) {
        const template = await templatesActions.getOrDownloadTemplate(templateLink, dispatch, getState);
        linksToDelete.push(templateLink);
        linksToDelete.push(template.links.image);

        const downloadedTemplate = await downloadedTemplatesActions.getOrDownloadTemplate(template, dispatch, getState);

        linksToDelete.push(template.links.generatedTemplate);
        const generatedTemplate = downloadedTemplate.generatedTemplate!;

        linksToDelete.push(generatedTemplate.links.atlas);
        linksToDelete.push(...generatedTemplate.links.images);
    }
    /*
        Background
    */
    if (backgroundLink != null && puzzles.every(p => p.background !== backgroundLink)) {
        const background = await backgroundsActions.getOrDownloadBackground(backgroundLink, dispatch, getState);
        linksToDelete.push(backgroundLink);
        linksToDelete.push(background.links['image-compressed']);
        linksToDelete.push(background.links.image);
    }

    function onError(err: Error) {
        console.log(err);
    }

    await caches.open("customPuzzles").then((cache) => {
        return Promise.all(
            linksToDelete.map(link => cache.delete(link).catch(onError))
        );
    });
}

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