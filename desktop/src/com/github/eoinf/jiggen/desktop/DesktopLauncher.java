package com.github.eoinf.jiggen.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.eoinf.jiggen.Jiggen;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.fullscreen = false;

		Jiggen game = new Jiggen(isFullScreen -> {
			if (isFullScreen) {
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			} else {
				Gdx.graphics.setWindowedMode(config.width, config.height);
			}
		});
		new LwjglApplication(game, config);
		Gdx.app.postRunnable(game::loadDefaultPuzzle);
	}
}