package com.github.eoinf.jiggen.views.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleGraphTemplate;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzlePieceTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PuzzleSolverScreen implements Screen {

    private OrthographicCamera camera;

    private Actor pieceHeld;
    private Vector2 pieceOffsetHeld;

    private Stage gameStage;
    private PuzzleGraphTemplate puzzleGraph;
    private List<Actor> pieces;

    private ScreenViewport viewport;

    public PuzzleSolverScreen(OrthographicCamera camera, SpriteBatch batch) {
        this.camera = camera;


        viewport = new ScreenViewport(camera);
        gameStage = new Stage(viewport, batch);
        pieces = new ArrayList<>();

        Gdx.input.setInputProcessor(gameStage);
    }

    @Override
    public void show() {

    }

    private void update(float delta) {
        if (pieceHeld != null) {
            Vector3 mousePositionInWorld = camera.unproject(
                    new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            pieceHeld.setPosition(mousePositionInWorld.x + pieceOffsetHeld.x,
                    mousePositionInWorld.y + pieceOffsetHeld.y);
        }
        gameStage.act();
    }

    @Override
    public void render(float delta) {
        update(delta);

        gameStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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

    public void setPuzzleGraph(PuzzleGraphTemplate puzzleGraph) {
        this.puzzleGraph = puzzleGraph;
        for (Actor piece : pieces) {
            piece.remove();
        }
        pieces.clear();

        for (PuzzlePieceTemplate<TextureRegion> piece: puzzleGraph.getVertices().values()) {
            Image image = new Image(piece.getData());
            image.setUserObject(piece);
            image.setPosition(centreX() + piece.x(), centreY() + piece.y());
            pieces.add(image);
            gameStage.addActor(image);
        }

        gameStage.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Vector3 mousePositionInWorld = new Vector3(x, y, 0);
                Actor a = gameStage.hit(mousePositionInWorld.x, mousePositionInWorld.y, true);
                if (a != null) {
                    int index = pieces.indexOf(a);
                    if (index >= 0) {
                        pieceHeld = pieces.get(index);

                        pieceOffsetHeld = new Vector2(
                                pieceHeld.getX() - mousePositionInWorld.x,
                                pieceHeld.getY() - mousePositionInWorld.y
                        );
                        pieceHeld.toFront();
                    }
                }
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                pieceHeld = null;
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.SPACE) {
                    shuffle();
                } else if (keycode == Input.Keys.A) {
                    autoSolve();
                }
                return super.keyDown(event, keycode);
            }
        });
    }

    public void autoSolve() {
        for (Actor piece: pieces) {
            MoveToAction moveToHome = new MoveToAction();
            PuzzlePieceTemplate templatePiece = (PuzzlePieceTemplate)piece.getUserObject();
            int homeX = centreX() + templatePiece.x();
            int homeY = centreY() + templatePiece.y();
            moveToHome.setX(homeX);
            moveToHome.setY(homeY);
            moveToHome.setDuration(3 * Vector2.dst(homeX, homeY, piece.getX(), piece.getY())
                    / Math.max(viewport.getWorldWidth(), viewport.getWorldHeight()));
            piece.addAction(moveToHome);
        }
    }

    public void shuffle() {
        Random random = new Random();

        for (Actor piece: pieces) {
            float r1 = random.nextFloat();
            float r2 = random.nextFloat();

            int x = (int)(r1 * (viewport.getWorldWidth() - puzzleGraph.getMaxPieceWidth()));
            int y = (int)(r2 * (viewport.getWorldHeight() - puzzleGraph.getMaxPieceHeight()));
            piece.setPosition(x, y);

            if (r1 + r2 > 1){
                piece.toFront();
            }
        }
    }

    public int centreX() {
        return (int)(viewport.getWorldWidth() - puzzleGraph.getWidth()) / 2;
    }
    public int centreY() {
        return (int)(viewport.getWorldHeight() - puzzleGraph.getHeight()) / 2;
    }
}
