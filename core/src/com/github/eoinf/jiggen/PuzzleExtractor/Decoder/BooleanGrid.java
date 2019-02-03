package com.github.eoinf.jiggen.PuzzleExtractor.Decoder;

import java.util.HashMap;
import java.util.Map;

public class BooleanGrid {
    private Map<Integer, Boolean> data;
    private int gridWidth;

    public BooleanGrid(int gridWidth) {
        this.gridWidth = gridWidth;
        data = new HashMap<>();
    }

    boolean isTrue(int x, int y) {
        Boolean entry = this.data.get(x + (y * gridWidth));
        return entry != null && entry;
    }

    void setValue(int x, int y, boolean value) {
        this.data.put(x + (y * gridWidth), value);
    }
}
