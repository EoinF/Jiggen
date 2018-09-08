package com.github.eoinf.jiggen.screens.widgets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.github.eoinf.jiggen.screens.models.ConnectedPuzzlePieces;
import com.github.eoinf.jiggen.screens.models.PuzzlePiece;

public class ConnectedPiecesGroup extends Group {

    private final Vector2 scales;
    private final Texture background;

    public ConnectedPiecesGroup(ConnectedPuzzlePieces connectedPieces, Vector2 scales, Texture background) {
        super();
        // Disable transforming the batch (greatly improves performance)
        setTransform(false);
        this.scales = scales;
        this.background = background;
        for (PuzzlePiece piece : connectedPieces.getConnectedPieces()) {
            PuzzlePieceActor puzzlePieceActor = new PuzzlePieceActor(piece, scales, background);
            addActor(puzzlePieceActor);
        }
        setUserObject(connectedPieces);
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        if (super.hit(x, y, touchable) != null) {
            return this;
        } else {
            return null;
        }
    }

    @Override
    public float getX() {
        return ((ConnectedPuzzlePieces)getUserObject()).getPosition().x;
    }
    @Override
    public float getY() {
        return ((ConnectedPuzzlePieces)getUserObject()).getPosition().y;
    }

    public ConnectedPuzzlePieces getConnectedPieces() {
        return (ConnectedPuzzlePieces)getUserObject();
    }

    public void update(ConnectedPuzzlePieces connectedPieces) {
        this.clearChildren();

        for (PuzzlePiece piece : connectedPieces.getConnectedPieces()) {
            PuzzlePieceActor puzzlePieceActor = new PuzzlePieceActor(piece, scales, background);
            addActor(puzzlePieceActor);
        }
    }
}
