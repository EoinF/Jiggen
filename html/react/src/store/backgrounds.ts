import axios from 'axios';
import { handleActions, createActions, Action } from 'redux-actions';

import { Resource, ReducersRoot, BaseState, JiggenThunkAction } from '../models';
import { getOrFetchResourceLinks } from '../actions/resourceLinks';
import base from './base';

export class Background extends Resource {
	width?: number;
	height?: number;

	constructor(image: HTMLImageElement);
	constructor(link: string);
	constructor(imageOrLink: HTMLImageElement | string) {
		let link:string;
		if (imageOrLink instanceof HTMLImageElement) {
			link = imageOrLink.src;
		} else {
			link = imageOrLink;
		}
		super(link, {
			self: link,
			image: link
		})
		if (imageOrLink instanceof HTMLImageElement) {
			this.width = imageOrLink.width;
			this.height = imageOrLink.height;
		}
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
	selectBackground
} = createActions({	
	START_FETCHING_BACKGROUNDS: () => ({isFetching: true}),
	SET_BACKGROUNDS: (backgrounds: Background[]) => ({resourceList: backgrounds}),
	ADD_BACKGROUNDS: (backgrounds: Background[]) => ({ resourceList: backgrounds}),
	SET_BACKGROUND: (background: Background) => ({resource: background}),
	UPDATE_BACKGROUND: (backgroundId: string, updatedAttributes: Background) => ({resourceId: backgroundId, updatedAttributes}),
	SELECT_BACKGROUND: (backgroundId: string) => ({selectedId: backgroundId})
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
		dispatch(startFetchingBackgrounds());
		const result = await axios.get(link);
		dispatch(setBackground(result.data));
	};
}

function loadBackgroundImageData(background: Background): JiggenThunkAction {
	return async(dispatch, getState) => {
		const imageLoadingPromise = new Promise<HTMLImageElement>((resolve, reject) => {
			const image = new Image();
			image.onload = data => resolve(image);
			image.onerror = reject;
			image.src = background.links.image;
		});
		const {width, height} = await imageLoadingPromise;
		dispatch(updateBackground(background.id, {width, height}));
	}
}

function selectBackgroundByLink (link: string): JiggenThunkAction {
	return async (dispatch, getState) => {
		const background = await base.getOrFetchResourceByLink(
			link,
			() => dispatch(fetchBackgroundByLink(link)),
			() => getState().backgrounds
		);
		dispatch(selectBackground(background.id));
	};
}

const reducers = handleActions<BackgroundsState>({
		FETCH_BACKGROUNDS: (state, {payload}: Action<any>) => base.setIsFetching(state, payload) as BackgroundsState,
		SET_BACKGROUND: (state, {payload}: Action<any>) => base.setOrUpdateResource(state, payload) as BackgroundsState,
		SET_BACKGROUNDS: (state, {payload}: Action<any>) => base.setResources(state, payload) as BackgroundsState,
		ADD_BACKGROUNDS: (state, {payload}: Action<any>) => base.addResources(state, payload) as BackgroundsState,
		SELECT_BACKGROUND: (state, {payload}: Action<any>) => base.selectResource(state, payload) as BackgroundsState,
		UPDATE_BACKGROUND: (state, {
			payload: {resourceId, updatedAttributes}
		}: any) => {
			const updatedBackground = {
				...state.resourceMap[resourceId],
				...updatedAttributes
			} as Background;
			return base.setOrUpdateResource(state, {resource: updatedBackground}) as BackgroundsState;
		}
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
	loadBackgroundImageData
}

export {
	backgroundsActions
};

export default reducers;