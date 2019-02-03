package com.github.eoinf.jiggen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.eoinf.jiggen.PuzzleExtractor.Decoder.DecodedTemplate;
import com.github.eoinf.jiggen.PuzzleExtractor.GraphEdge;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.IntRectangle;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleFactory;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleGraphTemplate;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzlePieceTemplate;
import com.github.eoinf.jiggen.graphics.PuzzleOverlayBatch;
import com.github.eoinf.jiggen.screens.PuzzleSolverScreen;

import java.util.Map;
import java.util.function.Consumer;

import static com.github.eoinf.jiggen.utils.PixmapUtils.getRandomBackground;
import static com.github.eoinf.jiggen.utils.PixmapUtils.getRandomTemplate;

public class Jiggen extends Game {

    private PuzzleOverlayBatch batch;
	private Skin skin;
	private PuzzleSolverScreen puzzleSolverScreen;
    private TextureAtlas atlas = null;

    public Consumer<Boolean> onSetFullScreen;

    public Jiggen(Consumer<Boolean> onSetFullScreen) {
    	this.onSetFullScreen = onSetFullScreen;
	}

	@Override
	public void create () {
		batch = new PuzzleOverlayBatch();
		SpriteBatch spriteBatch = new SpriteBatch();
		FileHandle f = Gdx.files.internal("skin/cloud-form-ui.json");
		skin = new Skin(f);

		TextureAtlas uiTextureAtlas = new TextureAtlas("ui/ui.atlas");

		puzzleSolverScreen = new PuzzleSolverScreen(this, batch, uiTextureAtlas, skin);
//		TemplateCreatorScreen templateCreatorScreen = new TemplateCreatorScreen(this, spriteBatch, uiTextureAtlas, skin);

		setScreen(puzzleSolverScreen);
	}

	public void loadDefaultPuzzle() {
		loadFromTemplate(getRandomTemplate());
	}

	public void loadFromTemplate(FileHandle template) {
		Texture tex = new Texture(template);
		DecodedTemplate t = new DecodedTemplate(tex);

		PuzzleGraphTemplate puzzle = PuzzleFactory.generateTexturePuzzleFromTemplate(t);
		puzzleSolverScreen.setTemplate(puzzle);
		puzzleSolverScreen.setBackground(new Texture(getRandomBackground()));
	}

	public void setBackground(FileHandle backgroundFile) {
		puzzleSolverScreen.setBackground(new Texture(backgroundFile));
	}

	public void setTemplateFromAtlas(FileHandle atlasFile, FileHandle atlasImageFolder,
							  Map<Integer, IntRectangle> vertices, GraphEdge[] graphEdges) {
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
			PuzzlePieceTemplate piece = new PuzzlePieceTemplate<>(vertices.get(key), region);
			graph.addVertex(piece);
		}
		for (GraphEdge edge: graphEdges) {
			graph.addEdge(edge.v0, edge.v1);
		}

		puzzleSolverScreen.setTemplate(graph);
	}

	public void shuffle() {
    	puzzleSolverScreen.shuffle();
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
		skin.dispose();
		if (atlas != null) {
            atlas.dispose();
        }
	}
}
