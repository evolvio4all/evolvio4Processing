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
public class TanhActivation implements ActivationFunction {

    @Override
    public double activate(double value) {
        return Math.tanh(value);
    }

}
