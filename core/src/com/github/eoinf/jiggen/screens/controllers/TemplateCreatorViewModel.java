package com.github.eoinf.jiggen.screens.controllers;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.github.eoinf.jiggen.utils.ReplaySubject;
import com.github.eoinf.jiggen.utils.SimpleObservable;

public class TemplateCreatorViewModel {
    public TemplateCreatorViewModel(GridPoint2 defaultDimensions, Vector2 defaultAspectRatio) {
        templateDimensionsSubject = ReplaySubject.createDefault(defaultDimensions);
        templateAspectRatioSubject = ReplaySubject.createDefault(defaultAspectRatio);
    }

    private ReplaySubject<GridPoint2> templateDimensionsSubject;
    private ReplaySubject<Vector2> templateAspectRatioSubject;

    public void setTemplateDimensions(GridPoint2 dimensions) {
        this.templateDimensionsSubject.onNext(dimensions);
    }

    public SimpleObservable<GridPoint2> getTemplateDimensions() {
        return this.templateDimensionsSubject;
    }


    public void setTemplateAspectRatio(Vector2 aspectRatio) {
        this.templateAspectRatioSubject.onNext(aspectRatio);
    }

    public SimpleObservable<Vector2> getTemplateAspectRatio() {
        return this.templateAspectRatioSubject;
    }
}