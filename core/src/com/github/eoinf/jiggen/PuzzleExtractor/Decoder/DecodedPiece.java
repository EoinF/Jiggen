package com.github.eoinf.jiggen.PuzzleExtractor.Decoder;

import com.badlogic.gdx.math.GridPoint2;

public class DecodedPiece {
    private boolean[][] binaryPixelMap;
    private GridPoint2 position;
    private int width;
    private int height;

    public GridPoint2 getPosition() {
        return position;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isTraversed(int x, int y) {
        return binaryPixelMap[y][x];
    }

    public DecodedPiece(boolean[][] binaryPixelMap, GridPoint2 position, int maxX, int maxY) {
        this.binaryPixelMap = binaryPixelMap;
        this.position = position;
        this.width = maxX - position.x;
        this.height = maxY - position.y;
    }
}
