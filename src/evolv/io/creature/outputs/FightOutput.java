/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.creature.outputs;

import evolv.io.Configuration;
import evolv.io.creature.Creature;
import evolv.io.engine.GameObject;
import evolv.io.environment.Environment;
import java.util.List;
import processing.core.PApplet;

/**
 *
 * @author quentin
 */
public class FightOutput extends InteractionOutput {

    private static final String[] LABELS = {"Fightlevel", "Fightradius"};

    private float fightLevel;
    private double fightRadius;

    public FightOutput(Environment environment, Creature body) {
        super(environment, body);
    }

    @Override
    public void applyValues(double[] values) {
        fightLevel = (float) values[0];
        fightRadius = values[1];
    }

    public double getFightLevel() {
        return fightLevel;
    }

    public double getFightRadius() {
        return fightRadius;
    }

    @Override
    public String[] getLabels() {
        return LABELS;
    }

    @Override
    public int getOutputLength() {
        return 2;
    }

    @Override
    protected void interact(double value) {
        Creature me = (Creature) getBody();
        double dmgDealt = 0;
        double dmgTaken = 0;
        if (value > 0 && me.getAge() >= Configuration.MATURE_AGE) {
            fightLevel = (float) value;
            dmgTaken = getFightLevel() * Configuration.FIGHT_ENERGY * me.getEnergyValue() * Configuration.TIME_STEP;
            me.loseEnergy(dmgTaken);
            List<GameObject> objectsNear = getEnvironment().getGround().getObjectsNear(me);
            for (int i = 0; i < objectsNear.size(); i++) {
                GameObject collider = objectsNear.get(i);
                if (collider instanceof Creature) {
                    float distance = PApplet.dist((float) me.getxLocation(), (float) me.getyLocation(),
                            (float) collider.getxLocation(), (float) collider.getyLocation()
                    );
                    double combinedRadius = me.getBodySize() * Configuration.FIGHT_RANGE + collider.getBodySize();
                    if (distance < combinedRadius) {
                        double dmgValue = getFightLevel() * Configuration.INJURED_ENERGY * Configuration.TIME_STEP;
                        getEnvironment().getGround().dropEnergy((Creature) collider, dmgDealt);
                        dmgDealt += dmgValue;
                    }
                }
            }
        } else {
            fightLevel = 0;
        }
    }

}
