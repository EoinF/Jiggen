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
        int x = 0;

        // Record the y coordinate for each of the previous pieces above the current row
        int[] topLineCoordinates = new int[width];

        int amount = 0;

        System.out.println("Starting decoding...");
        while(!isBottomLine(topLineCoordinates) && amount < 20) {
            amount++;

            while(x < width) {
                DecodedPiece piece = extractNextPiece(templatePixmap, x, topLineCoordinates[x] + 1);

                this.decodedPieces.add(piece);
                x = nextValidXPosition(piece.getPosition().x + piece.getWidth(), topLineCoordinates);
                this.updateTopLineCoordinates(topLineCoordinates, piece);
            }
            x = nextValidXPosition(0, topLineCoordinates);
        }
        System.out.println("Done decoding");
        System.out.println("Total pieces decoded: " + decodedPieces.size());
    }

    private int nextValidXPosition(int i, int[] topLineCoordinates) {
        int x = i;
        while(x < topLineCoordinates.length && topLineCoordinates[x] >= height) {
            x++;
        }
        return x;
    }

    private boolean isBottomLine(int[] topLineCoordinates) {
        for (int coordinate: topLineCoordinates) {
            if (coordinate < height) {
                return false;
            }
        }
        return true;
    }

    private void updateTopLineCoordinates(int[] coordinates, DecodedPiece newPiece) {
        int minX = newPiece.getPosition().x;
        int maxX = minX + newPiece.getWidth();
        for (int x = minX; x < maxX; x++) {
            int minY = Math.max(newPiece.getPosition().y, coordinates[x]);
            int maxY = newPiece.getPosition().y + newPiece.getHeight();
            for (int y = maxY; y >= minY; y--) {
                if (newPiece.isTraversed(x, y)) {
                    coordinates[x] = y + 1;
                    break;
                }
            }
        }
    }


    private DecodedPiece extractNextPiece(Pixmap templatePixmap, int startX, int startY) {
        PixelSearcher pixelSearcher = new PixelSearcher(templatePixmap, width, height);

        GridPoint2 topLeftCorner = pixelSearcher.findNearestPixel(false, startX, startY);

        return pixelSearcher.floodFillPiece(topLeftCorner.x, topLeftCorner.y);
    }
}
