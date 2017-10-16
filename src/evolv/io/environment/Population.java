/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.environment;

import evolv.io.Configuration;
import evolv.io.NameGenerator;
import evolv.io.creature.Creature;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import processing.core.PApplet;

/**
 *
 * @author Quentin
 */
public class Population {

    private final List<Creature> creatures = new ArrayList<>();
    private float spawnChance = Configuration.SPAWN_CHANCE;
    private final PopulationHistory history = new PopulationHistory();
    private Creature selectedCreature;
    private int nextCreatureId = 0;

    public boolean addCreature(Creature e) {
        return creatures.add(e);
    }

    public List<Creature> getCreatures() {
        return creatures;
    }

    public PopulationHistory getHistory() {
        return history;
    }

    public Creature getSelectedCreature() {
        return selectedCreature;
    }

    public void setSelectedCreature(Creature selectedCreature) {
        this.selectedCreature = selectedCreature;
    }

    public void update(Environment environment, double timestep) {
        Terrain ground = environment.getGround();
        for (Creature creature : creatures) {
            creature.update(environment, timestep);
            if (!creature.isAlive()) {
                ground.removeObjectFromGround(creature);
            }
        }
        spawnRandomCreature(environment, true, timestep, environment.getYear());
    }

    public int getNextCreatureId() {
        return nextCreatureId++;
    }

    private void spawnRandomCreature(Environment environment, boolean choosePreexisting, double timestep, double year) {
        if (new Random().nextDouble() < spawnChance) {
            if (choosePreexisting) {
                Creature c = getRandomCreature();
                if(c == null){
                    return;
                }
                Creature child = c.reproduceASexually();
                creatures.add(child);
            } else {
                NameGenerator nameGenerator = new NameGenerator();
                String name = NameGenerator.newName();
                creatures.add(new Creature(name, "", 0, nextCreatureId++, year));
            }
        }
    }

    private Creature getRandomCreature() {
        if(creatures.isEmpty()){
            return null;
        }
        int index = (int) (new Random().nextDouble() * creatures.size());
        return creatures.get(index);
    }

    public void render(PApplet g, float scale, float camZoom) {
        for (Creature creature : creatures) {
            creature.render(g, scale, camZoom);
        }
    }
}
