/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.nn.structure;

import evolv.io.nn.NN;
import evolv.io.nn.activation.Activation;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Quentin
 */
public class NetworkStructure {

    private LinkedList<LayerStructure> layers = new LinkedList<>();
    private List<Input> inputs;
    private List<Output> outputs;
    private final int inputSize;
    private final int outputSize;
    private boolean finalized = false;

    public NetworkStructure(int inputSize, int outputSize) {
        this.inputSize = inputSize;
        this.outputSize = outputSize;
    }

    public void addInputLayer(Activation activation) {
        if (!layers.isEmpty()) {
            throw new UnsupportedOperationException("The inputlayer has to be added first.");
        }
        layers.add(new LayerStructure(inputSize, inputSize, activation));
    }

    public void addHiddenLayer(int size, Activation activation) {
        if (layers.isEmpty()) {
            throw new UnsupportedOperationException("The inputlayer has to be added first.");
        }
        if (finalized) {
            throw new UnsupportedOperationException("Can not add any Layers after the outputLayer");
        }
        layers.add(new LayerStructure(layers.getLast().getLayerOutputSize(), size, activation));
    }

    public void addOutputLayer(Activation activation) {
        layers.add(new LayerStructure(layers.getLast().getLayerOutputSize(), outputSize, activation));
        finalized = true;
    }

    public int getInputSize() {
        int size = 0;
        size = inputs.stream().map((input) -> input.getNumberOfInputs()).reduce(size, Integer::sum);
        return size;
    }

    public LinkedList<LayerStructure> getLayers() {
        return layers;
    }

    public NN createNetwork() {
        return new NN(this);
    }

    public int getOutputSize() {
        return outputSize;
    }
}
