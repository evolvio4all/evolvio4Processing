/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.engine;

import evolv.io.creature.inputs.StatInput;
import evolv.io.environment.Environment;

/**
 *
 * @author quentin
 */
public class Stat implements StatInput {

    private static final String[] LABELS = {"Stat saturation"};

    private final String name;

    private double value;
    private double maxCap;
    private double minCap;
    private double maxValue;
    private double minValue;

    public Stat(String name) {
        this(name, 0);
    }

    public Stat(String name, double value) {
        this(name, value, 100, 0);
    }

    public Stat(String name, double value, double maxValue, double minValue) {
        this(name, value, maxValue, minValue, maxValue, minValue);
    }

    public Stat(String name, double value, double maxValue, double minValue, double maxCap, double minCap) {
        this.name = name;
        this.value = value;
        this.maxCap = maxCap;
        this.minCap = minCap;
        this.maxValue = maxValue;
        this.minValue = minValue;
    }

    @Override
    public void add(double change) {
        value += change;
    }

    @Override
    public void cap() {
        value = Math.min(Math.max(minCap, value), maxCap);
    }

    @Override
    public String[] getLabels() {
        return LABELS;
    }

    @Override
    public int getNumberOfInputs() {
        return 1;
    }

    @Override
    public double maxValue() {
        return maxValue;
    }

    @Override
    public double minValue() {
        return minValue;
    }

    @Override
    public void multiply(double factor) {
        value *= factor;
    }

    @Override
    public double saturation() {
        return (value - minValue) / (maxValue - minValue);
    }

    @Override
    public void scan(Environment environment) {
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public void subtract(double change) {
        value -= change;
    }

    public double getExessValue() {
        double exess = value - maxValue;
        return Math.max(0, exess);
    }

    @Override
    public int validate() {
        if (value < minValue) {
            return -1;
        }
        if (value > maxValue) {
            return 1;
        }
        return 0;
    }

    @Override
    public double value() {
        return value;
    }

}
