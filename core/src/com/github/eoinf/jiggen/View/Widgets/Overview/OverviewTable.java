package com.github.eoinf.jiggen.View.Widgets.Overview;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.eoinf.jiggen.View.Widgets.TemplateWidget;

public class OverviewTable extends Table {

    TemplateWidget templateSection;

    Table generatedPuzzleSection;

    Table backgroundSection;
    Table solveSection;

    public OverviewTable(Skin skin) {
        debug();

        templateSection = new TemplateWidget(null, skin);

        generatedPuzzleSection = new Table();
        backgroundSection = new Table();
        solveSection = new Table();

        add(templateSection)
                .expandX();
        add(generatedPuzzleSection)
                .expandX();
        row();
        add(backgroundSection)
                .expandX();
        add(solveSection)
                .expandX();

        setFillParent(true);
    }
}
