package com.github.eoinf.jiggen.client;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.files.FileHandle;
import com.github.eoinf.jiggen.Jiggen;
import com.google.gwt.core.client.GWT;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(name = "gwtAdapter", namespace = JsPackage.GLOBAL)
public class GwtAdapter {

    private static Jiggen jiggen;
    private static Application gdxApp;

    public static void setGeneratedTemplate(GeneratedTemplate generatedTemplate) {
        GWT.log(JSON.stringify(generatedTemplate));

        new PuzzleLoader(generatedTemplate.links.image, generatedTemplate.links.atlas,
                (templateFile, atlasFile) -> {

                    FileHandle fakeDirectory = new FileHandle() {
                        @Override
                        public FileHandle child(String name) {
                            return templateFile;
                        }
                    };

                    GWT.log(String.valueOf(templateFile.readBytes() == null));
                    GWT.log(String.valueOf(atlasFile.readBytes() == null));
                    gdxApp.postRunnable(() -> jiggen.loadFromAtlas(atlasFile, fakeDirectory));
                });
    }

    static void setJiggen(Jiggen _jiggen) {
        jiggen = _jiggen;
    }

    static void setApp(Application _gdxApp) {
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