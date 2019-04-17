package com.github.eoinf.jiggen.webapp.screens.models;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class ConnectedPuzzlePieces {
    private List<PuzzlePiece> connectedPieces;

    private float width;
    private float height;
    private Vector2 position;

    public ConnectedPuzzlePieces(PuzzlePiece piece) {
        List<PuzzlePiece> connectedPieces = new ArrayList<>();
        connectedPieces.add(piece);
        this.setConnectedPieces(connectedPieces);
    }

    public void setConnectedPieces(List<PuzzlePiece> connectedPieces) {
        this.connectedPieces = connectedPieces;
        float left = Float.MAX_VALUE;
        float right = Float.MIN_VALUE;
        float top = Float.MAX_VALUE;
        float bottom = Float.MIN_VALUE;

        for (PuzzlePiece piece: connectedPieces) {
            left = Math.min(left, piece.x());
            right = Math.max(right, piece.x() + piece.getWidth());
            top = Math.min(top, piece.y());
            bottom = Math.max(bottom, piece.y() + piece.getHeight());
        }
        this.width = right - left;
        this.height = bottom - top;
        this.position = new Vector2(left, top);
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
        return position;
    }

    public void setPosition(Vector2 nextPosition) {
        // Get the offset from the base position
        nextPosition.sub(getPosition());

        this.position.add(nextPosition);

        for (PuzzlePiece puzzlePiece: connectedPieces) {
            puzzlePiece.setPosition(puzzlePiece.getPosition().add(nextPosition));
        }
    }
}
