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
                !data.randomSeed.equals(newData.randomSeed);
    }

    @Override
    protected TemplateCreatorData calculate(TemplateCreatorData newData) {
        Pixmap pixmap = new Pixmap(newData.size.x, newData.size.y, Pixmap.Format.RGBA8888);

        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        pixmap.setColor(Color.BLACK);
        pixmap.drawRectangle(0, 0, newData.size.x, newData.size.y);

        TemplateCreatorData updatedData = new TemplateCreatorData(newData);
        if (updatedData.pixmap != null) {
            updatedData.pixmap.dispose();
        }
        updatedData.pixmap = pixmap;
        return updatedData;
    }
}
