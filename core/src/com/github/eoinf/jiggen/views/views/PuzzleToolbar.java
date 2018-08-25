package com.github.eoinf.jiggen.views.views;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.eoinf.jiggen.views.PuzzleViewModel;

import java.util.function.Consumer;

public class PuzzleToolbar {
    //private static int TOOLBAR_HEIGHT = 200;

    public Stage stage;
    private boolean isFullScreen;

    public PuzzleToolbar(Viewport viewport, TextureAtlas textureAtlas, PuzzleViewModel viewModel) {
        this.stage = new Stage(viewport);

        //
        // Layout
        //
        Table mainTable = new Table();
        mainTable.setFillParent(true);
//        PixmapUtils.setBackgroundColour(mainTable, Color.GREEN);

        Table toolbarTable = new Table();
        toolbarTable.align(Align.top);
        //PixmapUtils.setBackgroundColour(toolbarTable, Color.BLUE);
        mainTable
                .add(toolbarTable)
                .expand()
                .fillX()
                .top();

        stage.addActor(mainTable);


        //
        // Content
        //
        ImageButton resizeButton = new ImageButton(
                new TextureRegionDrawable(textureAtlas.findRegion("toolbar/resize"))
        );

        toolbarTable
                .add(resizeButton)
                .expand()
                .pad(5)
                .right();

        resizeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                viewModel.setFullScreen(isFullScreen);
                super.clicked(event, x, y);
            }
        });

        viewModel.getFullScreenObservable().subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean _isFullScreen) {
                isFullScreen = _isFullScreen;
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
        stage.dispose();
    }
}
