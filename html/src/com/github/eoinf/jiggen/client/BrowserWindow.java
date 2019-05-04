package com.github.eoinf.jiggen.client;

import com.github.eoinf.jiggen.client.callbacks.DownloadFailureCallback;
import com.github.eoinf.jiggen.client.callbacks.DownloadSuccessCallback;
import com.github.eoinf.jiggen.client.callbacks.SetDownloadedBytesCallback;
import com.github.eoinf.jiggen.client.callbacks.SetTotalBytesCallback;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, name="window", namespace = JsPackage.GLOBAL)
public class BrowserWindow {
    public static native void setGwtLoaded();
    public static native void downloadImage(String src,
                                            SetTotalBytesCallback setTotalBytes,
                                            SetDownloadedBytesCallback setDownloadedBytes,
                                            DownloadFailureCallback onFailure,
                                            DownloadSuccessCallback onSuccess);

    public static native void setPuzzleComplete();
}
