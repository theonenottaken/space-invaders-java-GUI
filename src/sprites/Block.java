package sprites;

import animations.GameLevel;
import biuoop.DrawSurface;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import levels.BlockFiller;
import levels.BlocksColorFill;
import collisions.BallRemover;
import collisions.Collidable;
import collisions.HitListener;
import collisions.HitNotifier;
import collisions.Velocity;
import shapes.Line;
import shapes.Point;
import shapes.Rectangle;

/**
 * Block Class. class implements Sprite, Collidable and HitNotifier.
 *
 * @author Mor Barak and Caleb shere ID numbers: 2493276919 and 302620638
 *
 */
public class Block implements Collidable, Sprite, HitNotifier {
    private Rectangle rect;
    private int hits;
    private int initHits;
    private Color color;
    private Color stroke;
    private List<HitListener> hitListeners;
    private Map<Integer, BlockFiller> fillers;
    private BlockFiller currFill;

    /**
     * First constructor.
     *
     * @param upperLeft
     *            - the upper left point of the block.
     * @param width
     *            - the width of the block
     * @param height
     *            - the height of the block
     * @param hits
     *            - the primary hits points
     */
    public Block(Point upperLeft, double width, double height, int hits) {
        this.rect = new Rectangle(upperLeft, width, height);
        this.hits = hits;
        this.initHits = hits;
        // this.hitString = "";
        this.color = null;
        this.stroke = Color.black;
        this.fillers = new HashMap<Integer, BlockFiller>();
        this.fillers.put(0, new BlocksColorFill(Color.ORANGE));
        this.hitListeners = new ArrayList<HitListener>();
        this.getFiller();
    }

    /**
     * Second constructor.
     *
     * @param upperLeft
     *            - the upper left point of the block.
     * @param width
     *            - the width of the block
     * @param height
     *            - the height of the block
     */
    public Block(Point upperLeft, double width, double height) {
        this.rect = new Rectangle(upperLeft, width, height);
        // this.hitString = "";
        this.color = Color.ORANGE;
        this.stroke = Color.black;
        this.fillers = new HashMap<Integer, BlockFiller>();
        this.fillers.put(0, new BlocksColorFill(Color.ORANGE));
        this.hitListeners = new ArrayList<HitListener>();
    }

    /**
     * constructor.
     * 
     * @param rect
     *            - rectangle.
     * @param hitPoints
     *            - number of hits.
     * @param stroke
     *            - color.
     * @param filler
     *            - map of fillers.
     */
    public Block(Rectangle rect, int hitPoints, Color stroke,
            Map<Integer, BlockFiller> filler) {
        this.hits = hitPoints;
        this.initHits = hitPoints;
        this.rect = rect;
        if (stroke != null) {
            this.stroke = stroke;
        } else {
            this.stroke = Color.BLACK;
        }

        this.color = Color.YELLOW;
        this.hitListeners = new ArrayList<HitListener>();
        this.fillers = new HashMap<Integer, BlockFiller>();
        this.fillers.putAll(filler);
    }

    /**
     *
     * @return the filler.
     */
    public BlockFiller getFiller() {
        return this.fillers.get(this.initHits - this.hits + 1);
    }

    /**
     *
     * @return - Getting the collision rectangle.
     */
    public Rectangle getCollisionRectangle() {
        return this.rect;
    }

    /**
     * @param hl
     *            - Add hl as a listener to hit events.
     */
    public void addHitListener(HitListener hl) {
        this.hitListeners.add(hl);
    }

    /**
     * @param hl
     *            - Remove hl from the list of listeners to hit events.
     */
    public void removeHitListener(HitListener hl) {
        this.hitListeners.remove(hl);
    }

    /**
     * will be called whenever a hit() occurs.
     *
     * @param hitter
     *            - the ball that hit the current block.
     */

    public void notifyHit(Ball hitter) {
        // Make a copy of the hitListeners before iterating over them.
        List<HitListener> listeners = new ArrayList<HitListener>(
                this.hitListeners);
        // Notify all listeners about a hit event:
        for (HitListener hl : listeners) {
            hl.hitEvent(this, hitter);
        }
    }

    /**
     * The function calculates the new velocity after a collision.
     *
     * @param collisionPoint
     *            - the collision point
     * @param currentVelocity
     *            - the current velocity
     * @param hitter
     *            - the hitter ball.
     */
    public void hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        this.changeHits();
        this.notifyHit(hitter);

    }

    /**
     * This function will be used also for the draw of the paddle.
     *
     * @param surface
     *            - the surface used for the draws.
     */
    public void drawOn(DrawSurface surface) {
        surface.setColor((this.stroke));
        surface.drawRectangle((int) this.getCollisionRectangle().getUpperLeft()
                .getX(), (int) this.getCollisionRectangle().getUpperLeft()
                .getY(), (int) this.getCollisionRectangle().getWidth(),
                (int) this.getCollisionRectangle().getHeight());
        surface.setColor(this.color);
        surface.fillRectangle((int) this.getCollisionRectangle().getUpperLeft()
                .getX(), (int) this.getCollisionRectangle().getUpperLeft()
                .getY(), (int) this.getCollisionRectangle().getWidth(),
                (int) this.getCollisionRectangle().getHeight());
    }

    /**
     * This function notify the block that time passed.
     *
     * @param dt
     *            - specifies the amount of seconds passed since the last call.
     */
    public void timePassed(double dt) {
        // do nothing.
    }

    /**
     * This function add the specific block to the game.
     *
     * @param g
     *            - the game which the block is added to.
     */
    public void addToGame(GameLevel g) {
        g.addSprite(this);
        g.addCollidable(this);
    }

    /**
     * removes the block from the game.
     *
     * @param game
     *            - current game level
     */
    public void removeFromGame(GameLevel game) {
        game.removeCollidable(this);
        game.removeSprite(this);
    }

    /**
     * This function subtract the number of hits in each collision.
     *
     */
    public void changeHits() {
        if (this.hits > 0) {
            hits--;
        }
    }

    /**
     * This function sets the color of the specific block.
     *
     * @param setColor
     *            - the new color.
     */
    public void setBlockColor(Color setColor) {
        this.color = setColor;
    }

    /**
     *
     * @return - the hit points.
     */
    public double getHitPoints() {
        return this.hits;
    }

    /**
     *
     * @return - the current color of the block.
     */
    public Color getColor() {
        return this.color;
    }
}
