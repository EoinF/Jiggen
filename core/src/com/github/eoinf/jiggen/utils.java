package com.github.eoinf.jiggen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleGraphTemplate;

import java.util.ArrayList;
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
                utils.getBrightness(getPixelColour(pixmap, currentX, currentY)) < BRIGHTNESS_THRESHOLD;
    }

    private static Color getPixelColour(Pixmap pixmap, int currentX, int currentY) {
        Color.rgba8888ToColor(buffer, pixmap.getPixel(currentX, currentY));
        return buffer.cpy();
    }

    public static void shuffle(PuzzleGraphTemplate graph, List<Actor> pieces) {
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

    public static List<FileHandle> getBackgroundFiles() {
        FileHandle[] files = Gdx.files.internal("backgrounds/").list();

        List<FileHandle> backgroundFiles = new ArrayList<>();
        for (FileHandle f: files) {
            if (!f.isDirectory() && f.name().endsWith(".jpg")) {
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
}
