package com.github.eoinf.jiggen.views.Screens;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleGraphTemplate;
import com.github.eoinf.jiggen.views.PuzzlePieceGroup;
import io.reactivex.Observable;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PuzzleViewModel {

    private PuzzleGraphTemplate puzzleGraphTemplate;
    private List<PuzzlePieceGroup> puzzlePieceGroups;

    private Subject<PuzzlePieceGroup> puzzlePieceAddedSubject;
    private Subject<PuzzlePieceGroup> puzzlePieceRemovedSubject;
    private ReplaySubject<HeldPuzzlePiece> heldPuzzlePieceSubject;
    private ReplaySubject<Vector2> scalesSubject;
    private ReplaySubject<GridPoint2> worldBoundsSubject;

    //
    // On puzzle scales changed
    //
    public void setScales(Vector2 scales) {
        this.scalesSubject.onNext(scales);
    }
    public Observable<Vector2> getScalesObservable() {
        return this.scalesSubject;
    }

    //
    // On world bounds changed
    //
    public void setWorldBounds(GridPoint2 worldBounds) {
        this.worldBoundsSubject.onNext(worldBounds);
    }
    public Observable<GridPoint2> getWorldBoundsObservable() {
        return this.worldBoundsSubject;
    }


    //
    //  On Puzzle Piece Added
    //
    public Observable<PuzzlePieceGroup> getPuzzlePieceAddedObservable() {
        return puzzlePieceAddedSubject;
    }

    public void addPiece(PuzzlePieceGroup pieceGroup) {
        puzzlePieceGroups.add(pieceGroup);
        puzzlePieceAddedSubject.onNext(pieceGroup);
    }

    //
    //  Held puzzle piece
    //
    public Observable<HeldPuzzlePiece> getHeldPieceObservable() {
        return heldPuzzlePieceSubject;
    }

    public void pickUpPiece(PuzzlePieceGroup pieceGroup, Vector2 mouseOffset) {
        heldPuzzlePieceSubject.onNext(new HeldPuzzlePiece(pieceGroup, mouseOffset));
    }

    public void dropPiece(Vector2 positionDropped) {
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

            Vector2 newPosition = fitInWorldBounds(new Vector2(positionDropped.x + heldPuzzlePiece.getOffset().x,
                    positionDropped.y + heldPuzzlePiece.getOffset().y),
                    pieceGroupHeld.getWidth(), pieceGroupHeld.getHeight());

            pieceGroupHeld.setPosition(newPosition.x, newPosition.y);
            heldPuzzlePieceSubject.onNext(HeldPuzzlePiece.NONE);
        }
    }

    public boolean isHoldingPiece() {
        return heldPuzzlePieceSubject.getValue() != HeldPuzzlePiece.NONE;
    }

    //
    // On Puzzle Piece removed
    //
    public Observable<PuzzlePieceGroup> getRemovedPieceObservable() {
        return puzzlePieceRemovedSubject;
    }

    public PuzzleViewModel() {
        puzzlePieceAddedSubject = ReplaySubject.create();
        puzzlePieceRemovedSubject = ReplaySubject.create();
        heldPuzzlePieceSubject = ReplaySubject.createWithSize(1);
        scalesSubject = ReplaySubject.createWithSize(1);
        worldBoundsSubject = ReplaySubject.createWithSize(1);

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

    public void shuffle() {
        Random random = new Random();
        GridPoint2 worldBounds = worldBoundsSubject.getValue();

        for (PuzzlePieceGroup group: puzzlePieceGroups) {
            float r1 = random.nextFloat();
            float r2 = 0; //random.nextFloat();

            int x = (int)(r1 * (worldBounds.x - group.getWidth()));
            int y = (int)(r2 * (worldBounds.y - group.getHeight()));
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
