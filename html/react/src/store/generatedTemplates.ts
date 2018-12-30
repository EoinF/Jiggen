import axios from 'axios';
import { handleActions, createActions, Action } from 'redux-actions';

import { Resource, ReducersRoot, BaseState, JiggenThunkAction, StringMap } from '../models';
import base from './base';

export interface GeneratedTemplate extends Resource {
	edges: any[];
	vertices: StringMap<any>;
	width: number;
	height: number;
	templateFile: any;
	extension: string;
}
interface GeneratedTemplateState extends BaseState<GeneratedTemplate> {}

const initialState: GeneratedTemplateState = {
	...base.initialState as GeneratedTemplateState
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

function fetchGeneratedTemplateByLink (link: string): JiggenThunkAction {
	return async (dispatch, getState) => {
		dispatch(startFetchingGeneratedTemplates());
		const result = await axios.get(link);
		dispatch(setGeneratedTemplate(result.data));
	};
}

function fetchGeneratedTemplatesByLink(link: string): JiggenThunkAction {
	return (dispatch, getState) => {
		dispatch(startFetchingGeneratedTemplates());
		axios.get(link)
			.then(result => {
				dispatch(addGeneratedTemplates(result.data));
			});
	};
};

function selectGeneratedTemplateByLink (link: string): JiggenThunkAction {
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