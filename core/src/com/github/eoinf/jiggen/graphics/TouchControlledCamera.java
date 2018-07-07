package com.github.eoinf.jiggen.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

public class TouchControlledCamera extends WorldBoundedCamera {

    private Map<Integer, Vector2> pivots;
    private Map<Integer, Vector2> deltaVectors;

    public TouchControlledCamera(OrthographicCamera camera, int worldWidth, int worldHeight) {
        super(camera, worldWidth, worldHeight);
        pivots = new HashMap<>();
        deltaVectors = new HashMap<>();
    }

    public void setPivotPoint(float screenX, float screenY, int pointer) {
        this.pivots.put(pointer, new Vector2(
                camera.position.x + (screenX * camera.zoom),
                camera.position.y + (screenY * camera.zoom))
        );
    }

    public void dragTo(float x, float y, int pointer) {
        Vector2 pivotCurrent = pivots.get(pointer);
        if (pivotCurrent != null) {
            deltaVectors.put(pointer, new Vector2(
                    pivotCurrent.x - (x * camera.zoom),
                    pivotCurrent.y - (y * camera.zoom))
            );
        }

        if (pivots.size() == 1) {
            Vector2 pivot = deltaVectors.get(0);
            setX(pivot.x);
            setY(pivot.y);
        } else if (pivots.size() == 2) {
            Vector2[] pivotArray = new Vector2[2];
            pivots.values().toArray(pivotArray);
            Vector2[] deltasArray = new Vector2[2];
            deltaVectors.values().toArray(deltasArray);

            Vector2 distanceBetweenPivots = pivotArray[0].sub(pivotArray[1]);
            Vector2 directionA = distanceBetweenPivots.mulAdd(deltasArray[0], 0);
            Vector2 directionB = distanceBetweenPivots.mulAdd(deltasArray[1], 0);
        }
    }

    public void liftPivot(int pointer) {
        pivots.remove(pointer);
        deltaVectors.remove(pointer);
    }
}
