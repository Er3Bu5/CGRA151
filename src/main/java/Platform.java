import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

public class Platform implements Tile {
    private Main main;
    PVector pos, platformSize; // player startPos and size
    PImage img;

    Platform(PVector pos, PVector platformSize) {
        this.main = Main.instance;
        this.pos = pos;
        this.platformSize = platformSize;
        img = main.loadImage("platform.png");
    }

    public void draw() {
        main.fill(0);
        main.imageMode(PConstants.CORNER);
        main.image(img, pos.x, pos.y, platformSize.x, platformSize.y);
        main.noFill();
    }

    public Shape getShape() {
        return new Rectangle(pos, platformSize);
    }
}
