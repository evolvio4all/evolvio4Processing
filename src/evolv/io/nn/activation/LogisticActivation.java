/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.nn.activation;

import evolv.io.util.MathUtil;

/**
 *
 * @author quentin
 */
public class LogisticActivation implements ActivationFunction {

    @Override
    public double activate(double value) {
        return MathUtil.sigmoid(value);
    }

}
