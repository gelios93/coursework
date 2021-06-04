package com.imap.game;

import com.badlogic.gdx.utils.Array;

public class Build {
    private BuildMaket maket;
    private Array<Coord> coords;

    public Build(BuildMaket maket, Array<Coord> coords) {
        this.maket = maket;
        this.coords = coords;
    }

    public BuildMaket getMaket() {
        return maket;
    }

    public Array<Coord> getCoords() {
        return coords;
    }
}
