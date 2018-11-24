package com.pjmartin.mariobros.shroud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pjmartin.mariobros.Characters.Enemy;
import com.pjmartin.mariobros.Characters.Flying;
import com.pjmartin.mariobros.Characters.Items;
import com.pjmartin.mariobros.Characters.Mario;
import com.pjmartin.mariobros.Characters.piranha;
import com.pjmartin.mariobros.MarioBros;
import com.pjmartin.mariobros.Pictures.HUD;

import java.util.Random;

public class MainScreen implements Screen, ContactListener {

    private MarioBros play;
    private HUD hud;

    private OrthographicCamera cam;
    private Viewport port;

    private TmxMapLoader mapLoad;
    private TiledMap map;
    private OrthogonalTiledMapRenderer render;

    private World world;
    private Box2DDebugRenderer box;

    private Mario player;

    public Filter destroyed;
    public Filter broken;
    private Array<Enemy> goomba;
    private Array<Flying> flying;
    private Array<piranha> pir;
    public Array<Items> coin;

    public Random random = new Random();


    public MainScreen(MarioBros play) {
        destroyed = new Filter();
        destroyed.categoryBits = 64;
        coin = new Array<Items>();
        goomba = new Array<Enemy>();
        flying = new Array<Flying>();
        pir = new Array<piranha>();

        broken = new Filter();
        broken.categoryBits = MarioBros.BrokenBlock;
        this.play = play;
        cam = new OrthographicCamera();
        port = new FitViewport(MarioBros.WIDTH / MarioBros.Pixels, MarioBros.HEIGHT / MarioBros.Pixels, cam);
        hud = new HUD(play.a, MarioBros.lives, MarioBros.marioScore,"1-1" );

        mapLoad = new TmxMapLoader();
        map = mapLoad.load("level1.tmx");
        render = new OrthogonalTiledMapRenderer(map, 1 / MarioBros.Pixels);
        cam.position.set(port.getWorldWidth()/2, port.getWorldHeight()/2, 0);

        world = new World(new Vector2(0,-10), true);
        world.setContactListener(this);
        box = new Box2DDebugRenderer();
        player = new Mario(world);

        //flying.add (new Flying(world, 1000/MarioBros.Pixels));

        BodyDef bodyD = new BodyDef();
        PolygonShape poly = new PolygonShape();
        FixtureDef fixture = new FixtureDef();
        Filter filter = new Filter();
        filter.categoryBits = 1;
        Body body;

        //ground texture
        for (MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            bodyD.type = BodyDef.BodyType.StaticBody;
            bodyD.position.set((rectangle.getX() + rectangle.getWidth()/2) / MarioBros.Pixels, (rectangle.getY() + rectangle.getHeight()/2) / MarioBros.Pixels);

            body = world.createBody(bodyD);


            poly.setAsBox(rectangle.getWidth() / 2 / MarioBros.Pixels, rectangle.getHeight() / 2 / MarioBros.Pixels);
            fixture.shape = poly;
            Fixture temp = body.createFixture(fixture);
            temp.setUserData("Bitch");
        }

        //piranha
        for (MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            pir.add(new piranha(world,(rectangle.getX() + rectangle.getWidth()/2) / MarioBros.Pixels));
        }

        //coin texture
        for (MapObject object: map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            bodyD.type = BodyDef.BodyType.StaticBody;
            bodyD.position.set((rectangle.getX() + rectangle.getWidth()/2) / MarioBros.Pixels, (rectangle.getY() + rectangle.getHeight()/2) / MarioBros.Pixels);

            body = world.createBody(bodyD);

            coin.add(new Items(world,(rectangle.getX() + rectangle.getWidth()/2) / MarioBros.Pixels, (rectangle.getY() +  2 * rectangle.getHeight()) / MarioBros.Pixels,
                    random.nextInt(4)));

            poly.setAsBox(rectangle.getWidth() / 2 / MarioBros.Pixels, rectangle.getHeight() / 2 / MarioBros.Pixels);
            fixture.shape = poly;
            Fixture temp = body.createFixture(fixture);
            temp.setFilterData(filter);
            temp.setUserData("Question");
    }
        //brick texture
        for (MapObject object: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            bodyD.type = BodyDef.BodyType.StaticBody;
            bodyD.position.set((rectangle.getX() + rectangle.getWidth()/2) / MarioBros.Pixels, (rectangle.getY() + rectangle.getHeight()/2) / MarioBros.Pixels);

            body = world.createBody(bodyD);

            poly.setAsBox(rectangle.getWidth() / 2 / MarioBros.Pixels, rectangle.getHeight() / 2 / MarioBros.Pixels);
            fixture.shape = poly;
            body.createFixture(fixture).setUserData("Brick");

        }

        //goomba
        for (MapObject object: map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            goomba.add(new Enemy(world,(rectangle.getX() + rectangle.getWidth()/2) / MarioBros.Pixels));
        }

        //flying stuff
        for (MapObject object: map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            flying.add(new Flying(world,(rectangle.getX() + rectangle.getWidth()/2) / MarioBros.Pixels));
        }

        //pipe texture
        for (MapObject object: map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            bodyD.type = BodyDef.BodyType.StaticBody;
            bodyD.position.set((rectangle.getX() + rectangle.getWidth()/2) / MarioBros.Pixels, (rectangle.getY() + rectangle.getHeight()/2) / MarioBros.Pixels);

            body = world.createBody(bodyD);

            poly.setAsBox(rectangle.getWidth() / 2 / MarioBros.Pixels, rectangle.getHeight() / 2 / MarioBros.Pixels);
            fixture.shape = poly;
            body.createFixture(fixture).setUserData("Pipe");
        }

    }

