package com.github.eoinf.jiggen.PuzzleExtractor;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.github.eoinf.jiggen.PuzzleExtractor.PixelSearcher.BorderTracer;
import com.github.eoinf.jiggen.PuzzleExtractor.PixelSearcher.FloodFiller;
import com.github.eoinf.jiggen.PuzzleExtractor.PixelSearcher.FloodFillerIterative;
import com.github.eoinf.jiggen.PuzzleExtractor.PixelSearcher.PixelSearcher;
import com.github.eoinf.jiggen.PuzzleExtractor.PixelSearcher.TracingStrategy;
import com.github.eoinf.jiggen.utils;

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

    public DecodedTemplate(Texture template, TracingStrategy strategy) {
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
                TemplatePiece piece = extractNextPiece(strategy, templatePixmap, x, y);
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


    private TemplatePiece extractNextPiece(TracingStrategy strategy, Pixmap templatePixmap, int startX, int startY) {
        PixelSearcher pixelSearcher;
        switch (strategy) {
            case BorderTrace:
                pixelSearcher = new BorderTracer(templatePixmap, width, height);
                break;
            case FloodFillIterative:
                pixelSearcher = new FloodFillerIterative(templatePixmap, width, height);
                break;
            default: // Default is TracingStrategy.FloodFill
                pixelSearcher = new FloodFiller(templatePixmap, width, height);
                break;
        }

        GridPoint2 topLeftCorner = utils.findNearestPixel(templatePixmap, width, height, false, startX, startY);

        return pixelSearcher.extractPiece(topLeftCorner.x, topLeftCorner.y);
    }
}
