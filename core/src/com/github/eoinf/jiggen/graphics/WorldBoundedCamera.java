package com.github.eoinf.jiggen.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class WorldBoundedCamera {
    public OrthographicCamera camera;

    private static float minZoom = 0.5f;

    private float worldWidth;
    private float worldHeight;
    private float maxZoom;


    public WorldBoundedCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    public void update() {
        adjustPositionToWorldBounds();
        adjustZoomToWorldBounds();
        camera.update();
    }

    private void adjustZoomToWorldBounds() {
        if (camera.zoom > maxZoom) {
            setZoom(maxZoom);
        } else if (camera.zoom < minZoom) {
            setZoom(minZoom);
        }
    }

    private void adjustPositionToWorldBounds() {
        float x = x();
        float y = y();
        float minX = (this.camera.zoom * this.camera.viewportWidth / 2f);
        float maxX = worldWidth - minX;
        float minY = (this.camera.zoom * this.camera.viewportHeight / 2f);
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
        return camera.position.x;
    }

    public float y() {
        return camera.position.y;
    }

    public void setCameraBounds(float worldWidth, float worldHeight, float maxZoom) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.maxZoom = maxZoom;
    }

    public void setX(float x) {
        camera.position.x = x;
    }

    public void setY(float y) {
        camera.position.y = y;
    }

    public void zoomBy(float zoomDelta) {
        setZoom(camera.zoom + zoomDelta);
    }

    public void translate(float deltaX, float deltaY) {
        this.camera.translate(deltaX, deltaY);
    }

    void setZoom(float newZoom) {
        newZoom = Math.min(maxZoom, newZoom);
        camera.zoom = Math.max(minZoom, newZoom);
    }
}
