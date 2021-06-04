package com.imap.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class IsometricRender {

    public static final int TILE_WIDTH = 34;
    public static final int TILE_HEIGHT = 38;

    private Texture grass;
    private Texture water;
    private Texture sand;

    public IsometricRender() {
        grass = new Texture(Gdx.files.internal("grass.png"));
        water = new Texture(Gdx.files.internal("water.png"));
        sand = new Texture(Gdx.files.internal("sand.png"));
    }

    public void drawGround(SpriteBatch batch) {
        for (int row = 15; row >= 0; row--) {
            for (int col = 15; col >= 0; col--) {
                float x = (col - row) * (TILE_WIDTH/2f);
                float y = (col + row) * (TILE_WIDTH/4f);

                if(row/2f != row/2)
                    batch.draw(water, x, y, TILE_WIDTH, 32);
                else
                    batch.draw(grass, x, y, TILE_WIDTH, TILE_HEIGHT);
            }
        }
    }
}
