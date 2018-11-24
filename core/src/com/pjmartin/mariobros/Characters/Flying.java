package com.pjmartin.mariobros.Characters;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.pjmartin.mariobros.MarioBros;
import com.pjmartin.mariobros.shroud.MainScreen;

public class Flying extends Sprite {
    public boolean running;
    public boolean moving;
    public boolean left;
    public int state;
    public float vel;
    public World world;
    public Body body1;
    private TextureRegion test;
    public TextureRegion action[];
    public TextureRegion action2[];
    public boolean dead;
    public float detect;

    public Flying(World world, float x){

        moving = true;

        detect = x;
        dead = false;
        left = false;
        running = false;
        state = 1;
        this.world = world;
        defMario(x);
        action = new TextureRegion[4];
        action2 = new TextureRegion[4];

        action[0] = new TextureRegion(new Texture("bird.png"),0,1,1600,815);
        action[1] = new TextureRegion(new Texture("bird.png"),0,1,1600,815);
        action[1].flip(true,false);
        setBounds(0,0,.60f,.40f);
        setRegion(action[0]);
    }

    public void update(float time){
        if(body1.getPosition().x <= detect - (4* .16))
            moving =false;

        if(body1.getPosition().x > detect + (4* .16))
            moving =true;

            if(moving) {
                setRegion(action[1]);
                body1.setLinearVelocity(new Vector2(-1.5f, 0));
            }

            if(!moving) {
                setRegion(action[0]);
                body1.setLinearVelocity(new Vector2(1.5f, 0));
            }


        setPosition(body1.getPosition().x - getWidth() / 2,body1.getPosition().y - getHeight() / 2);
    }
    public void defMario(float x) {
        BodyDef bodyD = new BodyDef();
        bodyD.position.set(x, 150 / MarioBros.Pixels);
        bodyD.type = BodyDef.BodyType.KinematicBody;
        body1 = world.createBody(bodyD);

        FixtureDef fixture = new FixtureDef();
        PolygonShape Rectangle  = new PolygonShape();
        Rectangle.setAsBox(.10f,.10f);
        fixture.filter.categoryBits = 16;

        fixture.shape = Rectangle;
        body1.createFixture(fixture).setUserData(this);

    }


}