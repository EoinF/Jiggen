package com.github.eoinf.jiggen.views.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.eoinf.jiggen.Jiggen;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleGraphTemplate;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzlePieceTemplate;
import com.github.eoinf.jiggen.graphics.PuzzleOverlayBatch;
import com.github.eoinf.jiggen.graphics.TextureOverlayImage;
import com.github.eoinf.jiggen.graphics.WorldBoundedCamera;
import com.github.eoinf.jiggen.views.PuzzleViewModel;
import com.github.eoinf.jiggen.views.views.PuzzleToolbar;
import com.github.eoinf.jiggen.views.views.PuzzleView;
import com.github.eoinf.jiggen.views.widgets.PuzzlePieceGroup;

import java.util.function.Consumer;

import static com.github.eoinf.jiggen.utils.PixmapUtils.getMinimumScaleToFixAspectRatio;

public class PuzzleSolverScreen implements Screen {
    private Jiggen game;

    private PuzzleGraphTemplate puzzleGraph;
    private Viewport viewport;
    private Viewport viewportHUD;

    private Texture backgroundImage;

    private PuzzleViewModel puzzleViewModel;
    private PuzzleView puzzleView;
    private PuzzleToolbar toolbar;

    public PuzzleSolverScreen(Jiggen game, PuzzleOverlayBatch batch, TextureAtlas uiTextureAtlas, Skin skin) {
        this.game = game;
        WorldBoundedCamera camera = new WorldBoundedCamera();

        viewport = new ScreenViewport(camera);
        viewportHUD = new ScreenViewport();

        this.puzzleViewModel = new PuzzleViewModel();
        this.puzzleView = new PuzzleView(puzzleViewModel, camera, viewport, batch, skin);
        this.toolbar = new PuzzleToolbar(viewportHUD, uiTextureAtlas, puzzleViewModel);

        this.puzzleViewModel.getFullScreenObservable().subscribe(game.onSetFullScreen);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(toolbar.stage);
        multiplexer.addProcessor(puzzleView.stage);
        multiplexer.addProcessor(puzzleView.getGestureDetector());
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void show() {

    }

    private void update(float delta) {
        puzzleView.update();
        toolbar.update();
    }

    @Override
    public void render(float delta) {
        update(delta);

        viewport.apply();
        puzzleView.draw();

        viewportHUD.apply();
        toolbar.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        viewportHUD.update(width, height, true);

        puzzleViewModel.setViewportSize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        backgroundImage.dispose();
        toolbar.dispose();
    }

    public void setPuzzleGraph(PuzzleGraphTemplate puzzleGraphTemplate, Texture backgroundImage) {
        this.puzzleGraph = puzzleGraphTemplate;
        this.backgroundImage = backgroundImage;

        puzzleViewModel.reset();
        puzzleViewModel.setPuzzleGraphTemplate(puzzleGraphTemplate);

        Vector2 scales = getMinimumScaleToFixAspectRatio(puzzleGraphTemplate.getWidth(), puzzleGraphTemplate.getHeight(),
                backgroundImage.getWidth(), backgroundImage.getHeight());

        puzzleViewModel.setScales(scales);
        puzzleViewModel.setViewportSize(viewport.getScreenWidth(), viewport.getScreenHeight());

        puzzleViewModel.getWorldBoundsObservable().subscribe(new Consumer<GridPoint2>() {
            @Override
            public void accept(GridPoint2 worldBounds) {
                // Only accept the first element
                puzzleViewModel.getWorldBoundsObservable().unsubscribe(this);

                for (PuzzlePieceTemplate<TextureRegion> piece : puzzleGraphTemplate.getVertices().values()) {
                    addNewPiece(piece, scales, worldBounds.x, worldBounds.y);
                }
            }
        });
    }

    private void addNewPiece(PuzzlePieceTemplate<TextureRegion> piece, Vector2 scales, float worldWidth, float worldHeight) {
        TextureOverlayImage image = new TextureOverlayImage(piece.getData());
        image.setUserObject(piece);
        // Calculate the bounds after the puzzle is scaled
        int sX = (int) (piece.x() * scales.x);
        int sY = (int) (piece.y() * scales.y);
        float sW = (piece.getWidth() * scales.x);
        float sH = (piece.getHeight() * scales.y);

        float scaledPuzzleWidth = puzzleGraph.getWidth() * scales.x;
        float scaledPuzzleHeight = puzzleGraph.getHeight() * scales.y;

        // Calculate the uv coordinates for rendering the background image
        float u = sX / scaledPuzzleWidth;
        float v = 1 - ((sY + sH) / scaledPuzzleHeight);
        float u2 = (sX + sW) / scaledPuzzleWidth;
        float v2 = 1 - (sY / scaledPuzzleHeight);

        image.setOverlay(new TextureRegionDrawable(
                new TextureRegion(backgroundImage, u, v, u2, v2)));

        PuzzlePieceGroup pieceGroup = new PuzzlePieceGroup(image);
        pieceGroup.setPosition(centreX(worldWidth, scales.x) + sX, centreY(worldHeight, scales.y) + sY);
        pieceGroup.setScale(scales.x, scales.y);

        puzzleViewModel.addPiece(pieceGroup);
    }

    public void shuffle() {
        puzzleViewModel.shuffle();
    }


    public int centreX(float worldWidth, float scaleX) {
        return (int) (worldWidth - puzzleGraph.getWidth() * scaleX) / 2;
    }

    public int centreY(float worldHeight, float scaleY) {
        return (int) (worldHeight - puzzleGraph.getHeight() * scaleY) / 2;
    }
}
