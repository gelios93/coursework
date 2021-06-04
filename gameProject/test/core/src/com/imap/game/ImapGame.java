package com.imap.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.Array;

public class ImapGame extends Game {
	private SpriteBatch batch;
	private GameScreen screen;
	private Array<BuildMaket> makets;

	public ImapGame(Array<BuildMaket> makets) {
		this.makets = makets;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		screen = new GameScreen(batch, makets);
		setScreen(screen);
		InputMultiplexer im = new InputMultiplexer();
		GestureDetector gd = new GestureDetector(screen);
		im.addProcessor(gd);
		im.addProcessor(screen);
		Gdx.input.setInputProcessor(im);
	}

	@Override
	public void render () {
		super.render();
	}


	
	@Override
	public void dispose () {
		batch.dispose();
		super.dispose();
	}
}
