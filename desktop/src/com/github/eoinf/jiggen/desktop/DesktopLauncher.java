package com.github.eoinf.jiggen.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.github.eoinf.jiggen.Jiggen;
import com.github.eoinf.jiggen.PuzzleExtractor.Decoder.DecodedTemplate;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleFactory;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleGraph;
import com.github.eoinf.jiggen.utils;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.fullscreen = false;

		Jiggen game = new Jiggen();
		new LwjglApplication(game, config);

		Gdx.app.postRunnable(() -> {
			// Set the puzzle (because puzzles are set differently in gwt application)
			FileHandle template = utils.getTemplateFiles().get(0);

			PuzzleGraph puzzle = PuzzleFactory.generateTexturePuzzleFromTemplate(new DecodedTemplate(new Texture(template)));
			game.screen.setPuzzleGraph(puzzle);
		});
	}
}
