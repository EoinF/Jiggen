import axios from 'axios';
import { getOrFetchResourceLinks } from '../actions/resourceLinks';
import { Resource, BaseState, JiggenThunkAction } from '../models';
import base from './base';
import { createActions, handleActions, Action } from 'redux-actions';

export class Template extends Resource {}

export interface TemplatesState extends BaseState<Template> {}

const initialState: TemplatesState = {
    ...base.initialState as TemplatesState
};

const {
    startFetchingTemplates,
    setTemplates,
    selectTemplate
} = createActions({
    START_FETCHING_TEMPLATES: () => ({isFetching: true}),
    SET_TEMPLATES: (templates: Template[]) => ({resourceList: templates}),
    SELECT_TEMPLATE: (templateLink: string) => ({selectedId: templateLink})
})

function fetchTemplates (): JiggenThunkAction {
	return async (dispatch, getState) => {
		const resourceLinks = await getOrFetchResourceLinks(dispatch, getState);
		dispatch(startFetchingTemplates());
		const result = await axios.get(resourceLinks.templates);
		dispatch(setTemplates(result.data));
	};
}

const reducers = handleActions<TemplatesState>({
        START_FETCHING_TEMPLATES: (state, {payload}: Action<any>) => base.setIsFetching(state, payload) as TemplatesState,
        SET_TEMPLATES: (state, {payload}: Action<any>) => base.setResources(state, payload) as TemplatesState,
        SELECT_TEMPLATE: (state, {payload}: Action<any>) => base.selectResource(state, payload) as TemplatesState,
    },
    initialState
);

const templatesActions = {
	fetchTemplates,
	setTemplates,
	selectTemplate
}
export {
	templatesActions,
};

export default reducers;