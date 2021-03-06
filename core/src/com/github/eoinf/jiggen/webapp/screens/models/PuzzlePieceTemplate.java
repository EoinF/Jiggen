package com.github.eoinf.jiggen.webapp.screens.models;

public class PuzzlePieceTemplate<T> {
    private T data;
    private int x;
    private int y;
    private int width;
    private int height;

    public T getData() {
        return data;
    }

    public int getHeight() {
        return this.height;
    }
    public int getWidth() {
        return this.width;
    }

    public int x() {
        return x;
    }
    public int y() {
        return y;
    }

    public PuzzlePieceTemplate(int startX, int startY, int width, int height, T textureData) {
        this.data = textureData;
        this.width = width;
        this.height = height;
        x = startX;
        y = startY;
    }

    public PuzzlePieceTemplate(IntRectangle positionData, T textureData) {
        this(positionData.x, positionData.y, positionData.width, positionData.height, textureData);
    }

    public IntRectangle getPositionData() {
        return new IntRectangle(x, y, width, height);
    }
}
