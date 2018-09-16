import axios from 'axios';
import { getOrFetchResourceLinks } from './resourceLinks';

const FETCH_BACKGROUNDS = "FETCH_BACKGROUNDS";
const SET_BACKGROUNDS = "SET_BACKGROUNDS";
const SET_BACKGROUND = "SET_BACKGROUND";
const SELECT_BACKGROUND = "SELECT_BACKGROUND";
const UPDATE_BACKGROUND = "UPDATE_BACKGROUND";


function startFetchingBackgrounds() {
	return {
		type: FETCH_BACKGROUNDS
	}
}

function setBackgrounds(backgrounds) {
	return {
		type: SET_BACKGROUNDS,
		backgrounds
	}
};

function setBackgroundFromUrl(url) {
	return {
		type: SET_BACKGROUND,
		background: {
			id: url,
			name: url,
			links: {
				image: url
			}
		}
	}
};

function selectBackground(id) {
	return {
		type: SELECT_BACKGROUND,
		id
	}
}

function updateBackgroundById(id, updatedAttributes) {
	return {
		type: UPDATE_BACKGROUND,
		id,
		updatedAttributes
	}
}

const fetchBackgrounds = () => {
	return async (dispatch, getState) => {
		const resourceLinks = await getOrFetchResourceLinks(dispatch, getState);
		dispatch(startFetchingBackgrounds());
		const result = await axios.get(resourceLinks.backgrounds);
		dispatch(setBackgrounds(result.data));
	};
}

const loadBackgroundImageDataById = (id, imageSrc) => {
	return async(dispatch, getState) => {
		console.log('blah');
		const imageLoadingPromise = new Promise((resolve, reject) => {
			const image = new Image();
			image.onload = data => resolve(image);
			image.onerror = reject;
			image.src = imageSrc;
		});
		const {width, height} = await imageLoadingPromise;
		dispatch(updateBackgroundById(id, {width, height}));
	}
}

const backgroundsActions = {
	fetchBackgrounds,
	setBackgroundFromUrl,
	setBackgrounds,
	selectBackground,
	loadBackgroundImageDataById
}

export {
	backgroundsActions,
	FETCH_BACKGROUNDS,
	SET_BACKGROUNDS,
	SET_BACKGROUND,
	SELECT_BACKGROUND,
	UPDATE_BACKGROUND
};