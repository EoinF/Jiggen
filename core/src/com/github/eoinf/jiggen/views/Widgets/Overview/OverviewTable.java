package com.github.eoinf.jiggen.views.Widgets.Overview;

import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.eoinf.jiggen.views.Widgets.TemplateWidget;

public class OverviewTable extends Table {
    private TemplateWidget templateSection;

    private Table generatedPuzzleSection;

    private Table backgroundSection;
    private Table solveSection;

    public OverviewTable(Skin skin) {
        debug();

        templateSection = new TemplateWidget(null, skin);

        generatedPuzzleSection = new Table();
        backgroundSection = new Table();
        solveSection = new Table();

        add(templateSection)
                .expand()
                .fill();
        add(generatedPuzzleSection)
                .expand()
                .fill();
        row();
        add(backgroundSection)
                .expand()
                .fill();
        add(solveSection)
                .expand()
                .fill();

        setFillParent(true);
    }

    public void onClickTemplateSection(InputListener listener) {
        this.templateSection.addListener(listener);
    }
}
