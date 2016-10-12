import processing.core.PConstants;

import java.util.Arrays;

public class KeyListener {
    public boolean keys[] = {false, false, false, false, false};
    private Main main;

    KeyListener() {
        this.main = Main.instance;
    }

    void keyPressed() {
        if (main.key == 'w' || main.key == 'W') {
            keys[0] = true;
        }
        if (main.key == 'a' || main.key == 'A') {
            keys[1] = true;

        }
        if (main.key == 'd' || main.key == 'D') {
            keys[2] = true;

        }

        if (main.key == PConstants.CODED) {
            if (main.keyCode == PConstants.LEFT) {
                keys[3] = true;
            } else if (main.keyCode == PConstants.RIGHT) {
                keys[4] = true;
            }

        }

    }

    void keyReleased() {
        if (main.key == 'w' || main.key == 'W') {
            keys[0] = false;
        }
        if (main.key == 'a' || main.key == 'A') {
            keys[1] = false;
        }
        if (main.key == 'd' || main.key == 'D') {
            keys[2] = false;
        }

        if (main.key == PConstants.CODED) {
            if (main.keyCode == PConstants.LEFT) {
                keys[3] = false;
            }
            if (main.keyCode == PConstants.RIGHT) {
                keys[4] = false;
            }
        }

    }
}