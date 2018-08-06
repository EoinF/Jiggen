package com.github.eoinf.jiggen.views;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.github.eoinf.jiggen.graphics.TouchControlledCamera;
import com.github.eoinf.jiggen.views.Screens.PuzzleViewModel;

import java.util.HashMap;
import java.util.Map;

public class PuzzleGestureListener extends ActorGestureListener {

    private Stage stage;
    private PuzzleViewModel puzzleViewModel;
    private TouchControlledCamera boundedCamera;

    private Map<Integer, Vector2> pointersTouchedDown;

    public PuzzleGestureListener(Stage stage, PuzzleViewModel puzzleViewModel, TouchControlledCamera camera) {
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
            boundedCamera.panBy(initialPointerPosition.x - x, initialPointerPosition.y - y);
        }
    }

    @Override
    public void pinch(InputEvent event, Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        boundedCamera.pinch(initialPointer1, initialPointer2, pointer1, pointer2);
        super.pinch(event, initialPointer1, initialPointer2, pointer1, pointer2);
    }
}