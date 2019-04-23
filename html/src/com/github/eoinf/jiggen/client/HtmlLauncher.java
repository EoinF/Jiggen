package com.github.eoinf.jiggen.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.backends.gwt.preloader.Preloader;
import com.github.eoinf.jiggen.webapp.Jiggen;
import com.github.eoinf.jiggen.webapp.JiggenState;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

import java.util.function.Consumer;

public class HtmlLauncher extends GwtApplication {

    @Override
    public Preloader createPreloader() {
        return new DynamicPreloader(getPreloaderBaseURL());
    }

    @Override
    public GwtApplicationConfiguration getConfig() {
        int w = 200;
        int h = 120;
        GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(w, h);
        Window.setMargin("0");
        cfg.preferFlash = false;
        return cfg;
    }

    public void resizeGameContainer(int width, int height) {
//        GWT.log(width + "," + height + ": " + Gdx.graphics.isFullscreen());

        getCanvasElement().setWidth(width);
        getCanvasElement().setHeight(height);
        getRootPanel().setWidth("" + width + "px");
        getRootPanel().setHeight("" + height + "px");
        getApplicationListener().resize(width, height);
    }

    @Override
    public ApplicationListener createApplicationListener() {
        GWT.log("Initializing GWT Adapter");

        Jiggen jiggen = new Jiggen(new Runnable() {
            @Override
            public void run() {
                GwtAdapter.toggleFullScreen();
            }
        }, new Consumer<JiggenState>() {
            @Override
            public void accept(JiggenState nextState) {
                if (nextState == JiggenState.LOADED) {
                    BrowserWindow.setGwtLoaded();
                }
            }
        });
        GwtAdapter.setJiggen(jiggen);
        GwtAdapter.setApp(this);
        return jiggen;
    }

    /**
     * Required for fetching from assets folder, as it defaults to current path
     * @return base url for fetching assets
     */
    public static native String getHostPageBaseURL() /*-{
            return $doc.location.origin + "/";
          }-*/;
}