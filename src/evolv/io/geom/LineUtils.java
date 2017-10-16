/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.geom;

import java.awt.geom.Point2D;
import static java.lang.Math.sqrt;

/**
 *
 * @author Quentin
 */
public class LineUtils {

    public static Point2D.Double intersectionpointWithRect(double lineX, double lineY, double angle, double lineLength, double rectX, double rectY, double width, double height, double rectRotation) {

        double bX = Math.cos(angle) * lineLength + lineX;
        double bY = Math.sin(angle) * lineLength + lineY;

        //top left corner
        double rectTLX = Math.cos(rectRotation) * width + rectX;
        double rectTLY = Math.sin(rectRotation) * width + rectY;

        double rectCos = Math.cos(rectRotation - 90);
        double rectSin = Math.sin(rectRotation - 90);
        //bottom left corner
        double rectBLX = rectCos * height + rectTLX;
        double rectBLY = rectSin * height + rectTLY;

        double rectBRX = rectCos * height + rectX;
        double rectBRY = rectSin * height + rectY;

        Point2D.Double[] intersectionPoints = new Point2D.Double[4];

        intersectionPoints[0] = getLineIntersection(lineX, lineY, bX, bY, rectX, rectY, rectBRX, rectBRY);
        intersectionPoints[1] = getLineIntersection(lineX, lineY, bX, bY, rectBRX, rectBRY, rectBLX, rectBLY);
        intersectionPoints[2] = getLineIntersection(lineX, lineY, bX, bY, rectBLX, rectBLY, rectTLX, rectTLY);
        intersectionPoints[3] = getLineIntersection(lineX, lineY, bX, bY, rectTLX, rectTLY, rectX, rectY);
        
        return getClosestToPoint(lineX, lineY, intersectionPoints);
    }

    public static Point2D.Double getLineIntersection(
            double l1StartX, double l1StartY, double l1EndX, double l1EndY,
            double l2StartX, double l2StartY, double l2EndX, double l2EndY) {
        double s1_x;
        s1_x = l1EndX - l1StartX;
        double s1_y;
        s1_y = l1EndY - l1StartY;
        double s2_x;
        s2_x = l2EndX - l2StartX;
        double s2_y;
        s2_y = l2EndY - l2StartY;

        double s;
        s = (-s1_y * (l1StartX - l2StartX) + s1_x * (l1StartY - l2StartY)) / (-s2_x * s1_y + s1_x * s2_y);
        double t;
        t = (s2_x * (l1StartY - l2StartY) - s2_y * (l1StartX - l2StartX)) / (-s2_x * s1_y + s1_x * s2_y);

        if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
            // Collision detected
            return new Point2D.Double(l1StartX + (t * s1_x), l1StartY + (t * s1_y));
        }
        return null;
    }

    public static Point2D.Double getClosestToPoint(double refX, double refY, Point2D.Double[] points) {
        double lowestSquaredDist = Double.MAX_VALUE;
        Point2D.Double result = null;
        for (Point2D.Double point : points) {
            if (point == null) {
                continue;
            }
            double xDist = refX - point.x;
            double yDist = refY - point.y;
            double squareDist = xDist * xDist + yDist * yDist;
            if (squareDist < lowestSquaredDist) {
                lowestSquaredDist = squareDist;
                result = point;
            }
        }
        return result;
    }
    
    public static int getIndexOfClosestToPoint(double refX, double refY, Point2D.Double[] points){
        double lowestSquaredDist = Double.MAX_VALUE;
        int result = -1;
        int idx = 0;
        for (Point2D.Double point : points) {
            if (point == null) {
                idx++;
                continue;
            }
            double xDist = refX - point.x;
            double yDist = refY - point.y;
            double squareDist = xDist * xDist + yDist * yDist;
            if (squareDist < lowestSquaredDist) {
                lowestSquaredDist = squareDist;
                result = idx;
            }
            idx++;
        }
        return result;
    }

    /**
     *
     * @param lineX x value of linestartpoint
     * @param lineY y value of linestartpoint
     * @param angle line angle
     * @param lineLength length of the line
     * @param circleX x value of the circle
     * @param circleY y value of the circle
     * @param radius radius of the circle
     * @return null if there is no intersechtin point otherwise the point
     * closest to the given lineX and lineY values
     */
    public static Point2D.Double intersectionpointWithCircle(double lineX, double lineY, double angle, double lineLength, double circleX, double circleY, double radius) {
        double extendedLength = lineLength + radius;
        double extendedSqareLength = extendedLength * extendedLength;

        double xDiff = circleX - lineX;
        double yDiff = circleY - lineY;

        double sqareDist = xDiff * xDiff + yDiff * yDiff;
        if (sqareDist > extendedSqareLength) {
            //We don't need further calculations if the distance between the circle center and the line start point is greater than the linelength + radius.
            return null;
        }

        double bX = Math.cos(angle) * lineLength + lineX;
        double bY = Math.sin(angle) * lineLength + lineY;

        double xDistAB = lineLength;
        double yDistAB = lineLength;
        double LAB = sqrt(xDistAB * xDistAB + yDistAB * yDistAB);
        double Dx = (bX - lineX) / LAB;
        double Dy = (bY - lineY) / LAB;
        double t = Dx * xDiff + Dy * yDiff;
        double Ex = t * Dx + lineX;
        double Ey = t * Dy + lineY;
        double xDistEC = Ex - circleX;
        double yDistEC = Ey - circleY;
        double LECSqare = xDistEC * xDistEC + yDistEC * yDistEC;
        double LEC = sqrt(LECSqare); //Length E to C

        if (LEC > radius) {
            return null;
        } else if (LEC == radius) {
            return new Point2D.Double(Ex, Ey);
        } else {
            double dt = sqrt(radius * radius - LECSqare);
            double Fx = (t - dt) * Dx + lineX;
            double Fy = (t - dt) * Dy + lineY;
            return new Point2D.Double(Fx, Fy);
        }
    }
}
