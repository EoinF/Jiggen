package com.github.eoinf.jiggen.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.github.eoinf.jiggen.Jiggen;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(Jiggen.VIEWPORT_WIDTH, Jiggen.VIEWPORT_HEIGHT);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new Jiggen();
        }
}