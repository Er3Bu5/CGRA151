import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

public class GlassShard implements Tile{
    private Main main;
    PVector pos, glassSize;
    PImage img;

    GlassShard(PVector pos, PVector glassSize) {
        this.main = Main.instance;
        this.pos = pos;
        this.glassSize = glassSize;
        img = main.loadImage("laserBlue.png");
    }

    public void draw() {
        main.fill(255, 0, 0);
        main.imageMode(PConstants.CORNER);
        main.image(img,pos.x, pos.y, glassSize.x, glassSize.y);
        main.noFill();
    }

    public Shape getShape() {
        return new Rectangle(pos, glassSize);
    }
}