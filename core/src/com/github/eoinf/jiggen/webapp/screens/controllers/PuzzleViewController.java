package com.github.eoinf.jiggen.webapp.screens.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.github.eoinf.jiggen.webapp.Jiggen;
import com.github.eoinf.jiggen.webapp.graphics.WorldBoundedCamera;
import com.github.eoinf.jiggen.webapp.screens.models.ConnectedPuzzlePieces;
import com.github.eoinf.jiggen.webapp.screens.models.ModalViewType;
import com.github.eoinf.jiggen.webapp.screens.models.PuzzleGraphTemplate;
import com.github.eoinf.jiggen.webapp.screens.models.PuzzlePiece;
import com.github.eoinf.jiggen.webapp.screens.models.PuzzlePieceTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.github.eoinf.jiggen.webapp.utils.PixmapUtils.getMinimumScaleToFixAspectRatio;

/**
 * Only the controller can submit events to the view model
 * This is enforced with the 'protected' keyword
 */
public class PuzzleViewController {
    private static float WORLD_BASE_PADDING = 20;
    private final Jiggen game;
    private static GridPoint2 previousViewportSize;

    private PuzzleViewModel puzzleViewModel;
    private HeldPieceController heldPieceController;
    private WorldBoundedCamera worldBoundedCamera;

    public PuzzleViewController(Jiggen game, PuzzleViewModel puzzleViewModel, WorldBoundedCamera worldBoundedCamera) {
        this.game = game;
        this.puzzleViewModel = puzzleViewModel;
        this.worldBoundedCamera = worldBoundedCamera;
        previousViewportSize = new GridPoint2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        heldPieceController = new HeldPieceController(puzzleViewModel, this, worldBoundedCamera);
    }

    public void toggleFullScreen() {
        this.game.onToggleFullScreen.run();
    }

    public void updatePuzzleGraph(PuzzleGraphTemplate puzzleGraphTemplate, Texture backgroundImage) {
        if (puzzleGraphTemplate != null && backgroundImage != null) {
            Vector2 scales = getMinimumScaleToFixAspectRatio(puzzleGraphTemplate.getWidth(), puzzleGraphTemplate.getHeight(),
                    backgroundImage.getWidth(), backgroundImage.getHeight());

            puzzleViewModel.setConnectedPiecesList(new ArrayList<>());
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
            shuffle();
        }
    }

    public void updateWorldBounds(int viewportWidth, int viewportHeight) {
        Vector2 scales = puzzleViewModel.getScalesObservable().getValue();
        PuzzleGraphTemplate puzzleGraphTemplate = puzzleViewModel.getPuzzleTemplateObservable().getValue();
        if (scales != null && puzzleGraphTemplate != null) {
            adjustConnectedPiecesToViewport(viewportWidth, viewportHeight);
            adjustWorldBoundsToViewport(puzzleGraphTemplate, scales, viewportWidth, viewportHeight);
        }
    }

    /**
     * Adjusts the puzzle piece positions so they remain in the same relative location when screen is resized
     * @param viewportWidth new viewport width
     * @param viewportHeight new viewport height
     */
    private void adjustConnectedPiecesToViewport(int viewportWidth, int viewportHeight) {
        List<ConnectedPuzzlePieces> puzzlePiecesList = puzzleViewModel.getConnectedPiecesListObservable().getValue();
        float diffX = viewportWidth / (float)previousViewportSize.x;
        float diffY = (float)viewportHeight /  (float)previousViewportSize.y;
        for (ConnectedPuzzlePieces connectedPieces: puzzlePiecesList) {
            Vector2 position = connectedPieces.getPosition();
            connectedPieces.setPosition(new Vector2(position.x * diffX, position.y * diffY));
        }
        puzzleViewModel.setConnectedPiecesList(puzzlePiecesList);
    }

