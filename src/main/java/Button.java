import processing.core.PConstants;

import java.awt.geom.Rectangle2D;

/**
 * Created by Mussel Pot on 11/10/2016.
 */
public class Button {
    Main main = Main.instance;
    Rectangle2D rect;
    Runnable toRun;
    String text;
    public Button(String text, float x, float y, float w, float h, Runnable toRun) {
        rect = new Rectangle2D.Float(x-w/2,y-h/2,w,h);
        this.toRun = toRun;
        this.text = text;
    }
    void draw() {
        main.textSize(40);
        main.textAlign(PConstants.CENTER);
        if (rect.contains(main.mouseX,main.mouseY)) {
            main.fill(255);
            main.rect((float)rect.getX(),(float)rect.getY(),(float)rect.getWidth(),(float)rect.getHeight());
            main.fill(0);
            main.text(text,(float)rect.getX()+(float)rect.getWidth()/2,(float)rect.getY()+(float)rect.getHeight()/2);
            if (main.mousePressed) {
                toRun.run();
            }
        } else {
            main.fill(0);
            main.rect((float)rect.getX(),(float)rect.getY(),(float)rect.getWidth(),(float)rect.getHeight());
            main.fill(255);
            main.text(text,(float)rect.getX()+(float)rect.getWidth()/2,(float)rect.getY()+(float)rect.getHeight()/2);
        }
        main.textSize(10);

    }
}
