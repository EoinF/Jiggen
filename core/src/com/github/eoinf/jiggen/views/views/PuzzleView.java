package com.github.eoinf.jiggen.views.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.eoinf.jiggen.graphics.WorldBoundedCamera;
import com.github.eoinf.jiggen.input.EnhancedGestureDetector;
import com.github.eoinf.jiggen.input.PuzzleGestureListener;
import com.github.eoinf.jiggen.views.PuzzleViewModel;
import com.github.eoinf.jiggen.views.widgets.PuzzlePieceGroup;

import java.util.function.Consumer;

public class PuzzleView {
    private static float ZOOM_RATE = 0.1f;

    private PuzzleViewModel viewModel;
    private WorldBoundedCamera boundedCamera;
    private Batch batch;
    public Stage stage;
    private GestureDetector gestureDetector;

    public PuzzleView(PuzzleViewModel puzzleViewModel, WorldBoundedCamera camera, Viewport viewport, Batch batch, Skin skin) {
        this.viewModel = puzzleViewModel;
        this.boundedCamera = camera;
        this.batch = batch;

        stage = new Stage(viewport, batch);
        this.gestureDetector = new EnhancedGestureDetector(new PuzzleGestureListener(stage, puzzleViewModel, boundedCamera, skin));

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.SPACE) {
                    puzzleViewModel.shuffle();
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

        puzzleViewModel.getPuzzlePieceAddedObservable().subscribe(new Consumer<PuzzlePieceGroup>() {
            @Override
            public void accept(PuzzlePieceGroup pieceGroup) {
                stage.addActor(pieceGroup);
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
    }

    public void update() {
        boundedCamera.update();

        Vector3 mousePositionInWorld = boundedCamera.unproject(
                new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        viewModel.setMousePosition(mousePositionInWorld);

        stage.act();
    }

    public void draw() {
        this.batch.setProjectionMatrix(boundedCamera.combined);
        stage.draw();
    }

    public GestureDetector getGestureDetector() {
        return gestureDetector;
    }
}
