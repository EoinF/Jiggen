package com.github.eoinf.jiggen.views;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.github.eoinf.jiggen.graphics.WorldBoundedCamera;
import com.github.eoinf.jiggen.views.Screens.PuzzleViewModel;

import java.util.HashMap;
import java.util.Map;

public class PuzzleGestureListener extends ActorGestureListener {

    private Stage stage;
    private PuzzleViewModel puzzleViewModel;
    private WorldBoundedCamera boundedCamera;
    private static float ZOOM_MULTIPLIER = 0.005f;

    private Map<Integer, Vector2> pointersTouchedDown;

    public PuzzleGestureListener(Stage stage, PuzzleViewModel puzzleViewModel, WorldBoundedCamera camera) {
        this.stage = stage;
        this.puzzleViewModel = puzzleViewModel;
        this.boundedCamera = camera;

        this.pointersTouchedDown = new HashMap<>();
    }

    private boolean isTouching() {
        return pointersTouchedDown.size() > 0;
    }

    @Override
    public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
        Vector3 mousePositionInWorld = new Vector3(x, y, 0);

        Actor a = stage.hit(mousePositionInWorld.x, mousePositionInWorld.y, true);

        if (a != null && !isTouching()) {
            if (a instanceof PuzzlePieceGroup) {
                puzzleViewModel.pickUpPiece((PuzzlePieceGroup) a, new Vector2(
                        a.getX() - mousePositionInWorld.x,
                        a.getY() - mousePositionInWorld.y
                ));
                a.toFront();
            }
        }
        pointersTouchedDown.put(pointer, new Vector2(x, y));
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        pointersTouchedDown.remove(pointer);
        puzzleViewModel.dropPiece(new Vector2(x, y));
    }


    @Override
    public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
        if (!puzzleViewModel.isHoldingPiece()) {
            System.out.println("Pan to " + x + ", " + y + " -> " + deltaX + ", " + deltaY );
            // Assume only one pointer is being used (Panning is only possible with a single pointer)
            Integer pointerMapIndex = (Integer)pointersTouchedDown.keySet().toArray()[0];
            Vector2 initialPointerPosition = pointersTouchedDown.get(pointerMapIndex);
            boundedCamera.translate(initialPointerPosition.x - x, initialPointerPosition.y - y);
        }
    }

    @Override
    public void pinch(InputEvent event, Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        float initialDistanceBetweenPivots = initialPointer1.dst(initialPointer2);
        float currentDistanceBetweenPivots = pointer1.dst(pointer2);

        float zoomChange = (initialDistanceBetweenPivots - currentDistanceBetweenPivots) * ZOOM_MULTIPLIER;
        boundedCamera.zoomBy(zoomChange);
        super.pinch(event, initialPointer1, initialPointer2, pointer1, pointer2);
    }
}