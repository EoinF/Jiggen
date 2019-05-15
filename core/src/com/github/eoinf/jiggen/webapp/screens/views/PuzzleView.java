package com.github.eoinf.jiggen.webapp.screens.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.eoinf.jiggen.webapp.graphics.PuzzleOverlayBatch;
import com.github.eoinf.jiggen.webapp.graphics.WorldBoundedCamera;
import com.github.eoinf.jiggen.webapp.input.EnhancedGestureDetector;
import com.github.eoinf.jiggen.webapp.input.PuzzleGestureListener;
import com.github.eoinf.jiggen.webapp.screens.controllers.PuzzleViewController;
import com.github.eoinf.jiggen.webapp.screens.controllers.PuzzleViewModel;
import com.github.eoinf.jiggen.webapp.screens.models.ConnectedPuzzlePieces;
import com.github.eoinf.jiggen.webapp.screens.widgets.ConnectedPiecesGroup;
import com.github.eoinf.jiggen.webapp.screens.widgets.HeldPuzzlePiece;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PuzzleView implements ScreenView {
    private final EnhancedGestureDetector gestureDetector;
    public Stage stage;
    private ScreenViewport viewport;

    private Map<ConnectedPuzzlePieces, ConnectedPiecesGroup> connectedPiecesGroupMap;
    private PuzzleViewModel puzzleViewModel;
    private PuzzleViewController puzzleViewController;
    private WorldBoundedCamera worldBoundedCamera;

    public PuzzleView(WorldBoundedCamera worldBoundedCamera, PuzzleOverlayBatch batch, Skin skin, PuzzleViewModel puzzleViewModel,
                      PuzzleViewController puzzleViewController) {
        this.worldBoundedCamera = worldBoundedCamera;
        this.viewport = new ScreenViewport(worldBoundedCamera.getCamera());
        this.connectedPiecesGroupMap = new HashMap<>();
        this.puzzleViewModel = puzzleViewModel;
        this.puzzleViewController = puzzleViewController;
        this.stage = new Stage(viewport, batch);

        this.gestureDetector = new EnhancedGestureDetector(new PuzzleGestureListener(puzzleViewController, stage));

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.SPACE) {
                    puzzleViewController.showShuffleModal();
                }
                return super.keyDown(event, keycode);
            }

            @Override
            public boolean scrolled(InputEvent event, float x, float y, int scrollDirection) {
                puzzleViewController.zoomBy(scrollDirection);
                return super.scrolled(event, x, y, scrollDirection);
            }
        });

        puzzleViewModel.getResizeScreenObservable().subscribe(new Consumer<GridPoint2>() {
            @Override
            public void accept(GridPoint2 screenBounds) {
                viewport.update(screenBounds.x, screenBounds.y);
            }
        });

        puzzleViewModel.getConnectedPiecesListObservable().subscribe(new Consumer<List<ConnectedPuzzlePieces>>() {
            @Override
            public void accept(List<ConnectedPuzzlePieces> connectedPiecesList) {
                for (Iterator<ConnectedPuzzlePieces> it = connectedPiecesGroupMap.keySet().iterator(); it.hasNext();) {
                    ConnectedPuzzlePieces existingPieces = it.next();
                    if (!connectedPiecesList.contains(existingPieces)) {
                        connectedPiecesGroupMap.get(existingPieces).remove(); // remove from scene2d
                        it.remove(); // remove from map
                    }
                }
                for (ConnectedPuzzlePieces connectedPieces : connectedPiecesList) {
                    addOrUpdateConnectedPieces(connectedPieces);
                }
            }
        });

        puzzleViewModel.getUpdatedPieceObservable().subscribe(new Consumer<ConnectedPuzzlePieces>() {
            @Override
            public void accept(ConnectedPuzzlePieces connectedPieces) {
                addOrUpdateConnectedPieces(connectedPieces);
            }
        });

        puzzleViewModel.getScalesObservable().subscribe(new Consumer<Vector2>() {
            @Override
            public void accept(Vector2 scales) {
                batch.setScales(scales);
                puzzleViewController.updateWorldBounds(viewport.getScreenWidth(), viewport.getScreenHeight());

                for (ConnectedPuzzlePieces connectedPieces : puzzleViewModel.getConnectedPiecesListObservable().getValue()) {
                    addOrUpdateConnectedPieces(connectedPieces, scales);
                }
            }
        });

        puzzleViewModel.getCameraZoomObservable().subscribe(new Consumer<Float>() {
            @Override
            public void accept(Float zoom) {
                worldBoundedCamera.setZoom(zoom);
                batch.setCameraZoom(worldBoundedCamera.getCamera().zoom);
            }
        });

        puzzleViewModel.getWorldBoundsObservable().subscribe(new Consumer<GridPoint2>() {
            @Override
            public void accept(GridPoint2 worldBounds) {
                float maxZoomX = worldBounds.x / (float)viewport.getScreenWidth();
                float maxZoomY = worldBounds.y / (float)viewport.getScreenHeight();

                float maxZoom = Math.max(maxZoomX, maxZoomY);
                worldBoundedCamera.setCameraBounds(worldBounds.x, worldBounds.y, maxZoom);
                viewport.apply(true);

                batch.setCameraZoom(worldBoundedCamera.getCamera().zoom);
            }
        });
    }

    private void addOrUpdateConnectedPieces(ConnectedPuzzlePieces connectedPieces) {
        addOrUpdateConnectedPieces(connectedPieces, puzzleViewModel.getScalesObservable().getValue());
    }

    private void addOrUpdateConnectedPieces(ConnectedPuzzlePieces connectedPieces, Vector2 scales) {
        Texture background = puzzleViewModel.getBackgroundImageObservable().getValue();

        ConnectedPiecesGroup connectedPiecesGroup = connectedPiecesGroupMap.get(connectedPieces);
        if (connectedPiecesGroup != null) {
            connectedPiecesGroup.update(connectedPieces);
        } else {
            connectedPiecesGroup = new ConnectedPiecesGroup(connectedPieces, scales, background);
            connectedPiecesGroupMap.put(connectedPieces, connectedPiecesGroup);
            stage.addActor(connectedPiecesGroup);
        }
    }

    public void act(float delta) {
        worldBoundedCamera.adjustToWorldBounds(delta);

        HeldPuzzlePiece heldPiece = puzzleViewModel.getHeldPieceObservable().getValue();
        if (heldPiece.getPiecesHeld() != null) {
            Vector3 mousePositionInWorld = worldBoundedCamera.getCamera().unproject(
                    new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            Vector2 updatedPosition = new Vector2(mousePositionInWorld.x + heldPiece.getOffset().x,
                    mousePositionInWorld.y + heldPiece.getOffset().y);
            puzzleViewController.setGroupPosition(heldPiece.getPiecesHeld(), updatedPosition);
        }
        stage.act(delta);
    }

    public void draw() {
        stage.draw();
    }

    public EnhancedGestureDetector getGestureDetector() {
        return gestureDetector;
    }
}
