import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

public class Portal implements Tile {
    private Main main;
    PVector pos, portalSize; // player startPos and size
    int type;
    PImage img1, img2;

    Portal(PVector pos, PVector portalSize, int type) {
        this.main = Main.instance;
        this.pos = pos;
        this.portalSize = portalSize;
        this.type = type;
        img1 = main.loadImage("platform_left.png");
        img2 = main.loadImage("platform_right.png");
    }

    public void draw() {
        main.fill(0);
        main.imageMode(PConstants.CORNER);
        if (type == 0) {
            main.image(img1, pos.x, pos.y, portalSize.x, portalSize.y);
        } else {
            main.image(img2, pos.x, pos.y, portalSize.x, portalSize.y);
        }
        main.noFill();
    }

    public Shape getShape() {
        return new Rectangle(pos, portalSize);
    }
}