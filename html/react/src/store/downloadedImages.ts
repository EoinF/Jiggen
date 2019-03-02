import { handleActions, createActions, Action } from 'redux-actions';

import { Resource, BaseState, JiggenThunkAction } from '../models';
import base from './base';

export class DownloadedImage extends Resource {
    bytesDownloaded: number;
    bytesTotal: number;
    isDownloading: Boolean
    error?: string;

    constructor(resource: Resource) {
        super(resource.id, {'image-compressed': resource.links['image-compressed']}, resource.name);
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
	return async (dispatch, getState) => {
        dispatch(setDownloadedImage(new DownloadedImage(resource)));
        const resourceId = resource.id;

        const xhr = new XMLHttpRequest();
        let notifiedNotComputable = false;
      
        xhr.open('GET', resource.links.image, true);
        xhr.responseType = 'arraybuffer';
      
        xhr.onprogress = function (ev) {
          if (ev.lengthComputable) {
            dispatch(setTotalBytes(resourceId, ev.total));
          } else {
            if (!notifiedNotComputable) {
              notifiedNotComputable = true;
              dispatch(setTotalBytes(resourceId, -1));
            }
          }
          dispatch(setDownloadedBytes(resourceId, ev.loaded));
        }
      
        xhr.onloadend = function () {
            console.log('onloadend');
          if (!xhr.status.toString().match(/^2/)) {
            dispatch(downloadFailure(resourceId, xhr));
          } else {
            const options: any = {};
            var headers = xhr.getAllResponseHeaders();
            var m = headers.match(/^Content-Type\:\s*(.*?)$/mi);
      
            if (m && m[1]) {
              options.type = m[1];
            }
      
            var blob = new Blob([this.response], options);
      
            dispatch(downloadSuccess(resourceId, window.URL.createObjectURL(blob)));
          }
        }
      
        xhr.send();
	};
}

const reducers = handleActions({
        SET_DOWNLOADED_IMAGE: (state, {payload}: Action<any>) => base.setOrUpdateResource<DownloadedImage>(state, payload),
        SET_DOWNLOADED_BYTES: (state: DownloadedImagesState, {payload}: Action<any>) => {
            const resource = { ...state.resourceMap[payload.resourceId] };
            resource.bytesDownloaded = payload.bytes;
            return base.setOrUpdateResource(state, {resource});
        },
        SET_TOTAL_BYTES:  (state: DownloadedImagesState, {payload}: Action<any>) => {
            const resource = { ...state.resourceMap[payload.resourceId] };
            resource.bytesTotal = payload.bytes;
            return base.setOrUpdateResource(state, {resource});
        },
        DOWNLOAD_SUCCESS:  (state: DownloadedImagesState, {payload}: Action<any>) => {
            const resource = { ...state.resourceMap[payload.resourceId] };
            resource.isDownloading = false;
            resource.links.image = payload.url;
            return base.setOrUpdateResource(state, {resource});
        },
        DOWNLOAD_FAILURE:  (state: DownloadedImagesState, {payload}: Action<any>) => {
            const resource = { ...state.resourceMap[payload.resourceId] };
            resource.error = payload.error;
            return base.setOrUpdateResource(state, {resource});
        },
	}, initialState
);

const downloadedImagesActions = {
    downloadImage
}

export {
	downloadedImagesActions
};
export default reducers;