import { BaseAction } from 'redux-actions';
import { ThunkAction } from 'redux-thunk';
import { BackgroundsState } from '../store/backgrounds';

export interface StringMap<T> {
	[key: string]: T
}

export interface Resource {
	id: string;
	links: any;
}

export interface BaseState<T extends Resource> {
	selectedId: string | null;
	resourceMap: StringMap<T>;
	linkMap:  StringMap<T>;
	isFetching: boolean;
}

export interface StateRoot {
	templates: any,
    backgrounds: BackgroundsState,
    generatedTemplates: any,
    resourceLinks: any,
    displayOptions: any,
    playablePuzzles: any
}

export interface ReducersRoot {
	templates: any,
    backgrounds: any,
    generatedTemplates: any,
    resourceLinks: any,
    displayOptions: any,
    playablePuzzles: any
}

export interface JiggenThunkAction extends ThunkAction<any, ReducersRoot, any, BaseAction> {

}