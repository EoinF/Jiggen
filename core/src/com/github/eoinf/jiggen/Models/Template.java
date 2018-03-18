package com.github.eoinf.jiggen.Models;

import com.badlogic.gdx.graphics.Texture;

public interface Template {
    String getId();
    String getName();

    Texture getTexture();
    String getExtension();
}
