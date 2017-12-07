package com.github.eoinf.jiggen.PuzzleExtractor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.github.eoinf.jiggen.utils;

public abstract class PuzzleFactory {

    public static PuzzleGraph generatePuzzle(DecodedTemplate decodedTemplate, Pixmap backgroundPixmap) {
        PuzzleGraph puzzleGraph = new PuzzleGraph(decodedTemplate.getWidth(), decodedTemplate.getHeight());

        for (TemplatePiece piece: decodedTemplate.getTemplatePieces()) {
            puzzleGraph.addVertex(
                    generatePuzzlePiece(decodedTemplate.getTemplatePixmap(), backgroundPixmap,
                            decodedTemplate.getWidth(), decodedTemplate.getHeight(), piece)
            );
        }

        // Flip all of the pieces vertically, because the y axis
        // is inverted when working with pixmaps
        puzzleGraph.flipPieces();

        return puzzleGraph;
    }

    private static PuzzlePiece generatePuzzlePiece(Pixmap templatePixmap, Pixmap bgPixmap, int bgWidth, int bgHeight,
                                                   TemplatePiece templatePiece) {

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
                if (templatePiece.isTraversed(templateX + x + 1, templateY + y + 1)) {

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
}
