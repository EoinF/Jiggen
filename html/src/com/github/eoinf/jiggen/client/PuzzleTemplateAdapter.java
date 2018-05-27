package com.github.eoinf.jiggen.client;

import com.google.gwt.core.client.GWT;
import jsinterop.annotations.JsType;

@JsType
public class PuzzleTemplateAdapter {
    public PuzzleTemplateAdapter(String blah) {
        GWT.log("hello");
    }

    public void setTemplate(String template) {
        GWT.log(template);
    }
}
