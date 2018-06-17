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
    public Preloader createPreloader() {
        Preloader preloader = new DynamicPreloader(getPreloaderBaseURL());
        BrowserWindow.setGwtLoaded();
        return preloader;
    }

    @Override
    public ApplicationListener createApplicationListener() {
        GWT.log("Initializing GWT Adapter");

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