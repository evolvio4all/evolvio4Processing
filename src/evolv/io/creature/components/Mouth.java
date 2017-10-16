/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.creature.components;

import com.sun.javafx.geom.Vec2f;
import evolv.io.Configuration;
import evolv.io.engine.Component;
import evolv.io.engine.GameObject;
import processing.core.PApplet;

/**
 *
 * @author quentin
 */
public class Mouth extends Component {

    private final double MUTATION_RATE = 0.0005;

    private float hue;

    public Mouth(GameObject parent) {
        super(parent);
    }

    public float getHue() {
        return hue;
    }

    public void setHue(float hue) {
        this.hue = hue;
    }

    @Override
    public void render(PApplet g, float scale, float camZoom) {
        translateToParent(g);
        g.noFill();
        g.strokeWeight(Configuration.CREATURE_STROKE_WEIGHT);
        g.stroke(0, 0, 1);
        g.ellipseMode(PApplet.RADIUS);
        Vec2f pos = getPosition();
        g.ellipse(pos.x * scale, pos.y * scale,
                Configuration.MINIMUM_SURVIVABLE_SIZE * scale, Configuration.MINIMUM_SURVIVABLE_SIZE * scale);
        g.pushMatrix();
        g.translate(pos.x * scale, pos.y * scale);
        double bodySize = getParent().getBodySize();
        g.scale((float) bodySize);
        g.rotate((float) getParent().getRotation());
        g.strokeWeight((float) (Configuration.CREATURE_STROKE_WEIGHT / bodySize));
        g.stroke(0, 0, 0);
        g.fill(hue, 1.0f, 1.0f);
        g.ellipse(0.6f * scale, 0, 0.37f * scale, 0.37f * scale);
        g.popMatrix();
        resetTranslation(g);
    }

}
