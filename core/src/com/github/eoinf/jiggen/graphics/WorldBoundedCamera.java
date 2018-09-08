package com.github.eoinf.jiggen.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class WorldBoundedCamera extends OrthographicCamera {
    private static float minZoom = 0.5f;

    private float worldWidth;
    private float worldHeight;
    private float maxZoom;

    @Override
    public void update() {
        adjustPositionToWorldBounds();
        adjustZoomToWorldBounds();
        super.update();
    }

    private void adjustZoomToWorldBounds() {
        if (zoom > maxZoom) {
            setZoom(maxZoom);
        } else if (zoom < minZoom) {
            setZoom(minZoom);
        }
    }

    private void adjustPositionToWorldBounds() {
        float x = x();
        float y = y();
        float minX = (this.zoom * this.viewportWidth / 2f);
        float maxX = worldWidth - minX;
        float minY = (this.zoom * this.viewportHeight / 2f);
        float maxY = worldHeight - minY;

        if (x > maxX) {
            this.setX(maxX);
        } else if (x < minX) {
            this.setX(minX);
        }
        if (y > maxY) {
            this.setY(maxY);
        } else if (y < minY) {
            this.setY(minY);
        }
    }

    public float x() {
        return this.position.x;
    }

    public float y() {
        return this.position.y;
    }

    public void setCameraBounds(float worldWidth, float worldHeight, float maxZoom) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.maxZoom = maxZoom;
    }

    public void setX(float x) {
        this.position.x = x;
    }

    public void setY(float y) {
        this.position.y = y;
    }

    void setZoom(float newZoom) {
        newZoom = Math.min(maxZoom, newZoom);
        this.zoom = Math.max(minZoom, newZoom);
    }
}
