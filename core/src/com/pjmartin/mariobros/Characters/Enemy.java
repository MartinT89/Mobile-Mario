package com.pjmartin.mariobros.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.pjmartin.mariobros.MarioBros;

public class Enemy extends Sprite {

    public boolean running;
    public boolean destroyed;
    public boolean erase;
    public boolean left;
    public int state;
    public float vel;
    public World world;
    public Body body1;
    private TextureRegion test;
    public TextureRegion action[];


    public Enemy(World world,float x) {
        destroyed = false;
        left = false;
        running = false;
        state = 1;
        this.world = world;
        defEnemy(x);

        action = new TextureRegion[4];

        action[0] = new TextureRegion(new Texture("Mario_and_Enemies.png"),386,38,16,30);
        action[1] = new TextureRegion(new Texture("Mario_and_Enemies.png"),403,38,16,30);
        action[2] = new TextureRegion(new Texture("Mario_and_Enemies.png"),386,38,16,30);

        action[2].flip(true,false);

        setBounds(0,0,.16f,.30f);
        setRegion(action[0]);
    }

    public void update(float time){
        if(destroyed) {
            setRegion(action[1]);
        }

        if(!destroyed) {
            setRegion(action[0]);
            if (!left) {
                setRegion(action[2]);
                body1.applyLinearImpulse(new Vector2(0.07f, 0), body1.getWorldCenter(), true);
            }
            if (left){
                setRegion(action[0]);
                body1.applyLinearImpulse(new Vector2(-0.07f, 0), body1.getWorldCenter(), true);}
            setPosition(body1.getPosition().x - getWidth() / 2, body1.getPosition().y - getHeight() / 2);
        }
    }

    public void defEnemy(float x) {
        BodyDef bodyD = new BodyDef();
        bodyD.position.set(x, 32 / MarioBros.Pixels);
        bodyD.type = BodyDef.BodyType.DynamicBody;
        body1 = world.createBody(bodyD);

        FixtureDef fixture = new FixtureDef();
        PolygonShape Rectangle  = new PolygonShape();
        Rectangle.setAsBox(.10f,.10f);
        fixture.filter.categoryBits = 2;

        fixture.shape = Rectangle;
        body1.createFixture(fixture).setUserData(this);


        EdgeShape top = new EdgeShape();
        top.set(new Vector2(-.1f, .15f),new Vector2(.1f, .15f));
        fixture.filter.categoryBits = 8;
        fixture.shape = top;
        body1.createFixture(fixture).setUserData(this);
    }

    public void OnHit(){
    destroyed = true;

    }
}
