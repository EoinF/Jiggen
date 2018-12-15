import axios from 'axios';
import { handleActions, createActions } from 'redux-actions';
import { getOrFetchResourceLinks } from '../actions/resourceLinks';
import base from './base';

const initialState = {
	...base.initialState
};


const {startFetchingBackgrounds, setBackgrounds, setBackground, updateBackgroundById, selectBackground} = createActions({	
	START_FETCHING_BACKGROUNDS: () => ({isFetching: true}),
	SET_BACKGROUNDS: (backgrounds) => ({resourceList: backgrounds}),
	SET_BACKGROUND: (background) => ({resource: background}),
	UPDATE_BACKGROUND_BY_ID: (backgroundId, updatedAttributes) => ({resourceId: backgroundId, updatedAttributes}),
	SELECT_BACKGROUND: (backgroundId) => ({selectedId: backgroundId})
});

const fetchBackgrounds = () => {
	return async (dispatch, getState) => {
		const resourceLinks = await getOrFetchResourceLinks(dispatch, getState);
		dispatch(startFetchingBackgrounds());
		const result = await axios.get(resourceLinks.backgrounds);
		dispatch(setBackgrounds(result.data));
	};
}

const fetchBackgroundByLink = (link) => {
	return async (dispatch, getState) => {
		dispatch(startFetchingBackgrounds());
		const result = await axios.get(link);
		dispatch(setBackground(result.data));
	};
}

const loadBackgroundImageData = (background) => {
	return async(dispatch, getState) => {
		const imageLoadingPromise = new Promise((resolve, reject) => {
			const image = new Image();
			image.onload = data => resolve(image);
			image.onerror = reject;
			image.src = background.links.image;
		});
		const {width, height} = await imageLoadingPromise;
		dispatch(updateBackgroundById(background.id, {width, height}));
	}
}

const selectBackgroundByLink = (link) => {
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
		FETCH_BACKGROUNDS: (state, {payload}) => base.setIsFetching(state, payload),
		SET_BACKGROUND: (state, {payload}) => base.setOrUpdateResource(state, payload),
		SET_BACKGROUNDS: (state, {payload}) => base.setResources(state, payload),
		SELECT_BACKGROUND: (state, {payload}) => base.selectResource(state, payload),
		UPDATE_BACKGROUND: (state, {
			payload: {id, updatedAttributes}
		}) => {
			const updatedBackground = {
				...state.backgroundsMap[id],
				...updatedAttributes
			};
			return base.setOrUpdateResource(state, {background: updatedBackground});
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