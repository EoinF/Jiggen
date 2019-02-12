package com.github.eoinf.jiggen.PuzzleExtractor.Puzzle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.github.eoinf.jiggen.PuzzleExtractor.Decoder.DecodedPiece;
import com.github.eoinf.jiggen.PuzzleExtractor.Decoder.DecodedTemplate;
import com.github.eoinf.jiggen.webapp.screens.models.PuzzleGraphTemplate;
import com.github.eoinf.jiggen.webapp.screens.models.PuzzlePieceTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.github.eoinf.jiggen.utils.PixmapUtils.combinePixmaps;
import static com.github.eoinf.jiggen.utils.PixmapUtils.combineTextures;
import static com.github.eoinf.jiggen.utils.PixmapUtils.isPixelDark;
import static com.github.eoinf.jiggen.utils.PixmapUtils.overlaps;
import static com.github.eoinf.jiggen.utils.PixmapUtils.stretchPixmap;
import static com.github.eoinf.jiggen.utils.PixmapUtils.stretchTexture;

public abstract class PuzzleFactory {

    public static PuzzleGraphTemplate generatePixmapPuzzleFromTemplate(DecodedTemplate decodedTemplate) {
        return generatePuzzleFromTemplate(decodedTemplate, new Function<PuzzlePieceTemplate<Pixmap>, PuzzlePieceTemplate>() {
            @Override
            public PuzzlePieceTemplate apply(PuzzlePieceTemplate<Pixmap> p) {
                return p;
            }
        });
    }

    public static PuzzleGraphTemplate generateTexturePuzzleFromTemplate(DecodedTemplate decodedTemplate) {
        return generatePuzzleFromTemplate(decodedTemplate, new Function<PuzzlePieceTemplate<Pixmap>, PuzzlePieceTemplate>() {
            @Override
            public PuzzlePieceTemplate apply(PuzzlePieceTemplate<Pixmap> p) {
                Pixmap pixmap = p.getData();

                return new PuzzlePieceTemplate<>(
                        p.x(), p.y(),
                        p.getWidth(), p.getHeight(), new TextureRegion(new Texture(pixmap)));
            }
        });
    }

    private static PuzzleGraphTemplate generatePuzzleFromTemplate(DecodedTemplate decodedTemplate, Function<PuzzlePieceTemplate<Pixmap>,
            PuzzlePieceTemplate> pieceTransformer) {
        PuzzleGraphTemplate puzzleGraph = new PuzzleGraphTemplate(decodedTemplate.getWidth(), decodedTemplate.getHeight());

        for (DecodedPiece piece: decodedTemplate.getDecodedPieces()) {
            PuzzlePieceTemplate<Pixmap> generatedPiece = generatePiece(decodedTemplate.getTemplatePixmap(),
                    decodedTemplate.getWidth(), decodedTemplate.getHeight(), piece);

            puzzleGraph.addVertex(
                pieceTransformer.apply(generatedPiece)
            );
        }

        // Gather a list of vertices we've already checked
        // so we don't end up adding edges ab and ba since they are identical
        List<Integer> calculatedVertices = new ArrayList<>();

        for (int p: puzzleGraph.getVertices().keySet()) {
            PuzzlePieceTemplate pieceP = puzzleGraph.getVertex(p);

            for (int q: puzzleGraph.getVertices().keySet()) {
                if (p != q && !calculatedVertices.contains(q)) {
                    PuzzlePieceTemplate pieceQ = puzzleGraph.getVertex(q);
                    if (overlaps(pieceP.getPositionData(), pieceQ.getPositionData())) {
                        puzzleGraph.addEdge(p, q);
                    }
                }
            }
            calculatedVertices.add(p);
        }

        return puzzleGraph;
    }

