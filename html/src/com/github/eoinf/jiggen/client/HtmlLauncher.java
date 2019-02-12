package com.github.eoinf.jiggen.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.backends.gwt.preloader.Preloader;
import com.github.eoinf.jiggen.webapp.Jiggen;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

import java.util.function.Consumer;

public class HtmlLauncher extends GwtApplication {

    @Override
    public Preloader createPreloader() {
        return new DynamicPreloader(getPreloaderBaseURL());
    }

    // PADDING is to avoid scrolling in iframes, set to 20 if you have problems
    private static final int PADDING = 0;
    private GwtApplicationConfiguration cfg;

    @Override
    public GwtApplicationConfiguration getConfig() {
        int w = Window.getClientWidth() - PADDING;
        int h = Window.getClientHeight() - PADDING;
        cfg = new GwtApplicationConfiguration(w, h);
        Window.enableScrolling(false);
        Window.setMargin("0");
        cfg.preferFlash = false;
        return cfg;
    }

    public void resizeGameContainer(int width, int height) {
        GWT.log(width + "," + height + ": " + Gdx.graphics.isFullscreen());

        getCanvasElement().setWidth(width);
        getCanvasElement().setHeight(height);
        getRootPanel().setWidth("" + width + "px");
        getRootPanel().setHeight("" + height + "px");
        getApplicationListener().resize(width, height);
    }

    @Override
    public ApplicationListener createApplicationListener() {
        GWT.log("Initializing GWT Adapter");
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
                GWT.log("Setting full screen" + isFullScreen);
                GwtAdapter.setFullScreen(isFullScreen);
            }
        });
        GwtAdapter.setJiggen(jiggen);
        GwtAdapter.setApp(this);
        return jiggen;
    }
}