package evolv.io.creature.components;

import com.sun.javafx.geom.Vec2f;
import evolv.io.Configuration;
import evolv.io.creature.inputs.VisualInput;
import evolv.io.engine.Component;
import evolv.io.engine.Container;
import evolv.io.engine.GameObject;
import evolv.io.environment.Environment;
import evolv.io.geom.LineUtils;
import java.awt.geom.Point2D;
import java.util.List;
import processing.core.PApplet;

public class Eye extends Component implements VisualInput {

    private static final float CROSS_SIZE = 0.022f;

    public static final int EYE_SENSOR_ASPECT_COUNT = 6;

    public static final int HUE_IDX = 0;
    public static final int SATURATION_IDX = 1;
    public static final int BRIGHTNESS_IDX = 2;
    public static final int DISTANCE_IDX = 3;
    public static final int IS_BLOCKED_IDX = 4;
    public static final int ANGLE_IDX = 5;

    private static final String[] LABELS = {"Hue", "Saturation", "Brightness", "View Distance", "Is Blocked", "View Angle"};

    final double distance;

    private float visionOccludedX;
    private float visionOccludedY;

    private final double[] eyeResult;

    public Eye(GameObject creature, double angle, double distance) {
        super(creature);
        super.rotation = angle;
        this.distance = distance;

        eyeResult = new double[EYE_SENSOR_ASPECT_COUNT];
    }

    @Override
    public double[] gatherInputs() {
        return eyeResult;
    }

    @Override
    public String[] getLabels() {
        return LABELS;
    }

    @Override
    public int getNumberOfInputs() {
        return EYE_SENSOR_ASPECT_COUNT;
    }

    @Override
    public double getViewDistance() {
        return eyeResult[DISTANCE_IDX];
    }

    @Override
    public int isBlocked() {
        return (int) Math.signum(eyeResult[IS_BLOCKED_IDX]);
    }

    @Override
    public float readBrightness() {
        return (float) eyeResult[BRIGHTNESS_IDX];
    }

    @Override
    public float readHue() {
        return (float) eyeResult[HUE_IDX];
    }

    @Override
    public float readSaturation() {
        return (float) eyeResult[SATURATION_IDX];
    }

    @Override
    public double relativeAngle() {
        return eyeResult[ANGLE_IDX];
    }

    public void scan(Environment environment) {
        Point2D visionStart = new Point2D.Double(parent.getxLocation(), parent.getyLocation());
        double visionTotalAngle = parent.getRotation() + rotation;

        float endX = getVisionEndX();
        float endY = getVisionEndY();

        visionOccludedX = endX;
        visionOccludedY = endY;
        double[] hsb = environment.getGround().getColorAt((int) endX, (int) endY);
        eyeResult[0] = hsb[0];
        eyeResult[1] = hsb[1];
        eyeResult[2] = hsb[2];
        eyeResult[3] = distance;
        eyeResult[4] = -1;
        eyeResult[5] = rotation;

        double parentX = parent.getxLocation();
        double parentY = parent.getyLocation();
        List<GameObject> potentialVisionOccluders = environment.getGround().getObjectsNear(
                (int) Math.min(visionOccludedX, parentX),
                (int) Math.max(visionOccludedX, parentX),
                (int) Math.min(visionOccludedY, parentY),
                (int) Math.max(visionOccludedY, parentY));
        if (potentialVisionOccluders.isEmpty()) {
            return;
        }

        Point2D.Double[] occulusionPoints = new Point2D.Double[potentialVisionOccluders.size()];
        int idx = 0;
        for (GameObject body : potentialVisionOccluders) {
            occulusionPoints[idx++] = body.intersectionPoint(parentX, parentY, visionTotalAngle, distance);
        }

        int idxClosest = LineUtils.getIndexOfClosestToPoint(parentX, parentY, occulusionPoints);
        if (idxClosest < 0) {
            return;
        }
        Point2D.Double intersectionPoint = occulusionPoints[idxClosest];
        Container body = potentialVisionOccluders.get(idxClosest);
        if (intersectionPoint != null) {
            visionOccludedX = (float) intersectionPoint.x;
            visionOccludedY = (float) intersectionPoint.y;
            double xDist = visionOccludedX - parentX;
            double yDist = visionOccludedY - parentY;
            double visionLineLength = Math.sqrt(xDist * xDist + yDist * yDist);
            eyeResult[0] = body.getBodyHue();
            eyeResult[1] = body.getBodySaturation();
            eyeResult[2] = body.getBodyBrightness();
            eyeResult[3] = visionLineLength;
            eyeResult[4] = 1;
        }
    }

    public Vec2f getOcculutedEndPoint() {
        return new Vec2f(visionOccludedX, visionOccludedY);
    }

    public Vec2f getVisionLineEntpoint() {
        return new Vec2f(getVisionEndX(), getVisionEndY());
    }

    private float getVisionEndX() {
        double visionTotalAngle = parent.getRotation() + rotation;
        return (float) (parent.getxLocation() + distance * Math.cos(visionTotalAngle));
    }

    private float getVisionEndY() {
        double visionTotalAngle = parent.getRotation() + rotation;
        return (float) (parent.getyLocation() + distance * Math.sin(visionTotalAngle));
    }

    @Override
    public void render(PApplet g, float scale, float camZoom) {
        translateToParent(g);
        int visionUIcolor = g.color(0, 0, 1);
        if (readBrightness() > Configuration.BRIGHTNESS_THRESHOLD) {
            visionUIcolor = g.color(0, 0, 0);
        }
        g.stroke(visionUIcolor);
        g.strokeWeight(Configuration.CREATURE_STROKE_WEIGHT);
        float endX = getVisionEndX();
        float endY = getVisionEndY();
        g.line(position.x * scale, position.y * scale, endX * scale, endY * scale);
        g.noStroke();
        g.fill(visionUIcolor);
        Vec2f occulutedEndPoint = getOcculutedEndPoint();
        g.ellipse(occulutedEndPoint.x * scale, occulutedEndPoint.y * scale,
                2 * CROSS_SIZE * scale, 2 * CROSS_SIZE * scale
        );
        g.stroke(readHue(), readSaturation(), readBrightness());
        g.strokeWeight(Configuration.CREATURE_STROKE_WEIGHT);
        g.line((occulutedEndPoint.x - CROSS_SIZE) * scale, (occulutedEndPoint.y - CROSS_SIZE) * scale,
                (occulutedEndPoint.x + CROSS_SIZE) * scale, (occulutedEndPoint.y + CROSS_SIZE) * scale);
        g.line((occulutedEndPoint.x - CROSS_SIZE) * scale, (occulutedEndPoint.y + CROSS_SIZE) * scale,
                (occulutedEndPoint.x + CROSS_SIZE) * scale, (occulutedEndPoint.y - CROSS_SIZE) * scale);
        resetTranslation(g);
    }
}
