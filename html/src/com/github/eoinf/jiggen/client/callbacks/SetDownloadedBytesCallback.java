package com.github.eoinf.jiggen.client.callbacks;

import jsinterop.annotations.JsFunction;

@JsFunction
public interface SetDownloadedBytesCallback {
    void call(Double bytes);
}
