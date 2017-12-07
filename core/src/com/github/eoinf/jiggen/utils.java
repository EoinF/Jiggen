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

        dstMap.drawPixmap(srcMap, 0, 0, data.getWidth(), data.getHeight(), 0, 0, dstBounds.x, dstBounds.y);

        return new Texture(dstMap);
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
