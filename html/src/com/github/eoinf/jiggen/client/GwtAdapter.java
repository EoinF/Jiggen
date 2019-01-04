package com.github.eoinf.jiggen.client;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.gwt.GwtFileHandle;
import com.badlogic.gdx.backends.gwt.preloader.AssetFilter;
import com.badlogic.gdx.backends.gwt.preloader.Preloader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.github.eoinf.jiggen.Jiggen;
import com.github.eoinf.jiggen.PuzzleExtractor.GraphEdge;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.IntRectangle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

import java.util.HashMap;
import java.util.Map;

@JsType(name = "gwtAdapter", namespace = JsPackage.GLOBAL)
public class GwtAdapter {

    private static Jiggen jiggen;
    private static HtmlLauncher gdxApp;

    public static void resizeGameContainer(int width, int height) {
        gdxApp.postRunnable(() -> gdxApp.resizeGameContainer(width, height));
    }

    public static void shuffle() {
        GWT.log("Shuffle jiggen");
        jiggen.shuffle();
    }

    public static void setBackground(Background background) {
        String backgroundLink = background.links.image;

        DynamicPreloader preloader = (DynamicPreloader) gdxApp.getPreloader();

        Array<Preloader.Asset> assets = new Array<>();

        assets.add(new Preloader.Asset(backgroundLink, AssetFilter.AssetType.Image, Integer.MAX_VALUE,
                "application/unknown"));

        GWT.log("Loading background assets");

        preloader.preload(assets, new Preloader.PreloaderCallback() {
            @Override
            public void error (String file) {
                gdxApp.error("Preloading dynamic assets", "Unhandled error!");
            }

            @Override
            public void update (Preloader.PreloaderState state) {
                if (state.hasEnded()) {
                    FileHandle backgroundFile = new GwtFileHandle(preloader, backgroundLink, Files.FileType.Internal);

                    gdxApp.postRunnable(() -> jiggen.setBackground(backgroundFile));
                }
            }
        });
    }

    public static void setTemplate(GeneratedTemplate generatedTemplate) {
        String atlasLink = generatedTemplate.links.atlas;
        String templateLink = generatedTemplate.links.image;

        DynamicPreloader preloader = (DynamicPreloader) gdxApp.getPreloader();
        Map<Integer, IntRectangle> verticesMap = generatedTemplate.vertices.toMap();
        GraphEdge[] graphEdges = generatedTemplate.edges.toArray();

        Array<Preloader.Asset> assets = new Array<>();
        assets.add(
                new Preloader.Asset(atlasLink, AssetFilter.AssetType.Text, Integer.MAX_VALUE, "text/plain"));

        assets.add(new Preloader.Asset(templateLink, AssetFilter.AssetType.Image, Integer.MAX_VALUE,
                        "image/" + generatedTemplate.extension));

        GWT.log("Loading generated template assets");

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

                    gdxApp.postRunnable(() -> jiggen.setTemplateFromAtlas(atlasFile, fakeDirectory, verticesMap, graphEdges));
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

    static void setApp(HtmlLauncher _gdxApp) {
        gdxApp = _gdxApp;
    }

    @JsMethod
    public static native void setFullScreen(boolean isFullScreen);
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
    GwtEdgesList edges;
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


class GwtEdgesList extends JavaScriptObject {
    protected GwtEdgesList() { }

    public final native GwtGraphEdge get(int key) /*-{
        return this[key];
    }-*/;

    public final native int length() /*-{
        return this.length;
    }-*/;

    public final GraphEdge[] toArray() {
        int length = this.length();

        GraphEdge[] edges = new GraphEdge[length];
        for (int i = 0; i < length; i++) {
            GwtGraphEdge edge = this.get(i);
            edges[i] = new GraphEdge(edge.v0, edge.v1);
        }
        return edges;
    }
}

@JsType(isNative = true)
class GwtRectangle {
    int x;
    int y;
    int width;
    int height;
}

@JsType(isNative = true)
class GwtGraphEdge {
    int v0;
    int v1;
}