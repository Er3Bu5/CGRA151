import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.awt.*;

class Circle implements Shape{
    PVector cPos;
            int radius;

    Circle(PVector cPos, int radius) {
        this.cPos = cPos;
        this.radius = radius;
    }

    public boolean collides(Shape s) {
        return false;
    }

    public PVector getBottomRight() {
        return PVector.sub(cPos,new PVector(radius,-radius));
    }

    public PVector getTopLeft() {

        return PVector.sub(cPos,new PVector(-radius,radius));
    }
}