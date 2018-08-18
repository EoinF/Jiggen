package com.github.eoinf.jiggen;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class GenerateAssets {

    public static void main(String[] args) {
        TexturePacker.Settings settings = new TexturePacker.Settings();
        TexturePacker.process(settings, "../atlas", "../core/assets/ui", "ui");
    }
}