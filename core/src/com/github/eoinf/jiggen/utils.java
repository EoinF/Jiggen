package com.github.eoinf.jiggen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class utils {
    public static final float BRIGHTNESS_THRESHOLD = 0.95f;

    private static Color buffer;

    static {
        buffer = Color.BLACK.cpy();
    }

    /**
     *
     * @param texture The texture we want to stretch
     * @param dstBounds The width and height of the new stretched texture
     * @return
     */
    public static Texture stretchTexture(Texture texture, GridPoint2 dstBounds) {
        TextureData data = texture.getTextureData();
        if (!data.isPrepared()) {
            data.prepare();
        }

        Pixmap srcMap = data.consumePixmap();
        Pixmap dstMap = new Pixmap(dstBounds.x, dstBounds.y, Pixmap.Format.RGBA8888);

        dstMap.drawPixmap(srcMap, 0, 0, data.getWidth(), data.getHeight(), 0, 0, dstBounds.x,
                dstBounds.y);

        return new Texture(dstMap);
    }

    public static GridPoint2 findNearestPixel(Pixmap templatePixmap, int width, int height, boolean targetValue,
                                       int currentX, int currentY) {
        GridPoint2[] currentPaths = new GridPoint2[] {new GridPoint2(currentX, currentY)};

        return findNearestPixel(templatePixmap, width, height, targetValue, currentPaths);
    }

    private static GridPoint2 findNearestPixel(Pixmap templatePixmap, int width, int height, boolean targetValue,
                                        GridPoint2[] currentPaths) {
        GridPoint2[] newPaths = new GridPoint2[currentPaths.length * 2];

        for (int p = 0; p < currentPaths.length; p++) {
            int x = currentPaths[p].x;
            int y = currentPaths[p].y;
            if (targetValue == utils.isPixelDark(templatePixmap, x, y, width, height)) {
                return new GridPoint2(x, y);
            } else {
                newPaths[p * 2] = new GridPoint2(x + 1, y);
                newPaths[p * 2 + 1] = new GridPoint2(x, y + 1);
            }
        }

        return findNearestPixel(templatePixmap, width, height, targetValue, newPaths);
    }

    public static float getBrightness(Color colour) {
        return ((colour.r + colour.g + colour.b) / 3) * colour.a;
    }

    public static boolean isPixelDark(Pixmap pixmap, int currentX, int currentY, int width, int height) {
        return currentX >= width || currentY >= height || currentX < 0 || currentY < 0 ||
                utils.getBrightness(getPixelColour(pixmap, currentX, currentY)) < BRIGHTNESS_THRESHOLD;
    }

    public static Color getPixelColour(Pixmap pixmap, int currentX, int currentY) {
        Color.rgba8888ToColor(buffer, pixmap.getPixel(currentX, currentY));
        return buffer;
    }
}
