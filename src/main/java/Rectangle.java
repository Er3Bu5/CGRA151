import com.sun.org.apache.regexp.internal.RE;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.awt.*;

class Rectangle implements Shape{
    PVector rPos, rSize;

    Rectangle(PVector rPos, PVector rSize) {
        this.rPos = rPos;
        this.rSize = rSize;
    }

    public PVector getTopLeft() { // top left
        return rPos;
    }

    PVector getTopRight() { // top right
        return new PVector(rPos.x + rSize.x, rPos.y);
    }

    public PVector getBottomRight() { // bottom right
        return new PVector(rPos.x + rSize.x, rPos.y + rSize.y);
    }

    PVector getBottomLeft() { // bottom left
        return new PVector(rPos.x, rPos.y + rSize.y);
    }
    public boolean collides(Shape s) {
        if (s instanceof Circle) return collidesWithCirc((Circle) s);
        return s instanceof Rectangle && collidesWithRect((Rectangle) s);
    }
    boolean collidesWithRect(Rectangle r) {
        //case 1
        if (this.getTopLeft().x > r.getTopLeft().x && this.getTopLeft().x < r.getTopRight().x
                && this.getTopLeft().y > r.getTopLeft().y && this.getTopLeft().y < r.getBottomRight().y) {
            return true;
        }
        //case 2
        else if (this.getTopRight().x > r.getTopLeft().x && this.getTopRight().x < r.getTopRight().x
                && this.getTopRight().y > r.getTopLeft().y && this.getTopRight().y < r.getBottomRight().y) {
            return true;
        }

        // case 3
        else if (this.getBottomRight().x > r.getTopLeft().x && this.getBottomRight().x < r.getTopRight().x
                && this.getBottomRight().y > r.getTopLeft().y && this.getBottomRight().y < r.getBottomRight().y) {
            return true;
        }
        //case 4
       else if (this.getBottomLeft().x > r.getTopLeft().x && this.getBottomLeft().x < r.getTopRight().x
                && this.getBottomLeft().y > r.getTopLeft().y && this.getBottomLeft().y < r.getBottomRight().y) {
            return true;
        }
        return false;
    }

    boolean collidesWithCirc(Circle c) {
        //case 1
        if (Math.pow((this.getTopLeft().x - c.cPos.x),2) + Math.pow((this.getTopLeft().y - c.cPos.y),2) < Math.pow(c.radius,2)){
            return true;
        }
        if (Math.pow((this.getTopRight().x - c.cPos.x),2) + Math.pow((this.getTopRight().y - c.cPos.y),2) < Math.pow(c.radius,2)){
            return true;
        }
        if (Math.pow((this.getBottomLeft().x - c.cPos.x),2) + Math.pow((this.getBottomLeft().y - c.cPos.y),2) < Math.pow(c.radius,2)){
            return true;
        }
        if (Math.pow((this.getTopRight().x - c.cPos.x),2) + Math.pow((this.getTopRight().y - c.cPos.y),2) < Math.pow(c.radius,2)){
            return true;
        }
    return false;
    }


}