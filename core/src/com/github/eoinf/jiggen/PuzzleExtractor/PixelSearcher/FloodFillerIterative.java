package com.github.eoinf.jiggen.PuzzleExtractor.PixelSearcher;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.GridPoint2;
import com.github.eoinf.jiggen.PuzzleExtractor.TemplatePiece;
import com.github.eoinf.jiggen.PuzzleExtractor.TemplatePieceFF;
import com.github.eoinf.jiggen.utils;

import java.util.HashSet;
import java.util.Set;

public class FloodFillerIterative extends PixelSearcher {

    // Represents each of the pixels traversed already
    private boolean[][] pixelsTraversed;

    public FloodFillerIterative(Pixmap templatePixmap, int width, int height) {
        super(templatePixmap, width, height);
    }

    @Override
    void initSearchState(int startX, int startY) {
        super.initSearchState(startX, startY);

        pixelsTraversed = new boolean[height + 2][];
        // Map out the implicit border of the values outside the image
        for (int y = 0; y < pixelsTraversed.length; y++) {
            pixelsTraversed[y] = new boolean[width + 2];
            pixelsTraversed[y][0] = true;
            pixelsTraversed[y][width + 1] = true;
        }
        for (int x = 0; x < pixelsTraversed[0].length; x++) {
            pixelsTraversed[0][x] = true;
            pixelsTraversed[height + 1][x] = true;
        }
    }

    /**
     * Map out the interior of the puzzle piece using a flood fill algorithm
     * @return Template piece
     */
    public TemplatePiece extractPiece(int startX, int startY) {
        initSearchState(startX, startY);
        Set<GridPoint2> paths = new HashSet<>();
        paths.add(new GridPoint2(startX, startY));
        fillNext(paths);

        adjustBorders();
        return new TemplatePieceFF(pixelsTraversed, new GridPoint2(minX, minY), maxX, maxY);
    }

    private void fillNext(Set<GridPoint2> currentPaths) {
        Set<GridPoint2> newPaths;
        do {
            newPaths = new HashSet<>();
            for (GridPoint2 path : currentPaths) {
                if (pixelsTraversed[path.y + 1][path.x + 1]) {
                    continue;
                }
                pixelsTraversed[path.y + 1][path.x + 1] = true;

                if (!utils.isPixelDark(templatePixmap, path.x, path.y, width, height)) {
                    newPaths.add(new GridPoint2(path.x + 1, path.y));
                    newPaths.add(new GridPoint2(path.x - 1, path.y));
                    newPaths.add(new GridPoint2(path.x, path.y + 1));
                    newPaths.add(new GridPoint2(path.x, path.y - 1));
                } else {
                    if (path.x > maxX) {
                        maxX = path.x;
                        maxXCoord = new GridPoint2(path.x, path.y);
                    } else if (path.x < minX) {
                        minX = path.x;
                    }
                    if (path.y > maxY) {
                        maxY = path.y;
                        maxYCoord = new GridPoint2(path.x, path.y);
                    } else if (path.y < minY) {
                        minY = path.y;
                    }
                }
            }
            currentPaths = new HashSet<>(newPaths);
        }
        while(currentPaths.size() != 0);
    }
}
