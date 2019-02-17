package com.github.eoinf.jiggen.webapp.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class WorldBoundedCamera extends OrthographicCamera {
    private static float minZoom = 0.3f;

    private static float ZOOM_RATE = 0.05f;
    private static float ZOOM_MODIFIER = 0.008f;
    private static float ZOOM_OVERFLOW = 0.2f;
    private static int ZOOM_RESET_COOLDOWN = 20;

    private int zoomResetCooldown;

    private float worldWidth;
    private float worldHeight;
    private float maxZoom;

    @Override
    public void update() {
        zoomResetCooldown--;
        adjustPositionToWorldBounds();
        adjustZoomToWorldBounds();
        super.update();
    }

    private void adjustZoomToWorldBounds() {
        if (zoomResetCooldown < 0) {
            if (zoom > maxZoom) {
                this.zoom -= ZOOM_MODIFIER;
            } else if (zoom < minZoom) {
                this.zoom = minZoom;
            }
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

        this.position.x = this.worldWidth / 2;
        this.position.y = this.worldHeight / 2;
        this.zoom = maxZoom;
    }

    public void setX(float x) {
        this.position.x = x;
    }

    public void setY(float y) {
        this.position.y = y;
    }

    public void setZoom(float newZoom) {
        newZoom = Math.min(maxZoom + ZOOM_OVERFLOW, newZoom);
        this.zoom = Math.max(minZoom, newZoom);
        zoomResetCooldown = ZOOM_RESET_COOLDOWN;
    }

    public float getZoomRate() {
        return ZOOM_RATE * this.maxZoom;
    }
}
