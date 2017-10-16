/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.environment;

import evolv.io.Configuration;
import evolv.io.creature.inputs.StatInput;
import evolv.io.engine.UIElement;
import processing.core.PApplet;

/**
 *
 * @author quentin
 */
public class Thermometer extends UIElement implements StatInput {

    private static final String[] LABELS = {"Temperature"};

    private final float MAX_TEMPERATURE = Configuration.THERMOMETER_MAXIMUM;
    private final float MIN_TEMPERATURE = Configuration.THERMOMETER_MINIMUM;

    private double temperature = 0.5;
    private float highTemp = Configuration.MAXIMUM_TEMPERATURE;
    private float lowTemp = Configuration.MINIMUM_TEMPERATURE;

    public Thermometer() {
        super(-45, 30, 20, 660);
    }

    @Override
    public void add(double change) {
        temperature += change;
    }

    @Override
    public void cap() {
        temperature = Math.min(Math.max(lowTemp, temperature), highTemp);
    }

    @Override
    public double[] gatherInputs() {
        return new double[]{temperature};
    }

    @Override
    public String[] getLabels() {
        return LABELS;
    }

    public float getMAX_TEMPERATURE() {
        return MAX_TEMPERATURE;
    }

    public float getMIN_TEMPERATURE() {
        return MIN_TEMPERATURE;
    }

    @Override
    public int getNumberOfInputs() {
        return 1;
    }

    @Override
    public double maxValue() {
        return highTemp;
    }

    @Override
    public double minValue() {
        return lowTemp;
    }

    @Override
    public void multiply(double factor) {
        temperature *= factor;
    }

    @Override
    public double saturation() {
        return (temperature - lowTemp) / (highTemp - lowTemp);
    }

    @Override
    public void scan(Environment environment) {

    }

    @Override
    public void subtract(double change) {
        temperature -= change;
    }

    @Override
    public int validate() {
        if (temperature > highTemp) {
            return 1;
        }
        if (temperature < lowTemp) {
            return -1;
        }
        return 0;
    }

    @Override
    public double value() {
        return temperature;
    }

    @Override
    public boolean intersectsWithPoint(double pointX, double pointY) {
        return false;
    }

    @Override
    public void render(PApplet g, float scale, float camZoom) {
        translateToParent(g);
        float x = (float) xLocation;
        float y = (float) yLocation;
        float w = (float) width;
        float h = (float) height;

        g.noStroke();
        g.fill(0, 0, 0.2f);
        g.rect(x, y, w, h);
        g.fill(g.color(0, 1, 1)); //red
        float proportionFilled = (float) saturation();
        g.rect(x, y + h * (1 - proportionFilled), w, proportionFilled * h);

        double zeroHeight = (0 - lowTemp) / (highTemp - lowTemp);
        double zeroLineY = y + h * (1 - zeroHeight);
        g.textAlign(PApplet.RIGHT);
        g.stroke(0, 0, 1);
        g.strokeWeight(3);
        g.line(x, (float) (zeroLineY), x + w, (float) (zeroLineY));
        double minY = y + h * (1 - (MIN_TEMPERATURE - lowTemp) / (highTemp - lowTemp));
        double maxY = y + h * (1 - (MAX_TEMPERATURE - lowTemp) / (highTemp - lowTemp));
        g.fill(0, 0, 0.8f);
        g.line(x, (float) (minY), x + w * 1.8f, (float) (minY));
        g.line(x, (float) (maxY), x + w * 1.8f, (float) (maxY));
        g.line(x + w * 1.8f, (float) (minY), x + w * 1.8f, (float) (maxY));

        g.fill(0, 0, 1);
        g.text("Zero", x - 5, (float) (zeroLineY + 8));
        g.text(PApplet.nf(MIN_TEMPERATURE, 0, 2), x - 5, (float) (minY + 8));
        g.text(PApplet.nf(MAX_TEMPERATURE, 0, 2), x - 5, (float) (maxY + 8));
        resetTranslationToParent(g);
    }
}
