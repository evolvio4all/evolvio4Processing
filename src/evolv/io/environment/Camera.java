/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.environment;

import evolv.io.Configuration;
import evolv.io.engine.Container;
import java.awt.geom.Point2D;
import processing.core.PApplet;
import processing.event.MouseEvent;

/**
 *
 * @author Quentin
 */
public class Camera extends Container {

    private double zoom;
    private double scaleFactor;

    public Camera() {
    }

    public double getScaleFactor() {
        return scaleFactor;
    }

    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    @Override
    public void update(Environment environment, double timestep) {

    }

    @Override
    public Point2D.Double intersectionPoint(double lineX, double lineY, double lineAngle, double lineLength) {
        return null;
    }

    @Override
    public boolean intersectsWithPoint(double pointX, double pointY) {
        return false;
    }

    @Override
    public void render(PApplet g, float scale, float camZoom) {

    }

    public double grossify(double input, double total) { // Very weird function
        return (input / scaleFactor - total * 0.5f * Configuration.SCALE_TO_FIXBUG) / Configuration.SCALE_TO_FIXBUG;
    }

    public void resetZoom() {
        xLocation = Configuration.BOARD_WIDTH * 0.5f;
        yLocation = Configuration.BOARD_HEIGHT * 0.5f;
        zoom = 1;
    }

    public void setZoom(double target, double x, double y) {
        double grossX = grossify(x, Configuration.BOARD_WIDTH);
        moveX(-(grossX / target - grossX / zoom));
        double grossY = grossify(y, Configuration.BOARD_HEIGHT);
        moveY(-(grossY / target - grossY / zoom));
        zoom = target;
    }

    public void zoom(MouseEvent event) {
        float delta = event.getCount();
        if (delta >= 0.5f) {
            setZoom(zoom * 0.90909f, event.getX(), event.getY());
        } else if (delta <= -0.5f) {
            setZoom(zoom * 1.1f, event.getX(), event.getY());
        }
    }
}
