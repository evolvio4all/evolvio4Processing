package evolv.io.creature;

import evolv.io.Brain;
import evolv.io.Configuration;
import evolv.io.EvolvioColor;
import evolv.io.NameGenerator;
import evolv.io.creature.components.Eye;
import evolv.io.creature.components.FightLayer;
import evolv.io.creature.components.Mouth;
import evolv.io.creature.inputs.SingleValueInput;
import evolv.io.creature.outputs.BodyColorChangeOutput;
import evolv.io.creature.outputs.EatOutput;
import evolv.io.creature.outputs.FightOutput;
import evolv.io.creature.outputs.MouthColorChangeOutput;
import evolv.io.creature.outputs.MovementOutput;
import evolv.io.creature.outputs.ReproductionOutput;
import evolv.io.engine.GameObject;
import evolv.io.engine.Stat;
import evolv.io.environment.Environment;
import evolv.io.geom.LineUtils;
import evolv.io.ui.overviewpanel.CreatureIcon;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Random;
import processing.core.PApplet;

public class Creature extends GameObject {

    private final double[] previousEnergy = new double[Configuration.ENERGY_HISTORY_LENGTH];

    // Family
    private final String name;
    private final String parents;
    private final int gen;
    private final int id;

    private final double birthTime;

    private SingleValueInput age;
    private FightLayer fightLayer;
    private List<Eye> eyes;
    private Mouth mouth;
    private boolean selectedCreature;
    private final Stat energy = new Stat("Energy", 1000, 1000, 0, 3000, 0);
    private final Stat reproduction = new Stat("Reproduction", 0, 1, -1);
    private Brain brain;

    private boolean nameVisible;

    public Creature(String name, String parents, int gen, int id, double BirthTime) {
        this.name = name;
        this.parents = parents;
        this.gen = gen;
        this.id = id;
        this.birthTime = BirthTime;
    }

    public SingleValueInput age() {
        return age;
    }

    public double getAge() {
        return age.getValue();
    }

    public double getBirthTime() {
        return birthTime;
    }

    @Override
    public double getBodySize() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Brain getBrain() {
        return brain;
    }

    public Stat getEnergy() {
        return energy;
    }

    public double getEnergyValue() {
        return energy.value();
    }

    public List<Eye> getEyes() {
        return eyes;
    }

    public FightLayer getFightLayer() {
        return fightLayer;
    }

    @Override
    public double getMass() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Mouth getMouth() {
        return mouth;
    }

    public Stat getReproduction() {
        return reproduction;
    }

    public double getValue() {
        return age.getValue();
    }

    @Override
    public void initObject(Environment env) {
        addInput(energy);
        addInput(reproduction);
        addInputs(eyes);
        addInput(age);

        addOutput(new MovementOutput(this));
        addOutput(new MouthColorChangeOutput(mouth));
        addOutput(new FightOutput(env, this));
        addOutput(new ReproductionOutput(env, this));
        addOutput(new EatOutput(env, this));
        addOutput(new BodyColorChangeOutput(this));
    }

    @Override
    public boolean isAlive() {
        return energy.validate() == 0;
    }

    public boolean isNameVisible() {
        return nameVisible;
    }

    public boolean isSelectedCreature() {
        return selectedCreature;
    }

    @Override
    public void update(Environment environment, double timestep) {
        selectedCreature = this == environment.getPopulation().getSelectedCreature();
        //energy.subtract();
        loseEnergy(getEnergyValue() * Configuration.METABOLISM_ENERGY * getAge() * timestep);
        if (energy.validate() < 0) {
            environment.getGround().returnToEarth(this);
        }
    }

    private String createName(String tname, boolean mutateName) {
        if (tname.isEmpty()) {
            return NameGenerator.newName();
        }
        if (mutateName) {
            return NameGenerator.mutateName(tname);
        }
        return tname;
    }

    public void loseEnergy(double energyLost) {
        if (energyLost > 0) {
            energy.subtract(energyLost);
        } else {
            energy.add(energyLost);
        }
    }

    public void addEnergy(double energyGained) {
        if (energyGained > 0) {
            energy.add(energyGained);
        } else {
            energy.subtract(energyGained);
        }
    }

