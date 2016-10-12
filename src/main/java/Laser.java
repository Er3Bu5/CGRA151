import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

public class Laser implements Tile {
    private Main main;
    PVector pos, beamSize;
    PVector srcPos, destPos;
    int speed = 10;
    int dir = -speed;
    PImage img;

    Laser(PVector startPos, PVector endPos) {
        this.main = Main.instance;
        this.pos = startPos;
        this.beamSize = PVector.sub(endPos,startPos);
        this.beamSize.y = 40;
        this.srcPos = startPos.copy();
        this.destPos = endPos;
        img = main.loadImage("laserRed.png");
    }

    public void draw() {
        PVector move = PVector.sub(destPos, srcPos).normalize();
        move.mult(dir);
        main.image(img,pos.x, pos.y, beamSize.x, beamSize.y);
        main.noFill();

        pos.add(move);
        if (pos.dist(srcPos) < speed * 2) {
            dir = speed;
        }
      if (pos.dist(destPos)<speed*2) {
          pos = srcPos.copy();
        }
    }


    public Shape getShape() {
        return new Rectangle(pos, new PVector(40,40));
    }
}