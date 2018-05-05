package com.github.eoinf.jiggen.views;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.eoinf.jiggen.Jiggen;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleGraph;
import com.github.eoinf.jiggen.views.Screens.BackgroundSelectionScreen;
import com.github.eoinf.jiggen.views.Screens.PuzzleOverviewScreen;
import com.github.eoinf.jiggen.views.Screens.PuzzleSolverScreen;
import com.github.eoinf.jiggen.views.Screens.TemplateSelectionScreen;

public final class ScreenManager {

    private static Jiggen _game;

    private static PuzzleOverviewScreen puzzleOverviewScreen;
    private static BackgroundSelectionScreen backgroundSelectionScreen;
    private static TemplateSelectionScreen templateSelectionScreen;
    private static PuzzleSolverScreen puzzleSolverScreen;


    public static void init(Jiggen game) {
        _game = game;
    }

    public static void switchToTemplateSelection() {
        if (templateSelectionScreen == null) {
            templateSelectionScreen = new TemplateSelectionScreen(ViewManager.camera, ViewManager.batch, ViewManager.skin);
        }
        _game.setScreen(templateSelectionScreen);
    }

    public static void switchToBackgroundSelection() {
        if (backgroundSelectionScreen == null) {
            backgroundSelectionScreen = new BackgroundSelectionScreen(ViewManager.camera, ViewManager.batch);
        }
        _game.setScreen(backgroundSelectionScreen);
    }

    public static void switchToPuzzleSolver(PuzzleGraph puzzleGraph) {
        if (puzzleSolverScreen == null) {
            puzzleSolverScreen = new PuzzleSolverScreen(ViewManager.camera, ViewManager.batch, puzzleGraph);
        }
        _game.setScreen(puzzleSolverScreen);
    }

    public static void switchToPuzzleOverview() {
        if (puzzleOverviewScreen == null) {
            puzzleOverviewScreen = new PuzzleOverviewScreen(ViewManager.camera, ViewManager.batch, ViewManager.skin,
                    ViewManager.createStage());
        }
        _game.setScreen(puzzleOverviewScreen);
    }
}
