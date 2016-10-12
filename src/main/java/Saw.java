import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

public class Saw implements Tile {
    private Main main;
    private PImage img;
    PVector srcPos;
    PVector pos;
    int radius;
    float rotate = 0;
    PVector destPos;
    int speed = 5;
    int dir = -speed;

    public Saw(PVector pos, PVector destPos, int radius) {
        this.main = Main.instance;
        this.pos = pos;
        this.srcPos = pos.copy();
        this.destPos = destPos;
        this.radius = radius;
        img = main.loadImage("sawBlade.png");
    }

    public void draw() {
        PVector move = PVector.sub(destPos, srcPos).normalize();
        move.mult(dir);
        main.pushMatrix();
        main.translate(pos.x, pos.y);
        main.rotate(rotate);
        main.translate(-pos.x, -pos.y);
        //saw
        main.imageMode(PConstants.CENTER);
        main.image(img, pos.x, pos.y, radius * 2, radius * 2);
        main.noStroke();
        main.popMatrix();


        rotate += 0.1;
        pos.add(move);
        if (pos.dist(srcPos) < speed * 2) {
            dir = speed;
        }
        if (pos.dist(destPos) < speed * 2) {
            dir = -speed;
        }
    }

    public Shape getShape() {
        return new Circle(pos, radius);
    }
}
