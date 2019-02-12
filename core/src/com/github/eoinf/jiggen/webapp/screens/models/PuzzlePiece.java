package com.github.eoinf.jiggen.webapp.screens.models;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class PuzzlePiece {
    private Vector2 position;
    private Vector2 scales;

    private PuzzleGraphTemplate puzzleGraphTemplate;
    private PuzzlePieceTemplate<TextureRegion> puzzlePieceTemplate;

    public PuzzlePiece(Vector2 position, Vector2 scales, PuzzlePieceTemplate<TextureRegion> puzzlePieceTemplate,
                       PuzzleGraphTemplate puzzleGraphTemplate) {
        this.position = position;
        this.scales = scales;
        this.puzzleGraphTemplate = puzzleGraphTemplate;
        this.puzzlePieceTemplate = puzzlePieceTemplate;
    }

    public PuzzlePieceTemplate<TextureRegion> getPuzzlePieceTemplate() {
        return puzzlePieceTemplate;
    }

    public PuzzleGraphTemplate getPuzzleGraphTemplate() {
        return puzzleGraphTemplate;
    }

    public float getWidth() {
        return puzzlePieceTemplate.getWidth() * scales.x;
    }

    public float getHeight() {
        return puzzlePieceTemplate.getHeight() * scales.y;
    }

    public float x() {
        return position.x;
    }
    public float y() {
        return position.y;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 nextPosition) {
        this.position = nextPosition;
    }
}
