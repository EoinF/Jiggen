package com.github.eoinf.jiggen.views.Screens;

import com.badlogic.gdx.math.Vector2;
import com.github.eoinf.jiggen.views.PuzzlePieceGroup;

public class HeldPuzzlePiece {
    private PuzzlePieceGroup pieceGroup;
    private Vector2 offset;

    public PuzzlePieceGroup getPieceGroup() {
        return pieceGroup;
    }
    public Vector2 getOffset() {
        return offset;
    }

    public void updatePosition(float newX, float newY) {
        if (this.pieceGroup != null) {
            this.pieceGroup.setPosition(newX + offset.x, newY + offset.y);
        }
    }

    public HeldPuzzlePiece(PuzzlePieceGroup pieceGroupHeld, Vector2 pieceHeldOffset) {
        this.pieceGroup = pieceGroupHeld;
        this.offset = pieceHeldOffset;
    }

    public static HeldPuzzlePiece NONE = new HeldPuzzlePiece(null, null);
}
