package com.github.eoinf.jiggen.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

public class TextureOverlayImage extends Widget {
    private Scaling scaling;
    private int align = Align.center;
    private float imageX, imageY, imageWidth, imageHeight;
    private TextureRegionDrawable drawable;
    private TextureRegionDrawable overlay;

    /**
     * Creates an image with no region or patch, stretched, and aligned center.
     */
    public TextureOverlayImage() {
        this((TextureRegionDrawable) null);
    }


    /**
     * Creates an image stretched, and aligned center.
     *
     * @param region May be null.
     */
    public TextureOverlayImage(TextureRegion region) {
        this(new TextureRegionDrawable(region), Scaling.none, Align.center);
    }

    /**
     * Creates an image stretched, and aligned center.
     */
    public TextureOverlayImage(Texture texture) {
        this(new TextureRegionDrawable(new TextureRegion(texture)));
    }

    /**
     * Creates an image stretched, and aligned center.
     *
     * @param drawable May be null.
     */
    public TextureOverlayImage(TextureRegionDrawable drawable) {
        this(drawable, Scaling.none, Align.center);
    }

    /**
     * Creates an image aligned center.
     *
     * @param drawable May be null.
     */
    public TextureOverlayImage(TextureRegionDrawable drawable, Scaling scaling) {
        this(drawable, scaling, Align.center);
    }

    /**
     * @param drawable May be null.
     */
    public TextureOverlayImage(TextureRegionDrawable drawable, Scaling scaling, int align) {
        setDrawable(drawable);
        this.scaling = scaling;
        this.align = align;
        setSize(getPrefWidth(), getPrefHeight());
    }

    @Override
    public void layout() {
        if (drawable == null) return;

        float regionWidth = drawable.getMinWidth();
        float regionHeight = drawable.getMinHeight();
        float width = getWidth();
        float height = getHeight();

        Vector2 size = scaling.apply(regionWidth, regionHeight, width, height);
        imageWidth = size.x;
        imageHeight = size.y;

        if ((align & Align.left) != 0)
            imageX = 0;
        else if ((align & Align.right) != 0)
            imageX = (int) (width - imageWidth);
        else
            imageX = (int) (width / 2 - imageWidth / 2);

        if ((align & Align.top) != 0)
            imageY = (int) (height - imageHeight);
        else if ((align & Align.bottom) != 0)
            imageY = 0;
        else
            imageY = (int) (height / 2 - imageHeight / 2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        validate();

        PuzzleOverlayBatch pBatch = (PuzzleOverlayBatch) batch;

        Color color = getColor();
        pBatch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        pBatch.setOverlayTextureRegion(overlay.getRegion());

        float x = getX();
        float y = getY();
        float scaleX = getScaleX();
        float scaleY = getScaleY();

        if (drawable != null) {
            float rotation = getRotation();
            pBatch.draw(drawable.getRegion(), x + imageX, y + imageY, getOriginX() - imageX, getOriginY() - imageY,
                    imageWidth, imageHeight, scaleX, scaleY, rotation);

        }
    }

    public void setOverlay(TextureRegionDrawable overlay) {
        overlay.getRegion()
                .getTexture()
                .setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        this.overlay = overlay;
    }

    public TextureRegionDrawable getOverlay() {
        return this.overlay;
    }

    /**
     * @param drawable May be null.
     */
    public void setDrawable(TextureRegionDrawable drawable) {
        drawable.getRegion()
                .getTexture()
                .setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        if (this.drawable == drawable) return;
        if (drawable != null) {
            if (getPrefWidth() != drawable.getMinWidth() || getPrefHeight() != drawable.getMinHeight())
                invalidateHierarchy();
        } else
            invalidateHierarchy();
        this.drawable = drawable;
    }

    /**
     * @return May be null.
     */
    public TextureRegionDrawable getDrawable() {
        return drawable;
    }

    public void setScaling(Scaling scaling) {
        if (scaling == null) throw new IllegalArgumentException("scaling cannot be null.");
        this.scaling = scaling;
        invalidate();
    }

    public void setAlign(int align) {
        this.align = align;
        invalidate();
    }


    @Override
    public float getMinWidth() {
        return 0;
    }

    @Override
    public float getMinHeight() {
        return 0;
    }

    @Override
    public float getPrefWidth() {
        if (drawable != null) return drawable.getMinWidth();
        return 0;
    }

    @Override
    public float getPrefHeight() {
        if (drawable != null) return drawable.getMinHeight();
        return 0;
    }

    public float getImageX() {
        return imageX;
    }

    public float getImageY() {
        return imageY;
    }

    public float getImageWidth() {
        return imageWidth;
    }

    public float getImageHeight() {
        return imageHeight;
    }
}
