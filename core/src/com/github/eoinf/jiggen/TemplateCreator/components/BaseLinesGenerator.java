package com.github.eoinf.jiggen.TemplateCreator.components;

import com.github.eoinf.jiggen.TemplateCreator.TemplateLine;

public final class BaseLinesGenerator extends TemplateCreatorComponent {

    public BaseLinesGenerator(DistortedLinesGenerator distortedLinesGenerator) {
        super(distortedLinesGenerator);
    }

    @Override
    boolean shouldRecalculate(TemplateCreatorData newData) {
        return !newData.dimensions.equals(data.dimensions)
                || newData.size != data.size;
    }

    @Override
    protected TemplateCreatorData calculate(TemplateCreatorData newData) {
        TemplateCreatorData updatedData = new TemplateCreatorData(newData);
        updatedData.linesHorizontal = new TemplateLine[newData.dimensions.y - 1];
        updatedData.linesVertical = new TemplateLine[newData.dimensions.x - 1];

        for (int i = 0; i < updatedData.linesHorizontal.length; i++) {
            int y = (int) ((i + 1) * ((float) newData.size.y / newData.dimensions.y));
            int x2 = newData.size.x;
            updatedData.linesHorizontal[i] = new TemplateLine(0, x2, y, false);
        }
        for (int i = 0; i < updatedData.linesVertical.length; i++) {
            int x = (int) ((i + 1) * ((float) newData.size.x / newData.dimensions.x));
            int y2 = newData.size.y;
            updatedData.linesVertical[i] = new TemplateLine(0, y2, x, true);
        }

        return updatedData;
    }
}
