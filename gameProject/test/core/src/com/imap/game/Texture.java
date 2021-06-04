package com.imap.game;

public class Texture {
    private int layer;
    private int x;
    private int y;
    private int tileId;

    public Texture(int layer, int x, int y, int tileId) {
        this.layer = layer;
        this.x = x;
        this.y = y;
        this.tileId = tileId;
    }

    public int getLayer() {
        return layer;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getTileId() {
        return tileId;
    }
}
