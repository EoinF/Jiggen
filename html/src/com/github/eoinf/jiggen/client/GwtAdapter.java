package com.github.eoinf.jiggen.client;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtFileHandle;
import com.badlogic.gdx.backends.gwt.preloader.AssetFilter;
import com.badlogic.gdx.backends.gwt.preloader.Preloader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.github.eoinf.jiggen.Jiggen;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.IntRectangle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.CanvasElement;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

import java.util.HashMap;
import java.util.Map;

@JsType(name = "gwtAdapter", namespace = JsPackage.GLOBAL)
public class GwtAdapter {

    private static Jiggen jiggen;
    private static GwtApplication gdxApp;

    public static void startPuzzle(GeneratedTemplate generatedTemplate, Background background) {
        String atlasLink = generatedTemplate.links.atlas;
        String templateLink = generatedTemplate.links.image;
        String backgroundLink = background.links.image;

        DynamicPreloader preloader = (DynamicPreloader) gdxApp.getPreloader();
        Map<Integer, IntRectangle> verticesMap = generatedTemplate.vertices.toMap();

        Array<Preloader.Asset> assets = new Array<>();
        assets.add(
                new Preloader.Asset(atlasLink, AssetFilter.AssetType.Text, Integer.MAX_VALUE, "text/plain"));

        assets.add(new Preloader.Asset(templateLink, AssetFilter.AssetType.Image, Integer.MAX_VALUE,
                        "image/" + generatedTemplate.extension));

        assets.add(new Preloader.Asset(backgroundLink, AssetFilter.AssetType.Image, Integer.MAX_VALUE,
                "application/unknown"));

        GWT.log("Loading assets");

        preloader.preload(assets, new Preloader.PreloaderCallback() {
            @Override
            public void error (String file) {
                gdxApp.error("Preloading dynamic assets", "Unhandled error!");
            }

            @Override
            public void update (Preloader.PreloaderState state) {
                if (state.hasEnded()) {
                    FileHandle backgroundFile = new GwtFileHandle(preloader, backgroundLink, Files.FileType.Internal);
                    FileHandle atlasFile = new GwtFileHandle(preloader, atlasLink, Files.FileType.Internal);
                    FileHandle templateFile = new GwtFileHandle(preloader, templateLink, Files.FileType.Internal);
                    FileHandle fakeDirectory = new FileHandle() {
                        @Override
                        public FileHandle child(String name) {
                            return templateFile;
                        }
                    };

                    gdxApp.postRunnable(() -> jiggen.loadFromAtlas(atlasFile, fakeDirectory, verticesMap, backgroundFile));
                }
            }
        });
    }

    public static void startDemo() {
        gdxApp.postRunnable(() -> jiggen.loadDefaultPuzzle());
    }

    static void setJiggen(Jiggen _jiggen) {
        jiggen = _jiggen;
    }

    static void setApp(GwtApplication _gdxApp) {
        gdxApp = _gdxApp;
    }

    public static void resize(int width, int height) {
        CanvasElement canvas = gdxApp.getCanvasElement();
        gdxApp.getRootPanel().setWidth("" + width + "px");
        gdxApp.getRootPanel().setHeight("" + height + "px");
        canvas.setWidth(width);
        canvas.setHeight(height);
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
    GwtVerticesMap vertices;
}

@JsType(isNative = true)
class Background {
    String id;
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

class GwtVerticesMap extends JavaScriptObject {
    protected GwtVerticesMap() { }

    public final native GwtRectangle get(Object key) /*-{
        return this[key];
    }-*/;

    public final native String[] keySet() /*-{
        return Object.keys(this);
    }-*/;

    public final Map<Integer, IntRectangle> toMap() {
        String[] keys = this.keySet();

        Map<Integer, IntRectangle> map = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            Integer key = Integer.valueOf(keys[i]);
            GwtRectangle value = this.get(i);
            map.put(key, new IntRectangle(value.x, value.y, value.width, value.height));
        }
        return map;
    }
}

@JsType(isNative = true)
class GwtRectangle {
    int x;
    int y;
    int width;
    int height;
}