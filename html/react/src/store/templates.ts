import axios from 'axios';
import { getOrFetchResourceLinks } from '../actions/resourceLinks';
import { Resource, BaseState, JiggenThunkAction } from '../models';
import base from './base';
import { createActions, handleActions, Action } from 'redux-actions';
import { Dispatch } from 'redux';
import store from '.';

export class Template extends Resource {
    pieces: number;

    constructor(links: any, name: string) {
        super(links, name);
        this.pieces = 0;
    }
}

export interface TemplatesState extends BaseState<Template> {}

const initialState: TemplatesState = {
    ...base.initialState as TemplatesState
};

const {
    startFetchingTemplates,
    setTemplates,
    setTemplate,
    selectTemplate
} = createActions({
    START_FETCHING_TEMPLATES: () => ({isFetching: true}),
    SET_TEMPLATES: (templates: Template[]) => ({resourceList: templates}),
	SET_TEMPLATE: (template: Template) => ({ resource: template }),
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
        SET_TEMPLATE: (state, {payload}: Action<any>) => base.setOrUpdateResource(state, payload) as TemplatesState,
        SELECT_TEMPLATE: (state, {payload}: Action<any>) => base.selectResource(state, payload) as TemplatesState,
    },
    initialState
);

function fetchTemplateByLink (link: string, onDownloadCompleteResolver = () => {}): JiggenThunkAction {
	return async (dispatch, getState) => {
		dispatch(startFetchingTemplates());
		const result = await axios.get(link);
		dispatch(setTemplate(result.data));
		onDownloadCompleteResolver();
	};
}

const getOrDownloadTemplate = (link: string, dispatch: Dispatch, getState: any) => {
    return new Promise<Template>(resolve => {
        const template = getState().templates.linkMap[link];
        if (template != null) {
            resolve(template);
        } else {
			const unsubscribe = store.subscribe(() => {
                const _template = getState().templates.linkMap[link];
				if (_template != null) {
					resolve(_template);
                    unsubscribe();
                }
            });
			const fetchTemplateThunk = fetchTemplateByLink(link);
			fetchTemplateThunk(dispatch, getState, null);
        }
    });
}

const templatesActions = {
    fetchTemplates,
    fetchByLink: fetchTemplateByLink,
	setTemplates,
    selectTemplate,
    getOrDownloadTemplate
}
export {
	templatesActions,
};

export default reducers;