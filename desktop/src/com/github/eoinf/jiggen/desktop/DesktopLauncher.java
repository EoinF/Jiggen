package com.github.eoinf.jiggen.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.github.eoinf.jiggen.PuzzleExtractor.Decoder.DecodedTemplate;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleFactory;
import com.github.eoinf.jiggen.webapp.Jiggen;
import com.github.eoinf.jiggen.webapp.screens.models.PuzzleGraphTemplate;

import static com.github.eoinf.jiggen.utils.PixmapUtils.getRandomBackground;
import static com.github.eoinf.jiggen.utils.PixmapUtils.getRandomTemplate;

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
		Gdx.app.postRunnable(() -> loadDefaultPuzzle(game));
	}


	private static void loadDefaultPuzzle(Jiggen game) {
		loadFromTemplate(game, getRandomTemplate());
	}

	private static void loadFromTemplate(Jiggen game, FileHandle template) {
		Texture tex = new Texture(template);
		DecodedTemplate t = new DecodedTemplate(tex);

		PuzzleGraphTemplate puzzle = PuzzleFactory.generateTexturePuzzleFromTemplate(t);
		game.setTemplate(puzzle);
		game.setBackground(getRandomBackground());
	}
}