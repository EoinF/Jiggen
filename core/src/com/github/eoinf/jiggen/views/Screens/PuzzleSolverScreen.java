package com.github.eoinf.jiggen.views.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleGraphTemplate;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzlePieceTemplate;
import com.github.eoinf.jiggen.graphics.PuzzleOverlayBatch;
import com.github.eoinf.jiggen.graphics.TextureOverlayImage;
import com.github.eoinf.jiggen.graphics.TouchControlledCamera;
import com.github.eoinf.jiggen.views.PuzzleGestureListener;
import com.github.eoinf.jiggen.views.PuzzlePieceGroup;
import io.reactivex.functions.Consumer;

import static com.github.eoinf.jiggen.utils.getMinimumScaleToFixAspectRatio;

public class PuzzleSolverScreen implements Screen {

    private static int WORLD_PADDING = 50;
    private static float ZOOM_RATE = 0.1f;

    private TouchControlledCamera boundedCamera;

    private Stage gameStage;
    private PuzzleGraphTemplate puzzleGraph;

    private ScreenViewport viewport;

    private Texture backgroundImage;
    private Batch batch;

    private PuzzleViewModel puzzleViewModel;
    private PuzzleView puzzleView;

    public PuzzleSolverScreen(final OrthographicCamera camera, PuzzleOverlayBatch batch) {
        viewport = new ScreenViewport(camera);
        gameStage = new Stage(viewport, batch);
        this.batch = batch;

        this.boundedCamera = new TouchControlledCamera(camera);

        this.puzzleViewModel = new PuzzleViewModel();
        this.puzzleView = new PuzzleView(puzzleViewModel);

        gameStage.addActor(puzzleView);
        gameStage.addListener(new PuzzleGestureListener(gameStage, puzzleViewModel, boundedCamera));
        gameStage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.SPACE) {
                    shuffle();
                } else if (keycode == Input.Keys.T) {
//                    puzzleViewModel.centreCamera();
//                    boundedCamera.setX(worldWidth / 2);
//                    boundedCamera.setY(worldHeight / 2);
                }
                return super.keyDown(event, keycode);
            }

            @Override
            public boolean scrolled(InputEvent event, float x, float y, int scrollDirection) {
                boundedCamera.zoomBy(scrollDirection * ZOOM_RATE);
                return super.scrolled(event, x, y, scrollDirection);
            }
        });

        puzzleViewModel.getWorldBoundsObservable().subscribe(new Consumer<GridPoint2>() {
            @Override
            public void accept(GridPoint2 worldBounds) {
                float maxZoomX = worldBounds.x / camera.viewportWidth;
                float maxZoomY = worldBounds.y / camera.viewportHeight;

                float maxZoom = Math.min(maxZoomX, maxZoomY);
                boundedCamera.setCameraBounds(worldBounds.x, worldBounds.y, maxZoom);
            }
        });

        Gdx.input.setInputProcessor(gameStage);
    }

    @Override
    public void show() {

    }

    private void update(float delta) {
        boundedCamera.update();

        Vector3 mousePositionInWorld = boundedCamera.camera.unproject(
                new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        puzzleViewModel.setMousePosition(mousePositionInWorld);

        gameStage.act();
    }

    @Override
    public void render(float delta) {
        update(delta);

        batch.setProjectionMatrix(boundedCamera.camera.combined);
        gameStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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

    }

    public void setPuzzleGraph(PuzzleGraphTemplate puzzleGraphTemplate, Texture backgroundImage) {
        this.puzzleGraph = puzzleGraphTemplate;
        this.backgroundImage = backgroundImage;

        puzzleViewModel.reset();
        puzzleViewModel.setPuzzleGraphTemplate(puzzleGraphTemplate);

        Vector2 scales = getMinimumScaleToFixAspectRatio(puzzleGraphTemplate.getWidth(), puzzleGraphTemplate.getHeight(),
                backgroundImage.getWidth(), backgroundImage.getHeight());

        puzzleViewModel.setScales(scales);

        int worldWidth = (int) Math.max(puzzleGraphTemplate.getWidth() * scales.x, viewport.getScreenWidth());
        int worldHeight = (int) Math.max(puzzleGraphTemplate.getHeight() * scales.y, viewport.getScreenHeight());

        worldHeight = worldWidth = Math.max(worldWidth, worldHeight) + WORLD_PADDING;

        puzzleViewModel.setWorldBounds(new GridPoint2(worldWidth, worldHeight));

        for (PuzzlePieceTemplate<TextureRegion> piece : puzzleGraphTemplate.getVertices().values()) {
            addNewPiece(piece, scales, worldWidth, worldHeight);
        }
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
        pieceGroup.setPosition(centreX(worldWidth , scales.x) + sX, centreY(worldHeight, scales.y) + sY);
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
