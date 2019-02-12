package com.github.eoinf.jiggen.webapp.screens.models;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class ConnectedPuzzlePieces {
    private List<PuzzlePiece> connectedPieces;
    private float width;
    private float height;

    public ConnectedPuzzlePieces(PuzzlePiece piece) {
        this.connectedPieces = new ArrayList<>();
        this.connectedPieces.add(piece);

        this.width = piece.getWidth();
        this.height = piece.getHeight();
    }

    public void setConnectedPieces(List<PuzzlePiece> connectedPieces) {
        this.connectedPieces = connectedPieces;
        for (PuzzlePiece piece: connectedPieces) {

        }
    }

    public List<PuzzlePiece> getConnectedPieces() {
        return connectedPieces;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public Vector2 getPosition() {
        return connectedPieces.get(0).getPosition();
    }

    public void setPosition(Vector2 nextPosition) {
        // Get the offset from the base position
        nextPosition.sub(getPosition());

        for (PuzzlePiece puzzlePiece: connectedPieces) {
            puzzlePiece.setPosition(puzzlePiece.getPosition().add(nextPosition));
        }
    }
}
