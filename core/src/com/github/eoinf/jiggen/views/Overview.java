package com.github.eoinf.jiggen.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.impl.AbstractLmlView;
import com.github.eoinf.jiggen.views.Widgets.TemplateWidget;

public class Overview extends AbstractLmlView {
    public Overview() {
        super(ViewManager.createStage());
    }

    public void loadTemplateWidget() {
        System.out.println("load template widget");
        if (this.templateWidget != null)
            this.templateWidget.add(new TemplateWidget(null, ViewManager.skin));
    }

    @LmlActor("widget")
    Table templateWidget;

    @Override
    public FileHandle getTemplateFile() {
        return Gdx.files.internal("views/overview.lml");
    }

    @Override
    public String getViewId() {
        return "overview";
    }

}