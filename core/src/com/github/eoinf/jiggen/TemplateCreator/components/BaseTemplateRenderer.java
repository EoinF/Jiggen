package com.github.eoinf.jiggen.TemplateCreator.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;

public final class BaseTemplateRenderer extends TemplateCreatorComponent {
    public BaseTemplateRenderer(TemplateCreatorComponent nextComponent) {
        super(nextComponent);
    }

    @Override
    boolean shouldRecalculate(TemplateCreatorData newData) {
        return data.size != newData.size ||
                data.dimensions != newData.dimensions ||
                data.waveDistortionData != newData.waveDistortionData ||
                data.aspectRatio != newData.aspectRatio ||
                data.randomSeed != newData.randomSeed;
    }

    @Override
    protected TemplateCreatorData calculate(TemplateCreatorData newData) {
        if (this.pixmap != null) {
            this.pixmap.dispose();
        }
        this.pixmap = new Pixmap(newData.size.x, newData.size.y, Pixmap.Format.RGBA8888);

        this.pixmap.setColor(Color.WHITE);
        this.pixmap.fill();

        this.pixmap.setColor(Color.BLACK);
        this.pixmap.drawRectangle(0, 0, newData.size.x, newData.size.y);
        return newData;
    }
}
