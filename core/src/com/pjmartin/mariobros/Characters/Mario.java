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

public class Mario extends Sprite {
    public boolean running;
    public boolean left;
    public int state;
    public int firestate;
    public float vel;
    public World world;
    public Body body1;
    private TextureRegion test;
    public TextureRegion action[];
    public TextureRegion action2[];
    public TextureRegion firemario[];
    public TextureRegion firemario2[];
    public boolean dead;
    public float detect;
    public boolean big;
    public boolean fire;

    public Mario(World world){
        dead = false;
        left = false;
        running = false;
        fire = false;
        state = 1;
        firestate = 1;
        this.world = world;
        defMario();
        action = new TextureRegion[4];
        action2 = new TextureRegion[4];
        firemario = new TextureRegion[4];
        firemario2 = new TextureRegion[4];

        action[0] = new TextureRegion(new Texture("Sheet.png"),0,1,16,30);
        action[1] = new TextureRegion(new Texture("Sheet.png"),17,1,16,30);
        action[2] = new TextureRegion(new Texture("Sheet.png"),34,1,16,30);
        action[3] = new TextureRegion(new Texture("Sheet.png"),50,1,16,30);

        action2[0] = new TextureRegion(new Texture("Sheet.png"),0,1,16,30);
        action2[1] = new TextureRegion(new Texture("Sheet.png"),17,1,16,30);
        action2[2] = new TextureRegion(new Texture("Sheet.png"),34,1,16,30);
        action2[3] = new TextureRegion(new Texture("Sheet.png"),50,1,16,30);

        firemario[0] = new TextureRegion(new Texture("firemario.png"),80,129,16,32);
        firemario[1] = new TextureRegion(new Texture("firemario.png"),97,129,16,32);
        firemario[2] = new TextureRegion(new Texture("firemario.png"),114,129,16,32);
        firemario[3] = new TextureRegion(new Texture("firemario.png"),131,129,16,32);

        firemario2[0] = new TextureRegion(new Texture("firemario.png"),80,129,16,32);
        firemario2[1] = new TextureRegion(new Texture("firemario.png"),97,129,16,32);
        firemario2[2] = new TextureRegion(new Texture("firemario.png"),114,129,16,32);
        firemario2[3] = new TextureRegion(new Texture("firemario.png"),131,129,16,32);

        for(int i  = 0; i < 4 ; i++){
            action2[i].flip(true, false);
            firemario2[i].flip(true, false);
        }
        setBounds(0,0,.12f,.20f);
        setRegion(action[0]);
    }

    public void update(float time){

        while(vel != 0 && !fire) {
            if((state == 1)){
                if (left){
                    setRegion(action2[1]);
                    state = 2;
                    break;
                }

                setRegion(action[1]);
                state = 2;
                break;
            }

            if(state == 2){
                if (left) {
                    setRegion(action2[2]);
                    state = 1;
                    break;
                }

                setRegion(action[2]);
                state = 1;
                break;
            }
        }

        while(vel != 0 && fire) {
            if((state == 1)){
                if (left){
                    setRegion(firemario2[1]);
                    state = 2;
                    break;
                }

                setRegion(firemario[1]);
                state = 2;
                break;
            }

            if(state == 2){
                if (left) {
                    setRegion(firemario2[2]);
                    state = 1;
                    break;
                }

                setRegion(firemario[2]);
                state = 1;
                break;
            }
        }



        if(vel == 0 && !fire){
            running = false;
            if(!left)
                setRegion(action[0]);
            if(left)
                setRegion(action2[0]);
        }

        if(vel == 0 && fire){
            running = false;
            if(!left)
                setRegion(firemario[0]);
            if(left)
                setRegion(firemario2[0]);
        }

        if(big)
            setBounds(0,0,.16f,.30f);
        if(!big)
            setBounds(0,0,.12f,.20f);
        if(!big && !fire)
            setBounds(0,0,.12f,.20f);

        setPosition(body1.getPosition().x - getWidth() / 2,body1.getPosition().y - getHeight() / 2);
        detect =   body1.getPosition().x - getWidth() / 2;
        if(body1.getPosition().y < -1) {
            MarioBros.lives -= 1;
            dead = true;
        }
    }

    public void defMario() {
        BodyDef bodyD = new BodyDef();
        bodyD.position.set(32 / MarioBros.Pixels, 32 / MarioBros.Pixels);
        bodyD.type = BodyDef.BodyType.DynamicBody;
        body1 = world.createBody(bodyD);

        FixtureDef fixture = new FixtureDef();
        PolygonShape Rectangle  = new PolygonShape();
        Rectangle.setAsBox(.10f, .10f);
        fixture.filter.categoryBits = 1;
        fixture.filter.maskBits = 1|2|4|8|16 ;

        fixture.shape = Rectangle;
        body1.createFixture(fixture).setUserData("Mario");

        EdgeShape top = new EdgeShape();
        top.set(new Vector2(-.05f, .15f),new Vector2(.05f, .15f));
        fixture.filter.categoryBits = 1;
        fixture.filter.maskBits = 1|2|4 ;
        fixture.shape = top;
        fixture.isSensor=true;
        body1.createFixture(fixture).setUserData("Mario's head");
    }
}
