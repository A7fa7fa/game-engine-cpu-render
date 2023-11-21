package org.a7fa7fa.engine.gfx;

public class ImageTile extends Image {

    private int tileHeight;
    private int tileWidth;

    public ImageTile(String path, int tileWidth, int tileHeight){
        super(path);
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(int tileHeight) {
        this.tileHeight = tileHeight;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
    }
}
