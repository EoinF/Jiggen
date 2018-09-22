import axios from 'axios';
import { API_URL } from '../constants';

const FETCH_RESOURCE_LINKS = "FETCH_RESOURCE_LINKS";
const SET_RESOURCE_LINKS = "SET_RESOURCE_LINKS";

function startFetchingResourceLinks() {
	return {
		type: FETCH_RESOURCE_LINKS
	}
}

function setResourceLinks(resourceLinks) {
	return {
		type: SET_RESOURCE_LINKS,
		resourceLinks
	};
};

function fetchResourceLinks(onFetchLinksResolver = () => {}) {
	return (dispatch, getState) => {
		dispatch(startFetchingResourceLinks);
		axios.get(API_URL).then(result => {
			const resourceLinks = result.data.links;
			dispatch(setResourceLinks(resourceLinks));
			onFetchLinksResolver(resourceLinks);
		});
	};
};

function getOrFetchResourceLinks(dispatch, getState) {
	return new Promise(function(resolve, reject) {
		if (getState().resourceLinks.isLoaded) {
			resolve(getState().resourceLinks.resourceLinks);
		} else {
			const resourceLinksThunk = fetchResourceLinks(resolve);
			resourceLinksThunk(dispatch, getState);
		}
	});
}

export {
	FETCH_RESOURCE_LINKS,
	SET_RESOURCE_LINKS,
	fetchResourceLinks,
	getOrFetchResourceLinks
}