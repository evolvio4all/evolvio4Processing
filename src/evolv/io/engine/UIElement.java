/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.engine;

import evolv.io.environment.Environment;
import java.awt.geom.Point2D;

/**
 *
 * @author quentin
 */
public abstract class UIElement extends Container {

    public UIElement() {
    }

    public UIElement(double x, double y) {
        xLocation = x;
        yLocation = y;
    }

    public UIElement(double x, double y, double width, double height) {
        xLocation = x;
        yLocation = y;
        this.width = width;
        this.height = height;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public Point2D.Double intersectionPoint(double lineX, double lineY, double lineAngle, double lineLength) {
        return null;
    }

    @Override
    public boolean intersectsWithPoint(double x, double y) {
        boolean xBound = x > xLocation && x < xLocation + width;
        boolean yBound = y > yLocation && y < yLocation + height;
        return xBound && yBound;
    }

    @Override
    public void update(Environment environment, double timestep) {

    }

}
