import axios from 'axios';
import { ThunkAction } from 'redux-thunk';
import { handleActions, createActions, Action, BaseAction } from 'redux-actions';

import { Resource, ReducersRoot, BaseState, JiggenThunkAction } from '../models';
import { getOrFetchResourceLinks } from '../actions/resourceLinks';
import base from './base';

export interface Background extends Resource {}
export interface BackgroundsState extends BaseState<Background> {}

const initialState: BackgroundsState = {
	...base.initialState
};

const {startFetchingBackgrounds, setBackgrounds, setBackground, updateBackgroundById, selectBackground} = createActions({	
	START_FETCHING_BACKGROUNDS: () => ({isFetching: true}),
	SET_BACKGROUNDS: (backgrounds) => ({resourceList: backgrounds}),
	SET_BACKGROUND: (background) => ({resource: background}),
	UPDATE_BACKGROUND_BY_ID: (backgroundId, updatedAttributes) => ({resourceId: backgroundId, updatedAttributes}),
	SELECT_BACKGROUND: (backgroundId) => ({selectedId: backgroundId})
});

function fetchBackgrounds (): JiggenThunkAction {
	return async (dispatch, getState) => {
		const resourceLinks = await getOrFetchResourceLinks(dispatch, getState);
		dispatch(startFetchingBackgrounds());
		const result = await axios.get(resourceLinks.backgrounds);
		dispatch(setBackgrounds(result.data));
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
		dispatch(updateBackgroundById(background.id, {width, height}));
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

const reducers = handleActions({
		FETCH_BACKGROUNDS: (state, {payload}: Action<any>) => base.setIsFetching(state, payload),
		SET_BACKGROUND: (state, {payload}: Action<any>) => base.setOrUpdateResource(state, payload),
		SET_BACKGROUNDS: (state, {payload}: Action<any>) => base.setResources(state, payload),
		SELECT_BACKGROUND: (state, {payload}: Action<any>) => base.selectResource(state, payload),
		UPDATE_BACKGROUND: (state, {
			payload: {id, updatedAttributes}
		}) => {
			const updatedBackground = {
				...state.backgroundsMap[id],
				...updatedAttributes
			} as Background;
			return base.setOrUpdateResource(state, {resource: updatedBackground});
		}
	},
	initialState
);

const backgroundsActions = {
	fetchBackgrounds,
	setBackground,
	setBackgrounds,
	selectBackground,
	selectByLink: selectBackgroundByLink,
	loadBackgroundImageData
}

export {
	backgroundsActions
};

export default reducers;