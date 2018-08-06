package com.github.eoinf.jiggen.views;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.SnapshotArray;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleGraphTemplate;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzlePieceTemplate;
import com.github.eoinf.jiggen.graphics.TextureOverlayImage;

public class PuzzlePieceGroup extends Group {

    private static float PIECE_CONNECT_THRESHOLD = 15;

    // Used to keep track of the groups movements (without changing the actual position of the group)
    // and propagates the change in position to each of the children
    private Vector2 internalPosition;

    public PuzzlePieceGroup(TextureOverlayImage child) {
        super();
        this.internalPosition = Vector2.Zero.cpy();
        addActor(child);
    }

    public boolean tryConnectTo(PuzzlePieceGroup otherPieceGroup, Vector2 scales, PuzzleGraphTemplate puzzleGraph) {
        for (Actor pieceActor : this.getChildren()) {
            PuzzlePieceTemplate template = (PuzzlePieceTemplate) pieceActor.getUserObject();

            SnapshotArray<Actor> otherPieceChildren = otherPieceGroup.getChildren();
            for (int i = 0; i < otherPieceChildren.size; i++) {
                Actor otherPieceActor = otherPieceChildren.get(i);
                PuzzlePieceTemplate otherTemplate = (PuzzlePieceTemplate) otherPieceActor.getUserObject();
                if (puzzleGraph.hasEdge(template, otherTemplate)) {
                    if (tryConnectIndividualPieces(pieceActor, otherPieceActor, template, otherTemplate, scales)) {
                        otherPieceGroup.connectToPiece(this);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean tryConnectIndividualPieces(Actor pieceActor, Actor otherPieceActor,
                                               PuzzlePieceTemplate template, PuzzlePieceTemplate otherTemplate,
                                               Vector2 scales) {
        float expectedXDelta = (template.x() - otherTemplate.x()) * scales.x;
        float expectedYDelta = (template.y() - otherTemplate.y()) * scales.y;
        float currentXDelta = pieceActor.getX() - otherPieceActor.getX();
        float currentYDelta = pieceActor.getY() - otherPieceActor.getY();
        float errorX = expectedXDelta - currentXDelta;
        float errorY = expectedYDelta - currentYDelta;

        if (Math.abs(errorX) + Math.abs(errorY) < PIECE_CONNECT_THRESHOLD) {
            this.setPosition(this.getX() + errorX, this.getY() + errorY);
            return true;
        }
        return false;
    }

    private void connectToPiece(PuzzlePieceGroup otherPieceActor) {
        SnapshotArray<Actor> children = otherPieceActor.getChildren();
        while (children.size > 0) {
            Actor child = children.first();
            this.addActor(child);
            otherPieceActor.removeActor(child);
        }
    }

    @Override
    public float getX() {
        return internalPosition.x;
    }

    @Override
    public float getY() {
        return internalPosition.y;
    }

    @Override
    public void setScale(float x, float y) {
        for (Actor item : this.getChildren()) {
            item.setScale(x, y);
        }
    }

    @Override
    public void setPosition(float x, float y) {
        float deltaX = x - this.internalPosition.x;
        float deltaY = y - this.internalPosition.y;
        this.internalPosition.x = x;
        this.internalPosition.y = y;

        for (Actor child: this.getChildren()) {
            child.setPosition(child.getX() + deltaX, child.getY() + deltaY);
        }
    }
    @Override
    public Actor hit(float x, float y, boolean touchable) {
        if (super.hit(x, y, touchable) != null) {
            return this;
        } else {
            return null;
        }
    }
}
