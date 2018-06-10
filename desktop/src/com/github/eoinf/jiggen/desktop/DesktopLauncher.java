package com.github.eoinf.jiggen.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.eoinf.jiggen.Jiggen;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.fullscreen = false;

		Jiggen game = new Jiggen();
		game.loadDefaultPuzzle();
		new LwjglApplication(game, config);
	}
}