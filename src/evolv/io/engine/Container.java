/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.engine;

import evolv.io.environment.Environment;
import java.awt.geom.Point2D;
import processing.core.PApplet;

/**
 *
 * @author quentin
 */
public abstract class Container {

    protected double xLocation;
    protected double yLocation;

    protected double width;
    protected double height;

    protected double rotation;

    private float bodyHue;
    private float bodySaturation = 1;
    private float bodyBrightness = 1;

    private int prevMinTileXIdx;
    private int prevMinTileYIdx;
    private int prevMaxTileXIdx;
    private int prevMaxTileYIdx;

    private int minTileXIdx;
    private int minTileYIdx;
    private int maxTileXIdx;
    private int maxTileYIdx;

    private Container parent;

    public Container() {
    }

    public Container(Container parent) {
        this.parent = parent;
    }

    public Container(double xLocation, double yLocation, double width, double height, Container parent) {
        this.xLocation = xLocation;
        this.yLocation = yLocation;
        this.width = width;
        this.height = height;
        this.parent = parent;
    }

    public double getHeight() {
        return height;
    }

    public int getMaxTileXIdx() {
        return maxTileXIdx;
    }

    public int getMaxTileYIdx() {
        return maxTileYIdx;
    }

    public int getMinTileXIdx() {
        return minTileXIdx;
    }

    public int getMinTileYIdx() {
        return minTileYIdx;
    }

    public Container getParent() {
        return parent;
    }

    public int getPrevMaxTileXIdx() {
        return prevMaxTileXIdx;
    }

    public int getPrevMaxTileYIdx() {
        return prevMaxTileYIdx;
    }

    public int getPrevMinTileXIdx() {
        return prevMinTileXIdx;
    }

    public int getPrevMinTileYIdx() {
        return prevMinTileYIdx;
    }

    public double getRotation() {
        return rotation;
    }

    public double getWidth() {
        return width;
    }

    public double getxLocation() {
        return xLocation;
    }

    public double getyLocation() {
        return yLocation;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setParent(Container parent) {
        this.parent = parent;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setxLocation(double xLocation) {
        this.xLocation = xLocation;
    }

    public void setyLocation(double yLocation) {
        this.yLocation = yLocation;
    }

    public float getBodyBrightness() {
        return bodyBrightness;
    }

    public float getBodyHue() {
        return bodyHue;
    }

    public float getBodySaturation() {
        return bodySaturation;
    }

    public void setBodyBrightness(float bodyBrightness) {
        this.bodyBrightness = bodyBrightness;
    }

    public void setBodyHue(float bodyHue) {
        this.bodyHue = bodyHue;
    }

    public void setBodySaturation(float bodySaturation) {
        this.bodySaturation = bodySaturation;
    }

    public void setLocation(double x, double y) {
        xLocation = x;
        yLocation = y;
    }

    protected void updateTileIdx() {
        double cos = Math.cos(rotation);
        double sin = Math.sin(rotation);

        prevMinTileXIdx = minTileXIdx;
        prevMinTileYIdx = minTileYIdx;
        prevMaxTileXIdx = maxTileXIdx;
        prevMaxTileYIdx = maxTileYIdx;

        minTileXIdx = (int) Math.floor(Math.min(xLocation, cos * width + xLocation));
        minTileYIdx = (int) Math.floor(Math.min(yLocation, sin * height + yLocation));
        maxTileXIdx = (int) Math.floor(Math.max(xLocation, cos * width + xLocation));
        maxTileYIdx = (int) Math.floor(Math.max(yLocation, sin * height + yLocation));
    }

    public boolean hasTileIdxChanged() {
        return prevMinTileXIdx != minTileXIdx || prevMinTileYIdx != minTileYIdx
                || prevMaxTileXIdx != maxTileXIdx || prevMaxTileYIdx != maxTileYIdx;
    }

    public boolean opaquesTileAt(int x, int y) {
        return x > minTileXIdx && x < maxTileXIdx && y > minTileYIdx && y < maxTileYIdx;
    }

    public boolean isNewTileAt(int x, int y) {
        return x < prevMinTileXIdx || x > prevMaxTileXIdx || y < prevMinTileYIdx || y > prevMaxTileYIdx;
    }

    public abstract void update(Environment environment, double timestep);

    public abstract Point2D.Double intersectionPoint(double lineX, double lineY, double lineAngle, double lineLength);

    public abstract boolean intersectsWithPoint(double pointX, double pointY);

    public void rotate(double rotationValue) {
        rotation += rotationValue;
    }

    public abstract void render(PApplet g, float scale, float camZoom);

    public void translateToContainer(PApplet g) {
        g.pushMatrix();
        g.translate((float) xLocation, (float) yLocation);
    }

    protected void translateToParent(PApplet g) {
        if (parent != null) {
            parent.translateToContainer(g);
        }
    }

    protected void resetTranslationToParent(PApplet g) {
        if (parent != null) {
            parent.resetTranslation(g);
        }
    }

    public void resetTranslation(PApplet g) {
        g.popMatrix();
    }

    public double distance(double x1, double y1, double x2, double y2) {
        double xDist = x2 - x1;
        double yDist = y2 - y1;
        return Math.sqrt(xDist * xDist + yDist * yDist);
    }

    public double distance(Container other) {
        return distance(xLocation, yLocation, other.getxLocation(), other.getyLocation());
    }

    public void moveX(double amount) {
        xLocation += amount;
    }

    public void moveY(double amount) {
        yLocation += amount;
    }
}
