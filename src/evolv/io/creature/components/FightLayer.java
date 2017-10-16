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
import evolv.io.environment.Environment;
import evolv.io.nn.structure.Output;
import processing.core.PApplet;

/**
 *
 * @author quentin
 */
public class FightLayer extends Component implements Output {

    private static final String[] LABELS = {"Fightlevel", "Fightradius"};

    private float fightLevel;
    private double fightRadius;
    private final Environment environment;

    public FightLayer(GameObject parent, Environment environment) {
        super(parent);
        this.environment = environment;
    }

    @Override
    public void applyValues(double[] values) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String[] getLabels() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getOutputLength() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void render(PApplet g, float scale, float camZoom) {
        translateToParent(g);
        g.fill(0, 1, 1, fightLevel * 0.8f);
        double radius = getParent().getBodySize();
        Vec2f pos = getPosition();
        g.ellipse(pos.x * scale, pos.y * scale, (float) (Configuration.FIGHT_RANGE * radius * scale), (float) (Configuration.FIGHT_RANGE * radius * scale));
        resetTranslation(g);
    }

}
