package com.github.eoinf.jiggen.PuzzleExtractor.Decoder;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.List;

import static com.github.eoinf.jiggen.utils.PixmapUtils.preparePixmap;

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

    private List<DecodedPiece> decodedPieces;
    public List<DecodedPiece> getDecodedPieces() {
        return decodedPieces;
    }

    public Pixmap getTemplatePixmap() {
        return templatePixmap;
    }

    public DecodedTemplate(Texture texture) {
        this(preparePixmap(texture));
    }

    public DecodedTemplate(Pixmap templatePixmap) {
        this.templatePixmap = templatePixmap;
        decodedPieces = new ArrayList<>();

        width = templatePixmap.getWidth();
        height = templatePixmap.getHeight();
        int x = 0, y = 0;

        while(y < height) {
            DecodedPiece tallestPieceInRow = null;

            while(x < width) {
                DecodedPiece piece = extractNextPiece(templatePixmap, x, y);
                x = piece.getPosition().x + piece.getWidth();
                if (tallestPieceInRow == null
                        || piece.getHeight() > tallestPieceInRow.getHeight()) {
                    tallestPieceInRow = piece;
                }
                this.decodedPieces.add(piece);
            }
            x = 0;
            y = tallestPieceInRow.getPosition().y + tallestPieceInRow.getHeight();
        }
    }


    private DecodedPiece extractNextPiece(Pixmap templatePixmap, int startX, int startY) {
        PixelSearcher pixelSearcher = new PixelSearcher(templatePixmap, width, height);

        GridPoint2 topLeftCorner = pixelSearcher.findNearestPixel(false, startX, startY);

        return pixelSearcher.floodFillPiece(topLeftCorner.x, topLeftCorner.y);
    }
}
