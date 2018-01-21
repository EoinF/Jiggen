package com.github.eoinf.jiggen.PuzzleExtractor.Puzzle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.github.eoinf.jiggen.PuzzleExtractor.Decoder.DecodedPiece;
import com.github.eoinf.jiggen.PuzzleExtractor.Decoder.DecodedTemplate;
import com.github.eoinf.jiggen.utils;

public abstract class PuzzleFactory {

    public static PuzzleGraph generateTemplatePuzzle(DecodedTemplate decodedTemplate) {
        PuzzleGraph puzzleGraph = new PuzzleGraph(decodedTemplate.getWidth(), decodedTemplate.getHeight());

        for (DecodedPiece piece: decodedTemplate.getDecodedPieces()) {
            puzzleGraph.addVertex(
                    generatePiece(decodedTemplate.getTemplatePixmap(),
                            decodedTemplate.getWidth(), decodedTemplate.getHeight(), piece)
            );
        }

        utils.flipPieces(puzzleGraph);
        return puzzleGraph;
    }

    /**
     * Takes in a blank template puzzle, stretches the pieces and combines it with a background texture
     * @param templateGraph The puzzle with blank pieces
     * @param backgroundTexture The texture to overlay onto the pieces
     * @return The finished puzzle with the incorporated background picture
     */
    public static PuzzleGraph generateFinishedPuzzle(PuzzleGraph templateGraph, Texture backgroundTexture) {
        PuzzleGraph finishedPuzzle = new PuzzleGraph(backgroundTexture.getWidth(), backgroundTexture.getHeight());
        float ratioX = (float)backgroundTexture.getWidth() / (float)templateGraph.getWidth();
        float ratioY = (float)backgroundTexture.getHeight() / (float)templateGraph.getHeight();


        TextureData dstData = backgroundTexture.getTextureData();
        if (!dstData.isPrepared()) {
            dstData.prepare();
        }
        Pixmap backgroundPixmap = dstData.consumePixmap();

        templateGraph.getVertices().forEach(v -> {
                    int scaledWidth = (int) (ratioX * v.getWidth());
                    int scaledHeight = (int) (ratioY * v.getHeight());

                    TextureRegion templateTexture = v.getTextureRegion();
                    templateTexture = utils.stretchTexture(templateTexture, new GridPoint2(scaledWidth, scaledHeight));

                    GridPoint2 scaledPosition = new GridPoint2((int) (v.getPosition().x * ratioX),
                            (int) (v.getPosition().y * ratioY)
                    );
                    templateTexture = utils.combineTextures(templateTexture, backgroundPixmap,
                            scaledPosition.x, backgroundTexture.getHeight() - scaledPosition.y);
                    finishedPuzzle.addVertex(
                            new PuzzlePiece(scaledPosition.x, scaledPosition.y,
                                    scaledWidth, scaledHeight,
                                    templateTexture)
                    );
                }
        );

        backgroundPixmap.dispose();
        return finishedPuzzle;
    }

    private static PuzzlePiece generatePiece(Pixmap templatePixmap, int bgWidth, int bgHeight,
                                                       DecodedPiece decodedPiece) {

        int templateX = decodedPiece.getPosition().x;
        int templateY = decodedPiece.getPosition().y;

        Pixmap newPixMap = new Pixmap(decodedPiece.getWidth(), decodedPiece.getHeight(), Pixmap.Format.RGBA8888);
        newPixMap.setColor(Color.CLEAR);
        newPixMap.fill();

        for (int x = 0; x < decodedPiece.getWidth(); x++) {
            for (int y = 0; y < decodedPiece.getHeight(); y++) {
                boolean isDark =
                        utils.isPixelDark(templatePixmap, x + templateX, y + templateY, bgWidth, bgHeight);
                // If we haven't traversed this ignore it
                if (decodedPiece.isTraversed(templateX + x + 1, templateY + y + 1)) {

                    Color background = isDark ?
                            Color.BLACK :
                            Color.WHITE;
                    // We have traversed this pixel
                    newPixMap.drawPixel(x, y, Color.rgba8888(background));
                }
            }
        }

        return new PuzzlePiece(templateX, templateY, decodedPiece.getWidth(), decodedPiece.getHeight(),
                new TextureRegion(new Texture(newPixMap)));
    }
}
