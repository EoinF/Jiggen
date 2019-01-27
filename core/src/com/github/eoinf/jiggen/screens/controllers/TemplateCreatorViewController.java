package com.github.eoinf.jiggen.screens.controllers;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.github.eoinf.jiggen.TemplateCreator.WaveDistortionData;

import java.io.File;

public class TemplateCreatorViewController {

    private TemplateCreatorViewModel templateCreatorViewModel;

    public TemplateCreatorViewController(TemplateCreatorViewModel templateCreatorViewModel) {
        this.templateCreatorViewModel = templateCreatorViewModel;
    }

    public void setPixmap(Pixmap pixmap) {
        templateCreatorViewModel.setTemplatePixmap(pixmap);
    }

    public void resizeScreen(int width, int height) {
        templateCreatorViewModel.resizeScreen(width, height);
    }

    public void setDimensions(GridPoint2 dimensions) {
        templateCreatorViewModel.setTemplateDimensions(dimensions);
    }

    public void setAspectRatio(Vector2 aspectRatio) {
        templateCreatorViewModel.setTemplateAspectRatio(aspectRatio);
    }

    public void setMaxSize(GridPoint2 maxSize) {
        templateCreatorViewModel.setTemplateMaxSize(maxSize);
    }

    public void saveTemplateToFile(File file) {
        templateCreatorViewModel.saveTemplateToFile(file);
    }

    public void setWaveDistortionData(WaveDistortionData newValue) {
        templateCreatorViewModel.setWaveDistortion(newValue);
    }
}
