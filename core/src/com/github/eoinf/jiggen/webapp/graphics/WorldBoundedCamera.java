package com.github.eoinf.jiggen.webapp.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class WorldBoundedCamera {
    private static float minZoom = 0.3f;

    private static float ZOOM_RATE = 0.05f;
    private static float ZOOM_RESET_MODIFIER = 0.8f;
    private static float ZOOM_OVERFLOW = 0.2f;
    private static float ZOOM_RESET_COOLDOWN_SECONDS = 0.3f;

    private float zoomResetCooldown;

    private float worldWidth;
    private float worldHeight;
    private float maxZoom;

    public WorldBoundedCamera() {
        this.camera = new OrthographicCamera();
    }

    private OrthographicCamera camera;
    public OrthographicCamera getCamera() {
        return this.camera;
    }

    public void adjustToWorldBounds(float delta) {
        zoomResetCooldown -= delta;
        adjustPositionToWorldBounds();
        adjustZoomToWorldBounds(delta);
    }

    private void adjustZoomToWorldBounds(float delta) {
        if (zoomResetCooldown < 0) {
            if (this.camera.zoom > maxZoom) {
                this.camera.zoom -= ZOOM_RESET_MODIFIER * delta;
            } else if (this.camera.zoom < minZoom) {
                this.camera.zoom = minZoom;
            }
        }
    }

    private void adjustPositionToWorldBounds() {
        float x = x();
        float y = y();
        float minX = (this.camera.zoom * this.camera.viewportWidth / 2f);
        float maxX = Math.max(minX, worldWidth - minX);
        float minY = (this.camera.zoom * this.camera.viewportHeight / 2f);
        float maxY = Math.max(minY, worldHeight - minY);

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
        return this.camera.position.x;
    }

    public float y() {
        return this.camera.position.y;
    }

    public void setCameraBounds(float worldWidth, float worldHeight, float maxZoom) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.maxZoom = maxZoom;

        this.camera.position.x = this.worldWidth / 2;
        this.camera.position.y = this.worldHeight / 2;
        this.camera.zoom = maxZoom;
    }

    public void setX(float x) {
        this.camera.position.x = x;
    }

    public void setY(float y) {
        this.camera.position.y = y;
    }

    public void setZoom(float newZoom) {
        newZoom = Math.min(maxZoom + ZOOM_OVERFLOW, newZoom);
        this.camera.zoom = Math.max(minZoom, newZoom);
        zoomResetCooldown = ZOOM_RESET_COOLDOWN_SECONDS;
    }

    public float getZoomRate() {
        return ZOOM_RATE * this.maxZoom;
    }
}
