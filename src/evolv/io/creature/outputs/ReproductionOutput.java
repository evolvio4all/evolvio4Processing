/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.creature.outputs;

import evolv.io.creature.Creature;
import evolv.io.engine.GameObject;
import evolv.io.engine.Stat;
import evolv.io.environment.Environment;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author quentin
 */
public class ReproductionOutput extends InteractionOutput {

    private static final double minBabyEnergy = 100;

    private static final String[] LABELS = {"Reproduce"};
    private final double sexualReproductionThreshold = 0.6;
    private final double aSexualReproductionThreshold = 0.8;

    public ReproductionOutput(Environment environment, Creature body) {
        super(environment, body);
    }

    private double value;

    @Override
    public String[] getLabels() {
        return LABELS;
    }

    public double getValue() {
        return value;
    }

    @Override
    protected void interact(double value) {
        if (value < sexualReproductionThreshold) { // we need the will to reproduce orelse no further calculations needed.
            return;
        }
        Creature me = (Creature) getBody();
        double availableEnergy = me.getEnergy().getExessValue();
        if (availableEnergy == 0) { // has to have some exess energy. If not, other calculations are not nescessary.
            return;
        }
        List<Creature> possibleParents = new ArrayList();
        Environment env = getEnvironment();
        List<GameObject> objectsNear = env.getGround().getObjectsNear(getBody());
        int highestGen = 0;
        for (GameObject gameObject : objectsNear) {
            if (!(gameObject instanceof Creature)) {// As by now only creatures are able to reproduce!
                continue;
            }
            Creature c = (Creature) gameObject;
            double creatureExess = c.getEnergy().getExessValue();
            if (creatureExess == 0) {//the creature has to be able to give away some of its energy!
                continue;
            }

            Stat output = c.getReproduction();
            if (output == null) { // if the other creature does not have the ability to reproduce; continue!
                continue;
            }
            if (output.value() > sexualReproductionThreshold) { // the other creature has to want to reproduce too!
                possibleParents.add(c);
                availableEnergy += creatureExess;
            }
        }
        if (availableEnergy < minBabyEnergy) {
            return; // there is not enougth energy
        }
        Creature child = null;
        if (!possibleParents.isEmpty()) { //if there are suitable parents around, reproduceSexually
            child = me.reproduceSexually(possibleParents, availableEnergy, env.getPopulation().getNextCreatureId());
        } else if (value > aSexualReproductionThreshold) { // if it is really urgent to reproduce, do it on your own.
            child = me.reproduceASexually();
        }
        if (child != null) {
            child.initObject(env);
            env.getPopulation().addCreature(child);
        }
    }

}
