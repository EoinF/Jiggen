package com.github.eoinf.jiggen.screens.views;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.eoinf.jiggen.screens.controllers.PuzzleViewController;
import com.github.eoinf.jiggen.screens.controllers.PuzzleViewModel;

import java.util.function.Consumer;

public class PuzzleToolbar {
    public Stage stage;


    public PuzzleToolbar(TextureAtlas uiTextureAtlas, PuzzleViewModel puzzleViewModel,
                         PuzzleViewController puzzleViewController) {
        Viewport viewport = new ScreenViewport();
        stage = new Stage(viewport);

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
                new TextureRegionDrawable(uiTextureAtlas.findRegion("toolbar/resize"))
        );

        toolbarTable
                .add(resizeButton)
                .expand()
                .pad(5)
                .right();

        resizeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                puzzleViewController.toggleFullScreen();
                super.clicked(event, x, y);
            }
        });

        puzzleViewModel.getResizeScreenObservable().subscribe(new Consumer<GridPoint2>() {
            @Override
            public void accept(GridPoint2 screenBounds) {
                viewport.update(screenBounds.x, screenBounds.y, true);
            }
        });
    }

    public void act(float delta) {
        stage.act(delta);
    }

    public void draw() {
        stage.draw();
    }
}
