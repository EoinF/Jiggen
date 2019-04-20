package com.github.eoinf.jiggen.webapp.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.eoinf.jiggen.webapp.Jiggen;
import com.github.eoinf.jiggen.webapp.graphics.PuzzleOverlayBatch;
import com.github.eoinf.jiggen.webapp.graphics.WorldBoundedCamera;
import com.github.eoinf.jiggen.webapp.screens.controllers.PuzzleViewController;
import com.github.eoinf.jiggen.webapp.screens.controllers.PuzzleViewModel;
import com.github.eoinf.jiggen.webapp.screens.models.PuzzleGraphTemplate;
import com.github.eoinf.jiggen.webapp.screens.views.ModalView;
import com.github.eoinf.jiggen.webapp.screens.views.PuzzleToolbar;
import com.github.eoinf.jiggen.webapp.screens.views.PuzzleView;

import java.util.function.Consumer;

public class PuzzleSolverScreen implements Screen {

    private final ModalView modalView;
    private final PuzzleView puzzleView;
    private final PuzzleToolbar toolbar;
    private final PuzzleViewModel puzzleViewModel;
    private final PuzzleViewController puzzleViewController;

    public PuzzleSolverScreen(Jiggen game, PuzzleOverlayBatch batch, TextureAtlas uiTextureAtlas, Skin skin) {
        WorldBoundedCamera camera = new WorldBoundedCamera();

        puzzleViewModel = new PuzzleViewModel();
        puzzleViewController = new PuzzleViewController(game, puzzleViewModel, camera);
        this.puzzleView = new PuzzleView(camera, batch, skin, puzzleViewModel, puzzleViewController);
        this.toolbar = new PuzzleToolbar(uiTextureAtlas, puzzleViewModel, puzzleViewController);
        this.modalView = new ModalView(puzzleViewModel, puzzleViewController, skin);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(modalView.stage);
        multiplexer.addProcessor(puzzleView.stage);
        multiplexer.addProcessor(puzzleView.getGestureDetector());
        multiplexer.addProcessor(toolbar.stage);
        Gdx.input.setInputProcessor(multiplexer);

        puzzleViewModel.getPuzzleTemplateObservable().subscribe(new Consumer<PuzzleGraphTemplate>() {
            @Override
            public void accept(PuzzleGraphTemplate puzzleGraphTemplate) {
                Texture backgroundImage = puzzleViewModel.getBackgroundImageObservable().getValue();
                puzzleViewController.updatePuzzleGraph(puzzleGraphTemplate, backgroundImage);
            }
        });

        puzzleViewModel.getBackgroundImageObservable().subscribe(new Consumer<Texture>() {
            @Override
            public void accept(Texture backgroundImage) {
                PuzzleGraphTemplate puzzleGraphTemplate = puzzleViewModel.getPuzzleTemplateObservable().getValue();
                puzzleViewController.updatePuzzleGraph(puzzleGraphTemplate, backgroundImage);
            }
        });

        puzzleViewModel.getResizeScreenObservable().subscribe(new Consumer<GridPoint2>() {
            @Override
            public void accept(GridPoint2 newScreenSize) {
                puzzleViewController.updateWorldBounds(newScreenSize.x, newScreenSize.y);
            }
        });
    }

    public void setTemplate(PuzzleGraphTemplate puzzleGraphTemplate) {
        puzzleViewController.setTemplate(puzzleGraphTemplate);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        modalView.act(delta);
        toolbar.act(delta);
        puzzleView.act(delta);

        puzzleView.draw();
        toolbar.draw();
        modalView.draw();
    }

    @Override
    public void resize(int width, int height) {
        puzzleViewController.resizeScreen(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public void shuffle() {
        puzzleViewController.shuffle();
    }

    public void setBackground(Texture background) {
        puzzleViewController.setBackground(background);
    }
}
