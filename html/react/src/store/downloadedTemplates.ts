import { handleActions, createActions, Action } from 'redux-actions';

import { Resource, BaseState, JiggenThunkAction, StringMap } from '../models';
import base from './base';
import { Dispatch } from 'redux';
import store from '.';
import axios from 'axios';
import { downloadImageAsPromise } from '../utils/cachedImageDownload';


export interface GeneratedTemplate {
    links: {
        self: string;
        images: string[];
        atlas: string;
    }
	edges: any[];
	vertices: StringMap<any>;
	width: number;
	height: number;
	templateFile: any;
	extension: string;
}

interface LinkPair {
    src: string,
    cachedSrc: string
}

export class DownloadedTemplate extends Resource {
    atlasDataUrl?: string;
    atlasImageDataUrls?: LinkPair[];
    generatedTemplate?: GeneratedTemplate;

    isDownloading: Boolean;
    error?: string;

    constructor(template: Resource) {
        super(template.links, template.name);
        this.isDownloading = true;
    }
}

export interface DownloadedTemplatesState extends BaseState<DownloadedTemplate> {};

const initialState: DownloadedTemplatesState = {
	...base.initialState as DownloadedTemplatesState
};

const {
    templateSetDownloadedTemplate,
    templateSetAtlasDataLink,
    templateSetAtlasImageLinks,
    templateSetGeneratedTemplate,
    templateDownloadSuccess,
    templateDownloadFailure
} = createActions({
	TEMPLATE_SET_DOWNLOADED_TEMPLATE: (template: DownloadedTemplate) => ({ resource: template }),
	TEMPLATE_SET_ATLAS_DATA_LINK: (id: string, atlasDataUrl: string) => ({ resourceId: id, atlasDataUrl}),
    TEMPLATE_SET_ATLAS_IMAGE_LINKS: (id: string, atlasImageDataUrls: LinkPair[]) => ({ resourceId: id, atlasImageDataUrls }),
    TEMPLATE_SET_GENERATED_TEMPLATE: (id: string, generatedTemplate: GeneratedTemplate) => ({ resourceId: id, generatedTemplate }),
	TEMPLATE_DOWNLOAD_SUCCESS: (id: string) => ({ resourceId: id }),
    TEMPLATE_DOWNLOAD_FAILURE: (id: string, error: string) => ({resourceId: id, error})
});

function downloadTemplate (resource: Resource): JiggenThunkAction {
	return async (dispatch, getState) => {
        dispatch(templateSetDownloadedTemplate(new DownloadedTemplate(resource)));
        const resourceId = resource.links.self;

        try {
            const {data: generatedTemplate} = await axios.get<GeneratedTemplate>(resource.links.generatedTemplate);
            dispatch(templateSetGeneratedTemplate(resourceId, generatedTemplate));

            const atlasPromise = downloadImageAsPromise(generatedTemplate.links.atlas)
                .then(src => {
                        dispatch(templateSetAtlasDataLink(resourceId, src))
                    }
                );

            const imagePromisePairs = generatedTemplate.links.images
                .map(src => (
                    {
                        src,
                        promise: downloadImageAsPromise(src)
                    })
                );

            await Promise.all<any>([atlasPromise, ...imagePromisePairs.map(pair => pair.promise)]);

            const imageLinkPairs: LinkPair[] = await Promise.all(imagePromisePairs.map(async ({src, promise}) => {
                    return {
                        src,
                        cachedSrc: await promise
                    };
                }
            ));

            dispatch(templateSetAtlasImageLinks(resourceId, imageLinkPairs));
            dispatch(templateDownloadSuccess(resourceId));
        } catch (error) {
            console.error(error);
            dispatch(templateDownloadFailure(resourceId, error));
        }
  };
}

const reducers = handleActions({
        TEMPLATE_SET_DOWNLOADED_TEMPLATE: (state, {payload}: Action<any>) => base.setOrUpdateResource<DownloadedTemplate>(state, payload),
        TEMPLATE_SET_ATLAS_DATA_LINK: (state: DownloadedTemplatesState, {payload}: Action<any>) => {
            const resource = { ...state.linkMap[payload.resourceId] };
            resource.atlasDataUrl = payload.atlasDataUrl;
            return base.setOrUpdateResource(state, {resource});
        },
        TEMPLATE_SET_ATLAS_IMAGE_LINKS:  (state: DownloadedTemplatesState, {payload}: Action<any>) => {
            const resource = { ...state.linkMap[payload.resourceId] };
            resource.atlasImageDataUrls = payload.atlasImageDataUrls;
            return base.setOrUpdateResource(state, {resource});
        },
        TEMPLATE_SET_GENERATED_TEMPLATE:  (state: DownloadedTemplatesState, {payload}: Action<any>) => {
            const resource = { ...state.linkMap[payload.resourceId] };
            resource.generatedTemplate = payload.generatedTemplate;
            return base.setOrUpdateResource(state, {resource});
        },
        TEMPLATE_DOWNLOAD_SUCCESS:  (state: DownloadedTemplatesState, {payload}: Action<any>) => {
            const resource = { ...state.linkMap[payload.resourceId] };
            resource.isDownloading = false;
            return base.setOrUpdateResource(state, {resource});
        },
        TEMPLATE_DOWNLOAD_FAILURE:  (state: DownloadedTemplatesState, {payload}: Action<any>) => {
            const resource = { ...state.linkMap[payload.resourceId] };
            resource.isDownloading = false;
            resource.error = payload.error;
            return base.setOrUpdateResource(state, {resource});
        },
	}, initialState
);

const getOrDownloadTemplate = (resource: Resource, dispatch: Dispatch, getState: any): Promise<DownloadedTemplate> => {
    return new Promise(resolve => {
        const downloadedTemplate = getState().downloadedTemplates.linkMap[resource.links.self];
        
        if (downloadedTemplate != null && !downloadedTemplate.isDownloading) {
            resolve(downloadedTemplate);
        } else {
            const unsubscribe = store.subscribe(() => {
                const _downloadedTemplate = getState().downloadedTemplates.linkMap[resource.links.self];
                
                if (_downloadedTemplate != null && !_downloadedTemplate.isDownloading) {
                    resolve(_downloadedTemplate);
                    unsubscribe();
                }
            });
            const downloadImageThunk = downloadTemplate(resource);
            downloadImageThunk(dispatch, getState, null);
        }
    });
}

const downloadedTemplatesActions = {
    downloadTemplate,
    getOrDownloadTemplate
}

export {
	downloadedTemplatesActions
};
export default reducers;