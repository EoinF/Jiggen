package com.github.eoinf.jiggen.webapp.screens.views;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.eoinf.jiggen.utils.PixmapUtils;
import com.github.eoinf.jiggen.webapp.screens.controllers.PuzzleViewController;
import com.github.eoinf.jiggen.webapp.screens.controllers.PuzzleViewModel;

import java.util.function.Consumer;

public class BackgroundView implements ScreenView {
    public final Stage stage;

    public BackgroundView(TextureAtlas uiTextureAtlas, PuzzleViewModel puzzleViewModel, PuzzleViewController puzzleViewController) {
        final Viewport viewport = new ScreenViewport();
        stage = new Stage(viewport);

        //
        // Layout
        //
        Table mainTable = new Table();
        mainTable.setFillParent(true);

        stage.addActor(mainTable);

        Image backgroundImage = new Image();
        mainTable
                .add(backgroundImage)
                .center();

        PixmapUtils.setBackgroundColour(mainTable, new Color(0, 0, 0, 0.6f));

        ClickListener inputListener = new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                puzzleViewController.hideBackground();
                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.SPACE) {
                    puzzleViewController.showBackground();
                }
                return false;
            }
        };

        puzzleViewModel.getIsBackgroundVisibleObservable().subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean isVisible) {
                if (isVisible) {
                    stage.addListener(inputListener);
                } else {
                    stage.removeListener(inputListener);
                }
            }
        });

        puzzleViewModel.getBackgroundImageObservable().subscribe(new Consumer<Texture>() {
            @Override
            public void accept(Texture texture) {
                backgroundImage.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
            }
        });

        puzzleViewModel.getResizeScreenObservable().subscribe(new Consumer<GridPoint2>() {
            @Override
            public void accept(GridPoint2 screenBounds) {
                viewport.update(screenBounds.x, screenBounds.y, true);
            }
        });
    }

    @Override
    public void act(float delta) {
        stage.act(delta);
    }

    @Override
    public void draw() {
        stage.draw();
    }
}
