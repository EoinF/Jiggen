package com.github.eoinf.jiggen.PuzzleExtractor.Puzzle;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;


public class PuzzlePiece<T> {
    private T data;
    private GridPoint2 position;
    private int width;
    private int height;

    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
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

    PuzzlePiece(int startX, int startY, int width, int height, T data) {
        this.data = data;
        this.width = width;
        this.height = height;
        this.setPosition(startX, startY);
    }
}
