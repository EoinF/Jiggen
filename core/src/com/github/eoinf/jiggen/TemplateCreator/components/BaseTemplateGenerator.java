package com.github.eoinf.jiggen.TemplateCreator.components;

import com.badlogic.gdx.math.GridPoint2;

public class BaseTemplateGenerator extends TemplateCreatorComponent {

    public BaseTemplateGenerator(TemplateCreatorData baseData, BaseTemplateRenderer baseTemplateRenderer) {
        super(baseData, baseTemplateRenderer);
    }

    @Override
    boolean shouldRecalculate(TemplateCreatorData newData) {
        System.out.println(newData.aspectRatio);
        System.out.println(newData.maxSize);
        return newData.aspectRatio != data.aspectRatio
                || newData.maxSize != data.maxSize;
    }

    @Override
    protected TemplateCreatorData calculate(TemplateCreatorData newData) {
        float ratio = newData.aspectRatio.x / newData.aspectRatio.y;
        GridPoint2 maxSize = newData.maxSize;
        int width, height;
        if (maxSize.y * ratio < maxSize.x) {
            width = (int) ((maxSize.y * newData.aspectRatio.x) / newData.aspectRatio.y);
            height = maxSize.y;
        } else {
            width = maxSize.x;
            height = (int) ((maxSize.x * newData.aspectRatio.y) / newData.aspectRatio.x);
        }

        TemplateCreatorData updatedData = new TemplateCreatorData(newData);
        updatedData.size = new GridPoint2(width, height);
        return updatedData;
    }
}
