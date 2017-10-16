/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.nn.activation;

/**
 *
 * @author quentin
 */
public enum Activation {
    SIGMOID(new LogisticActivation()),
    TANH(new TanhActivation()),;

    private final ActivationFunction function;

    private Activation(ActivationFunction function) {
        this.function = function;
    }

    public double activate(double input) {
        return function.activate(input);
    }
}
