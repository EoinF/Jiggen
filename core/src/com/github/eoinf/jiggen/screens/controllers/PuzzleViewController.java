package com.github.eoinf.jiggen.screens.controllers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleGraphTemplate;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzlePieceTemplate;
import com.github.eoinf.jiggen.graphics.WorldBoundedCamera;
import com.github.eoinf.jiggen.screens.models.ConnectedPuzzlePieces;
import com.github.eoinf.jiggen.screens.models.PuzzlePiece;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import static com.github.eoinf.jiggen.utils.PixmapUtils.getMinimumScaleToFixAspectRatio;

/**
 * Only the controller can submit events to the view model
 * This is enforced with the 'protected' keyword
 */
public class PuzzleViewController {
    private static float WORLD_PADDING = 0;
    private static float ZOOM_RATE = 0.1f;

    private PuzzleViewModel puzzleViewModel;
    private HeldPieceController heldPieceController;
    private WorldBoundedCamera camera;

    public PuzzleViewController(PuzzleViewModel puzzleViewModel, WorldBoundedCamera camera) {
        this.puzzleViewModel = puzzleViewModel;
        this.camera = camera;
        heldPieceController = new HeldPieceController(puzzleViewModel, this, camera);

        puzzleViewModel.getPuzzleTemplateObservable().subscribe(new Consumer<PuzzleGraphTemplate>() {
            @Override
            public void accept(PuzzleGraphTemplate puzzleGraphTemplate) {
                setPuzzleGraph();
                calculateWorldBounds();
            }
        });

        puzzleViewModel.getBackgroundImageObservable().subscribe(new Consumer<Texture>() {
            @Override
            public void accept(Texture backgroundImage) {
                setPuzzleGraph();
            }
        });

        puzzleViewModel.getScalesObservable().subscribe(new Consumer<Vector2>() {
            @Override
            public void accept(Vector2 scales) {
                calculateWorldBounds();
            }
        });

        puzzleViewModel.getWorldBoundsObservable().subscribe(new Consumer<GridPoint2>() {
            @Override
            public void accept(GridPoint2 worldBounds) {
                float maxZoomX = worldBounds.x / camera.viewportWidth;
                float maxZoomY = worldBounds.y / camera.viewportHeight;

                float maxZoom = Math.min(maxZoomX, maxZoomY);
                camera.setCameraBounds(worldBounds.x, worldBounds.y, maxZoom);
            }
        });
    }

    private void setPuzzleGraph() {
        PuzzleGraphTemplate puzzleGraphTemplate = puzzleViewModel.getPuzzleTemplateObservable().getValue();
        Texture backgroundImage = puzzleViewModel.getBackgroundImageObservable().getValue();

        if (puzzleGraphTemplate != null && backgroundImage != null) {
            Vector2 scales = getMinimumScaleToFixAspectRatio(puzzleGraphTemplate.getWidth(), puzzleGraphTemplate.getHeight(),
                    backgroundImage.getWidth(), backgroundImage.getHeight());

            puzzleViewModel.setScales(scales);

            List<ConnectedPuzzlePieces> puzzlePieces = new ArrayList<>();

            GridPoint2 worldBounds = puzzleViewModel.getWorldBoundsObservable().getValue();

            float offsetX = (worldBounds.x - puzzleGraphTemplate.getWidth() * scales.x) / 2f;
            float offsetY = (worldBounds.y - puzzleGraphTemplate.getHeight() * scales.y) / 2f;

            for (PuzzlePieceTemplate<TextureRegion> piece : puzzleGraphTemplate.getVertices().values()) {
                Vector2 position = new Vector2(offsetX + piece.x() * scales.x, offsetY + piece.y() * scales.y);
                puzzlePieces.add(new ConnectedPuzzlePieces(new PuzzlePiece(position, scales, piece, puzzleGraphTemplate)));
            }

            puzzleViewModel.setConnectedPiecesList(puzzlePieces);
        }
    }

