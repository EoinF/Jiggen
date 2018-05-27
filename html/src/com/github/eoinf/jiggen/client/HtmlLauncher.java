package com.github.eoinf.jiggen.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.github.eoinf.jiggen.Jiggen;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig() {
                return new GwtApplicationConfiguration(800, 600);
        }

        @Override
        public String getPreloaderBaseURL()
        {
                return getHostPageBaseURL() + "assets/";
        }

        @Override
        public ApplicationListener createApplicationListener() {
                return new Jiggen();
        }

        public static native String getHostPageBaseURL() /*-{
            var s = $doc.location.origin;
            return s + "/" + $moduleName + "/";
          }-*/;
}