package com.github.eoinf.jiggen.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.IntRectangle;

import java.util.ArrayList;
import java.util.List;

public abstract class PixmapUtils {
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

        dstMap = stretchPixmap(srcMap, new GridPoint2(region.getRegionX(), region.getRegionY()),
                new GridPoint2(region.getRegionWidth(), region.getRegionHeight()), dstMap, dstBounds);

        return new TextureRegion(new Texture(dstMap), dstBounds.x, dstBounds.y);
    }

    public static Pixmap stretchPixmap(Pixmap srcMap, GridPoint2 dstBounds) {
        Pixmap dstMap = new Pixmap(dstBounds.x, dstBounds.y, Pixmap.Format.RGBA8888);
        stretchPixmap(srcMap, new GridPoint2(0, 0), new GridPoint2(0, 0), dstMap, dstBounds);
        return dstMap;
    }

    public static Pixmap stretchPixmap(Pixmap srcMap, GridPoint2 srcOffset, GridPoint2 srcBounds,
                                       Pixmap dstMap, GridPoint2 dstBounds) {
        dstMap.drawPixmap(srcMap,
                srcOffset.x, srcOffset.y,
                srcBounds.x, srcBounds.y,
                0, 0,
                dstBounds.x, dstBounds.y
        );
        return dstMap;
    }

    public static TextureRegion combineTextures(TextureRegion region, Pixmap srcMap, int offsetSrcX, int offsetSrcY) {
        TextureData dstData = region.getTexture().getTextureData();
        if (!dstData.isPrepared()) {
            dstData.prepare();
        }

        Pixmap dstMap = dstData.consumePixmap();
        dstMap = combinePixmaps(dstMap, srcMap, offsetSrcX, offsetSrcY,
                new GridPoint2(region.getRegionX(), region.getRegionY()),
                new GridPoint2(region.getRegionWidth(), region.getRegionHeight()));

        TextureRegion result = new TextureRegion(new Texture(dstMap),
                region.getRegionX(), region.getRegionY(),
                region.getRegionWidth(), region.getRegionHeight()
        );
        dstMap.dispose();
        return result;
    }

    public static Pixmap combinePixmaps(Pixmap dstMap, Pixmap srcMap, int offsetSrcX, int offsetSrcY) {
        return combinePixmaps(dstMap, srcMap, offsetSrcX, offsetSrcY, new GridPoint2(0, 0),
                new GridPoint2(dstMap.getWidth(), dstMap.getHeight()));
    }

    public static Pixmap combinePixmaps(Pixmap dstMap, Pixmap srcMap, int offsetSrcX, int offsetSrcY,
                                                   GridPoint2 dstOffset, GridPoint2 dstBounds) {

        for (int x = 0; x < dstBounds.x; x++) {
            for (int y = 0; y < dstBounds.y; y++) {
                Color colour1 = getPixelColour(srcMap,
                        x + offsetSrcX,
                        y + offsetSrcY
                );
                Color colour2 = getPixelColour(dstMap,
                        x + dstOffset.x,
                        y + dstOffset.y
                );

                Color blendedColour = colour1.mul(colour2);
                int c = Color.rgba8888(blendedColour);
                dstMap.drawPixel(x, y , c);
            }
        }

        return dstMap;
    }

    public static Pixmap preparePixmap(Texture texture) {
        TextureData textureData = texture.getTextureData();
        if (!textureData.isPrepared()) {
            textureData.prepare();
        }
        return textureData.consumePixmap();
    }

    private static float getBrightness(Color colour) {
        return ((colour.r + colour.g + colour.b) / 3) * colour.a;
    }

    public static boolean isPixelDark(Pixmap pixmap, int currentX, int currentY, int width, int height) {
        return currentX >= width || currentY >= height || currentX < 0 || currentY < 0 ||
                getBrightness(getPixelColour(pixmap, currentX, currentY)) < BRIGHTNESS_THRESHOLD;
    }

    private static Color getPixelColour(Pixmap pixmap, int currentX, int currentY) {
        Color.rgba8888ToColor(buffer, pixmap.getPixel(currentX, currentY));
        return buffer.cpy();
    }

    public static FileHandle getRandomTemplate() {
        List<FileHandle> templates = getTemplateFiles();

        if (templates.isEmpty()) {
            return Gdx.files.internal("templates/5x7puzzletemplate.jpg");
        } else {
            return templates.get(0);
        }
    }

    public static List<FileHandle> getTemplateFiles() {
        FileHandle[] files = Gdx.files.internal("templates").list();

        List<FileHandle> templateFiles = new ArrayList<>();
        for (FileHandle f: files) {
            if (!f.isDirectory() && f.name().endsWith(".jpg")) {
                templateFiles.add(f);
            }
        }
        return templateFiles;
    }

    public static FileHandle getRandomBackground() {
        List<FileHandle> backgrounds = getBackgroundFiles();

        if (backgrounds.isEmpty()) {
            return Gdx.files.internal("backgrounds/pizza.jpg");
        } else {
            return backgrounds.get(0);
        }

    }

    public static List<FileHandle> getBackgroundFiles() {
        FileHandle[] files = Gdx.files.internal("backgrounds").list();

        List<FileHandle> backgroundFiles = new ArrayList<>();
        for (FileHandle f: files) {
            if (!f.isDirectory() && (f.name().endsWith(".jpg") || f.name().endsWith(".png"))) {
                backgroundFiles.add(f);
            }
        }
        return backgroundFiles;
    }

    public static void setBackgroundColour(Table table, Color colour) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
        pixmap.setColor(colour);
        pixmap.fill();
        Drawable background = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        table.setBackground(background);
        pixmap.dispose();
    }

    public static Vector2 getMinimumScaleToFixAspectRatio(int a, int b, int c, int d) {
        //
        // The variables s1 and s2 are the scales required to transform the aspect ratio
        // The variables a and b are the width and height of the current texture
        // The variables c and d are the width and height of the target texture
        // s1 * a(x) + s2 * b(y) = c(x) + d(y)
        //
        // ad >= bc  ->  s1 = 1
        // bc >= ad  ->  s2 = 1
        //
        //
        float s1; float s2;
        float ad = a * d;
        float bc = b * c;

        if (ad >= bc) {
            s1 = 1;
            s2 = ad / bc;
        } else {
            s2 = 1;
            s1 = bc / ad;
        }

        return new Vector2(s1, s2);
    }

    public static boolean overlaps(IntRectangle rect1, IntRectangle rect2) {
        return (rect1.x + rect1.width >= rect2.x
                && rect1.x <= rect2.x + rect2.width
                && rect1.y + rect1.height >= rect2.y
                && rect1.y <= rect2.y + rect2.height);
    }

    public static Pixmap copyPixmap(Pixmap srcPixmap) {
        Pixmap dstPixmap = new Pixmap(srcPixmap.getWidth(), srcPixmap.getHeight(), Pixmap.Format.RGBA8888);
        dstPixmap.drawPixmap(srcPixmap, 0, 0);
        return dstPixmap;
    }
}
