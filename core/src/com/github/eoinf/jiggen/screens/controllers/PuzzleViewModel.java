package com.github.eoinf.jiggen.screens.controllers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleGraphTemplate;
import com.github.eoinf.jiggen.screens.models.ConnectedPuzzlePieces;
import com.github.eoinf.jiggen.screens.widgets.HeldPuzzlePiece;
import com.github.eoinf.jiggen.utils.SimpleObservable;
import com.github.eoinf.jiggen.utils.SimpleSubject;

import java.util.ArrayList;
import java.util.List;

//
// The single source of truth for state updates
// Can only be updated via the view controller
//
public class PuzzleViewModel {
    private SimpleSubject<HeldPuzzlePiece> heldPuzzlePieceSubject;
    private SimpleSubject<Vector2> scalesSubject;
    private SimpleSubject<GridPoint2> worldBoundsSubject;
    private SimpleSubject<Boolean> fullScreenSubject;

    //
    // Constructor
    //
    public PuzzleViewModel(boolean isFullScreen) {
        resizeScreenSubject = SimpleSubject.create();
        heldPuzzlePieceSubject = SimpleSubject.createDefault(HeldPuzzlePiece.NONE);
        scalesSubject = SimpleSubject.createDefault(new Vector2(1, 1));
        worldBoundsSubject = SimpleSubject.createDefault(new GridPoint2());
        fullScreenSubject = SimpleSubject.createDefault(isFullScreen);

        updateConnectedPiecesSubject = SimpleSubject.create();

        connectedPiecesListSubject = SimpleSubject.createDefault(new ArrayList<>());
        backgroundImageSubject = SimpleSubject.create();
        puzzleGraphTemplateSubject = SimpleSubject.create();
        cameraZoomSubject = SimpleSubject.createDefault(1.0f);
    }


    //
    // On puzzle scales changed
    //
    void setScales(Vector2 scales) {
        this.scalesSubject.onNext(scales);
    }

    public SimpleObservable<Vector2> getScalesObservable() {
        return this.scalesSubject;
    }

    //
    // On world bounds changed
    //
    void setWorldBounds(GridPoint2 worldBounds) {
        this.worldBoundsSubject.onNext(worldBounds);
    }

    public SimpleObservable<GridPoint2> getWorldBoundsObservable() {
        return this.worldBoundsSubject;
    }

    //
    //  Held puzzle piece
    //
    public SimpleObservable<HeldPuzzlePiece> getHeldPieceObservable() {
        return heldPuzzlePieceSubject;
    }


    void setHeldPiece(ConnectedPuzzlePieces piece, Vector2 mouseOffset) {
        heldPuzzlePieceSubject.onNext(new HeldPuzzlePiece(piece, mouseOffset));
    }

    public boolean isHoldingPiece() {
        return heldPuzzlePieceSubject.getValue().isHoldingPiece();
    }

    //
    // On set full screen
    //
    public SimpleObservable<Boolean> getFullScreenObservable() {
        return fullScreenSubject;
    }

    void setFullScreen(boolean isFullScreen) {
        fullScreenSubject.onNext(isFullScreen);
    }

    void reset() {
        connectedPiecesListSubject.onNext(new ArrayList<>());
        puzzleGraphTemplateSubject.onNext(null);
        backgroundImageSubject.onNext(null);
    }

    public void setViewportSize(int viewportWidth, int viewportHeight) {
        Vector2 scales = scalesSubject.getValue();
    }

    //
    // Resize screen
    //
    private SimpleSubject<GridPoint2> resizeScreenSubject;

    public SimpleObservable<GridPoint2> getResizeScreenObservable() {
        return resizeScreenSubject;
    }

    void resizeScreen(int width, int height) {
        resizeScreenSubject.onNext(new GridPoint2(width, height));
    }

    //
    // Start a new puzzle
    //

    private SimpleSubject<List<ConnectedPuzzlePieces>> connectedPiecesListSubject;

    public SimpleObservable<List<ConnectedPuzzlePieces>> getConnectedPiecesListObservable() {
        return connectedPiecesListSubject;
    }

    void setConnectedPiecesList(List<ConnectedPuzzlePieces> connectedPieces) {
        connectedPiecesListSubject.onNext(connectedPieces);
    }

    //
    // Set background image
    //
    private SimpleSubject<Texture> backgroundImageSubject;

    public SimpleObservable<Texture> getBackgroundImageObservable() {
        return backgroundImageSubject;
    }

    void setBackgroundImage(Texture backgroundImage) {
        backgroundImageSubject.onNext(backgroundImage);
    }

    //
    // Set puzzle graph template
    //
    private SimpleSubject<PuzzleGraphTemplate> puzzleGraphTemplateSubject;
    public SimpleObservable<PuzzleGraphTemplate> getPuzzleTemplateObservable() {
        return puzzleGraphTemplateSubject;
    }

    void setPuzzleGraphTemplate(PuzzleGraphTemplate puzzleGraphTemplate) {
        puzzleGraphTemplateSubject.onNext(puzzleGraphTemplate);
    }

    //
    // Mouse/Gesture Zoom
    //
    private SimpleSubject<Float> cameraZoomSubject;
    public SimpleObservable<Float> getCameraZoomObservable() {
        return cameraZoomSubject;
    }

    void setCameraZoom(float zoom) {
        cameraZoomSubject.onNext(zoom);
    }

    //
    // Update puzzle piece
    //
    private SimpleSubject<ConnectedPuzzlePieces> updateConnectedPiecesSubject;
    public SimpleObservable<ConnectedPuzzlePieces> getUpdatedPieceObservable() {
        return updateConnectedPiecesSubject;
    }
    void updateConnectedPieceGroup(ConnectedPuzzlePieces connectedPieces) {
        updateConnectedPiecesSubject.onNext(connectedPieces);
    }
}
