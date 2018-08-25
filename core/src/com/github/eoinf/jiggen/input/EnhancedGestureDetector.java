package com.github.eoinf.jiggen.input;

import com.badlogic.gdx.input.GestureDetector;

public class EnhancedGestureDetector extends GestureDetector {

    private EnhancedGestureListener listener;
    public EnhancedGestureDetector(EnhancedGestureListener listener) {
        super(listener);
        this.listener = listener;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        listener.touchUp(screenX, screenY, pointer, button);
        return super.touchUp(screenX, screenY, pointer, button);
    }
}
