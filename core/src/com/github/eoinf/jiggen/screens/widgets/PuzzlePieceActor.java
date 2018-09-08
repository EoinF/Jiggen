package com.github.eoinf.jiggen.screens.widgets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleGraphTemplate;
import com.github.eoinf.jiggen.graphics.TextureOverlayImage;
import com.github.eoinf.jiggen.screens.models.PuzzlePiece;

public class PuzzlePieceActor extends TextureOverlayImage {

    public PuzzlePieceActor(PuzzlePiece piece, Vector2 scales, Texture backgroundImage) {
        super(piece.getPuzzlePieceTemplate().getData());

        PuzzleGraphTemplate puzzleGraphTemplate = piece.getPuzzleGraphTemplate();

        // Calculate the bounds after the puzzle is scaled
        float sX = piece.getPuzzlePieceTemplate().x() * scales.x;
        float sY = piece.getPuzzlePieceTemplate().y() * scales.y;
        float sW = (piece.getWidth());
        float sH = (piece.getHeight());

        float scaledPuzzleWidth = puzzleGraphTemplate.getWidth() * scales.x;
        float scaledPuzzleHeight = puzzleGraphTemplate.getHeight() * scales.y;

        // Calculate the uv coordinates for rendering the background image
        float u = sX / scaledPuzzleWidth;
        float v = 1 - ((sY + sH) / scaledPuzzleHeight);
        float u2 = (sX + sW) / scaledPuzzleWidth;
        float v2 = 1 - (sY / scaledPuzzleHeight);

        setOverlay(new TextureRegionDrawable(
                new TextureRegion(backgroundImage, u, v, u2, v2)));

        setPosition(piece.x(), piece.y());
        setScale(scales.x, scales.y);

        setUserObject(piece);
    }

    public void update(PuzzlePiece piece) {
        setPosition(piece.x(), piece.y());
    }
}
