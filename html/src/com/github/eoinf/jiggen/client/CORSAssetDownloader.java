package com.github.eoinf.jiggen.client;

import com.badlogic.gdx.backends.gwt.preloader.AssetDownloader;
import com.badlogic.gdx.backends.gwt.preloader.AssetFilter;
import com.google.gwt.dom.client.ImageElement;

/**
 * A specialised asset downloader that asks the target server for CORS permission for the asset
 * Should work with, e.g. imgur, github, etc.
 */
public class CORSAssetDownloader extends AssetDownloader {

    CORSAssetDownloader() {
        super();
        // Disable browser cache to avoid loading images as binary data
        // This was preventing cross origin images from loading correctly
        setUseBrowserCache(false);
    }

    @Override
    public void load(String url, AssetFilter.AssetType type, String mimeType, AssetLoaderListener<?> listener) {
        switch (type) {
            case Image:
                loadImage(url, mimeType, "", (AssetLoaderListener<ImageElement>) listener);
                break;
            default:
                super.load(url, type, mimeType, listener);
        }
    }
}
