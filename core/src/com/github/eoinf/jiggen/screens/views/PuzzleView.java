package com.github.eoinf.jiggen.screens.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.eoinf.jiggen.graphics.PuzzleOverlayBatch;
import com.github.eoinf.jiggen.graphics.WorldBoundedCamera;
import com.github.eoinf.jiggen.input.EnhancedGestureDetector;
import com.github.eoinf.jiggen.input.PuzzleGestureListener;
import com.github.eoinf.jiggen.screens.controllers.PuzzleViewController;
import com.github.eoinf.jiggen.screens.controllers.PuzzleViewModel;
import com.github.eoinf.jiggen.screens.models.ConnectedPuzzlePieces;
import com.github.eoinf.jiggen.screens.widgets.ConnectedPiecesGroup;
import com.github.eoinf.jiggen.screens.widgets.HeldPuzzlePiece;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PuzzleView {
    private final EnhancedGestureDetector gestureDetector;
    private final PuzzleOverlayBatch batch;
    public Stage stage;
    private Viewport viewport;
    private Group root;

    private Map<ConnectedPuzzlePieces, ConnectedPiecesGroup> connectedPiecesGroupMap;
    private PuzzleViewModel puzzleViewModel;
    private PuzzleViewController puzzleViewController;
    private WorldBoundedCamera camera;

    public PuzzleView(WorldBoundedCamera camera, PuzzleOverlayBatch batch, Skin skin, PuzzleViewModel puzzleViewModel,
                      PuzzleViewController puzzleViewController) {
        this.batch = batch;
        this.camera = camera;
        this.viewport = new ScreenViewport(camera);
        this.connectedPiecesGroupMap = new HashMap<>();
        this.puzzleViewModel = puzzleViewModel;
        this.puzzleViewController = puzzleViewController;
        this.stage = new Stage(viewport, batch);

        this.root = new Group();
        stage.addActor(root);

        this.gestureDetector = new EnhancedGestureDetector(new PuzzleGestureListener(puzzleViewController, stage));

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.SPACE) {
                    puzzleViewController.shuffle();
                } else if (keycode == Input.Keys.T) {
//                    puzzleViewModel.centreCamera();
//                    boundedCamera.setX(worldWidth / 2);
//                    boundedCamera.setY(worldHeight / 2);
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
                for (ConnectedPuzzlePieces connectedPieces : puzzleViewModel.getConnectedPiecesListObservable().getValue()) {
                    addOrUpdateConnectedPieces(connectedPieces, scales);
                }
            }
        });

        puzzleViewModel.getCameraZoomObservable().subscribe(new Consumer<Float>() {
            @Override
            public void accept(Float zoom) {
                camera.zoom = zoom;
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
            root.addActor(connectedPiecesGroup);
        }
    }

    public void act(float delta) {
        camera.update();

        HeldPuzzlePiece heldPiece = puzzleViewModel.getHeldPieceObservable().getValue();
        if (heldPiece.getPiecesHeld() != null) {
            Vector3 mousePositionInWorld = camera.unproject(
                    new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            Vector2 updatedPosition = new Vector2(mousePositionInWorld.x + heldPiece.getOffset().x,
                    mousePositionInWorld.y + heldPiece.getOffset().y);
            System.out.println();
            puzzleViewController.setGroupPosition(heldPiece.getPiecesHeld(), updatedPosition);
        }
        stage.act(delta);
    }

    public void draw() {
        this.batch.setProjectionMatrix(camera.combined);
        stage.draw();
    }

    public EnhancedGestureDetector getGestureDetector() {
        return gestureDetector;
    }
}
