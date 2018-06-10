package com.github.eoinf.jiggen.client;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, name="window", namespace = JsPackage.GLOBAL)
public class BrowserWindow {
    public static native void setGwtLoaded();
}
