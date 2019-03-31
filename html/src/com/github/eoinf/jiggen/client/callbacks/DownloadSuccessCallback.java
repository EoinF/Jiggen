package com.github.eoinf.jiggen.client.callbacks;

import jsinterop.annotations.JsFunction;

@JsFunction
public interface DownloadSuccessCallback {
    void call(String url);
}
