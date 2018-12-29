package com.github.eoinf.jiggen.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.eoinf.jiggen.Jiggen;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleGraphTemplate;
import com.github.eoinf.jiggen.graphics.PuzzleOverlayBatch;
import com.github.eoinf.jiggen.graphics.WorldBoundedCamera;
import com.github.eoinf.jiggen.screens.controllers.PuzzleViewController;
import com.github.eoinf.jiggen.screens.controllers.PuzzleViewModel;
import com.github.eoinf.jiggen.screens.views.PuzzleToolbar;
import com.github.eoinf.jiggen.screens.views.PuzzleView;

import java.util.function.Consumer;

public class PuzzleSolverScreen implements Screen {

    private PuzzleView puzzleView;
    private PuzzleToolbar toolbar;
    private PuzzleViewModel puzzleViewModel;
    private PuzzleViewController puzzleViewController;

    public PuzzleSolverScreen(Jiggen game, PuzzleOverlayBatch batch, TextureAtlas uiTextureAtlas, Skin skin) {
        WorldBoundedCamera camera = new WorldBoundedCamera();

        puzzleViewModel = new PuzzleViewModel();
        puzzleViewController = new PuzzleViewController(puzzleViewModel, camera);
        this.puzzleView = new PuzzleView(camera, batch, skin, puzzleViewModel, puzzleViewController);
        this.toolbar = new PuzzleToolbar(uiTextureAtlas, puzzleViewModel, puzzleViewController);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(toolbar.stage);
        multiplexer.addProcessor(puzzleView.stage);
        multiplexer.addProcessor(puzzleView.getGestureDetector());
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
                    game.onSetFullScreen.accept(true);
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(multiplexer);

        puzzleViewModel.getPuzzleTemplateObservable().subscribe(new Consumer<PuzzleGraphTemplate>() {
            @Override
            public void accept(PuzzleGraphTemplate puzzleGraphTemplate) {
                Texture backgroundImage = puzzleViewModel.getBackgroundImageObservable().getValue();
                puzzleViewController.updatePuzzleGraph(puzzleGraphTemplate, backgroundImage);
                puzzleViewController.updateWorldBounds(camera.viewportWidth, camera.viewportHeight);
            }
        });

        puzzleViewModel.getBackgroundImageObservable().subscribe(new Consumer<Texture>() {
            @Override
            public void accept(Texture backgroundImage) {
                PuzzleGraphTemplate puzzleGraphTemplate = puzzleViewModel.getPuzzleTemplateObservable().getValue();
                puzzleViewController.updatePuzzleGraph(puzzleGraphTemplate, backgroundImage);
            }
        });

        puzzleViewModel.getScalesObservable().subscribe(new Consumer<Vector2>() {
            @Override
            public void accept(Vector2 scales) {
                puzzleViewController.updateWorldBounds(camera.viewportWidth, camera.viewportHeight);
            }
        });

        puzzleViewModel.getWorldBoundsObservable().subscribe(new Consumer<GridPoint2>() {
            @Override
            public void accept(GridPoint2 worldBounds) {
                float maxZoomX = worldBounds.x / camera.viewportWidth;
                float maxZoomY = worldBounds.y / camera.viewportHeight;

                float maxZoom = Math.min(maxZoomX, maxZoomY);
                camera.setCameraBounds(worldBounds.x, worldBounds.y, maxZoom);
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

    public void setBackground(Texture background) {
        puzzleViewController.setBackground(background);
    }
}