    private void adjustWorldBoundsToViewport(PuzzleGraphTemplate puzzleGraphTemplate, Vector2 scales,
                                             int viewportWidth, int viewportHeight) {
        List<ConnectedPuzzlePieces> puzzlePiecesList = puzzleViewModel.getConnectedPiecesListObservable().getValue();

        float padding = WORLD_BASE_PADDING +
                // 5% of the width + height so it scales well
                (puzzleGraphTemplate.getWidth() * scales.x + puzzleGraphTemplate.getHeight() * scales.y) / 20f;

        // Calculate the minimum required world width for the puzzle to be maximally visible
        int worldWidth = (int) (puzzleGraphTemplate.getWidth() * scales.x + padding);
        int worldHeight = (int) (puzzleGraphTemplate.getHeight() * scales.y + padding);

        // Ensure all the puzzle pieces will remain within the world bounds
        for (ConnectedPuzzlePieces connectedPieces: puzzlePiecesList) {
            int maxX = (int)(connectedPieces.getPosition().x + connectedPieces.getWidth());
            int maxY = (int)(connectedPieces.getPosition().y + connectedPieces.getHeight());

            if (worldWidth < maxX) {
                worldWidth = maxX;
            }
            if (worldHeight < maxY) {
                worldHeight = maxY;
            }
        }

        // Calculate the minimum required world width for the puzzle to be maximally visible
        worldWidth = Math.max(worldWidth, viewportWidth);
        worldHeight = Math.max(worldHeight, viewportHeight);

        // Allocate extra space so the world bounds match the viewport when fully zoomed out
        double heightRatio = worldHeight / (float)viewportHeight;
        double widthRatio = worldWidth / (float)viewportWidth;
        if (heightRatio > widthRatio) {
            worldWidth = (int)(viewportWidth * heightRatio);
        } else {
            worldHeight = (int)(viewportHeight * widthRatio);
        }
        puzzleViewModel.setWorldBounds(new GridPoint2(worldWidth, worldHeight));
    }

    public void zoomBy(float zoomDelta) {
        puzzleViewModel.setCameraZoom(worldBoundedCamera.getCamera().zoom + (zoomDelta * worldBoundedCamera.getZoomRate()));
    }

    public void shuffle() {
        Random random = new Random();
        GridPoint2 worldBounds = puzzleViewModel.getWorldBoundsObservable().getValue();
        List<ConnectedPuzzlePieces> connectedPiecesList = puzzleViewModel.getConnectedPiecesListObservable().getValue();
        List<ConnectedPuzzlePieces> shuffledConnectedPiecesList = new ArrayList<>();

        for (ConnectedPuzzlePieces connectedPieces : connectedPiecesList) {
            for (PuzzlePiece piece: connectedPieces.getConnectedPieces()) {
                float r1 = random.nextFloat();
                float r2 = random.nextFloat();

                int x = (int) (r1 * (worldBounds.x - piece.getWidth()));
                int y = (int) (r2 * (worldBounds.y - piece.getHeight()));
                piece.setPosition(new Vector2(x, y));
                shuffledConnectedPiecesList.add(new ConnectedPuzzlePieces(piece));
            }
        }
        puzzleViewModel.setConnectedPiecesList(shuffledConnectedPiecesList);
    }


    public void setBackground(Texture backgroundImage) {
        Texture oldBackground = puzzleViewModel.getBackgroundImageObservable().getValue();
        puzzleViewModel.setBackgroundImage(backgroundImage);
        if (oldBackground != null) {
            oldBackground.dispose();
        }
    }

    public void setTemplate(PuzzleGraphTemplate puzzleGraphTemplate) {
        PuzzleGraphTemplate oldTemplate = puzzleViewModel.getPuzzleTemplateObservable().getValue();
        puzzleViewModel.setPuzzleGraphTemplate(puzzleGraphTemplate);
        if (oldTemplate  != null) {
            for (PuzzlePieceTemplate<TextureRegion> pieceTemplate : oldTemplate.getVertices().values()) {
                pieceTemplate.getData().getTexture().dispose();
            }
        }
    }

    public void resizeScreen(int width, int height) {
        puzzleViewModel.resizeScreen(width, height);
        previousViewportSize = new GridPoint2(width, height);
    }

    public void pickUpPiece(ConnectedPuzzlePieces piece, Vector2 mouseOffset) {
        puzzleViewModel.setHeldPiece(piece, mouseOffset);
    }

    public void dropPiece() {
        heldPieceController.dropPiece();
    }

    public void panBy(float deltaX, float deltaY) {
        if (!puzzleViewModel.isHoldingPiece()) {
            worldBoundedCamera.getCamera().translate(-deltaX * worldBoundedCamera.getCamera().zoom, deltaY * worldBoundedCamera.getCamera().zoom);
        }
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

    public void showBackground() {
        puzzleViewModel.setActiveModal(ModalViewType.BACKGROUND_DISPLAY);
    }

    public void showShuffleModal() {
        puzzleViewModel.setActiveModal(ModalViewType.SHUFFLE_CONFIRM);
    }
    public void hideModal() {
        puzzleViewModel.setActiveModal(ModalViewType.NONE);
    }
}
