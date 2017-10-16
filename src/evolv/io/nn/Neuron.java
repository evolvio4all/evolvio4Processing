/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.nn;

import com.sun.javafx.geom.Vec2f;
import evolv.io.nn.activation.Activation;
import evolv.io.renderers.BrainRenderSettings;
import evolv.io.util.ArrayUtils;
import java.util.Arrays;
import java.util.Random;
import processing.core.PApplet;

/**
 *
 * @author Quentin
 */
public class Neuron {

    private final double START_MUTATION_RATE = 0.0005;
    private final double MUTATION_RATE_MUTABILITY = 0.01;
    private final double MAX_WEIGHT = 10;
    private final double MAX_MUTATION_RATE = 0.1;

    private final double[] weights;
    private double[] prevInputs;
    private final double[] mutationRates;
    private final Activation activation;
    private double value = 0;
    private double totalReward = 0;
    private double partialReward = 0;

    public Neuron(int inputSize, Activation activation) {
        this.weights = new double[inputSize];
        this.mutationRates = new double[inputSize];
        initWeights();
        initMutationRates();
        this.activation = activation;
    }

    public Neuron(double[] weights, double[] mutationRates, Activation activation) {
        this.weights = weights;
        this.mutationRates = mutationRates;
        this.activation = activation;
    }

    public double getTotalReward() {
        return totalReward;
    }

    public double getValue() {
        return value;
    }

    private void initWeights() {
        Random rnd = new Random();
        for (int i = 0; i < weights.length; i++) {
            weights[i] = rnd.nextGaussian();
        }
    }

    public double calculate(double[] inputs) {
        prevInputs = inputs;
        double[] weighted = ArrayUtils.arrayMultiply(inputs, weights);
        double sum = ArrayUtils.sum(weighted);
        value = activation.activate(sum);
        return value;
    }

    public Neuron reproduce(Neuron other) {
        double totalPartFitness = totalReward + other.getTotalReward();
        double mePercentage = totalReward / totalPartFitness;

        int inputsize = weights.length;

        double[] otherWeights = other.getWeights();
        double[] otherMutationRates = other.getMutationRates();

        double[] newWeights = new double[inputsize];
        double[] newMutationRates = new double[inputsize];

        Random rnd = new Random();

        for (int i = 0; i < inputsize; i++) {
            if (rnd.nextDouble() < mePercentage) {//use my values for this one
                newWeights[i] = weights[i];
                newMutationRates[i] = mutationRates[i];
            } else {
                newWeights[i] = otherWeights[i];
                newMutationRates[i] = otherMutationRates[i];
            }
        }

        Activation newActivation;
        if (rnd.nextDouble() < mePercentage) {
            newActivation = activation;
        } else {
            newActivation = other.getActivation();
        }

        Neuron neuron = new Neuron(newWeights, newMutationRates, newActivation);
        neuron.mutate();
        return neuron;
    }

    protected void mutate() {
        for (int i = 0; i < weights.length; i++) {
            weights[i] = mutate(weights[i], mutationRates[i], MAX_WEIGHT);
            mutationRates[i] = mutate(mutationRates[i], MUTATION_RATE_MUTABILITY, MAX_MUTATION_RATE);
        }
    }

    private double mutate(double prevValue, double mutationRate, double maxAbsValue) {
        double mutatedOutput = prevValue;
        if (Math.random() < mutationRate) {
            double multiple = new Random().nextGaussian();
            mutatedOutput *= multiple;
        }
        if (Math.abs(mutatedOutput) > maxAbsValue) {
            mutatedOutput = Math.signum(mutatedOutput) * maxAbsValue;
        }
        return mutatedOutput;
    }

    public double[] calculatePrevLayerReward(double[] nextLevelReward) {
        partialReward = ArrayUtils.sum(nextLevelReward);
        totalReward += partialReward;
        double[] partialRewards = new double[weights.length];
        for (int i = 0; i < weights.length; i++) {
            partialRewards[i] = Math.abs(weights[i]) * partialReward;
        }
        return partialRewards;
    }

    public void draw(PApplet g, float scale, Vec2f location, Vec2f[] connectionStartPoints) {
        float size = BrainRenderSettings.NEURON_DRAW_SIZE;
        float x = location.x;
        float y = location.y;
        g.noStroke();
        g.fill(neuronFillColor(g, value));
        g.ellipse(x * scale, y * scale, size * scale, size * scale);
        g.fill(neuronTextColor(g, value));
        g.text(PApplet.nf((float) value, 0, 1), x * scale, y * scale + (size * scale * 0.6f));

        final float connectionEndYOffset = size / 2;
        final float connectionEndXOffset = -size / 2;
        final float connectionStartYOffset = size / 2;
        final float connectionStartXOffset = size / 2;

        for (int i = 0; i < connectionStartPoints.length; i++) {
            Vec2f startPoint = new Vec2f(connectionStartPoints[i].x + connectionStartXOffset, connectionStartPoints[i].y + connectionStartYOffset);
            Vec2f endPoint = new Vec2f(x + connectionEndXOffset, y + connectionEndYOffset);
            drawConnection(g, startPoint, endPoint, prevInputs[i], weights[i], scale);
        }
    }

    private void drawConnection(PApplet g, Vec2f start, Vec2f end, double input, double weight, float scale) {
        g.stroke(neuronFillColor(g, input * weight));
        g.line(start.x * scale, start.y * scale, end.x * scale, end.y * scale);
    }

    public static int neuronTextColor(PApplet g, double d) {
        if (d >= 0) {
            return g.color(0, 0, 0);
        } else {
            return g.color(0, 0, 1);
        }
    }

    public static int neuronFillColor(PApplet g, double d) {
        if (d >= 0) {
            return g.color(0, 0, 1, (float) (d));
        } else {
            return g.color(0, 0, 0, (float) (-d));
        }
    }

    private void initMutationRates() {
        Arrays.fill(mutationRates, START_MUTATION_RATE);
    }

    protected double[] getWeights() {
        return weights;
    }

    protected double[] getMutationRates() {
        return mutationRates;
    }

    protected Activation getActivation() {
        return activation;
    }
}
