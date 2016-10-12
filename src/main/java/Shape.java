import processing.core.PVector;

public interface Shape {
    boolean collides(Shape s);

    PVector getBottomRight();

    PVector getTopLeft();
}