    private void calculateWorldBounds() {
        Vector2 scales = puzzleViewModel.getScalesObservable().getValue();
        PuzzleGraphTemplate puzzleGraphTemplate = puzzleViewModel.getPuzzleTemplateObservable().getValue();
        if (scales != null && puzzleGraphTemplate != null) {
            int worldWidth = (int) Math.max(puzzleGraphTemplate.getWidth() * scales.x + WORLD_PADDING, camera.viewportWidth);
            int worldHeight = (int) Math.max(puzzleGraphTemplate.getHeight() * scales.y + WORLD_PADDING, camera.viewportHeight);

            puzzleViewModel.setWorldBounds(new GridPoint2(worldWidth, worldHeight));
        }
    }

    public void zoomBy(float zoomDelta) {
        System.out.println("Camera zoom = " + camera.zoom);
        puzzleViewModel.setCameraZoom(camera.zoom + zoomDelta * ZOOM_RATE);
    }

    public void shuffle() {
        Random random = new Random();
        GridPoint2 worldBounds = puzzleViewModel.getWorldBoundsObservable().getValue();
        List<ConnectedPuzzlePieces> connectedPiecesList = puzzleViewModel.getConnectedPiecesListObservable().getValue();

        for (ConnectedPuzzlePieces connectedPieces : connectedPiecesList) {
            float r1 = random.nextFloat();
            float r2 = random.nextFloat();

            int x = (int) (r1 * (worldBounds.x - connectedPieces.getWidth()));
            int y = (int) (r2 * (worldBounds.y - connectedPieces.getHeight()));

            for (PuzzlePiece piece: connectedPieces.getConnectedPieces()) {
                piece.setPosition(new Vector2(x, y));
            }
        }

        puzzleViewModel.setConnectedPiecesList(connectedPiecesList);
    }

    public void startPuzzle(PuzzleGraphTemplate puzzleGraphTemplate, Texture backgroundImage) {
        // Clear the old puzzle first so all the UI is flushed out
        puzzleViewModel.reset();

        puzzleViewModel.setPuzzleGraphTemplate(puzzleGraphTemplate);
        puzzleViewModel.setBackgroundImage(backgroundImage);
    }

    public void resizeScreen(int width, int height) {
        puzzleViewModel.resizeScreen(width, height);
    }

    public void pickUpPiece(ConnectedPuzzlePieces piece, Vector2 mouseOffset) {
        puzzleViewModel.setHeldPiece(piece, mouseOffset);
    }

    public void dropPiece() {
        heldPieceController.dropPiece();
    }

    public void panBy(float deltaX, float deltaY) {
        if (!puzzleViewModel.isHoldingPiece()) {
            System.out.println("Pan by " + deltaX + ", " + deltaY);
            // Assume only one pointer is being used (Panning is only possible with a single pointer)
            camera.translate(-deltaX * camera.zoom, deltaY * camera.zoom);
        }
    }

    public void toggleFullScreen() {
        puzzleViewModel.setFullScreen(!puzzleViewModel.getFullScreenObservable().getValue());
    }

    public void setGroupPosition(ConnectedPuzzlePieces connectedPieces, Vector2 nextPosition) {
        connectedPieces.setPosition(nextPosition);

        puzzleViewModel.updateConnectedPieceGroup(connectedPieces);
    }

    public void combineConnectedPieces(ConnectedPuzzlePieces connectedPieces, ConnectedPuzzlePieces otherConnectedPieces, Vector2 error) {
        // Adjust the other pieces position so they fit together
        Vector2 newPosition = new Vector2(connectedPieces.getPosition().x + error.x,
                connectedPieces.getPosition().y + error.y);
        connectedPieces.setPosition(newPosition);

        // Combine the two lists and reuse the first list
        List<PuzzlePiece> combinedPieces = new ArrayList<>(connectedPieces.getConnectedPieces());
        combinedPieces.addAll(otherConnectedPieces.getConnectedPieces());
        connectedPieces.setConnectedPieces(combinedPieces);

        // Remove the second list
        List<ConnectedPuzzlePieces> connectedPuzzlePiecesList = puzzleViewModel.getConnectedPiecesListObservable().getValue();
        connectedPuzzlePiecesList.remove(otherConnectedPieces);

        // Notify observers
        puzzleViewModel.setConnectedPiecesList(connectedPuzzlePiecesList);
    }
}