    @Override
    public void show() {

    }

    public void handleInput(float deltaTime) {

        if (Gdx.input.isKeyPressed(Input.Keys.UP) && player.body1.getLinearVelocity().y == 0)
            player.body1.applyLinearImpulse(new Vector2(0, 4f), player.body1.getWorldCenter(), true);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.body1.getLinearVelocity().x <= 2){
            player.left = false;
            player.body1.applyLinearImpulse(new Vector2(0.07f, 0), player.body1.getWorldCenter(), true); }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.body1.getLinearVelocity().x >= -2) {
            player.left = true;
            player.body1.applyLinearImpulse(new Vector2(-0.07f, 0), player.body1.getWorldCenter(), true); }
        //if (Gdx.input.isKeyPressed(Input.Keys.SPACE)
    }

    public void update(float deltaTime) {

        handleInput(deltaTime);
        player.vel = player.body1.getLinearVelocity().x;
        world.step(1/60f, 6, 2);
        player.update(deltaTime);
        cam.position.x = player.body1.getPosition().x;


        for(Enemy i: goomba)
            i.update(deltaTime);

        for(Flying i: flying)
            i.update(deltaTime);

        for(Items i: coin)
            i.update(deltaTime);

        for(piranha i: pir)
            i.update(deltaTime);

        if(player.dead)
            System.out.println("DEAD");

        cam.update();
        hud.update(deltaTime);
        render.setView(cam);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        render.render();

        box.render(world, cam.combined);

        play.a.setProjectionMatrix(cam.combined);
        play.a.begin();
        player.draw(play.a);

        for(Enemy i: goomba)
            i.draw(play.a);

        for(Flying i: flying)
            i.draw(play.a);

        for(Items i: coin)
            i.draw(play.a);

        for(piranha i: pir)
            i.draw(play.a);

        play.a.end();


        play.a.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if(player.detect > 31.9){
            MarioBros.marioScore = hud.score;
            play.setScreen(new MainScreen2(play));
            dispose();
        }
        //31.9
        //5

        if(player.dead) {
            MarioBros.marioScore = hud.score;
            play.setScreen(new MainScreen(play));
            dispose();
        }

        if(MarioBros.lives == -1) {
            MarioBros.lives = 3;
            MarioBros.marioScore = 0;
            play.setScreen(new MainScreen(play));
            //dispose();
        }



    }

    @Override
    public void resize(int width, int height) {
        port.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        render.dispose();
        world.dispose();
        box.dispose();
        hud.dispose();
    }

    @Override
    public void beginContact(Contact contact) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        TiledMapTileSet set = map.getTileSets().getTileSet("tileset_gutter");
        Fixture firstbody = null;
        Fixture secondbody = null;

        if(contact.getFixtureA().getUserData() == "Mario's head"||contact.getFixtureA().getUserData() == "Mario"){
            firstbody = contact.getFixtureA();
            secondbody = contact.getFixtureB();
        }

        if (contact.getFixtureB().getUserData() == "Mario's head"||contact.getFixtureB().getUserData() == "Mario"){
            firstbody = contact.getFixtureB();
            secondbody = contact.getFixtureA();
        }

        if(contact.getFixtureA().getFilterData().categoryBits == 2||contact.getFixtureB().getFilterData().categoryBits == 2){
            if(contact.getFixtureA().getFilterData().categoryBits == 2)
                ((Enemy) contact.getFixtureA().getUserData()).left = !(((Enemy) contact.getFixtureA().getUserData()).left);
            else
                ((Enemy) contact.getFixtureB().getUserData()).left = !(((Enemy) contact.getFixtureB().getUserData()).left);
        }

        if(firstbody != null && secondbody !=null){
            System.out.println(firstbody.getUserData() + " collided with " + secondbody.getUserData());

            if(firstbody.getUserData() == "Mario's head" && secondbody.getUserData() == "Question"){
                for(Items i: coin) {
                    float x = secondbody.getBody().getPosition().x;

                    if(i.xidentifier == x ){
                        i.activated = true;
                        break;
                    }
                }

                secondbody.setFilterData(broken);
                layer.getCell((int)(secondbody.getBody().getPosition().x * MarioBros.Pixels / 16),
                        (int)(secondbody.getBody().getPosition().y * MarioBros.Pixels / 16)).setTile(set.getTile(28));
            }

            if (firstbody.getUserData() == "Mario's head" && secondbody.getUserData() == "Brick") {
                secondbody.setFilterData(destroyed);

                layer.getCell((int)(secondbody.getBody().getPosition().x * MarioBros.Pixels / 16), (int)(secondbody.getBody().getPosition().y * MarioBros.Pixels / 16)).setTile(set.getTile(0));
            }

            if(firstbody.getUserData() == "Mario" && (secondbody.getFilterData().categoryBits == 2)){
                if(((Enemy) secondbody.getUserData()).destroyed) {
                    secondbody.setFilterData(destroyed);
                }
                else {
                    if (!player.big) {
                        player.dead = true;
                        MarioBros.lives = MarioBros.lives - 1;
                    }
                    if (player.big)
                        player.big = false;
                }
            }

            if(firstbody.getUserData() == "Mario" && (secondbody.getFilterData().categoryBits == 8)){
                secondbody.setFilterData(destroyed);
                ((Enemy) secondbody.getUserData()).OnHit();
            }

            if(firstbody.getUserData() == "Mario" && (secondbody.getFilterData().categoryBits == 16)){
                if (!player.big) {
                    player.dead = true;
                    MarioBros.lives = MarioBros.lives - 1;
                }
                if (player.big)
                    player.big = false;
            }

            if(firstbody.getUserData() == "Mario" && secondbody.getFilterData().categoryBits == 4){
                if (((Items) secondbody.getUserData()).activated ){
                    ((Items) secondbody.getUserData()).taken = true;
                    hud.score += 200;

                    if(( (Items) secondbody.getUserData()).object == 1) {
                        player.big = true;
                        hud.score += 800;
                    }

                    if (( (Items) secondbody.getUserData()).object == 3){
                        player.fire = true;
                        player.big = true;
                        hud.score += 800;
                    }
                }
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
