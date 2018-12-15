import axios from 'axios';
import { handleActions, createActions, Action, BaseAction } from 'redux-actions';
import base from './base';
import { ThunkAction } from 'redux-thunk';
import { Resource, ReducersRoot } from './models';

interface GeneratedTemplate extends Resource {}

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
	SET_GENERATED_TEMPLATE: (generatedTemplate: GeneratedTemplate) => ({ resource: generatedTemplate }),
	ADD_GENERATED_TEMPLATES: (generatedTemplates: GeneratedTemplate[]) => ({ resourceList: generatedTemplates}),
	SELECT_GENERATED_TEMPLATE: (generatedTemplateId: string) => ({ selectedId: generatedTemplateId})
});

function fetchGeneratedTemplateByLink (link: string): ThunkAction<any, ReducersRoot, any, BaseAction> {
	return async (dispatch, getState) => {
		dispatch(startFetchingGeneratedTemplates());
		const result = await axios.get(link);
		dispatch(setGeneratedTemplate(result.data));
	};
}

function fetchGeneratedTemplatesByLink(link: string): ThunkAction<any, ReducersRoot, any, BaseAction> {
	return (dispatch, getState) => {
		dispatch(startFetchingGeneratedTemplates());
		axios.get(link)
			.then(result => {
				dispatch(addGeneratedTemplates(result.data));
			});
	};
};

function selectGeneratedTemplateByLink (link: string): ThunkAction<any, ReducersRoot, any, BaseAction> {
	return async (dispatch, getState) => {
		const generatedTemplate = await base.getOrFetchResourceByLink(
			link,
			() => dispatch(fetchGeneratedTemplateByLink(link)),
			() => getState().generatedTemplates
		);
		dispatch(selectGeneratedTemplate(generatedTemplate.id));
	};
}

const reducers = handleActions({
	 	FETCH_GENERATED_TEMPLATES: (state, {payload}: Action<any>) => base.setIsFetching(state, payload),
		SET_GENERATED_TEMPLATE: (state, {payload}: Action<any>) => base.setOrUpdateResource(state, payload),
		ADD_GENERATED_TEMPLATES: (state, {payload}: Action<any>) => base.addResources(state, payload),
		SELECT_GENERATED_TEMPLATE: (state, {payload}: Action<any>) => base.selectResource(state, payload)
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