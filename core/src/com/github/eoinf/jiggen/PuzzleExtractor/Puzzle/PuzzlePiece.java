package com.github.eoinf.jiggen.PuzzleExtractor.Puzzle;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;


public class PuzzlePiece {
    private TextureRegion textureRegion;
    private GridPoint2 position;
    private int width;
    private int height;

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }
    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    public int getHeight() {
        return this.height;
    }
    public int getWidth() {
        return this.width;
    }

    public GridPoint2 getPosition() {
        return position;
    }
    public void setPosition(int x, int y) {
        this.position = new GridPoint2(x, y);
    }

    PuzzlePiece(int startX, int startY, int width, int height, TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
        this.width = width;
        this.height = height;
        this.setPosition(startX, startY);
    }
}
