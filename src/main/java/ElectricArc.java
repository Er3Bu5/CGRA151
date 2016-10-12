import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;

public class ElectricArc implements Tile {
    private Main main;
    PVector startPos, endPos;
    int time;
    int invisTimer = 0;
    boolean show = true;
    PImage img;
    PShape shape;

    public ElectricArc(PVector startPos, PVector endPos, int time) {
        this.main = Main.instance;
        this.startPos = startPos;
        this.endPos = endPos;
        this.invisTimer = main.frameCount;
        this.time = time;
        img = main.loadImage("electricArc.png");
        shape = main.createShape();
        shape.beginShape(PConstants.QUAD);
        shape.texture(img);
        shape.noFill();
        shape.noStroke();
        shape.textureMode(PConstants.NORMAL);
        //startPos.x, startPos.y, endPos.x - startPos.x, endPos.y - startPos.y
        shape.vertex(startPos.x, startPos.y,0,0);
        shape.vertex(endPos.x,startPos.y,1,0);
        shape.vertex(endPos.x,endPos.y,1,1);
        shape.vertex(startPos.x,endPos.y,0,1);
        shape.endShape();
    }

    public void draw() {
        main.fill(49, 98, 100);
        if (main.frameCount - this.invisTimer > main.frameRate * time) {
            invisTimer = main.frameCount;
            show = !show;
        }
        if (show) {
            main.noFill();
            main.noStroke();
            main.shape(shape);
        }
    }

    public Shape getShape() {
        if (show) {
            return new Rectangle(startPos, PVector.sub(endPos,startPos));
        } else {
            return null;
        }
    }
}