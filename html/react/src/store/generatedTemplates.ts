import axios from 'axios';
import { handleActions, createActions, Action } from 'redux-actions';

import { Resource, ReducersRoot, BaseState, JiggenThunkAction, StringMap } from '../models';
import base from './base';
import { Dispatch } from 'redux';
import store from '.';

export interface GeneratedTemplate extends Resource {
	edges: any[];
	vertices: StringMap<any>;
	width: number;
	height: number;
	templateFile: any;
	extension: string;
}
export interface GeneratedTemplatesState extends BaseState<GeneratedTemplate> {}

const initialState: GeneratedTemplatesState = {
	...base.initialState as GeneratedTemplatesState
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

function fetchGeneratedTemplateByLink (link: string, onDownloadCompleteResolver = () => {}): JiggenThunkAction {
	return async (dispatch, getState) => {
		dispatch(startFetchingGeneratedTemplates());
		const result = await axios.get(link);
		dispatch(setGeneratedTemplate(result.data));
		onDownloadCompleteResolver();
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
		dispatch(selectGeneratedTemplate(generatedTemplate.links.self));
	};
}


const getOrDownloadGeneratedTemplate = (link: string, dispatch: Dispatch, getState: Function) => {
    return new Promise(resolve => {
        const generatedTemplate = getState().generatedTemplates.linkMap[link];
        if (generatedTemplate != null && generatedTemplate.vertices != null) {
            resolve(generatedTemplate);
        } else {
			const unsubscribe = store.subscribe(() => {
                const _generatedTemplate = getState().generatedTemplates.linkMap[link];
				if (_generatedTemplate != null && _generatedTemplate.vertices != null) {
					resolve(_generatedTemplate);
                    unsubscribe();
                }
            });
            fetchGeneratedTemplateByLink(link);
        }
    });
}

const reducers = handleActions({
		START_FETCHING_GENERATED_TEMPLATES: (state, {payload}: Action<any>) => base.setIsFetching(state, payload),
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
	addGeneratedTemplates,
	getOrDownloadGeneratedTemplate
}

export {
	generatedTemplatesActions
};
export default reducers;