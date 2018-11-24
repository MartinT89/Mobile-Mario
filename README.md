# MoreMonkeysMakeMario
Martin Trinidad (ID: 39621050)
Paul John Evangelista (ID: 46237144)

In this project, we implemented a version of the popular game which runs on an Android phone. To do so, we used Java as well as the LibGDX library, with Box2D included. 

We first began play making the levels. In order to make the levels we used a program known as Tiled. Tiled creates a map through a grid system, filing and creating new layers of tiles within the grids. The placement of theses layers determines what is drawn on screen, what kind of layer it is, and whether "Mario" can come into contact with it. Once the tiled maps was created, it was loaded with the libGDX API. From there, the all layers are analyzed, stored, and drawn into with what is known as a "fixture" or "body". What this is, is a collidable shape drawn onto the screen and holds physically properties. An example of how these bodies are drawn on screen is shown below. 
      

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

Mario, the Enemies, and the items are all implemented very similiarly. They are their own classes, Mario being one, each different enemy being it's on class, and the items all in once class. They are drawn the same way the tiles are drawn, with fixtures and a body. These fixtures and bodies and then given what is known as "User Data" which will be used later to find contact and status of each enemy. Their movement is also implemented very similiarly. Because of the Box2D library, physics like propeties are given to drawn bodies if labeled as "Dynamic". For Mario, we increased his horizontal velocity by negative of positive depending on the input of the player. The enemies are given restricted movement, varying from only moving back and fourth in a specific space, to only showing when Mario is around. Finally, the items are drawn above what we called the "coin" brick layer. The type of item is randomized when drawn as well. The items are drawn as what is called a sensor body, meaning that the Contact Handler we implemented will recognize collision with the body, but with allow the object to pass through it. When the brick under a said item has been broken by Mario's head, the item will draw and a flag will be called which allows Mario to change into big mario, fire mario, and just get coins (depending on the item), when Mario touches said item. 

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

One of the biggest challanges was to figure out detecting collision. Thankfully, libgdx comes with a class known as Contact Handler. This is a thread which allows the game to find the collisions throughout the game. In order to filter out which collisions and occur and what happens when they occur, we needed to set every drawn bodies user data. Mario's user data was set to either his body or his head, since there were 2 fixutres drawn on him. Enemies bodies were set to bit 8, bricks set to bit 4, and coin bricks set to bit 2. The bits which Mario was allowed to collide with, or what the game refers to as the mask bits, we also set. Through some if statements, we check to see if certain collisions occur.
  
   -For example, Mario's body colliding with bit 8, which is an enemy, would cause the death flags to come up for Mario.
    
   -Or Mario's body colliding with bit 16, which is an enemies head, would cause the death flags to come up for the contacted Enemy. 
   
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
            
