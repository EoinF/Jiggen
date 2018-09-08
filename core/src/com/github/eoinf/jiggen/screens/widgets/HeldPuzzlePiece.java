package com.github.eoinf.jiggen.screens.widgets;

import com.badlogic.gdx.math.Vector2;
import com.github.eoinf.jiggen.screens.models.ConnectedPuzzlePieces;

public class HeldPuzzlePiece {
    private ConnectedPuzzlePieces piecesHeld;
    private Vector2 offset;

    public ConnectedPuzzlePieces getPiecesHeld() {
        return piecesHeld;
    }

    public Vector2 getOffset() {
        return offset;
    }

    public HeldPuzzlePiece(ConnectedPuzzlePieces piecesHeld, Vector2 offset) {
        this.piecesHeld = piecesHeld;
        this.offset = offset;
    }

    public static HeldPuzzlePiece NONE = new HeldPuzzlePiece(null, null);

    public boolean isHoldingPiece() {
        return piecesHeld != null;
    }
}
