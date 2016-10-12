import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

class Player {
    public boolean isAlive = true;
    private float maxXVel = 15;//max X velocity
    private float maxYVel = 20;//max Y velocity

    private boolean isJumping = false; // is it jumping?
    private boolean canJump = true; // can it jump?
    private int timer = 0;
    private int jumpTimer;

    private boolean leftSpam = false;
    private boolean rightSpam = false;
    private int spamCountMax = 15;
    private float spamCount = spamCountMax / 3;

    PImage[] walkCycle = new PImage[4];
    PImage[] rightWalkCycle = new PImage[4];
    Main main;
    PImage img, imgJump, imgWallLeft, imgWallRight, currentImg;
    KeyListener K1;
    private Rectangle playerBounds;

    private PVector pos, playerSize; // player startPos and size
    private PlayerState state = PlayerState.AIR; //current interaction

    private PVector vel = new PVector(0, 0);//velocity
    private PVector acc = new PVector(0, 0);//acceleration
    private int currentCycle = 0;
    private PVector start;
    public boolean win;

    Player(PVector pos, PVector pSize) {
        this.main = Main.instance;
        this.pos = pos;
        this.playerSize = pSize;
        start = pos.copy();
        K1 = new KeyListener();

        img = main.loadImage("piBoy.png");
        imgJump = main.loadImage("piBoy_jump.png");
        imgWallLeft = main.loadImage("piBoy_leftWall.png");
        imgWallRight = main.loadImage("piBoy_rightWall.png");
        for (int i = 0; i < 4; i++) {
            walkCycle[i] = main.loadImage("piBoy_leftwalk_" + (i + 1) + ".png");
        }
        for (int i = 0; i < 4; i++) {
            rightWalkCycle[i] = main.loadImage("piBoy_rightwalk_" + (i + 1) + ".png");
        }
        currentImg = img;
    }

    void draw() {
        if (isAlive) {
            //essential for running the game
            timer++;
            playerBounds = new Rectangle(pos, playerSize);
            vel.x += acc.x;
            pos.x += vel.x;
            doPlatformCollisions("x");
            pos.y += vel.y;
            doPlatformCollisions("y");
            playerBounds = new Rectangle(pos, playerSize);
            // doWindowCollisions();
            move();
            //visual state checks
            main.noStroke();
            //rect draw
            main.imageMode(PConstants.CORNER);
            if (state == PlayerState.GROUND) {
                currentImg = walkCycle[currentCycle];
                if (vel.x < 0) currentImg = rightWalkCycle[currentCycle];
            }
            main.image(currentImg, pos.x, pos.y, playerSize.x, playerSize.y);
            main.noFill();
        }
    }

    private Tile checkPlatform() {
        for (Tile p : main.tiles) {
            if (playerBounds.collides(p.getShape())) {
                if (main.death.contains(p.getClass())) {
                    isAlive = false;
                    System.out.println("YOU DED");
                    return null;
                }
                return p;
            }

        }
        return null;
    }

    private void doPlatformCollisions(String axis) {
        Tile p = checkPlatform();
        if (p != null) {
            if (p instanceof Portal){
                if (((Portal) p).type==1){
                    // do end menu
                    System.out.println("winner");
                    win = true;
                }
            }
            if (axis.equals("x")) {
                //left wall collision
                if (vel.x < 0) {
                    vel.x = 0;
                    pos.x = p.getShape().getBottomRight().x + 1;
                    state = PlayerState.WALL_LEFT;
                }
                //right wall collision
                else if (vel.x > 0) {
                    vel.x = 0;
                    pos.x = p.getShape().getTopLeft().x - playerSize.x - 1;
                    state = PlayerState.WALL_RIGHT;
                }
            } else if (axis.equals("y")) {
                //bottom wall collision
                if (vel.y < 0) {
                    pos.y = p.getShape().getBottomRight().y + 1;
                    vel.y = 2;
                }
                //top wall collision
                else if (vel.y > 0) {
                    // vel.y += 0.5;
                    pos.y = p.getShape().getTopLeft().y - playerSize.y - 1;
                    state = PlayerState.GROUND;
                }
            }
        } else {
            // if (playerBounds.getTopLeft().y > 1 && playerBounds.getBottomRight().y < 600-1 && playerBounds.getTopLeft().x > 1 && playerBounds.getBottomRight().x < 900-1) {
            //state = PlayerState.AIR;

        }
    }

    private void doWindowCollisions() {

        if (playerBounds.getBottomRight().y >= 700) {
            pos.y = 600 - playerSize.y - 1;
            vel.y = 0;
            canJump = true;
            state = PlayerState.GROUND;
        }
    }

    private void move() {
        jump();

        //on the ground
        if (state == PlayerState.GROUND) {
            canJump = true;
            currentImg = img;
            moveStateGround();
        }
        //in the air
        if (state == PlayerState.AIR) {
            currentImg = imgJump;
            moveStateAir();
        }

        // touching left wall
        if (state == PlayerState.WALL_LEFT) {
            canJump = true;
            currentImg = imgWallLeft;
            moveStateWallLeft();
        }

        // touching right wall
        if (state == PlayerState.WALL_RIGHT) {
            canJump = true;
            currentImg = imgWallRight;
            moveStateWallRight();
        }
    }

    private void jump() {
        //jump, jump-time update calc & gravity
        if (timer > jumpTimer + 10) {
            isJumping = false;
        }
        if (isJumping) {
            state = PlayerState.AIR;
            vel.y = -5;
        } else {
            vel.y += 0.5;
        }
    }