    public String stitchName(String[] s) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < s.length; i++) {
            float portion = ((float) s[i].length()) / s.length;
            int start = Math.min(Math.max(Math.round(portion * i), 0), s[i].length());
            int end = Math.min(Math.max(Math.round(portion * (i + 1)), 0), s[i].length());
            builder.append(s[i], start, end);
        }
        return builder.toString();
    }

    public String andifyParents(String[] s) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < s.length; i++) {
            if (i >= 1) {
                builder.append(" & ");
            }
            builder.append(s[i]);
        }
        return builder.toString();
    }

    public String getName() {
        return name;
    }

    public double getEnergyUsage(double timeStep) {
        return (getEnergyValue() - previousEnergy[Configuration.ENERGY_HISTORY_LENGTH - 1])
                / Configuration.ENERGY_HISTORY_LENGTH / timeStep;
    }

    public double getBabyEnergy() {
        return getEnergyValue() - Configuration.SAFE_SIZE;
    }

    public void setPreviousEnergy() {
        for (int i = Configuration.ENERGY_HISTORY_LENGTH - 1; i >= 1; i--) {
            previousEnergy[i] = previousEnergy[i - 1];
        }
        previousEnergy[0] = getEnergyValue();
    }

    public String getParents() {
        return parents;
    }

    public int getGen() {
        return gen;
    }

    public int getId() {
        return id;
    }

    public double getRotation() {
        return rotation;
    }

    @Override
    public void render(PApplet g, float scale, float camZoom) {
        double x = xLocation;
        double y = yLocation;
        g.ellipseMode(PApplet.RADIUS);
        fightLayer.render(g, scale, camZoom);

        if (camZoom > Configuration.MAX_DETAILED_ZOOM) {
            eyes.stream().forEach((eye) -> {
                eye.render(g, scale, camZoom);
            });
        }

        double radius = width / 2;
        g.noStroke();
        g.strokeWeight(Configuration.CREATURE_STROKE_WEIGHT);
        g.stroke(0, 0, 1);
        g.fill(0, 0, 1);
        if (selectedCreature) {
            g.ellipse((float) (x * scale), (float) (y * scale), (float) (radius * scale + 1 + 75.0f / camZoom), (float) (radius * scale + 1 + 75.0f / camZoom));
        }
        g.stroke(0);
        g.strokeWeight(Configuration.CREATURE_STROKE_WEIGHT);
        g.fill(getBodyHue(), getBodySaturation(), getBodyBrightness());
        g.ellipseMode(EvolvioColor.RADIUS);
        g.ellipse((float) (x * scale), (float) (y * scale), (float) (radius * scale), (float) (radius * scale));

        if (camZoom > Configuration.MAX_DETAILED_ZOOM) {
            mouth.render(g, scale, camZoom);
            if (nameVisible) {
                g.fill(0, 0, 1);
                g.textSize(0.2f * scale);
                g.textAlign(EvolvioColor.CENTER);
                g.text(name, (float) (x * scale), (float) ((y - radius * 1.4f - 0.07f) * scale));
            }
        }
    }

    @Override
    public Point2D.Double intersectionPoint(double lineX, double lineY, double lineAngle, double lineLength) {
        return LineUtils.intersectionpointWithCircle(lineX, lineY, lineAngle, lineLength, xLocation, yLocation, getBodySize());
    }

    @Override
    public boolean intersectsWithPoint(double pointX, double pointY) {
        return distance(xLocation, yLocation, pointX, pointY) < getBodySize();
    }

    public CreatureIcon getIcon(float iconX, float iconY) {
        CreatureIcon icon = new CreatureIcon();
        icon.setBodyBrightness(getBodyBrightness());
        icon.setBodyHue(getBodyHue());
        icon.setBodySaturation(getBodySaturation());
        icon.setFightLayer(fightLayer);
        icon.setMouth(mouth);
        icon.setName(name);
        icon.setNameVisible(nameVisible);
        icon.setRadius(getBodySize());
        icon.setRotation(rotation);
        icon.setSelectedCreature(selectedCreature);
        icon.setxLocation(iconX);
        icon.setyLocation(iconY);
        return icon;
    }

    @Override
    public void applyColisionForces(GameObject other) {
        double distance = distance(other);
        double force = calculateMinDistance(other) * Configuration.COLLISION_FORCE;
        xVelocity += ((xLocation - other.getxLocation()) / distance) * force / getMass();
        yVelocity += ((yLocation - other.getyLocation()) / distance) * force / getMass();
    }

    public double calculateMinDistance(GameObject other) {
        float distance = PApplet.dist((float) xLocation, (float) yLocation, (float) other.getxLocation(), (float) other.getyLocation());
        double combinedRadius = getBodySize() + other.getBodySize();
        return combinedRadius;
    }

    public Creature reproduceSexually(List<Creature> parents, double availableEnergy, int childId) {
        final float spreadMin = -0.01f;
        final float spreadMax = 0.01f;
        Random rnd = new Random();
        double newPX = rnd.nextDouble() * (spreadMax - spreadMin) + spreadMin;
        double newPY = rnd.nextDouble() * (spreadMax - spreadMin) + spreadMin;
        double newHue = 0;
        double newSaturation = 0;
        double newBrightness = 0;
        double newMouthHue = 0;
        double newEyeAngles[] = new double[Configuration.NUM_EYES];
        double newEyeDistances[] = new double[Configuration.NUM_EYES];
        int parentsTotal = parents.size();
        String[] parentNames = new String[parentsTotal];
        Brain newBrain = brain.evolve(parents.get(rnd.nextInt(parents.size())));
        int highestGen = 0;
        for (int i = 0; i < parentsTotal; i++) {
            int chosenIndex = rnd.nextInt(parentsTotal);
            Creature parent = parents.get(chosenIndex);
            parents.remove(chosenIndex);
            parent.getEnergy().setValue((parent.getBabyEnergy() - 100) * (parent.getBabyEnergy() / availableEnergy));
            newPX += parent.getxLocation() / parentsTotal;
            newPY += parent.getyLocation() / parentsTotal;
            newHue += parent.getBodyHue() / parentsTotal;
//            newSaturation += parent.getBodySaturation() / parentsTotal;
//            newBrightness += parent.getBodyBrightness() / parentsTotal;
            newMouthHue += parent.getMouth().getHue() / parentsTotal;
            for (int j = 0; j < Configuration.NUM_EYES; j++) {
                newEyeAngles[j] += parent.getEyes().get(j).getRotation() / parentsTotal;
                newEyeDistances[j] += parent.getEyes().get(j).getViewDistance() / parentsTotal;
            }
            parentNames[i] = parent.name;
            if (parent.gen > highestGen) {
                highestGen = parent.gen;
            }
        }
        newSaturation = 1;
        newBrightness = 1;
        Creature child = new Creature(stitchName(parentNames), andifyParents(parentNames), gen + 1, childId, birthTime);
        child.initEyes(newEyeAngles, newEyeDistances);
        return child;
    }

    public void initEyes(double newEyeAngles[], double newEyeDistances[]) {
        for (int i = 0; i < Configuration.NUM_EYES; i++) {
            eyes.add(new Eye(this, newEyeAngles[i], newEyeDistances[i]));
        }
    }

    public Creature reproduceASexually() {
        return null;
    }
}
