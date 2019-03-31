package com.github.eoinf.jiggen.client;


import com.badlogic.gdx.backends.gwt.preloader.AssetDownloader;
import com.badlogic.gdx.backends.gwt.preloader.AssetFilter;
import com.badlogic.gdx.backends.gwt.preloader.Blob;
import com.badlogic.gdx.backends.gwt.preloader.Preloader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.google.gwt.dom.client.ImageElement;

class DynamicPreloader extends Preloader {

    DynamicPreloader(String preloaderUrl) {
        super(preloaderUrl);
    }

    public static class DynamicPreloaderState extends PreloaderState {
        public DynamicPreloaderState(Array<Asset> assets) {
            super(assets);
        }

        @Override
        public boolean hasEnded() {
            for (int i = 0; i < assets.size; i++) {
                if (!assets.get(i).succeed) {
                    return false;
                }
            }
            return true;
        }
    }

    public void preload (final String assetFileUrl, final PreloaderCallback callback) {
        final AssetDownloader loader = new CORSAssetDownloader();

        loader.loadText(baseUrl + assetFileUrl, new AssetDownloader.AssetLoaderListener<String>() {
            @Override
            public void onProgress (double amount) {
            }
            @Override
            public void onFailure () {
                callback.error(assetFileUrl);
            }
            @Override
            public void onSuccess (String result) {
                String[] lines = result.split("\n");
                Array<Asset> assets = new Array<Asset>(lines.length);
                for (String line : lines) {
                    String[] tokens = line.split(":");
                    if (tokens.length != 4) {
                        throw new GdxRuntimeException("Invalid assets description file.");
                    }
                    AssetFilter.AssetType type = AssetFilter.AssetType.Text;
                    if (tokens[0].equals("i")) type = AssetFilter.AssetType.Image;
                    if (tokens[0].equals("b")) type = AssetFilter.AssetType.Binary;
                    if (tokens[0].equals("a")) type = AssetFilter.AssetType.Audio;
                    if (tokens[0].equals("d")) type = AssetFilter.AssetType.Directory;
                    long size = Long.parseLong(tokens[2]);
                    if (type == AssetFilter.AssetType.Audio && !loader.isUseBrowserCache()) {
                        size = 0;
                    }
                    assets.add(new Asset(tokens[1].trim(), type, size, tokens[3]));
                }
                final PreloaderState state = new PreloaderState(assets);
                for (int i = 0; i < assets.size; i++) {
                    final Asset asset = assets.get(i);

                    if (contains(asset.url)) {
                        asset.loaded = asset.size;
                        asset.succeed = true;
                        continue;
                    }

                    loader.load(baseUrl + asset.url, asset.type, asset.mimeType, new AssetDownloader.AssetLoaderListener<Object>() {
                        @Override
                        public void onProgress (double amount) {
                            asset.loaded = (long) amount;
                            callback.update(state);
                        }
                        @Override
                        public void onFailure () {
                            asset.failed = true;
                            callback.error(asset.url);
                            callback.update(state);
                        }
                        @Override
                        public void onSuccess (Object result) {
                            switch (asset.type) {
                                case Text:
                                    texts.put(asset.url, (String) result);
                                    break;
                                case Image:
                                    images.put(asset.url, (ImageElement) result);
                                    break;
                                case Binary:
                                    binaries.put(asset.url, (Blob) result);
                                    break;
                                case Audio:
                                    audio.put(asset.url, null);
                                    break;
                                case Directory:
                                    directories.put(asset.url, null);
                                    break;
                            }
                            asset.succeed = true;
                            callback.update(state);
                        }
                    });
                }
                callback.update(state);
            }
        });
    }


    void preload(Array<Asset> assets, final PreloaderCallback callback) {
        final CORSAssetDownloader loader = new CORSAssetDownloader();

        final DynamicPreloaderState state = new DynamicPreloaderState(assets);
        for (int i = 0; i < assets.size; i++) {
            final Asset asset = assets.get(i);

            if (contains(asset.url)) {
                asset.loaded = asset.size;
                asset.succeed = true;
                continue;
            }

            loader.load(asset.url, asset.type, asset.mimeType,
                    new AssetDownloader.AssetLoaderListener<Object>() {
                        @Override
                        public void onProgress(double amount) {
                            asset.loaded = (long) amount;
                            callback.update(state);
                        }

                        @Override
                        public void onFailure() {
                            asset.failed = true;
                            callback.error(asset.url);
                            callback.update(state);
                        }

                        @Override
                        public void onSuccess(Object result) {
                            switch (asset.type) {
                                case Text:
                                    texts.put(asset.url, (String) result);
                                    break;
                                case Image:
                                    images.put(asset.url, (ImageElement) result);
                                    break;
                                case Binary:
                                    binaries.put(asset.url, (Blob) result);
                                    break;
                                case Audio:
                                    audio.put(asset.url, null);
                                    break;
                                case Directory:
                                    directories.put(asset.url, null);
                                    break;
                            }
                            asset.succeed = true;
                            callback.update(state);
                        }
                    });
        }
        callback.update(state);
    }
}