    public void spammyKeys() {
        //spammy key decay
        if (spamCount < 1) {
            spamCount = 0;
        }
        spamCount *= 0.99;
        // spammy keys
        if (!K1.keys[3] && !K1.keys[4]) {
            spamCount *= 0.9;
            return;
        }
        if (K1.keys[3] && !leftSpam) {

            currentCycle = ++currentCycle % 4;
            leftSpam = true;
            rightSpam = false;
            spamCount += 2;

            if (spamCount < 1) {
                spamCount = 0;
            }
        }

        if (K1.keys[4] && !rightSpam) {

            currentCycle = ++currentCycle % 4;
            leftSpam = false;
            rightSpam = true;
            spamCount += 2;
            if (spamCount < 1) {
                spamCount = 0;
            }
        }
    }

    private void moveStateGround() {
        //if W pressed and able to jump, jump
        if (K1.keys[0] && canJump) {
            isJumping = true;
            canJump = false;
            jumpTimer = timer;
        }

        // if A pressed and D not pressed, move left
        if (K1.keys[1] && !K1.keys[2]) {
            if (withinXLimits()) {
                spammyKeys();
                vel.x = spamCount * -1;
            } else {
                vel.x = maxXVel * -1;
            }
        }

        // if D pressed and A not pressed, move Right
        if (K1.keys[2] && !K1.keys[1]) {
            if (withinXLimits()) {
                spammyKeys();
                vel.x = spamCount;
            } else {
                vel.x = maxXVel;
            }
        }

        // if all keys released OR BOTH A & D pressed, apply 'drag'
        if ((K1.keys[2] && K1.keys[1]) || (!K1.keys[0] && !K1.keys[1] && !K1.keys[2]) || (!K1.keys[3] && !K1.keys[4])) {
            doDrag();
        }
    }

    private void moveStateAir() {
        //if jumping and within limits, increase speed
        if (K1.keys[0] && !canJump && isJumping && withinYLimits()) {
            vel.y -= 5;
        }

        // if A pressed and D not pressed, move left
        if (K1.keys[1] && !K1.keys[2]) {
            if (withinXLimits()) {
                spammyKeys();
                vel.x = spamCount * -1;
            } else {
                vel.x = maxXVel * -1;
            }
        }

        // if D pressed and A not pressed, move Right
        if (K1.keys[2] && !K1.keys[1]) {
            if (withinXLimits()) {
                spammyKeys();
                vel.x = spamCount;
            } else {
                vel.x = maxXVel;
            }
        }

        // if all keys released OR BOTH A & D pressed, apply 'drag'
        if ((K1.keys[2] && K1.keys[1]) || (!K1.keys[1] && !K1.keys[2]) || (!K1.keys[3] && !K1.keys[4])) {
            doDrag();
        }
    }

    private void moveStateWallLeft() {
        //if pressing W and D simultaneously, jump
        if (K1.keys[0] && K1.keys[2] && canJump) {
            isJumping = true;
            canJump = false;
            jumpTimer = timer;
            if (withinXLimits()) {
                spammyKeys();
                vel.x = spamCount;
            } else {
                vel.x = maxXVel;
            }

            //if jump keys held, jumping and within limits, increase speed
        } else if (K1.keys[0] && K1.keys[2] && !canJump && isJumping && withinYLimits()) {
            vel.y -= 5;
        }

        // if A is pressed 'hold' wall
        if (K1.keys[1] && !K1.keys[2]) {
            vel.y = 0;
        }

        //if D is pressed fall left
        if (K1.keys[2] && !K1.keys[0]) {
            if (withinXLimits()) {
                spammyKeys();
                vel.x = spamCount;
            } else {
                vel.x = maxXVel;
            }
            //vel.x = +1;
            state = PlayerState.AIR;
        }

        // if all keys released  apply 'drag'
        if ((!K1.keys[0] && !K1.keys[1] && !K1.keys[2] && !K1.keys[3] && !K1.keys[4])) {
            doDrag();
        }
    }

    private void moveStateWallRight() {
        //if pressing W and A simultaneously, jump
        if (K1.keys[0] && K1.keys[1] && canJump) {
            isJumping = true;
            canJump = false;
            jumpTimer = timer;
            spammyKeys();

            //if jump keys held, jumping and within limits, increase speed
        } else if (K1.keys[0] && K1.keys[1] && !canJump && isJumping && withinYLimits()) {
            vel.y -= 5;
        }

        // if D is pressed 'hold' wall
        if (K1.keys[2] && !K1.keys[1]) {
            vel.y = 0;
        }

        //if A is pressed fall left
        if (K1.keys[1] && !K1.keys[0]) {
            if (withinXLimits()) {
                spammyKeys();
                vel.x = spamCount * -1;
            } else {
                vel.x = maxXVel * -1;
            }
            state = PlayerState.AIR;
        }

        // if all keys released, apply wall'drag'
        if (!K1.keys[0] && !K1.keys[1] && !K1.keys[2] && !K1.keys[3] && !K1.keys[4]) {
            doDrag();
        }
    }

    private boolean withinYLimits() {
        return vel.y > -maxYVel && vel.y < maxYVel;
    }

    private boolean withinXLimits() {
        return vel.x > -maxXVel && vel.x < maxXVel;
    }

    private void doDrag() {
        switch (state) {
            case GROUND:
                vel.y = 0;
                if (vel.x > 1 || vel.x < -1) {
                    acc.x = 0;
                    vel.x /= 2;
                } else {
                    acc.x = 0;
                    vel.x = 0;
                }
                break;
            case AIR:
                vel.x *= 0.9;
                break;
            case WALL_LEFT:
            case WALL_RIGHT:
                vel.y -= 0.3; // counteract gravity by 60%
        }
    }

    PVector getPos() {
        return this.pos;
    }

    public void resetPos() {
        pos = start.copy();
        state = PlayerState.AIR;
        win = false;
    }

}