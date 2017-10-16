/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.nn.structure;

import evolv.io.nn.Layer;
import evolv.io.nn.activation.Activation;

/**
 *
 * @author Quentin
 */
public class LayerStructure {

    private int layerOutputSize;
    private int layerInputSize;
    private Activation activation;

    public LayerStructure(int layerInputSize, int layerOutputSize, Activation activation) {
        this.layerInputSize = layerInputSize;
        this.layerOutputSize = layerOutputSize;
        this.activation = activation;
    }

    public int getLayerOutputSize() {
        return layerOutputSize;
    }

    public void setLayerOutputSize(int layerOutputSize) {
        this.layerOutputSize = layerOutputSize;
    }

    public int getLayerInputSize() {
        return layerInputSize;
    }

    public void setLayerInputSize(int layerInputSize) {
        this.layerInputSize = layerInputSize;
    }

    public Activation getActivation() {
        return activation;
    }

    public void setActivation(Activation activation) {
        this.activation = activation;
    }

    public Layer createLayer() {
        return new Layer(this);
    }
}
