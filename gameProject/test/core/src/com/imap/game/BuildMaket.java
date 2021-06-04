package com.imap.game;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import lombok.Data;

@Data
@Entity(tableName = "makets")
public class BuildMaket {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private Vector3 size;
    private Array<Texture> tileset;

    @Ignore
    public BuildMaket(String name, Vector3 size, Array<Texture> tileset) {
        this.name = name;
        this.size = size;
        this.tileset = tileset;
    }

    public BuildMaket(int id, String name, Vector3 size, Array<Texture> tileset) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.tileset = tileset;
    }

    public String getName() {
        return name;
    }

    public Vector3 getSize() {
        return size;
    }

    public Array<Texture> getTileset() {
        return tileset;
    }
}
