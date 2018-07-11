package com.github.eoinf.jiggen.views.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
import com.github.eoinf.jiggen.views.PuzzlePieceGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.github.eoinf.jiggen.utils.getMinimumScaleToFixAspectRatio;

public class PuzzleSolverScreen implements Screen {

    private static int WORLD_PADDING = 150;
    private static float ZOOM_RATE = 0.1f;

    private TouchControlledCamera boundedCamera;

    private PuzzlePieceGroup pieceGroupHeld;
    private Vector2 pieceOffsetHeld;

    private Stage gameStage;
    private PuzzleGraphTemplate puzzleGraph;
    private List<PuzzlePieceGroup> pieceGroups;

    private ScreenViewport viewport;
    private Vector2 scales;

    private Texture backgroundImage;

    public PuzzleSolverScreen(OrthographicCamera camera, PuzzleOverlayBatch batch) {
        viewport = new ScreenViewport(camera);
        gameStage = new Stage(viewport, batch);
        pieceGroups = new ArrayList<>();
        scales = new Vector2(1, 1);

        this.boundedCamera = new TouchControlledCamera(camera,
                viewport.getScreenWidth(), viewport.getScreenHeight());

        initInputManager();
        Gdx.input.setInputProcessor(gameStage);
    }

    private void initInputManager() {
        gameStage.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Vector3 mousePositionInWorld = new Vector3(x, y, 0);
                Vector3 mousePositionInScreen = boundedCamera.camera.project(mousePositionInWorld.cpy());

                Actor a = gameStage.hit(mousePositionInWorld.x, mousePositionInWorld.y, true);

                if (a != null && !boundedCamera.isTouching()) {
                    int index = pieceGroups.indexOf(a);
                    if (index >= 0) {
                        pieceGroupHeld = pieceGroups.get(index);

                        pieceOffsetHeld = new Vector2(
                                pieceGroupHeld.getX() - mousePositionInWorld.x,
                                pieceGroupHeld.getY() - mousePositionInWorld.y
                        );
                        pieceGroupHeld.toFront();
                    }
                }
                boundedCamera.setInitialPointer(mousePositionInScreen.x, mousePositionInScreen.y, pointer);
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                boundedCamera.liftPivot(pointer);

                if (pieceGroupHeld != null) {
                    for (PuzzlePieceGroup otherPieceGroup : pieceGroups) {
                        if (pieceGroupHeld != otherPieceGroup) {
                            if (pieceGroupHeld.tryConnectTo(otherPieceGroup, scales, puzzleGraph)) {
                                pieceGroups.remove(pieceGroupHeld);
                                break;
                            }
                        }
                    }
                    pieceGroupHeld = null;
                }
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.SPACE) {
                    shuffle();
                } else if (keycode == Input.Keys.T) {
                    boundedCamera.centreCamera();
                }
                return super.keyDown(event, keycode);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (pieceGroupHeld == null) {
                    Vector3 mousePositionInWorld = new Vector3(x, y, 0);
                    Vector3 mousePositionInScreen = boundedCamera.camera.project(mousePositionInWorld);

                    boundedCamera.dragTo(mousePositionInScreen.x, mousePositionInScreen.y, pointer);

                    super.touchDragged(event, x, y, pointer);
                }
            }

            @Override
            public boolean scrolled(InputEvent event, float x, float y, int scrollDirection) {
                boundedCamera.zoomBy(scrollDirection * ZOOM_RATE);
                return super.scrolled(event, x, y, scrollDirection);
            }
        });
    }

    @Override
    public void show() {

    }

    private void update(float delta) {
        boundedCamera.update();
        if (pieceGroupHeld != null) {
            Vector3 mousePositionInWorld = boundedCamera.camera.unproject(
                    new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            pieceGroupHeld.setPosition(mousePositionInWorld.x + pieceOffsetHeld.x,
                    mousePositionInWorld.y + pieceOffsetHeld.y);
        }
        gameStage.act();
    }

    @Override
    public void render(float delta) {
        update(delta);

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

    public void setPuzzleGraph(PuzzleGraphTemplate puzzleGraph, Texture backgroundImage) {
        this.puzzleGraph = puzzleGraph;
        this.backgroundImage = backgroundImage;

        this.scales = getMinimumScaleToFixAspectRatio(puzzleGraph.getWidth(), puzzleGraph.getHeight(),
                backgroundImage.getWidth(), backgroundImage.getHeight());

        for (Actor pieceGroup : pieceGroups) {
            pieceGroup.remove();
        }
        pieceGroups.clear();

        this.boundedCamera.setWorldBounds(
                (int)Math.max(puzzleGraph.getWidth() * scales.x + WORLD_PADDING, viewport.getScreenWidth()),
                (int)Math.max(puzzleGraph.getHeight() * scales.y + WORLD_PADDING, viewport.getScreenHeight())
        );

        for (PuzzlePieceTemplate<TextureRegion> piece: puzzleGraph.getVertices().values()) {
            addNewPiece(piece);
        }
    }

    private void addNewPiece(PuzzlePieceTemplate<TextureRegion> piece) {
        TextureOverlayImage image = new TextureOverlayImage(piece.getData());
        image.setUserObject(piece);
        int sX = (int)(piece.x() * scales.x);
        int sY = (int)(piece.y() * scales.y);
        float sW = (piece.getWidth() * scales.x);
        float sH = (piece.getHeight() * scales.y);

        float scaledPuzzleWidth = puzzleGraph.getWidth() * scales.x;
        float scaledPuzzleHeight = puzzleGraph.getHeight() * scales.y;

        float u = sX / scaledPuzzleWidth;
        float v = 1 - ((sY + sH) / scaledPuzzleHeight);
        float u2 = (sX + sW) / scaledPuzzleWidth;
        float v2 = 1 - (sY / scaledPuzzleHeight);

        image.setOverlay(new TextureRegionDrawable(
                new TextureRegion(backgroundImage, u, v, u2, v2)));
        image.setScale(this.scales.x, this.scales.y);

        PuzzlePieceGroup pieceGroup = new PuzzlePieceGroup(image);
        pieceGroup.setPosition(centreX() + sX, centreY() + sY);

        pieceGroups.add(pieceGroup);
        gameStage.addActor(pieceGroup);
    }

    public void shuffle() {
        Random random = new Random();

        for (PuzzlePieceGroup group: pieceGroups) {
            float r1 = random.nextFloat();
            float r2 = random.nextFloat();

            int x = (int)(r1 * (boundedCamera.worldWidth() - puzzleGraph.getMaxPieceWidth() * scales.x));
            int y = (int)(r2 * (boundedCamera.worldHeight() - puzzleGraph.getMaxPieceHeight() * scales.y));
            group.setPosition(x, y);

            if (r1 + r2 > 1){
                group.toFront();
            }
        }
    }

    public int centreX() {
        return (int)(boundedCamera.worldWidth() - puzzleGraph.getWidth() * scales.x) / 2;
    }
    public int centreY() {
        return (int)(boundedCamera.worldHeight() - puzzleGraph.getHeight() * scales.y) / 2;
    }
}
