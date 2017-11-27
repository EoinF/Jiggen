package com.github.eoinf.jiggen;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;

public class DecodedTemplate {

    private Texture texture;
    private int width;
    private int height;
    private PuzzleGraph puzzleGraph;
    private PixelSearcher pixelSearcher;


    public DecodedTemplate(Texture template)
    {
        texture = template;

        if (!texture.getTextureData().isPrepared()) {
            texture.getTextureData().prepare();
        }

        width = texture.getWidth();
        height = texture.getHeight();
    }

    public PuzzleGraph decode(Texture backgroundTexture) {
        if (!texture.getTextureData().isPrepared()) {
            texture.getTextureData().prepare();
        }
        if (!backgroundTexture.getTextureData().isPrepared()) {
            backgroundTexture.getTextureData().prepare();
        }
        Pixmap fullTemplatePixmap = texture.getTextureData().consumePixmap();
        Pixmap fullBackgroundPixmap = backgroundTexture.getTextureData().consumePixmap();

        puzzleGraph = new PuzzleGraph(width, height);

        int x = 0, y = 0;

        while(y < height) {
            PuzzlePiece tallestPiece = null;

            while(x < width) {
                PuzzlePiece piece = extractNextPiece(fullTemplatePixmap, fullBackgroundPixmap, x, y);
                x = (int)piece.getPosition().x + piece.width;
                if (tallestPiece == null
                        || piece.height > tallestPiece.height) {
                    tallestPiece = piece;
                }
                puzzleGraph.addVertex(piece);
            }
            x = 0;
            y = (int)tallestPiece.getPosition().y + tallestPiece.height;
        }

        // Flip all of the pieces vertically, because the y axis
        // is inverted when working with pixmaps
        puzzleGraph.flipPieces();

        fullBackgroundPixmap.dispose();
        fullTemplatePixmap.dispose();
        return puzzleGraph;
    }


    private PuzzlePiece extractNextPiece(Pixmap templatePixmap, Pixmap backgroundPixmap, int startX, int startY) {
        pixelSearcher = new PixelSearcher(templatePixmap, backgroundPixmap, width, height);

        GridPoint2 topLeftCorner = pixelSearcher.findNearestPixel(false, startX, startY);
        pixelSearcher.floodFillPiece(topLeftCorner.x, topLeftCorner.y);

        return pixelSearcher.generatePuzzlePiece(startX, startY);
    }
}
