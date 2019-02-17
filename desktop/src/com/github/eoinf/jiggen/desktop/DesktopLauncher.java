package com.github.eoinf.jiggen.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Json;
import com.github.eoinf.jiggen.PuzzleExtractor.Decoder.DecodedTemplate;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleFactory;
import com.github.eoinf.jiggen.webapp.Jiggen;
import com.github.eoinf.jiggen.webapp.screens.models.IntRectangle;
import com.github.eoinf.jiggen.webapp.screens.models.PuzzleGraphTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.github.eoinf.jiggen.utils.PixmapUtils.getRandomBackground;
import static com.github.eoinf.jiggen.utils.PixmapUtils.getRandomTemplate;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.fullscreen = false;
        config.width = 900;
        config.height = 600;

		Jiggen game = new Jiggen(isFullScreen -> {
			if (isFullScreen) {
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			} else {
				Gdx.graphics.setWindowedMode(config.width, config.height);
			}
		});
		new LwjglApplication(game, config);
		Gdx.app.postRunnable(() -> {
			loadTemplateFromAtlas(game);
			game.setBackground(getRandomBackground());
		});
	}


	private static void loadDefaultPuzzle(Jiggen game) {
		loadFromTemplate(game, getRandomTemplate());
	}

	private static void loadFromTemplate(Jiggen game, FileHandle template) {
		Texture tex = new Texture(template);
		DecodedTemplate t = new DecodedTemplate(tex);

		PuzzleGraphTemplate puzzle = PuzzleFactory.generateTexturePuzzleFromTemplate(t);
		game.setTemplate(puzzle);
	}

	private static void loadTemplateFromAtlas(Jiggen game) {
    	FileHandle atlasFile = Gdx.files.internal("generated-templates/pieces.atlas");
    	FileHandle atlasFolder = Gdx.files.internal("generated-templates");
		FileHandle verticesJson = Gdx.files.internal("generated-templates/vertices.json");
		FileHandle edgesJson = Gdx.files.internal("generated-templates/edges.json");

		Json json = new Json();
		VerticesMap verticesObject = json.fromJson(VerticesMap.class, verticesJson);
		EdgesList edgesObject = json.fromJson(EdgesList.class, edgesJson);

		// Json library provided by GDX is always mapping the keys to integers so we need to
		// convert from string to integer manually
		Map<Integer, IntRectangle> intVerticesMap = new HashMap<>();
		verticesObject.vertices.keySet().forEach(key -> intVerticesMap.put(Integer.parseInt(key), verticesObject.vertices.get(key)));

    	game.setTemplateFromAtlas(atlasFile, atlasFolder, intVerticesMap, edgesObject.edges);
	}
}