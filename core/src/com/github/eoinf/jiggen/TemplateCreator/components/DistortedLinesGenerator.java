package com.github.eoinf.jiggen.TemplateCreator.components;

import com.github.eoinf.jiggen.TemplateCreator.TemplateLineWithDistortion;

public final class DistortedLinesGenerator extends TemplateCreatorComponent {

    public DistortedLinesGenerator(TemplateCreatorComponent nextComponent) {
        super(nextComponent);
    }

    @Override
    boolean shouldRecalculate(TemplateCreatorData newData) {
        return (newData.linesHorizontal != data.linesHorizontal
                || newData.linesVertical != data.linesVertical
                || newData.waveDistortionData != data.waveDistortionData
                || newData.size != data.size);
    }

    @Override
    protected TemplateCreatorData calculate(TemplateCreatorData newData) {
        TemplateCreatorData updatedData = new TemplateCreatorData(newData);
        updatedData.distortedLinesHorizontal = new TemplateLineWithDistortion[newData.linesHorizontal.length];
        updatedData.distortedLinesVertical = new TemplateLineWithDistortion[newData.linesVertical.length];
        for (int x = 0; x < newData.linesHorizontal.length; x++) {
            updatedData.distortedLinesHorizontal[x] = new TemplateLineWithDistortion(
                    newData.linesHorizontal[x], newData.waveDistortionData,
                    newData.size);
        }
        for (int y = 0; y < newData.linesVertical.length; y++) {
            updatedData.distortedLinesVertical[y] = new TemplateLineWithDistortion(
                    newData.linesVertical[y], newData.waveDistortionData,
                   newData.size);
        }
        return updatedData;
    }
}
