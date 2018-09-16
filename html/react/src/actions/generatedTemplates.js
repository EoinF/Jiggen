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

const fetchGeneratedTemplateById = (id, onFetchLinksResolver = () => {}) => {
	console.log("fetchGeneratedTemplate");
	return async (dispatch, getState) => {
		const resourceLinks = await getOrFetchResourceLinks(dispatch, getState);
		dispatch(startFetchingGeneratedTemplate());
		const result = await axios.get(resourceLinks.generatedTemplates + '/' + id);
		onFetchLinksResolver(result.data);
		dispatch(setGeneratedTemplate(id, result.data));
	};
}

const getOrFetchGeneratedTemplateById = (id, dispatch, getState) => {
	return new Promise(function(resolve, reject) {
		var selectedId = getState().generatedTemplates.selectedId;
		if (selectedId != null) {
			resolve(getState().generatedTemplates.generatedTemplatesMap[selectedId]);
		} else {
			const fetchGeneratedTemplateThunk = fetchGeneratedTemplateById(id, resolve);
			fetchGeneratedTemplateThunk(dispatch, getState);
		}
	});
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
	getOrFetchGeneratedTemplateById,
	fetchGeneratedTemplatesByLink,
	setGeneratedTemplate,
	addGeneratedTemplates,
	FETCH_GENERATED_TEMPLATE,
	SET_GENERATED_TEMPLATE,
	ADD_GENERATED_TEMPLATES
};