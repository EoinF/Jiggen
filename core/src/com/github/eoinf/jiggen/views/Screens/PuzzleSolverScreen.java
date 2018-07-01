package com.github.eoinf.jiggen.views.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PuzzleOverlayBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleGraphTemplate;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzlePieceTemplate;
import com.github.eoinf.jiggen.views.TextureOverlayImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PuzzleSolverScreen implements Screen {

    private OrthographicCamera camera;

    private Actor pieceHeld;
    private Vector2 pieceOffsetHeld;

    private Stage gameStage;
    private PuzzleGraphTemplate puzzleGraph;
    private List<Actor> pieceActors;

    private ScreenViewport viewport;
    private Vector2 scales;

    private Texture backgroundImage;

    public PuzzleSolverScreen(OrthographicCamera camera, PuzzleOverlayBatch batch) {
        this.camera = camera;


        viewport = new ScreenViewport(camera);
        gameStage = new Stage(viewport, batch);
        pieceActors = new ArrayList<>();
        scales = new Vector2(1, 1);

        initInputManager();
        Gdx.input.setInputProcessor(gameStage);
    }

    private void initInputManager() {
        gameStage.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Vector3 mousePositionInWorld = new Vector3(x, y, 0);
                Actor a = gameStage.hit(mousePositionInWorld.x, mousePositionInWorld.y, true);
                if (a != null) {
                    int index = pieceActors.indexOf(a);
                    if (index >= 0) {
                        pieceHeld = pieceActors.get(index);

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

    private Vector2 getMinimumScaleToFixAspectRatio(int a, int b, int c, int d) {
        //
        // The variables s1 and s2 are the scales required to transform the aspect ratio
        // The variables a and b are the width and height of the current texture
        // The variables c and d are the width and height of the target texture
        // s1 * a(x) + s2 * b(y) = c(x) + d(y)
        //
        // ad >= bc  ->  s1 = 1
        // bc >= ad  ->  s2 = 1
        //
        //
        float s1; float s2;
        float ad = a * d;
        float bc = b * c;

        if (ad >= bc) {
            s1 = 1;
            s2 = ad / bc;
        } else {
            s2 = 1;
            s1 = bc / ad;
        }

        return new Vector2(s1, s2);
    }

    public void setPuzzleGraph(PuzzleGraphTemplate puzzleGraph, Texture backgroundImage) {
        this.puzzleGraph = puzzleGraph;
        this.backgroundImage = backgroundImage;

        this.scales = getMinimumScaleToFixAspectRatio(puzzleGraph.getWidth(), puzzleGraph.getHeight(),
                backgroundImage.getWidth(), backgroundImage.getHeight());

        for (Actor piece : pieceActors) {
            piece.remove();
        }
        pieceActors.clear();

        for (PuzzlePieceTemplate<TextureRegion> piece: puzzleGraph.getVertices().values()) {
            addNewPiece(piece);
        }
    }

    private void addNewPiece(PuzzlePieceTemplate<TextureRegion> piece) {
        TextureOverlayImage image = new TextureOverlayImage(piece.getData());
        image.setUserObject(piece);
        int sX = (int)(piece.x() * scales.x);
        int sY = (int)(piece.y() * scales.y);
        float sW = (piece.getWidth() * scales.x);
        float sH = (piece.getHeight() * scales.y);

        float scaledPuzzleWidth = puzzleGraph.getWidth() * scales.x;
        float scaledPuzzleHeight = puzzleGraph.getHeight() * scales.y;

        float u = sX / scaledPuzzleWidth;
        float v = sY / scaledPuzzleHeight;
        float u2 = (sX + sW) / scaledPuzzleWidth;
        float v2 = (sY + sH) / scaledPuzzleHeight;

        image.setOverlay(new TextureRegionDrawable(
                new TextureRegion(backgroundImage, u, v, u2, v2)));
        image.setScale(this.scales.x, this.scales.y);
        image.setPosition(centreX() + sX, centreY() + sY);
        pieceActors.add(image);
        gameStage.addActor(image);
    }

    public void autoSolve() {
        for (Actor piece: pieceActors) {
            MoveToAction moveToHome = new MoveToAction();
            PuzzlePieceTemplate templatePiece = (PuzzlePieceTemplate)piece.getUserObject();
            int homeX = centreX() + (int)(templatePiece.x() * scales.x);
            int homeY = centreY() + (int)(templatePiece.y() * scales.y);
            moveToHome.setX(homeX);
            moveToHome.setY(homeY);
            moveToHome.setDuration(3 * Vector2.dst(homeX, homeY, piece.getX(), piece.getY())
                    / Math.max(viewport.getWorldWidth(), viewport.getWorldHeight()));
            piece.addAction(moveToHome);
        }
    }

    public void shuffle() {
        Random random = new Random();

        for (Actor piece: pieceActors) {
            float r1 = random.nextFloat();
            float r2 = random.nextFloat();

            int x = (int)(r1 * (viewport.getWorldWidth() - puzzleGraph.getMaxPieceWidth() * scales.x));
            int y = (int)(r2 * (viewport.getWorldHeight() - puzzleGraph.getMaxPieceHeight() * scales.y));
            piece.setPosition(x, y);

            if (r1 + r2 > 1){
                piece.toFront();
            }
        }
    }

    public int centreX() {
        return (int)(viewport.getWorldWidth() - puzzleGraph.getWidth() * scales.x) / 2;
    }
    public int centreY() {
        return (int)(viewport.getWorldHeight() - puzzleGraph.getHeight() * scales.y) / 2;
    }
}
