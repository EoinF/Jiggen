package com.github.eoinf.jiggen.views.Screens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PuzzleToolbar {
    private static int TOOLBAR_HEIGHT = 50;

    Stage stage;
    private Batch spriteBatch;

    public PuzzleToolbar(Viewport viewport, TextureAtlas textureAtlas, PuzzleViewModel viewModel) {
        this.spriteBatch = new SpriteBatch();
        stage = new Stage(viewport, spriteBatch);

        //
        // Layout
        //
        Table mainTable = new Table();
        mainTable.setFillParent(true);

        Table toolbarTable = new Table();
        toolbarTable.setFillParent(true);
        toolbarTable.setHeight(TOOLBAR_HEIGHT);

        mainTable
                .add(toolbarTable)
                .expand()
                .top();

        stage.addActor(toolbarTable);

        ImageButton resizeButton = new ImageButton(
                new TextureRegionDrawable(textureAtlas.findRegion("toolbar/resize"))
        );

        //
        // Content
        //
        toolbarTable
                .add(resizeButton)
                .expand()
                .pad(5)
                .top()
                .right();

        resizeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                viewModel.setFullScreen(true);
                super.clicked(event, x, y);
            }
        });
    }

    public void update() {
        stage.act();
    }

    public void draw() {
        stage.draw();
    }

    public void dispose() {
        spriteBatch.dispose();
    }
}
