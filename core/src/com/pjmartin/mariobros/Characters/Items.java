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

public class Items extends Sprite {
    public boolean running;
    public boolean activated;
    public int type;
    public float vel;
    public World world;
    public Body body1;
    private Texture item;
    public TextureRegion action[];
    public TextureRegion action2[];
    public float xidentifier, yidentifier;
    public boolean collected;
    public boolean taken;
    public int object;


    public Items(World world,float x,float y,int type){
        object = type;
        xidentifier = x;
        yidentifier = y;
        activated = false;
        running = false;
        this.world = world;
        defItems( x, y);
        action = new TextureRegion[5];

        action[0] = new TextureRegion(new Texture("Mario_and_Enemies.png"),243,1,16,1);

        if(type == 0)
            item = new Texture("coin.png");
        if(type == 1)
            item = new Texture("shroom.png");
        if(type == 2)
            item = new Texture("coin.png");
        if(type == 3)
            item = new Texture("fireflower.png");


        setBounds(0,0,.16f,.16f);
        setRegion(action[0]);
    }

    public void update(float time){

        if(activated){
            if(taken)
                setRegion(action[0]);
            else
                setRegion(item);
            }

        setPosition(body1.getPosition().x - getWidth() / 2,body1.getPosition().y - getHeight() / 2);
    }

    public void defItems(float x,float y) {
        BodyDef bodyD = new BodyDef();
        bodyD.position.set(x, y);
        bodyD.type = BodyDef.BodyType.StaticBody;
        body1 = world.createBody(bodyD);


        FixtureDef fixture = new FixtureDef();
        PolygonShape Rectangle  = new PolygonShape();
        Rectangle.setAsBox(.05f,.05f);
        fixture.filter.categoryBits = 4;

        fixture.shape = Rectangle;
        fixture.isSensor = true;
        body1.createFixture(fixture).setUserData(this);

    }


}