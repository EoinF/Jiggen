package com.github.eoinf.jiggen.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.github.czyzby.lml.parser.impl.AbstractLmlView;

public class TemplateSelectView extends AbstractLmlView {
    public TemplateSelectView() {
        super(ViewManager.createStage());
    }

    @Override
    public FileHandle getTemplateFile() {
        return Gdx.files.internal("views/templateSelectView.lml");
    }

    @Override
    public String getViewId() {
        return "templateSelect";
    }
}
