package com.github.eoinf.jiggen.PuzzleExtractor.Puzzle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.github.eoinf.jiggen.PuzzleExtractor.Decoder.DecodedPiece;
import com.github.eoinf.jiggen.PuzzleExtractor.Decoder.DecodedTemplate;
import com.github.eoinf.jiggen.utils;

import java.util.function.Function;

public abstract class PuzzleFactory {


    public static PuzzleGraph generatePixmapPuzzleFromTemplate(DecodedTemplate decodedTemplate) {
        return generatePuzzleFromTemplate(decodedTemplate, p -> p);
    }

    public static PuzzleGraph generateTexturePuzzleFromTemplate(DecodedTemplate decodedTemplate) {
        return generatePuzzleFromTemplate(decodedTemplate, p -> {
            Pixmap pixmap = p.getData();

            return new PuzzlePiece<>(
                    p.getPosition().x, p.getPosition().y,
                    p.getWidth(), p.getHeight(), new TextureRegion(new Texture(pixmap)));
        });
    }

    private static PuzzleGraph generatePuzzleFromTemplate(DecodedTemplate decodedTemplate, Function<PuzzlePiece<Pixmap>,
            PuzzlePiece> pieceTransformer) {
        PuzzleGraph puzzleGraph = new PuzzleGraph(decodedTemplate.getWidth(), decodedTemplate.getHeight());

        for (DecodedPiece piece: decodedTemplate.getDecodedPieces()) {
            PuzzlePiece<Pixmap> generatedPiece = generatePiece(decodedTemplate.getTemplatePixmap(),
                    decodedTemplate.getWidth(), decodedTemplate.getHeight(), piece);

            puzzleGraph.addVertex(
                pieceTransformer.apply(generatedPiece)
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

                    PuzzlePiece generatedPiece;
                    if (v.getData() instanceof Pixmap) {
                        generatedPiece =
                                overlayBackgroundOnPixmapPiece((PuzzlePiece<Pixmap>)v, backgroundPixmap,
                                        scaledWidth, scaledHeight, ratioX, ratioY);
                    } else {
                        generatedPiece =
                                overlayBackgroundOnTextureRegionPiece((PuzzlePiece<TextureRegion>)v, backgroundPixmap,
                                        scaledWidth, scaledHeight, ratioX, ratioY);
                    }
                    finishedPuzzle.addVertex(generatedPiece);
                }
        );

        backgroundPixmap.dispose();
        return finishedPuzzle;
    }

    private static PuzzlePiece<Pixmap> overlayBackgroundOnPixmapPiece(PuzzlePiece<Pixmap> piece,
                                                                Pixmap backgroundPixmap,
                                                                int scaledWidth, int scaledHeight,
                                                                float ratioX, float ratioY) {
        Pixmap templateTexture = piece.getData();
        templateTexture = utils.stretchPixmap(templateTexture, new GridPoint2(scaledWidth, scaledHeight));

        GridPoint2 scaledPosition = new GridPoint2((int) (piece.getPosition().x * ratioX),
                (int) (piece.getPosition().y * ratioY)
        );
        templateTexture = utils.combinePixmaps(templateTexture, backgroundPixmap,
                scaledPosition.x,
                backgroundPixmap.getHeight() - (scaledPosition.y + scaledHeight));

        return new PuzzlePiece<>(scaledPosition.x, scaledPosition.y,
                        scaledWidth, scaledHeight,
                        templateTexture);
    }

    private static PuzzlePiece<TextureRegion> overlayBackgroundOnTextureRegionPiece(PuzzlePiece<TextureRegion> piece,
                                                                Pixmap backgroundPixmap,
                                                                int scaledWidth, int scaledHeight,
                                                                float ratioX, float ratioY) {
        TextureRegion templateTexture = piece.getData();
        templateTexture = utils.stretchTexture(templateTexture, new GridPoint2(scaledWidth, scaledHeight));

        GridPoint2 scaledPosition = new GridPoint2((int) (piece.getPosition().x * ratioX),
                (int) (piece.getPosition().y * ratioY)
        );
        templateTexture = utils.combineTextures(templateTexture, backgroundPixmap,
                scaledPosition.x,
                backgroundPixmap.getHeight() - (scaledPosition.y + scaledHeight));

        return new PuzzlePiece<>(scaledPosition.x, scaledPosition.y,
                scaledWidth, scaledHeight,
                templateTexture);
    }

    private static PuzzlePiece<Pixmap> generatePiece(Pixmap templatePixmap, int bgWidth, int bgHeight,
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

        return new PuzzlePiece<Pixmap>(templateX, templateY, decodedPiece.getWidth(), decodedPiece.getHeight(), newPixMap);
    }
}