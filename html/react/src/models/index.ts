import { BaseAction } from 'redux-actions';
import { ThunkAction } from 'redux-thunk';
import { BackgroundsState } from '../store/backgrounds';
import { PlayablePuzzlesState } from '../store/playablePuzzles';
import { GeneratedTemplatesState } from '../store/generatedTemplates';
import { DisplayOptionsState } from '../store/displayOptions';
import { DownloadedImagesState } from '../store/downloadedImages';

export interface StringMap<T> {
	[key: string]: T
}

export class Resource {
	id: string;
    links: any;
    name: string;
    
    constructor(id: string, links: any, name: string) {
        this.id = id;
        this.links = links;
        this.name = name;
    }
}

export interface BaseState<T extends Resource> {
    selectedId: string | null;
    resourceList: T[];
	resourceMap: StringMap<T>;
	linkMap:  StringMap<T>;
	isFetching: boolean;
}

export interface StateRoot {
	templates: any,
    backgrounds: BackgroundsState,
    generatedTemplates: GeneratedTemplatesState,
    resourceLinks: any,
    displayOptions: DisplayOptionsState,
    playablePuzzles: PlayablePuzzlesState,
    downloadedImages: DownloadedImagesState
}

export interface ReducersRoot {
	templates: any,
    backgrounds: any,
    generatedTemplates: any,
    resourceLinks: any,
    displayOptions: any,
    playablePuzzles: any,
    downloadedImages: any
}

export interface JiggenThunkAction extends ThunkAction<any, ReducersRoot, any, BaseAction> {

}