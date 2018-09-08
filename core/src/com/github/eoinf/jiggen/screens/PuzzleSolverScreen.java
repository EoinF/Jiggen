package com.github.eoinf.jiggen.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.eoinf.jiggen.Jiggen;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleGraphTemplate;
import com.github.eoinf.jiggen.graphics.PuzzleOverlayBatch;
import com.github.eoinf.jiggen.graphics.WorldBoundedCamera;
import com.github.eoinf.jiggen.screens.controllers.PuzzleViewController;
import com.github.eoinf.jiggen.screens.controllers.PuzzleViewModel;
import com.github.eoinf.jiggen.screens.views.PuzzleToolbar;
import com.github.eoinf.jiggen.screens.views.PuzzleView;

public class PuzzleSolverScreen implements Screen {

    private PuzzleView puzzleView;
    private PuzzleToolbar toolbar;
    private PuzzleViewModel puzzleViewModel;
    private PuzzleViewController puzzleViewController;

    public PuzzleSolverScreen(Jiggen game, PuzzleOverlayBatch batch, TextureAtlas uiTextureAtlas, Skin skin) {
        WorldBoundedCamera camera = new WorldBoundedCamera();

        puzzleViewModel = new PuzzleViewModel(false);
        puzzleViewController = new PuzzleViewController(puzzleViewModel, camera);
        this.puzzleView = new PuzzleView(camera, batch, skin, puzzleViewModel, puzzleViewController);
        this.toolbar = new PuzzleToolbar(uiTextureAtlas, puzzleViewModel, puzzleViewController);

        this.puzzleViewModel.getFullScreenObservable().subscribe(game.onSetFullScreen);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(toolbar.stage);
        multiplexer.addProcessor(puzzleView.stage);
        multiplexer.addProcessor(puzzleView.getGestureDetector());
        Gdx.input.setInputProcessor(multiplexer);
    }

    public void setPuzzleGraph(PuzzleGraphTemplate puzzleGraphTemplate, Texture backgroundImage) {
        puzzleViewController.startPuzzle(puzzleGraphTemplate, backgroundImage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        puzzleView.act(delta);
        toolbar.act(delta);

        puzzleView.draw();
        toolbar.draw();
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
}
