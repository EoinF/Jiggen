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
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleFactory;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleGraph;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzlePiece;
import com.github.eoinf.jiggen.views.Screens.PuzzleSolverScreen;

import java.util.List;

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
		PuzzleGraph puzzle = PuzzleFactory.generateTexturePuzzleFromTemplate(new DecodedTemplate(new Texture(template)));
		screen.setPuzzleGraph(puzzle);
	}

	public void loadFromAtlas(FileHandle atlasFile, FileHandle atlasImageFolder) {
		TextureAtlas atlas = null;
		try {
			atlas = new TextureAtlas(atlasFile, atlasImageFolder);
		} catch(Exception ex) {
			Gdx.app.error("blah", ex.getMessage());
			for (StackTraceElement element: ex.getStackTrace()) {
				Gdx.app.error("error caught", element.toString());
			}
		}

		PuzzleGraph graph = new PuzzleGraph(320, 227);
		for (TextureRegion region: atlas.getRegions()) {
			PuzzlePiece piece = new PuzzlePiece<>(0, 0, region.getRegionWidth(), region.getRegionHeight(), region);
			graph.addVertex(piece);
		}
		screen.setPuzzleGraph(graph);
		screen.shuffle();
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
