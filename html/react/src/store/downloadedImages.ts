import { handleActions, createActions, Action } from 'redux-actions';
import cachedImageDownload from '../utils/cachedImageDownload';

import { Resource, BaseState, JiggenThunkAction } from '../models';
import base from './base';
import { Dispatch } from 'redux';
import store from '.';

export class DownloadedImage extends Resource {
    bytesDownloaded: number;
    bytesTotal: number;
    isDownloading: Boolean
    error?: string;

    constructor(resource: Resource) {
        super({
            self: resource.links.self,
            'image-compressed': resource.links['image-compressed']
        }, resource.name);
        this.bytesDownloaded = 0;
        this.bytesTotal = 0;
        this.isDownloading = true;
    }
}

export interface DownloadedImagesState extends BaseState<DownloadedImage> {}

const initialState: DownloadedImagesState = {
	...base.initialState as DownloadedImagesState
};

const {
    setDownloadedImage,
    setDownloadedBytes,
    setTotalBytes,
    downloadSuccess,
    downloadFailure
} = createActions({
	SET_DOWNLOADED_IMAGE: (image: DownloadedImage) => ({ resource: image }),
	SET_DOWNLOADED_BYTES: (id: string, bytes: number) => ({ resourceId: id, bytes}),
    SET_TOTAL_BYTES: (id: string, bytes: number) => ({ resourceId: id, bytes }),
	DOWNLOAD_SUCCESS: (id: string, url: string) => ({ resourceId: id, url }),
    DOWNLOAD_FAILURE: (id: string, error: string) => ({resourceId: id, error})
});

function downloadImage (resource: Resource): JiggenThunkAction {
	return (dispatch, getState) => {
    dispatch(setDownloadedImage(new DownloadedImage(resource)));
    const resourceId = resource.links.self;

    cachedImageDownload(resource.links.image,
      (bytes: number) => dispatch(setTotalBytes(resourceId, bytes)),
      (bytes: number) => dispatch(setDownloadedBytes(resourceId, bytes)),
      (error: String) => dispatch(downloadFailure(resourceId, error)),
      (url: String) => dispatch(downloadSuccess(resourceId, url))
    );
  };
}

const reducers = handleActions({
        SET_DOWNLOADED_IMAGE: (state, {payload}: Action<any>) => base.setOrUpdateResource<DownloadedImage>(state, payload),
        SET_DOWNLOADED_BYTES: (state: DownloadedImagesState, {payload}: Action<any>) => {
            const resource = { ...state.linkMap[payload.resourceId] };
            resource.bytesDownloaded = payload.bytes;
            return base.setOrUpdateResource(state, {resource});
        },
        SET_TOTAL_BYTES:  (state: DownloadedImagesState, {payload}: Action<any>) => {
            const resource = { ...state.linkMap[payload.resourceId] };
            resource.bytesTotal = payload.bytes;
            return base.setOrUpdateResource(state, {resource});
        },
        DOWNLOAD_SUCCESS:  (state: DownloadedImagesState, {payload}: Action<any>) => {
            const resource = { ...state.linkMap[payload.resourceId] };
            resource.isDownloading = false;
            resource.links.image = payload.url;
            return base.setOrUpdateResource(state, {resource});
        },
        DOWNLOAD_FAILURE:  (state: DownloadedImagesState, {payload}: Action<any>) => {
            const resource = { ...state.linkMap[payload.resourceId] };
            resource.error = payload.error;
            return base.setOrUpdateResource(state, {resource});
        },
	}, initialState
);

const getOrDownloadImage = (resource: Resource, dispatch: Dispatch, getState: Function): Promise<DownloadedImage> => {
    return new Promise(resolve => {
        const downloadedImage = getState().downloadedImages.linkMap[resource.links.self];
        
        console.log({downloadedImage});
        if (downloadedImage != null && !downloadedImage.isDownloading) {
            resolve(downloadedImage);
        } else {
            const unsubscribe = store.subscribe(() => {
                const _downloadedImage = getState().downloadedImages.linkMap[resource.links.self];
                
                console.log('downloaded image', {_downloadedImage});
                if (_downloadedImage != null && !_downloadedImage.isDownloading) {
                    resolve(_downloadedImage);
                    unsubscribe();
                }
            });
            downloadImage(resource);
        }
    });
}

const downloadedImagesActions = {
    downloadImage,
    getOrDownloadImage
}

export {
	downloadedImagesActions
};
export default reducers;