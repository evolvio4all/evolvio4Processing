/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.engine;

import com.sun.javafx.geom.Vec2f;
import processing.core.PApplet;

/**
 *
 * @author quentin
 */
public abstract class Component {

    private final double mutationRate = 0.0005;
    protected double rotation;
    protected final GameObject parent;

    protected Vec2f position;

    public Component(GameObject parent) {
        this(parent, new Vec2f(), 0);
    }

    public Component(GameObject parent, Vec2f position, double rotation) {
        this.parent = parent;
        this.rotation = rotation;
        this.position = position;
    }

    public GameObject getParent() {
        return parent;
    }

    public Vec2f getPosition() {
        return position;
    }

    public double getRotation() {
        return rotation;
    }

    public void rotate(double amount) {
        rotation += amount;
    }

    public void setPosition(Vec2f position) {
        this.position = position;
    }

    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public void translateToParent(PApplet g) {
        parent.translateToContainer(g);
    }

    public void resetTranslation(PApplet g) {
        parent.resetTranslation(g);
    }

    public abstract void render(PApplet g, float scale, float camZoom);

}
