package com.github.eoinf.jiggen.client.callbacks;

import jsinterop.annotations.JsFunction;

@JsFunction
public interface DownloadFailureCallback {
    void call(String error);
}
