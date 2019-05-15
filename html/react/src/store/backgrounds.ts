import axios from 'axios';
import { handleActions, createActions, Action } from 'redux-actions';

import { Resource, BaseState, JiggenThunkAction, StateRoot, StringMap } from '../models';
import { getOrFetchResourceLinks } from '../actions/resourceLinks';
import base from './base';
import { Dispatch } from 'redux';
import store from '.';
import { loadState, saveState } from './localStorage';
import { backgroundsStateKey } from '../constants';

export class Background extends Resource {
	width?: number;
	height?: number;
	isCustom: boolean;
	isUpload: boolean;

	constructor(image: HTMLImageElement, isCustom?: boolean, isUpload?: boolean);
	constructor(link: string, isCustom?: boolean, isUpload?: boolean);
	constructor(imageOrLink: HTMLImageElement | string, isCustom: boolean = false, isUpload: boolean = false) {
		let link:string;
		if (imageOrLink instanceof HTMLImageElement) {
			link = imageOrLink.src;
		} else {
			link = imageOrLink;
		}
		super({
			self: link,
			image: link
		}, "Custom image");
		if (imageOrLink instanceof HTMLImageElement) {
			this.width = imageOrLink.width;
			this.height = imageOrLink.height;
		}
		this.isCustom = isCustom;
		this.isUpload = isUpload;
	}
}
export interface BackgroundsState extends BaseState<Background> {}

const initialState: BackgroundsState = {
	...base.initialState as BackgroundsState,
	linkMap: loadState(backgroundsStateKey) || {}
};

const {
	startFetchingBackgrounds,
	setBackgrounds,
	addBackgrounds,
	setBackground,
	updateBackground,
	selectBackground,
	removeBackground
} = createActions({
	START_FETCHING_BACKGROUNDS: () => ({isFetching: true}),
	SET_BACKGROUNDS: (backgrounds: Background[]) => ({resourceList: backgrounds}),
	ADD_BACKGROUNDS: (backgrounds: Background[]) => ({ resourceList: backgrounds}),
	SET_BACKGROUND: (background: Background) => {
		const savedSuggestions: StringMap<Background> = loadState(backgroundsStateKey) || {};

		if ((background.isCustom || background.isUpload)
			&& Object.keys(savedSuggestions).every((link: string) => background.links.self !== link)) {
			savedSuggestions[background.links.self] = background;
			saveState(backgroundsStateKey, savedSuggestions);
		}
			
		return {resource: background};
	},
	UPDATE_BACKGROUND: (backgroundId: string, updatedAttributes: Background) => ({resourceId: backgroundId, updatedAttributes}),
	SELECT_BACKGROUND: (backgroundId: string) => ({selectedId: backgroundId}),
	REMOVE_BACKGROUND: (background: Background) => {
		// broken upload links can never be restored so remove them for good
		const savedSuggestions: StringMap<Background> = loadState(backgroundsStateKey) || {};
		delete savedSuggestions[background.links.self];
		saveState(backgroundsStateKey, savedSuggestions);
		
		return {resource: background};
	},
});

function fetchBackgrounds (): JiggenThunkAction {
	return async (dispatch, getState) => {
		const resourceLinks = await getOrFetchResourceLinks(dispatch, getState);
		dispatch(startFetchingBackgrounds());
		
		const result = await axios.get(resourceLinks.backgrounds);
		dispatch(addBackgrounds(result.data));	
	};
}

function fetchBackgroundByLink(link: string): JiggenThunkAction {
	return async (dispatch, getState) => {
		const existingBackground = (getState() as StateRoot).backgrounds.linkMap[link];
		if (existingBackground == null) {
			dispatch(startFetchingBackgrounds());
			const result = await axios.get(link);
			dispatch(setBackground(result.data));
		}
	};
}

const getOrDownloadBackground = (link: string, dispatch: Dispatch, getState: any) => {
    return new Promise<Background>(resolve => {
		const background = getState().backgrounds.linkMap[link];
        if (background != null) {
            resolve(background);
        } else {
			const unsubscribe = store.subscribe(() => {
                const background = getState().backgrounds.linkMap[link];
				if (background != null) {
					resolve(background);
                    unsubscribe();
                }
            });
			const fetchBackgroundThunk = fetchBackgroundByLink(link);
			fetchBackgroundThunk(dispatch, getState, null);
        }
    });
}

function selectBackgroundByLink (link: string): JiggenThunkAction {
	return async (dispatch, getState) => {
		const background = await base.getOrFetchResourceByLink(
			link,
			() => dispatch(fetchBackgroundByLink(link)),
			() => getState().backgrounds
		);
		dispatch(selectBackground(background.links.self));
	};
}

const reducers = handleActions<BackgroundsState>({
		FETCH_BACKGROUNDS: (state, {payload}: Action<any>) => base.setIsFetching(state, payload) as BackgroundsState,
		SET_BACKGROUND: (state, {payload}: Action<any>) => base.setOrUpdateResource(state, payload) as BackgroundsState,
		SET_BACKGROUNDS: (state, {payload}: Action<any>) => base.setResources(state, payload) as BackgroundsState,
		ADD_BACKGROUNDS: (state, {payload}: Action<any>) => {
			return base.addResources(state, payload) as BackgroundsState
		},
		SELECT_BACKGROUND: (state, {payload}: Action<any>) => base.selectResource(state, payload) as BackgroundsState,
		UPDATE_BACKGROUND: (state, {
			payload: {resourceId, updatedAttributes}
		}: any) => {
			const updatedBackground = {
				...state.linkMap[resourceId],
				...updatedAttributes
			} as Background;
			return base.setOrUpdateResource(state, {resource: updatedBackground}) as BackgroundsState;
		},
		REMOVE_BACKGROUND: (state, {payload}: Action<any>) => base.removeResource(state, payload) as BackgroundsState
	},
	initialState
);

const backgroundsActions = {
	fetchBackgrounds,
	fetchByLink: fetchBackgroundByLink,
	setBackground,
	setBackgrounds,
	addBackgrounds,
	selectById: selectBackground,
	selectByLink: selectBackgroundByLink,
	removeBackground,
	getOrDownloadBackground
}

export {
	backgroundsActions
};

export default reducers;