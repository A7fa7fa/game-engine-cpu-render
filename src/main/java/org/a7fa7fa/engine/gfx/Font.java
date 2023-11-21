package org.a7fa7fa.engine.gfx;

import org.a7fa7fa.engine.Color;

public class Font {

    public static final Font STANDARD = new Font("/fonts/standard.png");

    private Image fontImage;
    private int[] offsets;
    private int[] widths;

    private final int FIRST_UNICODE_CHAR = 32;
    private final int LAST_UNICODE_CHAR = 90;

    public Font(String path) {
        fontImage = new Image(path);

        offsets = new int[LAST_UNICODE_CHAR - FIRST_UNICODE_CHAR + 1]; // 58 different characters unicode (space 32 -> uppercase Z 90)
        widths = new int[LAST_UNICODE_CHAR - FIRST_UNICODE_CHAR + 1];

        int unicode = 0;
        for (int i = 0; i < fontImage.getWidth(); i++) {
            if (fontImage.getPixelValue(i, 0) == Color.FONT_START_SEP.getHexValue()) {
                offsets[unicode] = i;
            }
            if (fontImage.getPixelValue(i, 0) == Color.FONT_ENP_SEP.getHexValue()) {
                widths[unicode] = i - offsets[unicode];
                unicode++;
            }
        }
    }

    public int getFIRST_UNICODE_CHAR() {
        return FIRST_UNICODE_CHAR;
    }

    public Image getFontImage() {
        return fontImage;
    }

    public int[] getWidths() {
        return widths;
    }

    public int[] getOffsets() {
        return offsets;
    }
}
