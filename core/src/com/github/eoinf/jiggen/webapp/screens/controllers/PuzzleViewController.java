package com.github.eoinf.jiggen.webapp.screens.controllers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.github.eoinf.jiggen.webapp.Jiggen;
import com.github.eoinf.jiggen.webapp.graphics.WorldBoundedCamera;
import com.github.eoinf.jiggen.webapp.screens.models.ConnectedPuzzlePieces;
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
    private static float WORLD_BASE_PADDING = 50;
    private final Jiggen game;

    private PuzzleViewModel puzzleViewModel;
    private HeldPieceController heldPieceController;
    private WorldBoundedCamera camera;

    public PuzzleViewController(Jiggen game, PuzzleViewModel puzzleViewModel, WorldBoundedCamera camera) {
        this.game = game;
        this.puzzleViewModel = puzzleViewModel;
        this.camera = camera;
        heldPieceController = new HeldPieceController(puzzleViewModel, this, camera);
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
        }
    }

    public void updateWorldBounds(float viewportWidth, float viewportHeight) {
        Vector2 scales = puzzleViewModel.getScalesObservable().getValue();
        PuzzleGraphTemplate puzzleGraphTemplate = puzzleViewModel.getPuzzleTemplateObservable().getValue();
        if (scales != null && puzzleGraphTemplate != null) {
            float padding = WORLD_BASE_PADDING +
                    // 10% of the width + height so it scales well
                    (puzzleGraphTemplate.getWidth() * scales.x + puzzleGraphTemplate.getHeight() * scales.y) / 10f;

            // Calculate the minimum required world width for the puzzle to be maximally visible
            int worldWidth = (int) (puzzleGraphTemplate.getWidth() * scales.x + padding);
            int worldHeight = (int) (puzzleGraphTemplate.getHeight() * scales.y + padding);

            // Ensure all the puzzle pieces will remain within the world bounds
            for (ConnectedPuzzlePieces connectedPieces: puzzleViewModel.getConnectedPiecesListObservable().getValue()) {
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
            worldWidth = Math.max(worldWidth, (int)viewportWidth);
            worldHeight = Math.max(worldHeight, (int)viewportHeight);

            // Allocate extra space so the world bounds match the viewport when fully zoomed out
            double heightRatio = worldHeight / viewportHeight;
            double widthRatio = worldWidth / viewportWidth;
            if (heightRatio > widthRatio) {
                worldWidth = (int)(viewportWidth * heightRatio);
            } else {
                worldHeight = (int)(viewportHeight * widthRatio);
            }
            puzzleViewModel.setWorldBounds(new GridPoint2(worldWidth, worldHeight));
        }
    }

    public void zoomBy(float zoomDelta) {
        puzzleViewModel.setCameraZoom(camera.zoom + (zoomDelta * camera.getZoomRate()));
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
    }

    public void pickUpPiece(ConnectedPuzzlePieces piece, Vector2 mouseOffset) {
        puzzleViewModel.setHeldPiece(piece, mouseOffset);
    }

    public void dropPiece() {
        heldPieceController.dropPiece();
    }

    public void panBy(float deltaX, float deltaY) {
        if (!puzzleViewModel.isHoldingPiece()) {
            camera.translate(-deltaX * camera.zoom, deltaY * camera.zoom);
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
        puzzleViewModel.setIsBackgroundVisible(true);
    }

    public void hideBackground() {
        puzzleViewModel.setIsBackgroundVisible(false);
    }
}
