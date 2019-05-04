package com.github.eoinf.jiggen.webapp;

public enum JiggenState {
    LOADED("LOADED"),
    PUZZLE_COMPLETE("PUZZLE_COMPLETE");

    private String value;

    JiggenState(String state) {
        this.value = state;
    }

    public String getValue() {
        return this.value;
    }
}
