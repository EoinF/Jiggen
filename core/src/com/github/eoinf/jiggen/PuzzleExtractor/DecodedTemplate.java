package com.github.eoinf.jiggen.PuzzleExtractor;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.List;

public class DecodedTemplate {

    private int width;
    private int height;
    private Pixmap templatePixmap;

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    private List<TemplatePiece> templatePieces;
    public List<TemplatePiece> getTemplatePieces() {
        return templatePieces;
    }

    public Pixmap getTemplatePixmap() {
        return templatePixmap;
    }

    public DecodedTemplate(Texture template) {
        templatePieces = new ArrayList<>();

        width = template.getWidth();
        height = template.getHeight();

        if (!template.getTextureData().isPrepared()) {
            template.getTextureData().prepare();
        }
        templatePixmap = template.getTextureData().consumePixmap();

        int x = 0, y = 0;

        while(y < height) {
            TemplatePiece tallestPieceInRow = null;

            while(x < width) {
                TemplatePiece piece = extractNextPiece(templatePixmap, x, y);
                x = piece.getPosition().x + piece.getWidth();
                if (tallestPieceInRow == null
                        || piece.getHeight() > tallestPieceInRow.getHeight()) {
                    tallestPieceInRow = piece;
                }
                this.templatePieces.add(piece);
            }
            x = 0;
            y = tallestPieceInRow.getPosition().y + tallestPieceInRow.getHeight();
        }
    }


    private TemplatePiece extractNextPiece(Pixmap templatePixmap, int startX, int startY) {
        PixelSearcher pixelSearcher = new PixelSearcher(templatePixmap, width, height);

        GridPoint2 topLeftCorner = pixelSearcher.findNearestPixel(false, startX, startY);

        return pixelSearcher.floodFillPiece(topLeftCorner.x, topLeftCorner.y);
        //pixelSearcher.borderTracePiece(topLeftCorner.x, topLeftCorner.y);
    }
}
