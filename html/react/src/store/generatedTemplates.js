import axios from 'axios';
import { handleActions, createActions } from 'redux-actions';
import base from './base';

const initialState = {
	...base.initialState
};

const {
	startFetchingGeneratedTemplates,
	setGeneratedTemplate,
	addGeneratedTemplates,
	selectGeneratedTemplate
} = createActions({
	START_FETCHING_GENERATED_TEMPLATES: () => ({isFetching: true}),
	SET_GENERATED_TEMPLATE: (generatedTemplate) => ({ resource: generatedTemplate }),
	ADD_GENERATED_TEMPLATES: (generatedTemplates) => ({ resourceList: generatedTemplates}),
	SELECT_GENERATED_TEMPLATE: (generatedTemplateId) => ({ selectedId: generatedTemplateId})
});

const fetchGeneratedTemplateByLink = (link) => {
	return async (dispatch, getState) => {
		dispatch(startFetchingGeneratedTemplates());
		const result = await axios.get(link);
		dispatch(setGeneratedTemplate(result.data));
	};
}

const fetchGeneratedTemplatesByLink = link => {
	return (dispatch, getState) => {
		dispatch(startFetchingGeneratedTemplates());
		axios.get(link)
			.then(result => {
				dispatch(addGeneratedTemplates(result.data));
			});
	};
};

const selectGeneratedTemplateByLink = (link) => {
	return async (dispatch, getState) => {
		console.log(getState());
		const generatedTemplate = await base.getOrFetchResourceByLink(
			link,
			() => dispatch(fetchGeneratedTemplateByLink(link)),
			() => getState().generatedTemplates
		);
		dispatch(selectGeneratedTemplate(generatedTemplate.id));
	};
}

const reducers = handleActions({
	 	FETCH_GENERATED_TEMPLATES: (state, {payload}) => base.setIsFetching(state, payload),
		SET_GENERATED_TEMPLATE: (state, {payload}) => base.setOrUpdateResource(state, payload),
		ADD_GENERATED_TEMPLATES: (state, {payload}) => base.addResources(state, payload),
		SELECT_GENERATED_TEMPLATE: (state, {payload}) => base.selectResource(state, payload)
	}, initialState
);

const generatedTemplatesActions = {
	fetchByLink: fetchGeneratedTemplateByLink,
	selectByLink: selectGeneratedTemplateByLink,
	fetchAllByLink: fetchGeneratedTemplatesByLink,
	setGeneratedTemplate,
	addGeneratedTemplates
}

export {
	generatedTemplatesActions
};
export default reducers;