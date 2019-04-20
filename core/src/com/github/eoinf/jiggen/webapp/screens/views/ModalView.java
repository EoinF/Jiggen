package com.github.eoinf.jiggen.webapp.screens.views;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.eoinf.jiggen.webapp.screens.controllers.PuzzleViewController;
import com.github.eoinf.jiggen.webapp.screens.controllers.PuzzleViewModel;
import com.github.eoinf.jiggen.webapp.screens.models.ModalViewType;
import com.github.eoinf.jiggen.webapp.screens.widgets.BackgroundDisplay;
import com.github.eoinf.jiggen.webapp.screens.widgets.ShuffleConfirmModal;

import java.util.function.Consumer;

public class ModalView implements ScreenView {
    public final Stage stage;
    private final PuzzleViewModel puzzleViewModel;
    private final PuzzleViewController puzzleViewController;
    private Skin skin;

    public ModalView(PuzzleViewModel puzzleViewModel, PuzzleViewController puzzleViewController, Skin skin) {
        this.puzzleViewModel = puzzleViewModel;
        this.puzzleViewController = puzzleViewController;
        this.skin = skin;
        final Viewport viewport = new ScreenViewport();
        stage = new Stage(viewport);

        puzzleViewModel.getResizeScreenObservable().subscribe(new Consumer<GridPoint2>() {
            @Override
            public void accept(GridPoint2 screenBounds) {
                viewport.update(screenBounds.x, screenBounds.y, true);
            }
        });

        puzzleViewModel.getModalViewTypeSubject().subscribe(new Consumer<ModalViewType>() {
            @Override
            public void accept(ModalViewType type) {
                stage.clear();

                switch (type) {
                    case NONE:
                        break;
                    case SHUFFLE_CONFIRM:
                        setupShuffleConfirmModal();
                        break;
                    case BACKGROUND_DISPLAY:
                        setupBackgroundDisplay();
                        break;
                }
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

    private void setupShuffleConfirmModal() {
        stage.addActor(new ShuffleConfirmModal(puzzleViewController, skin));
        stage.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
    }

    private void setupBackgroundDisplay() {
        stage.addActor(new BackgroundDisplay(puzzleViewModel));
        stage.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                puzzleViewController.hideModal();
                super.touchUp(event, x, y, pointer, button);
            }
        });
    }
}
