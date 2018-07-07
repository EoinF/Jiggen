package com.github.eoinf.jiggen.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class WorldBoundedCamera {
    public OrthographicCamera camera;
    private int worldWidth;
    private int worldHeight;

    private static float minZoom = 0.5f;

    public WorldBoundedCamera(OrthographicCamera camera, int worldWidth, int worldHeight) {
        this.camera = camera;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    public void setWorldBounds(int worldWidth, int worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    public void update() {
        setX(camera.position.x);
        setY(camera.position.y);
        camera.update();
    }

    public float x() {
        return camera.position.x;
    }

    public float y() {
        return camera.position.y;
    }

    protected void setX(float x) {
        float halfCameraCoverageX = camera.zoom * camera.viewportWidth / 2;
        float minX = Math.min(worldWidth / 2, halfCameraCoverageX);
        float maxX = worldWidth - minX;

        if (x > maxX) {
            x = maxX;
        }
        else if (x < minX){
            x = minX;
        }
        camera.position.x = x;
    }

    protected void setY(float y) {
        float cameraCoverageY = camera.zoom * camera.viewportHeight / 2;
        float minY = Math.min(worldHeight / 2, cameraCoverageY);
        float maxY = worldHeight - minY;

        if (y > maxY) {
            y = maxY;
        }
        else if (y < minY){
            y = minY;
        }

        camera.position.y = y;
    }

    public int worldWidth() {
        return worldWidth;
    }

    public int worldHeight() {
        return worldHeight;
    }

    public void centreCamera() {
        this.camera.position.x = this.worldWidth / 2.0f;
        this.camera.position.y = this.worldHeight / 2.0f;
    }

    public void zoomBy(float zoomDelta) {
        setZoom(camera.zoom + zoomDelta);
    }

    void setZoom(float newZoom) {
        float maxZoomX = worldWidth / camera.viewportWidth;
        float maxZoomY = worldHeight / camera.viewportHeight;
        float maxZoom = Math.max(maxZoomX, maxZoomY);

        newZoom = Math.min(maxZoom, newZoom);
        camera.zoom = Math.max(minZoom, newZoom);

        // Refresh the camera position based on the new zoom
        setX(camera.position.x);
        setY(camera.position.y);
    }
}
