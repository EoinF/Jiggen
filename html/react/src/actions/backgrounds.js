import axios from 'axios';
import { getOrFetchResourceLinks } from './resourceLinks';

const FETCH_BACKGROUNDS = "FETCH_BACKGROUNDS";
const SET_BACKGROUNDS = "SET_BACKGROUNDS";
const SET_BACKGROUND = "SET_BACKGROUND";
const SELECT_BACKGROUND = "SELECT_BACKGROUND";


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

const fetchBackgrounds = () => {
	return (dispatch, getState) => {
		getOrFetchResourceLinks(dispatch, getState).then(resourceLinks => {
			dispatch(startFetchingBackgrounds());
			axios.get(resourceLinks.backgrounds)
				.then((result) => {
					dispatch(setBackgrounds(result.data));
				});
		});
	};
}

export {
	fetchBackgrounds,
	setBackgroundFromUrl,
	setBackgrounds,
	selectBackground,
	FETCH_BACKGROUNDS, 
	SET_BACKGROUNDS,
	SET_BACKGROUND,
	SELECT_BACKGROUND
};