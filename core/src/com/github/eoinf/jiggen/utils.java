package com.github.eoinf.jiggen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleGraph;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzlePiece;

import java.util.List;
import java.util.Random;

public abstract class utils {
    public static final float BRIGHTNESS_THRESHOLD = 0.85f;

    private static Color buffer;

    static {
        buffer = Color.BLACK.cpy();
    }

    /**
     *
     * @param region The texture region we want to stretch
     * @param dstBounds The width and height of the new stretched texture
     * @return
     */
    public static TextureRegion stretchTexture(TextureRegion region, GridPoint2 dstBounds) {
        Pixmap dstMap = new Pixmap(dstBounds.x, dstBounds.y, Pixmap.Format.RGBA8888);
        return stretchTexture(region, dstBounds, dstMap);
    }

    /**
     *
     * @param region The texture region we want to stretch
     * @param dstBounds The width and height of the new stretched texture
     * @return
     */
    public static TextureRegion stretchTexture(TextureRegion region, GridPoint2 dstBounds, Pixmap dstMap) {
        TextureData data = region.getTexture().getTextureData();
        if (!data.isPrepared()) {
            data.prepare();
        }

        Pixmap srcMap = data.consumePixmap();

        dstMap.drawPixmap(srcMap,
                region.getRegionX(), region.getRegionY(),
                region.getRegionWidth(), region.getRegionHeight(),
                0, 0,
                dstBounds.x, dstBounds.y
        );

        return new TextureRegion(new Texture(dstMap), dstBounds.x, dstBounds.y);
    }

    public static TextureRegion combineTextures(TextureRegion region, Pixmap srcMap, int offsetX, int offsetY) {
        assert region.getRegionWidth() >= offsetX;
        assert region.getRegionHeight() >= offsetY;

        TextureData dstData = region.getTexture().getTextureData();
        if (!dstData.isPrepared()) {
            dstData.prepare();
        }

        Pixmap dstMap = dstData.consumePixmap();

        for (int x = 0; x < region.getRegionWidth(); x++) {
            for (int y = 0; y < region.getRegionHeight(); y++) {
                Color colour1 = getPixelColour(srcMap,
                        x + offsetX,
                        y + offsetY
                );
                Color colour2 = getPixelColour(dstMap,
                        x + region.getRegionX(),
                        y + region.getRegionY()
                );

                Color blendedColour = colour1.mul(colour2);
                int c = Color.rgba8888(blendedColour);
                dstMap.drawPixel(x, y , c);
            }
        }

        TextureRegion result = new TextureRegion(new Texture(dstMap),
                region.getRegionX(), region.getRegionY(),
                region.getRegionWidth(), region.getRegionHeight()
        );

        dstMap.dispose();
        return result;
    }

    private static float getBrightness(Color colour) {
        return ((colour.r + colour.g + colour.b) / 3) * colour.a;
    }

    public static boolean isPixelDark(Pixmap pixmap, int currentX, int currentY, int width, int height) {
        return currentX >= width || currentY >= height || currentX < 0 || currentY < 0 ||
                utils.getBrightness(getPixelColour(pixmap, currentX, currentY)) < BRIGHTNESS_THRESHOLD;
    }

    private static Color getPixelColour(Pixmap pixmap, int currentX, int currentY) {
        Color.rgba8888ToColor(buffer, pixmap.getPixel(currentX, currentY));
        return buffer.cpy();
    }

    public static void flipPieces(PuzzleGraph graph) {
        for (PuzzlePiece piece: graph.getVertices()) {
            int x = piece.getPosition().x;
            int y = graph.getHeight() - piece.getPosition().y - piece.getHeight();
            piece.setPosition(x, y);
        }
    }

    public static void shuffle(PuzzleGraph graph, List<Actor> pieces) {
        Random random = new Random();

        for (Actor piece: pieces) {
            float r1 = random.nextFloat();
            float r2 = random.nextFloat();

            int x = (int)(r1 * graph.getWidth() + graph.getPosition().x);
            int y = (int)(r2 * graph.getHeight() + graph.getPosition().y);
            piece.setPosition(x, y);

            if (r1 + r2 > 1){
                piece.toFront();
            }
        }
    }
}
