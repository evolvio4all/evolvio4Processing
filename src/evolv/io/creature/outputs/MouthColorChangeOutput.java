/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.creature.outputs;

import evolv.io.creature.components.Mouth;

/**
 *
 * @author quentin
 */
public class MouthColorChangeOutput extends ColorChangeOutput {

    private final Mouth mouth;

    public MouthColorChangeOutput(Mouth mouth) {
        this.mouth = mouth;
    }

    @Override
    protected void setHue(float hue) {
        mouth.setHue(hue);
    }
}
