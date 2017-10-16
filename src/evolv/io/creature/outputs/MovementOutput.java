/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.creature.outputs;

import evolv.io.engine.GameObject;
import evolv.io.nn.structure.Output;

/**
 *
 * @author quentin
 */
public class MovementOutput implements Output {

    private static final String[] LABELS = {"Rotationforce", "Accelerationforce"};

    private final GameObject body;

    public MovementOutput(GameObject body) {
        this.body = body;
    }

    @Override
    public void applyValues(double[] values) {
        double rotationForce = values[0];
        double accelerationForce = values[1];

        body.applyRotationForce(rotationForce);
        body.applyAcceleration(accelerationForce);
    }

    @Override
    public String[] getLabels() {
        return LABELS;
    }

    @Override
    public int getOutputLength() {
        return 2;
    }
}
