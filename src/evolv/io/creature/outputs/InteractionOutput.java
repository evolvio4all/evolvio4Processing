/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.creature.outputs;

import evolv.io.engine.GameObject;
import evolv.io.environment.Environment;
import evolv.io.nn.structure.Output;

/**
 *
 * @author quentin
 */
public abstract class InteractionOutput implements Output {

    private static final String[] LABELS = {"Interaction Value"};

    private final Environment environment;
    private final GameObject body;

    public InteractionOutput(Environment environment, GameObject body) {
        this.environment = environment;
        this.body = body;
    }

    @Override
    public void applyValues(double[] values) {
        interact(values[0]);
    }

    public GameObject getBody() {
        return body;
    }

    public Environment getEnvironment() {
        return environment;
    }

    protected abstract void interact(double value);

    @Override
    public String[] getLabels() {
        return LABELS;
    }

    @Override
    public int getOutputLength() {
        return 1;
    }

}
