import axios from 'axios';
import { getOrFetchResourceLinks } from './resourceLinks';

const FETCH_GENERATED_TEMPLATE = "FETCH_GENERATED_TEMPLATE";
const SET_GENERATED_TEMPLATE = "SET_GENERATED_TEMPLATE";


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

const fetchGeneratedTemplate = (id) => {
	return (dispatch, getState) => {
		getOrFetchResourceLinks(dispatch, getState).then(resourceLinks => {
			dispatch(startFetchingGeneratedTemplate());
			axios.get(resourceLinks.generatedTemplates + '/' + id)
				.then((result) => {
					dispatch(setGeneratedTemplate(id, result.data));
				});
		});
	};
}

export {
	fetchGeneratedTemplate,
	setGeneratedTemplate,
	FETCH_GENERATED_TEMPLATE, 
	SET_GENERATED_TEMPLATE
};