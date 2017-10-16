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
public interface VisualInput extends Input {

    public float readHue();

    public float readSaturation();

    public float readBrightness();

    public double getViewDistance();

    public int isBlocked();

    public double relativeAngle();
}
