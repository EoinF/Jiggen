package com.github.eoinf.jiggen.screens.controllers;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.github.eoinf.jiggen.utils.ReplaySubject;
import com.github.eoinf.jiggen.utils.SimpleObservable;
import com.github.eoinf.jiggen.utils.SimpleSubject;

public class TemplateCreatorViewModel {

    public TemplateCreatorViewModel(GridPoint2 defaultDimensions, Vector2 defaultAspectRatio) {
        templateDimensionsSubject = ReplaySubject.createDefault(defaultDimensions);
        templateAspectRatioSubject = ReplaySubject.createDefault(defaultAspectRatio);
        templatePixmapSubject = SimpleSubject.create();
        templateMaxSizeSubject = SimpleSubject.create();
    }

    private ReplaySubject<GridPoint2> templateDimensionsSubject;
    private ReplaySubject<Vector2> templateAspectRatioSubject;

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