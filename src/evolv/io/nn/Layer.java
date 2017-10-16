/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.nn;

import com.sun.javafx.geom.Vec2f;
import static evolv.io.nn.Neuron.neuronFillColor;
import static evolv.io.nn.Neuron.neuronTextColor;
import evolv.io.nn.activation.Activation;
import evolv.io.nn.structure.LayerStructure;
import evolv.io.renderers.BrainRenderSettings;
import processing.core.PApplet;

/**
 *
 * @author Quentin
 */
public class Layer {

    private final Neuron[] neurons;
    private double[] prevInputs;
    private final int inputSize;

    public Layer(LayerStructure structure) {
        this.neurons = new Neuron[structure.getLayerOutputSize()];
        this.inputSize = structure.getLayerInputSize();
        this.prevInputs = new double[inputSize];
        initNeurons(structure.getActivation());
    }

    public Layer(Neuron[] neurons, int inputSize) {
        this.neurons = neurons;
        this.inputSize = inputSize;
        this.prevInputs = new double[inputSize];
    }

    public int getInputSize() {
        return inputSize;
    }

    private void initNeurons(Activation activation) {
        for (int i = 0; i < neurons.length; i++) {
            neurons[i] = new Neuron(inputSize, activation);
        }
    }

    public double[][] calculateOutputLayerReward(double[] rewards) {
        double[][] result = new double[neurons.length][inputSize];
        for (int i = 0; i < neurons.length; i++) {
            result[i] = neurons[i].calculatePrevLayerReward(rewards);
        }
        return result;
    }

    public double[][] calculatePrevLayerReward(double[][] rewards) {
        int nextLayerSize = rewards.length;
        double[][] result = new double[neurons.length][inputSize];
        for (int i = 0; i < neurons.length; i++) {
            double[] nextLayerReward = new double[nextLayerSize];
            for (int x = 0; x < nextLayerSize; x++) {
                nextLayerReward[x] = rewards[x][i];
            }
            result[i] = neurons[i].calculatePrevLayerReward(nextLayerReward);
        }
        return result;
    }

    public double[] calculate(double[] inputs) {
        prevInputs = inputs;
        double[] outputs = new double[neurons.length];
        for (int i = 0; i < neurons.length; i++) {
            outputs[i] = neurons[i].calculate(inputs);
        }
        return outputs;
    }

    public Layer reproduce(Layer other) {
        Neuron[] otherNeurons = other.getNeurons();
        Neuron[] newNeurons = new Neuron[neurons.length];

        for (int i = 0; i < neurons.length; i++) {
            newNeurons[i] = neurons[i].reproduce(otherNeurons[i]);
        }

        Layer newLayer = new Layer(newNeurons, inputSize);
        return newLayer;
    }

    public Vec2f[] draw(PApplet g, float scale, float x, float y, Vec2f[] prevLayerNeuronPositions) {
        if (prevLayerNeuronPositions == null) {
            prevLayerNeuronPositions = new Vec2f[inputSize];
            float inX = x - BrainRenderSettings.NEURON_DRAW_SIZE - BrainRenderSettings.LAYER_H_SPACING;
            for (int i = 0; i < inputSize; i++) {
                float inY = (i * (BrainRenderSettings.NEURON_DRAW_SIZE + BrainRenderSettings.NEURON_V_SPACHING)) + y;
                drawInputNeuron(g, scale, prevInputs[i], inX, inY, BrainRenderSettings.NEURON_DRAW_SIZE);
                prevLayerNeuronPositions[i] = new Vec2f(inX, inY);
            }
        }
        float neuronX = x, neuronY;
        Vec2f[] positions = new Vec2f[neurons.length];
        for (int i = 0; i < neurons.length; i++) {
            neuronY = (i * (BrainRenderSettings.NEURON_DRAW_SIZE + BrainRenderSettings.NEURON_V_SPACHING)) + y;
            Neuron neuron = neurons[i];
            Vec2f location = new Vec2f(neuronX, neuronY);
            positions[i] = location;
            neuron.draw(g, scale, location, prevLayerNeuronPositions);
        }
        return positions;
    }

    private void drawInputNeuron(PApplet g, float scale, double value, float x, float y, float size) {
        g.noStroke();
        g.fill(neuronFillColor(g, value));
        g.ellipse(x * scale, y * scale, size * scale, size * scale);
        g.fill(neuronTextColor(g, value));
        g.text(PApplet.nf((float) value, 0, 1), x * scale, y * scale + (size * scale / 2));
    }

    protected Neuron[] getNeurons() {
        return neurons;
    }

}
