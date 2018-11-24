package com.pjmartin.mariobros.Pictures;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pjmartin.mariobros.MarioBros;

public class HUD implements Disposable{
    private Viewport port;
    public Stage stage;
    private float counter;
    public int score;

    Label livesLab;
    Label lifeLab;
    Label pointsLab;
    Label levelLab;
    Label worldLab;
    Label scoreLab;

    public HUD(SpriteBatch b,int lives, int marioscore, String marioworld) {
        score = marioscore;

        port = new FitViewport(MarioBros.WIDTH, MarioBros.HEIGHT, new OrthographicCamera());
        stage = new Stage(port, b);

        lifeLab = new Label(String.format("%01d", lives), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        pointsLab = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        livesLab = new Label(String.format("LIVES"), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLab = new Label(String.format("WORLD"), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLab = new Label(String.format( marioworld), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLab = new Label(String.format("SCORE"), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        table.add(livesLab).expandX().padTop(10);
        table.add(worldLab).expandX().padTop(10);
        table.add(scoreLab).expandX().padTop(10);
        table.row();
        table.add(lifeLab).expandX().padTop(10);
        table.add(levelLab).expandX().padTop(10);
        table.add(pointsLab).expandX().padTop(10);

        stage.addActor(table);
    }

    public void update(float dt){
        pointsLab.setText(String.format("%06d",score));
    }
    public void dispose(){
        stage.dispose();
    }

}