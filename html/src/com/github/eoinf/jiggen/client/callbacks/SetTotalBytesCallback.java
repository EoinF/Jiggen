package com.github.eoinf.jiggen.client.callbacks;

import jsinterop.annotations.JsFunction;

@JsFunction
public interface SetTotalBytesCallback {
    void call(Double bytes);
}
