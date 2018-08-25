package com.github.eoinf.jiggen.views;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleGraphTemplate;
import com.github.eoinf.jiggen.utils.SimpleObservable;
import com.github.eoinf.jiggen.utils.SimpleSubject;
import com.github.eoinf.jiggen.views.widgets.HeldPuzzlePiece;
import com.github.eoinf.jiggen.views.widgets.PuzzlePieceGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PuzzleViewModel {

    private static final int WORLD_PADDING = 50;

    private PuzzleGraphTemplate puzzleGraphTemplate;
    private List<PuzzlePieceGroup> puzzlePieceGroups;

    private SimpleSubject<PuzzlePieceGroup> puzzlePieceAddedSubject;
    private SimpleSubject<PuzzlePieceGroup> puzzlePieceRemovedSubject;
    private SimpleSubject<HeldPuzzlePiece> heldPuzzlePieceSubject;
    private SimpleSubject<PuzzlePieceGroup> droppedPuzzlePieceSubject;
    private SimpleSubject<Vector2> scalesSubject;
    private SimpleSubject<GridPoint2> worldBoundsSubject;
    private SimpleSubject<Boolean> fullScreenSubject;

    //
    // On puzzle scales changed
    //
    public void setScales(Vector2 scales) {
        this.scalesSubject.onNext(scales);
    }

    public SimpleObservable getScalesObservable() {
        return this.scalesSubject;
    }

    //
    // On world bounds changed
    //
    public void setWorldBounds(GridPoint2 worldBounds) {
        GridPoint2 oldWorldBounds = worldBoundsSubject.getValue();
        this.worldBoundsSubject.onNext(new GridPoint2(
                Math.max(worldBounds.x, oldWorldBounds.x),
                Math.max(worldBounds.y, oldWorldBounds.y))
        );
    }

    public SimpleObservable<GridPoint2> getWorldBoundsObservable() {
        return this.worldBoundsSubject;
    }


    //
    //  On Puzzle Piece Added
    //
    public SimpleObservable<PuzzlePieceGroup> getPuzzlePieceAddedObservable() {
        return puzzlePieceAddedSubject;
    }

    public void addPiece(PuzzlePieceGroup pieceGroup) {
        puzzlePieceGroups.add(pieceGroup);
        puzzlePieceAddedSubject.onNext(pieceGroup);
    }

    //
    //  Held puzzle piece
    //
    public SimpleObservable<HeldPuzzlePiece> getHeldPieceObservable() {
        return heldPuzzlePieceSubject;
    }

    public void pickUpPiece(PuzzlePieceGroup pieceGroup, Vector2 mouseOffset) {
        heldPuzzlePieceSubject.onNext(new HeldPuzzlePiece(pieceGroup, mouseOffset));
    }

    // Dropped puzzle piece
    public SimpleObservable<PuzzlePieceGroup> getDroppedPuzzlePiece() {
        return droppedPuzzlePieceSubject;
    }

    public void dropPiece(float x, float y) {
        HeldPuzzlePiece heldPuzzlePiece = heldPuzzlePieceSubject.getValue();
        if (heldPuzzlePiece != HeldPuzzlePiece.NONE) {
            PuzzlePieceGroup pieceGroupHeld = heldPuzzlePiece.getPieceGroup();
            for (PuzzlePieceGroup otherPieceGroup : puzzlePieceGroups) {
                if (pieceGroupHeld != otherPieceGroup) {
                    if (pieceGroupHeld.tryConnectTo(otherPieceGroup, scalesSubject.getValue(), puzzleGraphTemplate)) {
                        puzzlePieceRemovedSubject.onNext(pieceGroupHeld);
                        break;
                    }
                }
            }

            Vector2 newPosition = fitInWorldBounds(new Vector2(x + heldPuzzlePiece.getOffset().x,
                            y + heldPuzzlePiece.getOffset().y),
                    pieceGroupHeld.getWidth(), pieceGroupHeld.getHeight());

            pieceGroupHeld.setPosition(newPosition.x, newPosition.y);
            droppedPuzzlePieceSubject.onNext(pieceGroupHeld);
            heldPuzzlePieceSubject.onNext(HeldPuzzlePiece.NONE);
        }
    }

    public boolean isHoldingPiece() {
        return heldPuzzlePieceSubject.getValue() != HeldPuzzlePiece.NONE;
    }

    //
    // On Puzzle Piece removed
    //
    public SimpleObservable<PuzzlePieceGroup> getRemovedPieceObservable() {
        return puzzlePieceRemovedSubject;
    }


    //
    // On set full screen
    //
    public SimpleObservable<Boolean> getFullScreenObservable() {
        return fullScreenSubject;
    }

    public void setFullScreen(boolean isFullScreen) {
        fullScreenSubject.onNext(isFullScreen);
    }


    //
    // Constructor
    //
    public PuzzleViewModel() {
        puzzlePieceAddedSubject = SimpleSubject.create();
        puzzlePieceRemovedSubject = SimpleSubject.create();
        heldPuzzlePieceSubject = SimpleSubject.create();
        scalesSubject = SimpleSubject.create();
        worldBoundsSubject = SimpleSubject.createDefault(new GridPoint2());
        fullScreenSubject = SimpleSubject.create();

        droppedPuzzlePieceSubject = SimpleSubject.create();

        heldPuzzlePieceSubject.onNext(HeldPuzzlePiece.NONE);


        puzzlePieceGroups = new ArrayList<>();
    }


    public void setPuzzleGraphTemplate(PuzzleGraphTemplate template) {
        this.puzzleGraphTemplate = template;
    }

    public void setMousePosition(Vector3 mousePositionInWorld) {
        HeldPuzzlePiece heldPuzzlePiece = heldPuzzlePieceSubject.getValue();
        heldPuzzlePiece.updatePosition(mousePositionInWorld.x, mousePositionInWorld.y);
    }

    public void reset() {
        for (PuzzlePieceGroup pieceGroup : puzzlePieceGroups) {
            puzzlePieceRemovedSubject.onNext(pieceGroup);
        }
        puzzlePieceGroups.clear();
    }

    public void setViewportSize(int viewportWidth, int viewportHeight) {
        Vector2 scales = scalesSubject.getValue();
        if (scales != null && puzzleGraphTemplate != null) {
            int worldWidth = (int) Math.max(puzzleGraphTemplate.getWidth() * scales.x, viewportWidth);
            int worldHeight = (int) Math.max(puzzleGraphTemplate.getHeight() * scales.y, viewportHeight);

            worldHeight = worldWidth = Math.max(worldWidth, worldHeight) + WORLD_PADDING;
            setWorldBounds(new GridPoint2(worldWidth, worldHeight));
        }
    }

    public void shuffle() {
        Random random = new Random();
        GridPoint2 worldBounds = worldBoundsSubject.getValue();

        for (PuzzlePieceGroup group : puzzlePieceGroups) {
            float r1 = random.nextFloat();
            float r2 = random.nextFloat();

            int x = (int) (r1 * (worldBounds.x - group.getWidth()));
            int y = (int) (r2 * (worldBounds.y - group.getHeight()));
            group.setPosition(x, y);

            if (r1 + r2 > 1) {
                group.toFront();
            }
        }
    }

    private Vector2 fitInWorldBounds(Vector2 position, float width, float height) {
        Vector2 newPosition = position.cpy();
        GridPoint2 worldBounds = worldBoundsSubject.getValue();
        if (newPosition.x + width > worldBounds.x) {
            newPosition.x = worldBounds.x - width;
        } else if (newPosition.x < 0) {
            newPosition.x = 0;
        }
        if (newPosition.y + height > worldBounds.y) {
            newPosition.y = worldBounds.y - height;
        } else if (newPosition.y < 0) {
            newPosition.y = 0;
        }
        return newPosition;
    }
}
