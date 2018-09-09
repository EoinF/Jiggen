package com.github.eoinf.jiggen.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.backends.gwt.preloader.Preloader;
import com.github.eoinf.jiggen.Jiggen;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;

import java.util.function.Consumer;

public class HtmlLauncher extends GwtApplication {

    @Override
    public String getPreloaderBaseURL() {
        return getHostPageBaseURL() + "assets/";
    }

    private static native String getHostPageBaseURL() /*-{
        var pathnameParts = $doc.location.pathname.split('/');
        return $doc.location.origin + '/' + pathnameParts[1] + '/';
    }-*/;

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
        Window.addResizeHandler(new ResizeListener());
        cfg.preferFlash = false;
        return cfg;
    }

    class ResizeListener implements ResizeHandler {
        @Override
        public void onResize(ResizeEvent event) {
            int width = event.getWidth() - PADDING;
            int height = event.getHeight() - PADDING;

            if (Gdx.graphics.isFullscreen()) {
                resizeGameContainer(width, height);
            }
        }
    }

    private void resizeGameContainer(int width, int height) {
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
                GwtAdapter.setFullScreen(isFullScreen);
                resizeGameContainer(Window.getClientWidth(), Window.getClientHeight());
            }
        });
        GwtAdapter.setJiggen(jiggen);
        GwtAdapter.setApp(this);
        return jiggen;
    }

    @Override
    public void addLifecycleListener(LifecycleListener listener) {
        super.addLifecycleListener(listener);
    }

}