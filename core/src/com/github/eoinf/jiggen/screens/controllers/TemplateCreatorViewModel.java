package com.github.eoinf.jiggen.screens.controllers;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.github.eoinf.jiggen.TemplateCreator.WaveDistortionData;
import com.github.eoinf.jiggen.webapp.utils.SimpleObservable;
import com.github.eoinf.jiggen.webapp.utils.SimpleSubject;

import java.io.File;

public class TemplateCreatorViewModel {

    public TemplateCreatorViewModel(GridPoint2 defaultDimensions, Vector2 defaultAspectRatio,
                                    WaveDistortionData defaultWaveDistortion, Long defaultSeed) {
        randomSeedSubject = SimpleSubject.createDefault(defaultSeed);
        templateDimensionsSubject = SimpleSubject.createDefault(defaultDimensions);
        templateAspectRatioSubject = SimpleSubject.createDefault(defaultAspectRatio);
        waveDistortionSubject = SimpleSubject.createDefault(defaultWaveDistortion);
        templatePixmapSubject = SimpleSubject.create();
        templateMaxSizeSubject = SimpleSubject.create();
        resizeScreenSubject = SimpleSubject.create();
        saveToFileSubject = SimpleSubject.create();
    }

    //
    // The number of horizontal and vertical lines respectively to use in making the template
    //

    private SimpleSubject<GridPoint2> templateDimensionsSubject;

    void setTemplateDimensions(GridPoint2 dimensions) {
        this.templateDimensionsSubject.onNext(dimensions);
    }

    public SimpleObservable<GridPoint2> getTemplateDimensionsObservable() {
        return this.templateDimensionsSubject;
    }


    //
    // The aspect ratio of the template
    //
    private SimpleSubject<Vector2> templateAspectRatioSubject;

    void setTemplateAspectRatio(Vector2 aspectRatio) {
        this.templateAspectRatioSubject.onNext(aspectRatio);
    }

    public SimpleObservable<Vector2> getTemplateAspectRatioObservable() {
        return this.templateAspectRatioSubject;
    }


    //
    // The data used for distorting the template lines
    //
    private SimpleSubject<WaveDistortionData> waveDistortionSubject;

    void setWaveDistortion(WaveDistortionData waveDistortion) {
        this.waveDistortionSubject.onNext(waveDistortion);
    }

    public SimpleObservable<WaveDistortionData> getWaveDistortionObservable() {
        return this.waveDistortionSubject;
    }

    //
    // The texture data for rendering the pixmap
    //
    private SimpleSubject<Pixmap> templatePixmapSubject;

    void setTemplatePixmap(Pixmap pixmap) {
        templatePixmapSubject.onNext(pixmap);
    }

    public SimpleObservable<Pixmap> getTemplatePixmapObservable() {
        return this.templatePixmapSubject;
    }


    //
    // Max size of a created template image
    //

    private SimpleSubject<GridPoint2> templateMaxSizeSubject;

    public void setTemplateMaxSize(GridPoint2 maxSize) {
        templateMaxSizeSubject.onNext(maxSize);
    }

    public SimpleObservable<GridPoint2> getTemplateMaxSizeObservable() {
        return this.templateMaxSizeSubject;
    }


    //
    // Resize screen
    //
    private SimpleSubject<GridPoint2> resizeScreenSubject;

    public SimpleObservable<GridPoint2> getResizeScreenObservable() {
        return resizeScreenSubject;
    }

    void resizeScreen(int width, int height) {
        resizeScreenSubject.onNext(new GridPoint2(width, height));
    }

    //
    // Save template to file
    //
    private SimpleSubject<File> saveToFileSubject;

    public SimpleObservable<File> getSaveToFileObservable() {
        return this.saveToFileSubject;
    }

    public void saveTemplateToFile(File file) {
        saveToFileSubject.onNext(file);
    }

    //
    // Random seed
    //
    private SimpleSubject<Long> randomSeedSubject;
    public SimpleObservable<Long> getRandomSeedObservable() {
        return randomSeedSubject;
    }

    public void setRandomSeed(Long randomSeed) {
        randomSeedSubject.onNext(randomSeed);
    }
}