package com.github.eoinf.jiggen.views.Screens;

import com.badlogic.gdx.input.GestureDetector;

public interface EnhancedGestureListener extends GestureDetector.GestureListener {
    boolean touchUp(float x, float y, int pointer, int button);
}
