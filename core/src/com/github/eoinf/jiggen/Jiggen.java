package com.github.eoinf.jiggen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.eoinf.jiggen.PuzzleExtractor.Decoder.DecodedTemplate;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleFactory;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleGraph;
import com.github.eoinf.jiggen.views.Screens.PuzzleSolverScreen;

public class Jiggen extends Game {

	public SpriteBatch batch;
	public Skin skin;
	public OrthographicCamera camera;

	public static final int VIEWPORT_WIDTH = 1280;
	public static final int VIEWPORT_HEIGHT = 720;

	@Override
	public void create () {
		batch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("skin/Holo-dark-hdpi.json"));

		camera = new OrthographicCamera();
		camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

		FileHandle template = utils.getTemplateFiles().get(0);

		PuzzleGraph puzzle = PuzzleFactory.generateTexturePuzzleFromTemplate(new DecodedTemplate(new Texture(template)));

		PuzzleSolverScreen screen = new PuzzleSolverScreen(camera, batch);
		setScreen(screen);
		screen.setPuzzleGraph(puzzle);
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
