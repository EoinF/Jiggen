package com.github.eoinf.jiggen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.github.eoinf.jiggen.PuzzleExtractor.Decoder.DecodedTemplate;
import com.github.eoinf.jiggen.PuzzleExtractor.GraphEdge;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.IntRectangle;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleFactory;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleGraphTemplate;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzlePieceTemplate;
import com.github.eoinf.jiggen.graphics.PuzzleOverlayBatch;
import com.github.eoinf.jiggen.views.Screens.PuzzleSolverScreen;

import java.util.Map;

public class Jiggen extends Game {

	public PuzzleOverlayBatch batch;
	public Skin skin;
	public OrthographicCamera camera;
	public PuzzleSolverScreen screen;
	private ShaderProgram shader;

	public static final int VIEWPORT_WIDTH = 320;
	public static final int VIEWPORT_HEIGHT = 240;

	@Override
	public void create () {
		this.shader = this.createShader();
		batch = new PuzzleOverlayBatch(1000, this.shader);
		FileHandle f = Gdx.files.internal("skin/Holo-dark-hdpi.json");
		skin = new Skin(f);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

		screen = new PuzzleSolverScreen(camera, batch);
		setScreen(screen);
	}

	private ShaderProgram createShader() {
		FileHandle vertSrc = Gdx.files.internal("shaders/puzzle.vert");
		FileHandle fragSrc = Gdx.files.internal("shaders/puzzle.frag");
		ShaderProgram prog = new ShaderProgram(vertSrc, fragSrc);
		if (!prog.isCompiled())
			throw new GdxRuntimeException("could not compile splat batch: " + prog.getLog());
		if (prog.getLog().length() != 0)
			Gdx.app.log("PuzzleBatch", prog.getLog());

		prog.begin();
		prog.setUniformi("u_background", 1);
		prog.end();
		return prog;
	}


	public void loadDefaultPuzzle() {
		loadFromTemplate(utils.getRandomTemplate());
	}

	public void loadFromTemplate(FileHandle template) {
		Texture tex = new Texture(template);
		DecodedTemplate t = new DecodedTemplate(tex);

		PuzzleGraphTemplate puzzle = PuzzleFactory.generateTexturePuzzleFromTemplate(t);
		screen.setPuzzleGraph(puzzle, new Texture(utils.getRandomBackground()));
	}

	public void loadFromAtlas(FileHandle atlasFile, FileHandle atlasImageFolder, FileHandle backgroundFile,
							  Map<Integer, IntRectangle> vertices, GraphEdge[] graphEdges) {
		TextureAtlas atlas = null;
		try {
			atlas = new TextureAtlas(atlasFile, atlasImageFolder);
		} catch(Exception ex) {
			Gdx.app.error("blah", ex.getMessage());
			for (StackTraceElement element: ex.getStackTrace()) {
				Gdx.app.error("error caught", element.toString());
			}
		}

		GridPoint2 puzzleSize = getPuzzleSize(vertices);

		PuzzleGraphTemplate graph = new PuzzleGraphTemplate(puzzleSize.x, puzzleSize.y);
		for (Integer key: vertices.keySet()) {
			TextureRegion region = atlas.findRegion(key.toString());
			PuzzlePieceTemplate piece = new PuzzlePieceTemplate(vertices.get(key), region);
			graph.addVertex(piece);
		}
		for (GraphEdge edge: graphEdges) {
			graph.addEdge(edge.v0, edge.v1);
		}
		screen.setPuzzleGraph(graph, new Texture(backgroundFile));
		screen.shuffle();
	}

	private GridPoint2 getPuzzleSize(Map<Integer, IntRectangle> vertices) {
		int width = 0; int height = 0;
		for (IntRectangle v: vertices.values()) {
			width = Math.max(v.x + v.width, width);
			height = Math.max(v.y + v.height, height);
		}
		return new GridPoint2(width, height);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
