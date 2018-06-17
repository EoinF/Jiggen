package com.github.eoinf.jiggen.client;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtFileHandle;
import com.badlogic.gdx.backends.gwt.preloader.AssetFilter;
import com.badlogic.gdx.backends.gwt.preloader.Preloader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.github.eoinf.jiggen.Jiggen;
import com.google.gwt.core.client.GWT;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(name = "gwtAdapter", namespace = JsPackage.GLOBAL)
public class GwtAdapter {

    private static Jiggen jiggen;
    private static GwtApplication gdxApp;

    public static void setGeneratedTemplate(GeneratedTemplate generatedTemplate) {
        String atlasLink = generatedTemplate.links.atlas;
        String templateLink = generatedTemplate.links.image;

        DynamicPreloader preloader = (DynamicPreloader) gdxApp.getPreloader();

        Array<Preloader.Asset> assets = new Array<>();
        assets.add(
                new Preloader.Asset(atlasLink, AssetFilter.AssetType.Text, Integer.MAX_VALUE, "text/plain"));

        assets.add(new Preloader.Asset(templateLink, AssetFilter.AssetType.Image, Integer.MAX_VALUE,
                        "image/" + generatedTemplate.extension));

        GWT.log("Loading assets");

        preloader.preload(assets, new Preloader.PreloaderCallback() {
            @Override
            public void error (String file) {
                gdxApp.error("Preloading dynamic assets", "Unhandled error!");
            }

            @Override
            public void update (Preloader.PreloaderState state) {
                if (state.hasEnded()) {
                    FileHandle atlasFile = new GwtFileHandle(preloader, atlasLink, Files.FileType.Internal);
                    FileHandle templateFile = new GwtFileHandle(preloader, templateLink, Files.FileType.Internal);
                    FileHandle fakeDirectory = new FileHandle() {
                        @Override
                        public FileHandle child(String name) {
                            return templateFile;
                        }
                    };

                    gdxApp.postRunnable(() -> jiggen.loadFromAtlas(atlasFile, fakeDirectory));
                }
            }
        });
    }

    static void setJiggen(Jiggen _jiggen) {
        jiggen = _jiggen;
    }

    static void setApp(GwtApplication _gdxApp) {
        gdxApp = _gdxApp;
    }
}

@JsType(isNative = true)
class RawTemplate {
    String id;
    HateosLinks links;
}


@JsType(isNative = true)
class GeneratedTemplate {
    String id;
    String extension;
    HateosLinks links;
}

@JsType(isNative = true)
class HateosLinks {
    String self;
    String image;
    String atlas;
}

@JsType(isNative = true, namespace = JsPackage.GLOBAL)
class JSON {
    public static native String stringify(Object obj);
}