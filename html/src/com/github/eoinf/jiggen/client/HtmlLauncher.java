package com.github.eoinf.jiggen.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.backends.gwt.preloader.Preloader;
import com.github.eoinf.jiggen.webapp.Jiggen;
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
        Window.enableScrolling(false);
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
//        GWT.log("Initializing GWT Adapter");
        this.setLoadingListener(new LoadingListener() {
            @Override
            public void beforeSetup() {
            }

            @Override
            public void afterSetup() {
                BrowserWindow.setGwtLoaded();
            }
        });

        Jiggen jiggen = new Jiggen(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean isFullScreen) {
//                GWT.log("Setting full screen" + isFullScreen);
                GwtAdapter.setFullScreen(isFullScreen);
            }
        });
        GwtAdapter.setJiggen(jiggen);
        GwtAdapter.setApp(this);
        return jiggen;
    }
}