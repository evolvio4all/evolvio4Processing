/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.nn;

import com.sun.javafx.geom.Vec2f;
import evolv.io.nn.structure.Input;
import evolv.io.nn.structure.LayerStructure;
import evolv.io.nn.structure.NetworkStructure;
import evolv.io.nn.structure.Output;
import evolv.io.renderers.BrainRenderSettings;
import evolv.io.util.ArrayUtils;
import java.util.LinkedList;
import java.util.List;
import processing.core.PApplet;

/**
 *
 * @author Quentin
 */
public class NN {

    private Layer[] layers;
    private double[] prevInputs;
    private final int inputsize;

    private List<Input> inputs;
    private List<Output> outputs;

    public NN(NetworkStructure structure) {
        prevInputs = new double[structure.getInputSize()];
        initLayers(structure.getLayers());
        this.inputsize = structure.getInputSize();
    }

    public NN(Layer[] layers) {
        inputsize = layers[0].getInputSize();
        prevInputs = new double[inputsize];
        this.layers = layers;
    }

    private void initLayers(LinkedList<LayerStructure> structrues) {
        layers = new Layer[structrues.size()];
        int idx = 0;
        for (LayerStructure structrue : structrues) {
            layers[idx++] = structrue.createLayer();
        }
    }

    public void update() {
        double[] inputs = new double[inputsize];
        int inIdx = 0;
        for (Input input : this.inputs) {
            double[] gatherInputs = input.gatherInputs();
            System.arraycopy(gatherInputs, 0, inputs, inIdx, gatherInputs.length);
            inIdx += gatherInputs.length;
        }
        double[] outputs = calculate(inputs);
        int outIdx = 0;
        for (Output output : this.outputs) {
            int outputLength = output.getOutputLength();
            double[] splitOut = new double[outputLength];
            System.arraycopy(outputs, outIdx, splitOut, 0, outputLength);
            output.applyValues(splitOut);
            outIdx += outputLength;
        }
    }

    public double[] calculate(double[] inputs) {
        prevInputs = inputs;
        double[] nextLayerInput = inputs;
        for (Layer layer : layers) {
            nextLayerInput = layer.calculate(nextLayerInput);
        }
        return nextLayerInput;
    }

    public void reward(double[] rawRewards, double[] calculationResults) {
        double[] absResults = ArrayUtils.abs(calculationResults);
        double totalResults = ArrayUtils.sum(absResults);

        double[] rewards = new double[absResults.length];

        for (int i = 0; i < absResults.length; i++) {
            double weight = absResults[i] / totalResults;
            rewards[i] = weight * rawRewards[i];
        }

        double[][] layerReward = layers[layers.length - 1].calculateOutputLayerReward(rewards);
        for (int i = layers.length - 2; i >= 0; i--) {
            Layer layer = layers[i];
            layerReward = layer.calculatePrevLayerReward(layerReward);
        }
    }

    public void draw(PApplet g, float scale, float xOffset, float yOffset) {
        Vec2f[] prevLayerPositions = null;
        for (int i = 1; i <= layers.length; i++) {
            prevLayerPositions = layers[i - 1].draw(g, scale, xOffset + (i * (BrainRenderSettings.LAYER_H_SPACING + BrainRenderSettings.NEURON_DRAW_SIZE)), yOffset, prevLayerPositions);
        }
    }

    protected Layer[] getLayers() {
        return layers;
    }

    public NN reproduce(NN otherNet) {
        Layer[] newLayers = new Layer[layers.length];

        Layer[] otherLayers = otherNet.getLayers();

        for (int i = 0; i < layers.length; i++) {
            Layer reproduce = layers[i].reproduce(otherLayers[i]);
            newLayers[i] = reproduce;
        }

        return new NN(newLayers);
    }
}
