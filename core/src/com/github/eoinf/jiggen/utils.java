package com.github.eoinf.jiggen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class utils {

    private static final float FLOAT_ERROR = 0.01f;
    private static final float BRIGHTNESS_THRESHOLD = 0.80f;
    private static final Color buffer = Color.WHITE.cpy();

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

        GridPoint2 srcBounds = new GridPoint2(texture.getWidth(), texture.getHeight());

        Vector2 ratio = new Vector2((float)srcBounds.x / dstBounds.x, (float)srcBounds.y / dstBounds.y);

        assert ratio.x > 0;
        assert ratio.y > 0;

        for (int x = 0; x < dstBounds.x; x++) {
            for (int y = 0; y < dstBounds.y; y++) {
                Rectangle tmpRect = new Rectangle(x * ratio.x, y * ratio.y, ratio.x, ratio.y);

                Color newPixelColour = findColorInRectangle(srcMap, tmpRect);
                Color fixedColour = getBrightness(newPixelColour) > BRIGHTNESS_THRESHOLD ? Color.WHITE : Color.BLACK;
                dstMap.drawPixel(x, y, Color.rgba8888(fixedColour));
            }
        }

        return new Texture(dstMap);
    }

    /**
     * Get the average weighted colour of all the pixels within the given rectangle of the templatePixmap
     * @param rectangle
     * @return
     */
    private static Color findColorInRectangle(Pixmap srcMap, Rectangle rectangle) {
        // Get the truncated coordinates so we can find the colour of the pixel
        GridPoint2 topLeftPixel = new GridPoint2((int)rectangle.x, (int)rectangle.y);
        GridPoint2 bottomRightPixel = new GridPoint2(
                (int)Math.round(Math.ceil((double)(rectangle.x + rectangle.width))),
                (int)Math.round(Math.ceil((double)(rectangle.y + rectangle.height)))
        );

        float[] totalColour = new float[4];

        float i = rectangle.x, j = rectangle.y;
        float iMax = rectangle.x + rectangle.width, jMax = rectangle.y + rectangle.height;
        while (i + FLOAT_ERROR < iMax) {
            float pctX = calculateWeight(i, iMax);
            while (j + FLOAT_ERROR < jMax) {
                float pctY = calculateWeight(j, jMax);
                Color.rgba8888ToColor(buffer, srcMap.getPixel((int)i, (int)j));

                buffer.mul(pctX * pctY);
                totalColour[0] += buffer.r;
                totalColour[1] += buffer.g;
                totalColour[2] += buffer.b;
                totalColour[3] += buffer.a;
                j += pctY;
            }
            j = rectangle.y;
            i += pctX;
        }

        // Divide the total colour by the total area to get the average colour
        float area = rectangle.width * rectangle.height;
        totalColour[0] /= area;
        totalColour[1] /= area;
        totalColour[2] /= area;
        totalColour[3] /= area;

        return new Color(totalColour[0], totalColour[1], totalColour[2], totalColour[3]);
    }

    private static float calculateWeight(float num, float max) {
        int current = (int)num;
        int next = current + 1;

        if (next < max) {
            return next - num;
        } else {
            return max - num;
        }
    }

    public static float getBrightness(Color colour) {
        return ((colour.r + colour.g + colour.b) / 3) * colour.a;
    }
}
