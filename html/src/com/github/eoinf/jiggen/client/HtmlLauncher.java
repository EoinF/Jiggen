package com.github.eoinf.jiggen.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.backends.gwt.preloader.Preloader;
import com.github.eoinf.jiggen.Jiggen;
import com.google.gwt.core.client.GWT;

public class HtmlLauncher extends GwtApplication {

    @Override
    public GwtApplicationConfiguration getConfig() {
        return new GwtApplicationConfiguration(800, 600);
    }

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

        Jiggen jiggen = new Jiggen();
        GwtAdapter.setJiggen(jiggen);
        GwtAdapter.setApp(this);
        return jiggen;
    }

    @Override
    public void addLifecycleListener(LifecycleListener listener) {
        super.addLifecycleListener(listener);
    }

}