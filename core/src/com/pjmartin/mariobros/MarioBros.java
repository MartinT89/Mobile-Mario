package com.pjmartin.mariobros;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pjmartin.mariobros.shroud.MainScreen;

public class MarioBros extends Game {
	public SpriteBatch a;
	public static final int WIDTH = 351;
	public static final int HEIGHT = 210;
	public static final float Pixels = 100;

	public static short Mario = 1;
	public static short Enemy = 2;
	public static short Dead = 10;
	public static short BrokenBlock = 5;
	public static int lives = 3;
	public static int marioScore = 0;

	@Override
	public void create () {
		a = new SpriteBatch();
		setScreen(new MainScreen(this));

	}

	@Override
	public void render () {
		super.render();
	}
}