package com.github.eoinf.jiggen.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.github.eoinf.jiggen.Jiggen;
import com.google.gwt.core.client.GWT;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig() {
                return new GwtApplicationConfiguration(800, 600);
        }

        @Override
        public String getPreloaderBaseURL()
        {
                String url = getHostPageBaseURL() + "assets/";
                GWT.log(url);
                return url;
        }

        @Override
        public ApplicationListener createApplicationListener() {
                return new Jiggen();
        }

        @JsMethod(namespace = JsPackage.GLOBAL)
        public static native void renderReact(String containerId) /*-{
                window.renderReact(containerId)
}-*/;

        public static native String getHostPageBaseURL() /*-{
            var s = $doc.location.origin;
            return s + "/" + $moduleName + "/";
          }-*/;
}