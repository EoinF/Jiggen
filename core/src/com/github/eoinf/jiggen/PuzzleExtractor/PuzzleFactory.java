package com.github.eoinf.jiggen.PuzzleExtractor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.GridPoint2;
import com.github.eoinf.jiggen.PuzzleExtractor.PixelSearcher.BorderTracer;
import com.github.eoinf.jiggen.PuzzleExtractor.PixelSearcher.TracingStrategy;
import com.github.eoinf.jiggen.utils;

public abstract class PuzzleFactory {

    public static PuzzleGraph generatePuzzle(TracingStrategy strategy, DecodedTemplate decodedTemplate, Pixmap backgroundPixmap) {
        PuzzleGraph puzzleGraph = new PuzzleGraph(decodedTemplate.getWidth(), decodedTemplate.getHeight());

        for (TemplatePiece piece: decodedTemplate.getTemplatePieces()) {
            PuzzlePiece puzzlePiece;
            switch(strategy) {
                case BorderTrace:
                    puzzlePiece = generateWithBorderOutlinePieces(decodedTemplate.getTemplatePixmap(), backgroundPixmap,
                            decodedTemplate.getWidth(), decodedTemplate.getHeight(), (TemplatePieceBT)piece);
                    break;
                default: // Default is TracingStrategy.FloodFill
                    puzzlePiece = generateWithFilledPieces(decodedTemplate.getTemplatePixmap(), backgroundPixmap,
                            decodedTemplate.getWidth(), decodedTemplate.getHeight(), (TemplatePieceFF)piece);
                    break;
            }
            puzzleGraph.addVertex(
                    puzzlePiece
            );
        }

        // Flip all of the pieces vertically, because the y axis
        // is inverted when working with pixmaps
        puzzleGraph.flipPieces();

        return puzzleGraph;
    }


    private static PuzzlePiece generateWithFilledPieces(Pixmap templatePixmap, Pixmap bgPixmap, int bgWidth, int bgHeight,
                                                         TemplatePieceFF templatePiece) {

        int templateX = templatePiece.getPosition().x;
        int templateY = templatePiece.getPosition().y;

        Pixmap newPixMap = new Pixmap(templatePiece.getWidth(), templatePiece.getHeight(), Pixmap.Format.RGBA8888);
        newPixMap.setColor(Color.CLEAR);
        newPixMap.fill();

        for (int x = 0; x < templatePiece.getWidth(); x++) {
            for (int y = 0; y < templatePiece.getHeight(); y++) {
                boolean isDark =
                        utils.isPixelDark(templatePixmap, x + templateX, y + templateY, bgWidth, bgHeight);
                // If we haven't traversed this ignore it
                if (templatePiece.getPixel(templateX + x + 1, templateY + y + 1)) {

                    Color background = isDark ?
                            Color.BLACK.cpy() :
                            utils.getPixelColour(bgPixmap, x + templateX, y + templateY);
                    // We have traversed this pixel
                    newPixMap.drawPixel(x, y, Color.rgba8888(background));
                }
            }
        }

        return new PuzzlePiece(templateX, templateY, templatePiece.getWidth(), templatePiece.getHeight(), newPixMap);
    }

    private static PuzzlePiece generateWithBorderOutlinePieces(
            Pixmap templatePixmap, Pixmap bgPixmap, int bgWidth, int bgHeight,
            TemplatePieceBT templatePiece) {

        int templatePosX = templatePiece.getPosition().x;
        int templatePosY = templatePiece.getPosition().y;

        Pixmap newPixMap = new Pixmap(templatePiece.getWidth(), templatePiece.getHeight(), Pixmap.Format.RGBA8888);
        newPixMap.setColor(Color.CLEAR);
        newPixMap.fill();

        for (int x = 0; x < templatePiece.getWidth(); x++) {
            int templateX = templatePosX + x;
            for (int y = 0; y < templatePiece.getHeight(); y++) {
                int templateY = templatePosY + y;

                boolean isDark =
                        utils.isPixelDark(templatePixmap, templateX, templateY, bgWidth, bgHeight);
                Color background = isDark ?
                        Color.BLACK.cpy() :
                        utils.getPixelColour(bgPixmap, templateX, templateY);

                // If this is a part of the border always draw it
                if (templatePiece.getPixel(templateX + 1, templateY + 1) != null) {
                    newPixMap.drawPixel(x, y, Color.rgba8888(Color.BLACK.cpy()));
                } else {
                    BorderTracer.BorderDirection directionToInside = getBorderFacingInward(templatePiece, templateX, templateY);
                    if (directionToInside != null) {
                        templatePiece.setPixel(templateX + 1, templateY + 1, BorderTracer.BorderDirection.INNER_PIECE);
                        newPixMap.drawPixel(x, y, Color.rgba8888(background));
                    }
                }
            }
        }

        return new PuzzlePiece(templatePosX, templatePosY, templatePiece.getWidth(), templatePiece.getHeight(), newPixMap);
    }

    private static BorderTracer.BorderDirection getBorderFacingInward(
            TemplatePieceBT templatePiece, int templateX, int templateY) {
        // Check if there's a border to the left
        BorderTracer.BorderDirection directionToInside = templatePiece.getPixel(templateX, templateY + 1);
        if (directionToInside != null) {
            if (directionToInside == BorderTracer.BorderDirection.RIGHT
                    || directionToInside == BorderTracer.BorderDirection.INNER_PIECE)
            return directionToInside;
        }
        // Check if there's a border above
        directionToInside = templatePiece.getPixel(templateX + 1, templateY);
        if (directionToInside != null) {
            if (directionToInside == BorderTracer.BorderDirection.DOWN
                    || directionToInside == BorderTracer.BorderDirection.INNER_PIECE)
                return directionToInside;
        }
        // Check if there's a border below
        directionToInside = templatePiece.getPixel(templateX + 1, templateY + 2);
        if (directionToInside != null) {
            if (directionToInside == BorderTracer.BorderDirection.UP
                    || directionToInside == BorderTracer.BorderDirection.INNER_PIECE)
                return directionToInside;
        }
        return null;
    }
}
