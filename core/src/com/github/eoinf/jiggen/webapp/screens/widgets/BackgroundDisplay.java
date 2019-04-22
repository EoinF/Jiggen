package com.github.eoinf.jiggen.webapp.screens.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.eoinf.jiggen.webapp.screens.controllers.PuzzleViewModel;
import com.github.eoinf.jiggen.webapp.utils.PixmapUtils;

public class BackgroundDisplay extends Table {
    public BackgroundDisplay(PuzzleViewModel puzzleViewModel) {
        //
        // Layout
        //
        super();
        setFillParent(true);

        Image backgroundImage = new Image(puzzleViewModel.getBackgroundImageObservable().getValue());

        add(backgroundImage)
                .center();

        PixmapUtils.setBackgroundColour(this, new Color(0, 0, 0, 0.6f));
    }
}
