package com.github.eoinf.jiggen.webapp;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.BufferUtils;
import com.github.eoinf.jiggen.webapp.graphics.PuzzleOverlayBatch;
import com.github.eoinf.jiggen.webapp.screens.PuzzleSolverScreen;
import com.github.eoinf.jiggen.webapp.screens.models.GraphEdge;
import com.github.eoinf.jiggen.webapp.screens.models.IntRectangle;
import com.github.eoinf.jiggen.webapp.screens.models.PuzzleGraphTemplate;
import com.github.eoinf.jiggen.webapp.screens.models.PuzzlePieceTemplate;

import java.nio.IntBuffer;
import java.util.Map;
import java.util.function.Consumer;

import static com.github.eoinf.jiggen.webapp.utils.PixmapUtils.stretchPixmap;

public class Jiggen extends Game {

    private PuzzleOverlayBatch batch;
	private Skin skin;
	private PuzzleSolverScreen puzzleSolverScreen;
    private TextureAtlas atlas = null;

    public Consumer<Boolean> onSetFullScreen;
	private Consumer<JiggenState> onStateChange;
	private int maxTextureSize;

    public Jiggen(Consumer<Boolean> onSetFullScreen, Consumer<JiggenState> onStateChange) {
    	this.onSetFullScreen = onSetFullScreen;
		this.onStateChange = onStateChange;
	}

	@Override
	public void create () {
		IntBuffer intBuffer = BufferUtils.newIntBuffer(16);
		Gdx.gl.glGetIntegerv(Gdx.gl.GL_MAX_TEXTURE_SIZE, intBuffer);
		maxTextureSize = intBuffer.get();

		batch = new PuzzleOverlayBatch();
//		SpriteBatch spriteBatch = new SpriteBatch();
		FileHandle f = Gdx.files.internal("skin/cloud-form-ui.json");
		skin = new Skin(f);

		TextureAtlas uiTextureAtlas = new TextureAtlas("ui/ui.atlas");

		puzzleSolverScreen = new PuzzleSolverScreen(this, batch, uiTextureAtlas, skin);
//		TemplateCreatorScreen templateCreatorScreen = new TemplateCreatorScreen(this, spriteBatch, uiTextureAtlas, skin);

		setScreen(puzzleSolverScreen);
		onStateChange.accept(JiggenState.LOADED);
	}

	public void setBackground(FileHandle backgroundFile) {
    	Texture imageTexture = scaleTextureToSupportedSize(new Pixmap(backgroundFile));
		puzzleSolverScreen.setBackground(imageTexture);
	}

	private Texture scaleTextureToSupportedSize(Pixmap imagePixmap) {
		int width = imagePixmap.getWidth();
		int height = imagePixmap.getHeight();

		Pixmap pixmap = imagePixmap;

		if (width > maxTextureSize || height > maxTextureSize) {
			float scale = Math.min(maxTextureSize / (float)width, maxTextureSize / (float)height);
			pixmap = stretchPixmap(
					imagePixmap,
					new GridPoint2((int)(width * scale), (int)(height * scale))
			);
			imagePixmap.dispose();
		}
		return new Texture(pixmap);
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

	public void setTemplate(PuzzleGraphTemplate puzzle) {
		puzzleSolverScreen.setTemplate(puzzle);
	}
}
