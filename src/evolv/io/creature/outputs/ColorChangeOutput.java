/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.creature.outputs;

import evolv.io.nn.structure.Output;

/**
 *
 * @author quentin
 */
public abstract class ColorChangeOutput implements Output {

    private static final String[] LABELS = {"Hue"};

    @Override
    public void applyValues(double[] values) {
        setHue((float) values[0]);
    }

    protected abstract void setHue(float hue);

    @Override
    public String[] getLabels() {
        return LABELS;
    }

    @Override
    public int getOutputLength() {
        return 1;
    }
}
