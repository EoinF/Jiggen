import { BaseAction } from 'redux-actions';
import { ThunkAction } from 'redux-thunk';
import { BackgroundsState } from '../store/backgrounds';
import { PlayablePuzzlesState } from '../store/playablePuzzles';
import { DownloadedTemplatesState } from '../store/downloadedTemplates';
import { DisplayOptionsState } from '../store/displayOptions';
import { DownloadedImagesState } from '../store/downloadedImages';
import { PuzzleSolverScreenState } from '../store/puzzleSolverScreen';
import { TemplatesState } from '../store/templates';
import { CustomPuzzleState } from '../store/customPuzzle';

export interface StringMap<T> {
	[key: string]: T
}

export class Resource {
    links: StringMap<string>;
    name: string;
    
    constructor(links: any, name: string) {
        this.links = links;
        this.name = name;
    }

    static hasImage(resource: Resource): boolean {
        return resource.links.image != null || resource.links['image-compressed'] != null;
    }
}

export interface BaseState<T extends Resource> {
    selectedId: string | null;
	linkMap:  StringMap<T>;
	isFetching: boolean;
}

export interface StateRoot {
	templates: TemplatesState,
    backgrounds: BackgroundsState,
    downloadedTemplates: DownloadedTemplatesState,
    resourceLinks: any,
    displayOptions: DisplayOptionsState,
    playablePuzzles: PlayablePuzzlesState,
    downloadedImages: DownloadedImagesState
    puzzleSolverScreen: PuzzleSolverScreenState
    customPuzzle: CustomPuzzleState
}

export interface ReducersRoot {
	templates: any,
    backgrounds: any,
    downloadedTemplates: any,
    resourceLinks: any,
    displayOptions: any,
    playablePuzzles: any,
    downloadedImages: any,
    puzzleSolverScreen: any,
    customPuzzle: any
}

export interface JiggenThunkAction extends ThunkAction<any, ReducersRoot, any, BaseAction> {

}