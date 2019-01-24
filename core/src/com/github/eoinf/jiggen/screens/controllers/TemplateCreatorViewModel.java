package com.github.eoinf.jiggen.screens.controllers;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.github.eoinf.jiggen.TemplateCreator.WaveDistortionData;
import com.github.eoinf.jiggen.utils.ReplaySubject;
import com.github.eoinf.jiggen.utils.SimpleObservable;
import com.github.eoinf.jiggen.utils.SimpleSubject;

public class TemplateCreatorViewModel {

    public TemplateCreatorViewModel(GridPoint2 defaultDimensions, Vector2 defaultAspectRatio,
                                    WaveDistortionData defaultWaveDistortion) {
        templateDimensionsSubject = ReplaySubject.createDefault(defaultDimensions);
        templateAspectRatioSubject = ReplaySubject.createDefault(defaultAspectRatio);
        waveDistortionSubject = ReplaySubject.createDefault(defaultWaveDistortion);
        templatePixmapSubject = SimpleSubject.create();
        templateMaxSizeSubject = SimpleSubject.create();
    }

    private ReplaySubject<GridPoint2> templateDimensionsSubject;
    private ReplaySubject<Vector2> templateAspectRatioSubject;
    private ReplaySubject<WaveDistortionData> waveDistortionSubject;

    void setTemplateDimensions(GridPoint2 dimensions) {
        this.templateDimensionsSubject.onNext(dimensions);
    }

    public SimpleObservable<GridPoint2> getTemplateDimensionsObservable() {
        return this.templateDimensionsSubject;
    }


    void setTemplateAspectRatio(Vector2 aspectRatio) {
        this.templateAspectRatioSubject.onNext(aspectRatio);
    }

    public SimpleObservable<Vector2> getTemplateAspectRatioObservable() {
        return this.templateAspectRatioSubject;
    }

    void setWaveDistortion(WaveDistortionData waveDistortion) {
        this.waveDistortionSubject.onNext(waveDistortion);
    }

    public SimpleObservable<WaveDistortionData> getWaveDistortionObservable() {
        return this.waveDistortionSubject;
    }


    private SimpleSubject<Pixmap> templatePixmapSubject;

    void setTemplatePixmap(Pixmap pixmap) {
        templatePixmapSubject.onNext(pixmap);
    }

    public SimpleObservable<Pixmap> getTemplatePixmapObservable() {
        return this.templatePixmapSubject;
    }


    private SimpleSubject<GridPoint2> templateMaxSizeSubject;

    public void setTemplateMaxSize(GridPoint2 maxSize) {
        templateMaxSizeSubject.onNext(maxSize);
    }

    public SimpleObservable<GridPoint2> getTemplateMaxSizeObservable() {
        return this.templateMaxSizeSubject;
    }
}