package org.a7fa7fa.engine;

public enum Color {
    WHITE(0xffffffff),
    BLACK(0xff000000),
    DONT_RENDER_COLOR(0xffff00ff),
    FONT_START_SEP(0xff0000ff),
    FONT_ENP_SEP(0xffffff00)
    ;

    private final int hex;

    private Color(int hex) {
        this.hex = hex;
    }

    public int getHexValue() {
        return this.hex;
    }
}