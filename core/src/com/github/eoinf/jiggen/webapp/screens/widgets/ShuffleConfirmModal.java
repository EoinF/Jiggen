package com.github.eoinf.jiggen.webapp.screens.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.eoinf.jiggen.utils.PixmapUtils;
import com.github.eoinf.jiggen.webapp.screens.controllers.PuzzleViewController;

public class ShuffleConfirmModal extends Table {
    private static final float BORDER_WIDTH = 1;
    private static final float MODAL_PADDING = 10;

    public ShuffleConfirmModal(PuzzleViewController puzzleViewController, Skin skin) {
        //
        // Layout
        //
        super(skin);
        setFillParent(true);

        Table modalBorder = new Table();
        PixmapUtils.setBackgroundColour(modalBorder, Color.WHITE);

        Table modalContainer = new Table();
        PixmapUtils.setBackgroundColour(modalContainer, Color.PURPLE);

        Label title = new Label("Shuffle the puzzle pieces?", skin);
        Label description = new Label("Warning: You will lose all current progress on this puzzle", skin);

        TextButton confirm = new TextButton("Shuffle", skin);
        TextButton cancel = new TextButton("Cancel", skin);

        modalContainer.add(title)
                .colspan(2)
                .expandX()
                .padBottom(5)
                .center();
        modalContainer.row();
        modalContainer.add(description)
                .colspan(2)
                .expandX()
                .padBottom(5)
                .center();
        modalContainer.row();
        modalContainer.add(confirm);
        modalContainer.add(cancel);

        modalBorder.pad(BORDER_WIDTH);
        modalContainer.pad(MODAL_PADDING);

        modalBorder.add(modalContainer);
        add(modalBorder);

        confirm.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                puzzleViewController.shuffle();
                puzzleViewController.hideModal();
                super.touchUp(event, x, y, pointer, button);
            }
        });
        cancel.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                puzzleViewController.hideModal();
                super.touchUp(event, x, y, pointer, button);
            }
        });
        PixmapUtils.setBackgroundColour(this, new Color(0, 0, 0, 0.6f));
    }
}
