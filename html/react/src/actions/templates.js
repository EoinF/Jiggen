import axios from 'axios';
import { getOrFetchResourceLinks } from './resourceLinks';

const FETCH_TEMPLATES = "FETCH_TEMPLATES";
const SET_TEMPLATES = "SET_TEMPLATES";
const SELECT_TEMPLATE = "SELECT_TEMPLATE";


function startFetchingTemplates() {
	return {
		type: FETCH_TEMPLATES
	}
}

function setTemplates(id, templates) {
	return {
		type: SET_TEMPLATES,
		templates
	}
};

function selectTemplate(id) {
	return {
		type: SELECT_TEMPLATE,
		id
	}
}

const fetchTemplates = (id) => {
	return (dispatch, getState) => {
		getOrFetchResourceLinks(dispatch, getState).then(resourceLinks => {
			dispatch(startFetchingTemplates());
			axios.get(resourceLinks.templates)
				.then((result) => {
					dispatch(setTemplates(id, result.data));
				});
		});
	};
}

export {
	fetchTemplates,
	setTemplates,
	selectTemplate,
	FETCH_TEMPLATES, 
	SET_TEMPLATES,
	SELECT_TEMPLATE
};