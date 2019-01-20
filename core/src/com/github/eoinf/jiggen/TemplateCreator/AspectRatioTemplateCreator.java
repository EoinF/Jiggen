package com.github.eoinf.jiggen.TemplateCreator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;

public class AspectRatioTemplateCreator extends TemplateCreatorComponent<Vector2> {
    Pixmap pixmap;

    private static final int DEFAULT_PIXMAP_WIDTH = 1000;
    private static final int DEFAULT_PIXMAP_HEIGHT = 1000;

    public AspectRatioTemplateCreator() {
        this.pixmap = new Pixmap(DEFAULT_PIXMAP_WIDTH, DEFAULT_PIXMAP_HEIGHT, Pixmap.Format.RGBA8888);
        this.pixmap.setColor(Color.WHITE);
        this.pixmap.fill();
    }

    public Pixmap getGeneratedPixmap() {
        return this.pixmap;
    }

    public void setValue(Vector2 aspectRatio) {
        this.pixmap.dispose();
        float ratio = (aspectRatio.x / aspectRatio.y);
        this.pixmap = new Pixmap((int)(DEFAULT_PIXMAP_WIDTH * ratio), DEFAULT_PIXMAP_HEIGHT, Pixmap.Format.RGBA8888);
        this.pixmap.setColor(Color.WHITE);
        this.pixmap.fill();
    }
}
