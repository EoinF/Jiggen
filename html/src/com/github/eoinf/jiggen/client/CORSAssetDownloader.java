package com.github.eoinf.jiggen.client;

import com.badlogic.gdx.backends.gwt.preloader.AssetDownloader;
import com.github.eoinf.jiggen.client.callbacks.DownloadFailureCallback;
import com.github.eoinf.jiggen.client.callbacks.DownloadSuccessCallback;
import com.github.eoinf.jiggen.client.callbacks.SetDownloadedBytesCallback;
import com.github.eoinf.jiggen.client.callbacks.SetTotalBytesCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.NativeEvent;

/**
 * A specialised asset downloader that asks the target server for CORS permission for the asset
 * Should work with, e.g. imgur, github, etc.
 */
public class CORSAssetDownloader extends AssetDownloader {

    @Override
    public void loadImage(String url, String mimeType, AssetLoaderListener<ImageElement> listener) {
        loadImage(url, mimeType, "", listener);
    }

    // Load image using xhr from gwt adapter
    @Override
    public void loadImage(String url, String mimeType, String crossOrigin,  AssetLoaderListener<ImageElement> listener) {
        BrowserWindow.downloadImage(url, new SetTotalBytesCallback() {
                    @Override
                    public void call(Double bytes) {
                        // Ignore the event
                    }
                },
                new SetDownloadedBytesCallback() {
                    @Override
                    public void call(Double bytes) {
                        // ignore the event
                    }
                },
                new DownloadFailureCallback() {
                    @Override
                    public void call(String error) {
                        listener.onFailure();
                    }
                },
                new DownloadSuccessCallback() {
                    @Override
                    public void call(String downloadedUrl) {
                        ImageElement image = createImage();
                        if (crossOrigin != null) {
                            image.setAttribute("crossOrigin", crossOrigin);
                        }
                        hookImgListener(image, new ImgEventListener() {
                            @Override
                            public void onEvent (NativeEvent event) {
                                if (event.getType().equals("error"))
                                    listener.onFailure();
                                else
                                    listener.onSuccess(image);
                            }
                        });
                        GWT.log("success" + downloadedUrl);
                        image.setSrc(downloadedUrl);
                    }
                }
        );
    }

    static native ImageElement createImage () /*-{
		return new Image();
	}-*/;

    private static interface ImgEventListener {
        public void onEvent (NativeEvent event);
    }

    static native void hookImgListener (ImageElement img, ImgEventListener h) /*-{
		img
				.addEventListener(
						'load',
						function(e) {
							h.@com.github.eoinf.jiggen.client.CORSAssetDownloader.ImgEventListener::onEvent(Lcom/google/gwt/dom/client/NativeEvent;)(e);
						}, false);
		img
				.addEventListener(
						'error',
						function(e) {
							h.@com.github.eoinf.jiggen.client.CORSAssetDownloader.ImgEventListener::onEvent(Lcom/google/gwt/dom/client/NativeEvent;)(e);
						}, false);
	}-*/;
}
