import axios from 'axios';
import { handleActions, createActions, Action } from 'redux-actions';

import { Resource, ReducersRoot, BaseState, JiggenThunkAction } from '../models';
import { getOrFetchResourceLinks } from '../actions/resourceLinks';
import base from './base';
import { Dispatch } from 'redux';
import store from '.';

export class Background extends Resource {
	width?: number;
	height?: number;
	isCustom: Boolean;
	isUpload: Boolean;

	constructor(image: HTMLImageElement, isCustom?: Boolean, isUpload?: Boolean);
	constructor(link: string, isCustom?: Boolean, isUpload?: Boolean);
	constructor(imageOrLink: HTMLImageElement | string, isCustom: Boolean = false, isUpload: Boolean = false) {
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
	...base.initialState as BackgroundsState
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
		const savedSuggestions: Background[] = JSON.parse(localStorage.getItem('savedBackgrounds') || '[]');

		if (savedSuggestions.every((existingBackground: Background) => background.links.self !== existingBackground.links.self)) {
			savedSuggestions.push(background);
			localStorage.setItem('savedBackgrounds', JSON.stringify(savedSuggestions));
		}
			
		return {resource: background};
	},
	UPDATE_BACKGROUND: (backgroundId: string, updatedAttributes: Background) => ({resourceId: backgroundId, updatedAttributes}),
	SELECT_BACKGROUND: (backgroundId: string) => ({selectedId: backgroundId}),
	REMOVE_BACKGROUND: (background: Background) => {
		if (background.isUpload || background.isCustom) {
			 // broken upload links can never be restored so remove them for good
			const savedSuggestions: Background[] = JSON.parse(localStorage.getItem('savedBackgrounds') || '[]');
			
			localStorage.setItem('savedBackgrounds', JSON.stringify(
				savedSuggestions.filter(existingBackground => existingBackground.links.self !== background.links.self)
			));
		}
		return {resource: background};
	},
});

function fetchBackgrounds (): JiggenThunkAction {
	return async (dispatch, getState) => {
		const savedSuggestions: Background[] = JSON.parse(localStorage.getItem('savedBackgrounds') || '[]');
		dispatch(addBackgrounds(savedSuggestions));

		const resourceLinks = await getOrFetchResourceLinks(dispatch, getState);
		dispatch(startFetchingBackgrounds());
		
		const result = await axios.get(resourceLinks.backgrounds);
		dispatch(addBackgrounds(result.data));	
	};
}

function fetchBackgroundByLink(link: string): JiggenThunkAction {
	return async (dispatch, getState) => {
		dispatch(startFetchingBackgrounds());
		const result = await axios.get(link);
		dispatch(setBackground(result.data));
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