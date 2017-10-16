/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.creature.outputs;

import evolv.io.engine.GameObject;

/**
 *
 * @author quentin
 */
public class BodyColorChangeOutput extends ColorChangeOutput {

    private final GameObject body;

    public BodyColorChangeOutput(GameObject body) {
        this.body = body;
    }

    @Override
    protected void setHue(float hue) {
        body.setBodyHue(hue);
    }

}
