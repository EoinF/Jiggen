package com.github.eoinf.jiggen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.eoinf.jiggen.PuzzleExtractor.Decoder.DecodedTemplate;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.IntRectangle;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleFactory;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleGraphTemplate;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzlePieceTemplate;
import com.github.eoinf.jiggen.views.Screens.PuzzleSolverScreen;

import java.util.List;
import java.util.Map;

public class Jiggen extends Game {

	public SpriteBatch batch;
	public Skin skin;
	public OrthographicCamera camera;
	public PuzzleSolverScreen screen;

	public static final int VIEWPORT_WIDTH = 1280;
	public static final int VIEWPORT_HEIGHT = 720;

	@Override
	public void create () {
		batch = new SpriteBatch();
		FileHandle f = Gdx.files.internal("skin/Holo-dark-hdpi.json");
		skin = new Skin(f);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

		screen = new PuzzleSolverScreen(camera, batch);
		setScreen(screen);
	}

	public void loadDefaultPuzzle() {
		// Set the puzzle (because puzzles are set differently in the gwt application)
		List<FileHandle> templates = utils.getTemplateFiles();

		FileHandle template;

		if (templates.isEmpty()) {
			Gdx.files.internal("");
			template = Gdx.files.internal("templates/5x7puzzletemplate.jpg");
		} else {
			template = templates.get(0);
		}
		loadFromTemplate(template);
	}

	public void loadFromTemplate(FileHandle template) {
		PuzzleGraphTemplate puzzle = PuzzleFactory.generateTexturePuzzleFromTemplate(new DecodedTemplate(new Texture(template)));
		screen.setPuzzleGraph(puzzle);
	}

	public void loadFromAtlas(FileHandle atlasFile, FileHandle atlasImageFolder, Map<Integer, IntRectangle> vertices) {
		TextureAtlas atlas = null;
		try {
			atlas = new TextureAtlas(atlasFile, atlasImageFolder);
		} catch(Exception ex) {
			Gdx.app.error("blah", ex.getMessage());
			for (StackTraceElement element: ex.getStackTrace()) {
				Gdx.app.error("error caught", element.toString());
			}
		}

		PuzzleGraphTemplate graph = new PuzzleGraphTemplate(320, 227);
		for (Integer key: vertices.keySet()) {
			TextureRegion region = atlas.findRegion(key.toString());
			PuzzlePieceTemplate piece = new PuzzlePieceTemplate<>(vertices.get(key), region);
			graph.addVertex(piece);
		}
		screen.setPuzzleGraph(graph);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
