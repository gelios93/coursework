package com.imap.game;

public class Coord {
    private int layer;
    private int x;
    private int y;

    public Coord(int layer, int x, int y) {
        this.layer = layer;
        this.x = x;
        this.y = y;
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
}
