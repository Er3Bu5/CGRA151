import com.sanjay900.ProcessingRunner;
import processing.core.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends PApplet {

    public static void main(String[] args) {
        ProcessingRunner.run(new Main());
    }

    public static Main instance;
    PImage img;
    String state = "Menu";
    public Player P1;
    public ArrayList<Tile> tiles = new ArrayList<Tile>();
    public ArrayList<Class<? extends Tile>> death = new ArrayList<Class<? extends Tile>>();
    public ArrayList<Button> menu = new ArrayList<Button>();
    private int levelWidth, levelHeight;

    PVector windowPos, windowSize;
    PVector start, end;
    public Rectangle windowBounds;

    private PVector playerSize; //player size
    float levelTimer;
    int tileSize = 40;

    public void settings() {
        size(1366, 768, P2D);
        instance = this;
        levelWidth = 1920 * 2;
        levelHeight = 1080 * 2;
        //img = loadImage("background.png");
        windowPos = new PVector(0, 0);
        windowSize = new PVector(width, height);
        windowBounds = new Rectangle(windowPos, windowSize);
        playerSize = new PVector(32/*width*/, 26 /*height*/);
        menu.add(new Button("Start",width / 2, height / 10, 300, 80,  new Runnable() {
            public void run() {
                P1.resetPos();
                startTimer();
                state = "G";
            }
        }));
        menu.add(new Button("Credits", width / 2,height / 10+height / 9, 300, 80, new Runnable() {
            public void run() {
                state = "C";
            }
        }));
        menu.add(new Button("Quit",  width / 2, height / 10+height / 9+height / 9, 300, 80,new Runnable() {
            public void run() {
                exit();
            }
        }));
    }
    PShape shape;

    public void startTimer() {
        levelTimer = frameCount;
    }

    public void setup() {
        death.add(ElectricArc.class);
        death.add(Saw.class);
        death.add(Laser.class);
        death.add(GlassShard.class);
        frameRate(100);
        PImage level = loadImage("level1.png");
        loadLevel(level);
        P1 = new Player(start.copy(), playerSize);

        shape = createShape();
        shape.beginShape(PConstants.QUAD);
        shape.texture(loadImage("back.png"));
        shape.noFill();
        shape.noStroke();
        shape.textureMode(PConstants.NORMAL);
        //startPos.x, startPos.y, endPos.x - startPos.x, endPos.y - startPos.y
        shape.vertex(0, 0,0,0);
        shape.vertex(width,0,1,0);
        shape.vertex(width,height,1,1);
        shape.vertex(0,height,0,1);
        shape.endShape();
        cr = loadImage("credits.png");
    }
    PImage cr;
    int lvl = 1;
    boolean lvlLoaded = false;
    int lastCount = 0;
    public void draw() {
        shape(shape);

        if (state.equals("C")) {
            background(cr);
            new Button("Back",  width / 2, height -height / 9, 300, 80,new Runnable() {
                public void run() {
                    state = "Menu";
                }}).draw();


            return;
        }
        if (P1.win || state.equals("Menu")) {
            if (state.equals("G")) {
                lastCount = frameCount;
                lvlLoaded = false;
            }
            state = "Menu";
            if (P1.win) {
                if (!lvlLoaded) {
                    lvl++;
                    if (lvl ==5 ) lvl = 1;
                    PImage level = loadImage("level"+lvl+".png");
                    loadLevel(level);
                    lvlLoaded = true;
                }
                float timeSince = lastCount - levelTimer;
                timeSince /= 60;
                textSize(40);
                fill(0);
                text("You Win!!!!!!!!!!!!\nTime Taken:"+timeSince+" seconds",width/2, height / 10+height / 9+height / 9+height / 9);
            }
            for (Button b: menu) {
                b.draw();
            }
            return;
        }
        if (!P1.isAlive) {
            death();
        }
        pushMatrix();
        translate(-Math.min(Math.max(P1.getPos().x - width / 2, 0), levelWidth - width),
                -Math.min(Math.max(P1.getPos().y - height / 2, 0), levelHeight - height));
        drawPlayer();
        drawTiles();
        popMatrix();
        float timeSince = frameCount - levelTimer;
        timeSince /= frameRate;
        text(timeSince + "", 100, 100);
        text(frameRate + "", 100, 80);
    }

    private void death() {
        P1.isAlive = true;
        levelTimer = frameCount;
        P1.resetPos();
    }

    @Override
    public void keyPressed() {
        if (key == ESC) {
            state = "Menu";
            key = 0;
            return;
        }
        P1.K1.keyPressed();
    }

    @Override
    public void keyReleased() {
        P1.K1.keyReleased();
    }

    private void drawPlayer() {
        P1.draw();
    }

    private void drawTiles() {
        for (Tile p : tiles) {
            float clipX = (Math.min(Math.max(P1.getPos().x - width / 2, 0), levelWidth - width));
            float clipY = (Math.min(Math.max(P1.getPos().y - height / 2, 0), levelHeight - height));

            if (p.getShape() == null || (p.getShape().getTopLeft().x > clipX - width*2 && p.getShape().getTopLeft().y > clipY - height*2 && p.getShape().getBottomRight().x < clipX + width*2 && p.getShape().getBottomRight().y < clipY + height*2 )){
                p.draw();
            }
        }
    }

    private void loadLevel(PImage level) {
        tiles.clear();
        level.loadPixels();
        levelWidth = tileSize * level.width;
        levelHeight = tileSize * level.height;
        HashMap<Integer, PVector> saws = new HashMap<Integer, PVector>();
        HashMap<Integer, PVector> lasers = new HashMap<Integer, PVector>();
        HashMap<Integer, PVector> arcs = new HashMap<Integer, PVector>();
        for (int y = 0; y < level.height; y++) {
            for (int x = 0; x < level.width; x++) {
                int rgb = level.pixels[y * level.width + x];
                int red = new Color(rgb).getRed();
                int green = new Color(rgb).getGreen();
                int blue = new Color(rgb).getBlue();


                // platform
                if (rgb == Color.BLACK.getRGB()) {
                    tiles.add(new Platform(new PVector(x * tileSize, y * tileSize), new PVector(tileSize, tileSize)));
                    continue;
                }
                //start & end portals
                if (red == 1) {
                    if (blue == 0) {
                        start = new PVector(x * tileSize, y * tileSize);
                        tiles.add(new Portal(start.copy().add(0,-1), new PVector(tileSize, tileSize), blue));
                        continue;

                    } else if (blue == 1) {
                        end = new PVector(x * tileSize, y * tileSize);
                        tiles.add(new Portal(end, new PVector(tileSize, tileSize), blue));
                        continue;
                    } else {
                        System.out.println("invalid portal");
                    }
                }

                //death tiles
                if (red == 123) {
                    if (saws.containsKey(green)) {
                        tiles.add(new Saw(new PVector(x * tileSize, y * tileSize), saws.get(green), tileSize));
                        continue;
                    } else {
                        saws.put(green, new PVector(x * tileSize, y * tileSize));
                        continue;
                    }

                }
                if (red == 124) {
                    if (lasers.containsKey(green)) {
                        tiles.add(new Laser(new PVector(x * tileSize, y * tileSize), lasers.get(green)));
                        continue;
                    } else {
                        lasers.put(green, new PVector(x * tileSize, y * tileSize));
                        continue;
                    }
                }
                if (red == 125) {
                    if (arcs.containsKey(green)) {
                        tiles.add(new ElectricArc(new PVector(x * tileSize, y * tileSize), arcs.get(green), blue));
                        continue;
                    } else {
                        arcs.put(green, new PVector(x * tileSize, y * tileSize));
                        continue;
                    }
                }
                if (red == 126) {
                    tiles.add(new GlassShard(new PVector(x * tileSize, y * tileSize), new PVector(tileSize, tileSize)));
                    continue;
                }
            }
        }
    }

}
