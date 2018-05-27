import axios from 'axios';
import { getOrFetchResourceLinks } from './resourceLinks';

const FETCH_GENERATED_TEMPLATE = "FETCH_GENERATED_TEMPLATE";
const SET_GENERATED_TEMPLATE = "SET_GENERATED_TEMPLATE";
const ADD_GENERATED_TEMPLATES = "ADD_GENERATED_TEMPLATES";


function startFetchingGeneratedTemplate() {
	return {
		type: FETCH_GENERATED_TEMPLATE
	}
}

function setGeneratedTemplate(id, generatedTemplate) {
	return {
		type: SET_GENERATED_TEMPLATE,
		generatedTemplateId: id,
		generatedTemplate
	}
};

function addGeneratedTemplates(generatedTemplates) {
	return {
		type: ADD_GENERATED_TEMPLATES,
		generatedTemplates
	}
}

const fetchGeneratedTemplateById = id => {
	console.log("fetchGeneratedTemplate");
	return (dispatch, getState) => {
		getOrFetchResourceLinks(dispatch, getState).then(resourceLinks => {
			dispatch(startFetchingGeneratedTemplate());
			console.log(resourceLinks);
			axios.get(resourceLinks.generatedTemplates + '/' + id)
				.then((result) => {
					dispatch(setGeneratedTemplate(id, result.data));
				});
		});
	};
}

const fetchGeneratedTemplatesByLink = link => {
	return (dispatch, getState) => {
		dispatch(startFetchingGeneratedTemplate());
		axios.get(link)
			.then(result => {
				dispatch(addGeneratedTemplates(result.data))
			});
	};
};

export {
	fetchGeneratedTemplateById,
	fetchGeneratedTemplatesByLink,
	setGeneratedTemplate,
	addGeneratedTemplates,
	FETCH_GENERATED_TEMPLATE,
	SET_GENERATED_TEMPLATE,
	ADD_GENERATED_TEMPLATES
};