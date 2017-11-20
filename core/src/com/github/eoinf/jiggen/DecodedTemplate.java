package com.github.eoinf.jiggen;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;

import java.io.File;

public class DecodedTemplate {

    private Texture texture;
    private int width;
    private int height;
    private PuzzleGraph puzzleGraph;
    private PixelSearcher pixelSearcher;

    public PuzzleGraph getGraph() {
        return puzzleGraph;
    }

    public Texture getTexture() {
        return texture;
    }

    public DecodedTemplate(File templateFile) {
        texture = new Texture(new FileHandle(templateFile));

        if (!texture.getTextureData().isPrepared()) {
            texture.getTextureData().prepare();
        }

        Pixmap fullTemplatePixmap = texture.getTextureData().consumePixmap();

        width = texture.getWidth();
        height = texture.getHeight();


        puzzleGraph = new PuzzleGraph(width, height);

        int x = 0, y = 0;

        while(y < height) {
            PuzzlePiece tallestPiece = null;

            while(x < width) {
                PuzzlePiece piece = extractNextPiece(fullTemplatePixmap, x, y);
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

        puzzleGraph.flipPieces();
    }


    private PuzzlePiece extractNextPiece(Pixmap pixmap, int startX, int startY) {
        pixelSearcher = new PixelSearcher(pixmap, width, height);

        GridPoint2 topLeftCorner = pixelSearcher.findNearestPixel(false, startX, startY);
        pixelSearcher.floodFillPiece(topLeftCorner.x, topLeftCorner.y);

        return pixelSearcher.generatePuzzlePiece(startX, startY);
    }
}