    /**
     * Takes in a blank template puzzle, stretches the pieces and combines it with a background texture
     * @param templateGraph The puzzle with blank pieces
     * @param backgroundTexture The texture to overlay onto the pieces
     * @return The finished puzzle with the incorporated background picture
     */
    public static PuzzleGraphTemplate generateFinishedPuzzle(PuzzleGraphTemplate templateGraph, Texture backgroundTexture) {
        PuzzleGraphTemplate finishedPuzzle = new PuzzleGraphTemplate(backgroundTexture.getWidth(), backgroundTexture.getHeight());
        float ratioX = (float)backgroundTexture.getWidth() / (float)templateGraph.getWidth();
        float ratioY = (float)backgroundTexture.getHeight() / (float)templateGraph.getHeight();


        TextureData dstData = backgroundTexture.getTextureData();
        if (!dstData.isPrepared()) {
            dstData.prepare();
        }
        Pixmap backgroundPixmap = dstData.consumePixmap();

        for (PuzzlePieceTemplate v : templateGraph.getVertices().values()) {
            int scaledWidth = (int) (ratioX * v.getWidth());
            int scaledHeight = (int) (ratioY * v.getHeight());

            PuzzlePieceTemplate generatedPiece;
            if (v.getData() instanceof Pixmap) {
                generatedPiece =
                        overlayBackgroundOnPixmapPiece((PuzzlePieceTemplate<Pixmap>) v, backgroundPixmap,
                                scaledWidth, scaledHeight, ratioX, ratioY);
            } else {
                generatedPiece =
                        overlayBackgroundOnTextureRegionPiece((PuzzlePieceTemplate<TextureRegion>) v, backgroundPixmap,
                                scaledWidth, scaledHeight, ratioX, ratioY);
            }
            finishedPuzzle.addVertex(generatedPiece);
        }

        backgroundPixmap.dispose();
        return finishedPuzzle;
    }

    private static PuzzlePieceTemplate<Pixmap> overlayBackgroundOnPixmapPiece(PuzzlePieceTemplate<Pixmap> piece,
                                                                              Pixmap backgroundPixmap,
                                                                              int scaledWidth, int scaledHeight,
                                                                              float ratioX, float ratioY) {
        Pixmap templateTexture = piece.getData();
        templateTexture = stretchPixmap(templateTexture, new GridPoint2(scaledWidth, scaledHeight));

        GridPoint2 scaledPosition = new GridPoint2((int) (piece.x() * ratioX),
                (int) (piece.y() * ratioY)
        );
        templateTexture = combinePixmaps(templateTexture, backgroundPixmap,
                scaledPosition.x,
                backgroundPixmap.getHeight() - (scaledPosition.y + scaledHeight));

        return new PuzzlePieceTemplate<>(scaledPosition.x, scaledPosition.y,
                        scaledWidth, scaledHeight,
                        templateTexture);
    }

    private static PuzzlePieceTemplate<TextureRegion> overlayBackgroundOnTextureRegionPiece(PuzzlePieceTemplate<TextureRegion> piece,
                                                                                            Pixmap backgroundPixmap,
                                                                                            int scaledWidth, int scaledHeight,
                                                                                            float ratioX, float ratioY) {
        TextureRegion templateTexture = piece.getData();
        templateTexture = stretchTexture(templateTexture, new GridPoint2(scaledWidth, scaledHeight));

        GridPoint2 scaledPosition = new GridPoint2((int) (piece.x() * ratioX),
                (int) (piece.y() * ratioY)
        );
        templateTexture = combineTextures(templateTexture, backgroundPixmap,
                scaledPosition.x,
                backgroundPixmap.getHeight() - (scaledPosition.y + scaledHeight));

        return new PuzzlePieceTemplate<>(scaledPosition.x, scaledPosition.y,
                scaledWidth, scaledHeight,
                templateTexture);
    }

    private static PuzzlePieceTemplate<Pixmap> generatePiece(Pixmap templatePixmap, int bgWidth, int bgHeight,
                                                             DecodedPiece decodedPiece) {

        int templateX = decodedPiece.getPosition().x;
        int templateY = decodedPiece.getPosition().y;

        Pixmap newPixMap = new Pixmap(decodedPiece.getWidth(), decodedPiece.getHeight(), Pixmap.Format.RGBA8888);
        newPixMap.setColor(Color.CLEAR);
        newPixMap.fill();

        for (int x = 0; x < decodedPiece.getWidth(); x++) {
            for (int y = 0; y < decodedPiece.getHeight(); y++) {
                boolean isDark =
                        isPixelDark(templatePixmap, x + templateX, y + templateY, bgWidth, bgHeight);
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

        return new PuzzlePieceTemplate<>(templateX, bgHeight - templateY - decodedPiece.getHeight(),
                decodedPiece.getWidth(), decodedPiece.getHeight(), newPixMap);
    }
}