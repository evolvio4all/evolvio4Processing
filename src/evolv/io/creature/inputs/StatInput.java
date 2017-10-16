/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.creature.inputs;

import evolv.io.nn.structure.Input;

/**
 *
 * @author quentin
 */
public interface StatInput extends Input {

    @Override
    public default double[] gatherInputs() {
        return new double[]{saturation()};
    }

    public double maxValue();

    public double minValue();

    public double saturation();

    public void add(double change);

    public void subtract(double change);

    public void multiply(double factor);

    public double value();

    public int validate();

    public void cap();
}
