package com.github.eoinf.jiggen.client;

import com.badlogic.gdx.backends.gwt.preloader.AssetDownloader;
import com.badlogic.gdx.backends.gwt.preloader.AssetFilter;
import com.badlogic.gdx.backends.gwt.preloader.Blob;
import com.badlogic.gdx.files.FileHandle;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.ImageElement;

import java.util.function.BiConsumer;

public class PuzzleLoader {
    BiConsumer<FileHandle, FileHandle> callback;
    AssetDownloader loader;

    private FileHandle templateFile;
    private FileHandle atlasFile;

    public PuzzleLoader(String templateUrl, String atlasUrl, BiConsumer<FileHandle, FileHandle> callback) {
        this.callback = callback;

        loader = new AssetDownloader();
        loader.load(templateUrl, AssetFilter.AssetType.Binary, "image/png",
                new AssetDownloader.AssetLoaderListener<Blob>() {
                    @Override
                    public void onProgress(double amount) {
                    }

                    @Override
                    public void onFailure() {
                    }

                    @Override
                    public void onSuccess(Blob result) {
                        templateFile = new GWTFileHandle(result, templateUrl, "png");
                    }
                });
        loader.load(atlasUrl, AssetFilter.AssetType.Image, "text/plain",
                new AssetDownloader.AssetLoaderListener<ImageElement>() {
                    @Override
                    public void onProgress(double amount) {
                    }

                    @Override
                    public void onFailure() {
                    }

                    @Override
                    public void onSuccess(ImageElement result) {
                        atlasFile = new GWTFileHandle(result, atlasUrl, "atlas");
                    }
                });

        Scheduler.get().scheduleFixedPeriod(() -> {
                if (templateFile != null && atlasFile != null) {
                    callback.accept(templateFile, atlasFile);
                    return false;
                } else {
                    return true;
                }
            }, 10);
    }
}
