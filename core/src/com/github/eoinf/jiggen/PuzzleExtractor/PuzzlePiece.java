package com.github.eoinf.jiggen.PuzzleExtractor;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class PuzzlePiece extends Image {
    private Vector2 position;
    public Vector2 getPosition() {
        return position;
    }
    int width;
    int height;


    PuzzlePiece(int startX, int startY, int width, int height, Pixmap pixmap) {
        super(new Texture(pixmap));
        this.position = new Vector2(startX, startY);
        this.width = width;
        this.height = height;
        this.setPosition(startX, startY);
    }

    @Override
    public void setPosition(float x, float y) {
        this.position = new Vector2(x, y);
        super.setPosition(x, y);
    }
}
