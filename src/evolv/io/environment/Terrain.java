/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.environment;

import evolv.io.Configuration;
import evolv.io.creature.Creature;
import evolv.io.engine.Container;
import evolv.io.engine.GameObject;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import processing.core.PApplet;

/**
 *
 * @author Quentin
 */
public class Terrain extends Container {

    private final Tile[][] ground = new Tile[Configuration.BOARD_WIDTH][Configuration.BOARD_HEIGHT];
    private final Camera cam;

    public Terrain() {
        width = Configuration.BOARD_WIDTH;
        height = Configuration.BOARD_HEIGHT;
        cam = new Camera();
        cam.setLocation(width * 0.5, height * 0.5);
    }

    public Camera getCam() {
        return cam;
    }

    public void generate(PApplet applet) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float bigForce = PApplet.pow((float) (y / height), 0.5f);
                float fertility = applet.noise(x * Configuration.NOISE_STEP_SIZE * 3,
                        y * Configuration.NOISE_STEP_SIZE * 3) * (1 - bigForce) * 5.0f
                        + applet.noise(x * Configuration.NOISE_STEP_SIZE * 0.5f,
                                y * Configuration.NOISE_STEP_SIZE * 0.5f) * bigForce * 5.0f
                        - 1.5f;
                float climateType = applet.noise(x * Configuration.NOISE_STEP_SIZE * 0.2f + 10000,
                        y * Configuration.NOISE_STEP_SIZE * 0.2f + 10000) * 1.63f - 0.4f;
                climateType = PApplet.min(PApplet.max(climateType, 0), 0.8f);
                ground[x][y] = new Tile(fertility, climateType, climateType, x, y, 1, 1);
            }
        }
    }

    public double[] getColorAt(int x, int y) {
        return ground[x][y].getColor();
    }

    @Override
    public Point2D.Double intersectionPoint(double lineX, double lineY, double lineAngle, double lineLength) {
        return null;
    }

    @Override
    public boolean intersectsWithPoint(double pointX, double pointY) {
        return false;
    }

    @Override
    public void render(PApplet g, float scale, float camZoom) {
        g.fill(g.color(0, 0, 0));
        g.rect(0, 0, Configuration.SCALE_TO_FIXBUG * (float) width,
                Configuration.SCALE_TO_FIXBUG * (float) height);
        for (Tile[] tileArray : ground) {
            for (Tile tile : tileArray) {
                tile.render(g, scale, camZoom);
            }
        }
        int xIdx = (int) toWorldXCoordinate(g.mouseX, g.mouseY);
        int yIdx = (int) toWorldYCoordinate(g.mouseX, g.mouseY);
        if (xIdx >= 0 && xIdx < Configuration.BOARD_WIDTH && yIdx >= 0 && yIdx < Configuration.BOARD_HEIGHT) {
            ground[xIdx][yIdx].renderTileValues(g, scale, camZoom);
        }
    }

    public void removeObjectFromGround(GameObject object) {
        for (int x = object.getMinTileXIdx(); x <= object.getMaxTileXIdx(); x++) {
            for (int y = object.getMinTileYIdx(); y <= object.getMaxTileYIdx(); y++) {
                if (isNewTileAt(x, y)) {
                    ground[x][y].removeObjectFromTile(object);
                }
            }
        }
    }

    public void returnToEarth(Creature deadCreature) {
        int pieces = 20;
        for (int i = 0; i < pieces; i++) {
            Tile selectedTile = getRandomCoveredTile(deadCreature.getxLocation(), deadCreature.getyLocation(), deadCreature.getBodySize());
            getRandomCoveredTile(deadCreature.getxLocation(), deadCreature.getyLocation(), deadCreature.getBodySize()).addFood(deadCreature.getEnergyValue() / pieces, deadCreature.getBodyHue());
        }
        removeObjectFromGround(deadCreature);
    }

    @Override
    public void update(Environment environment, double timestep) {
        updateTileOccupation(environment);
        for (Tile[] tiles : ground) {
            for (Tile tile : tiles) {
                tile.update(environment, timestep);
            }
        }
    }

    private void updateTileOccupation(Environment environment) {
        Population population = environment.getPopulation();
        population.getCreatures().parallelStream().filter((creature) -> (creature.hasTileIdxChanged())).map((creature) -> {
            for (int x = creature.getPrevMinTileXIdx(); x <= creature.getPrevMaxTileXIdx(); x++) {
                for (int y = creature.getPrevMinTileYIdx(); y <= creature.getPrevMinTileYIdx(); y++) {
                    if (!creature.opaquesTileAt(x, y)) {
                        ground[x][y].removeObjectFromTile(creature);
                    }
                }
            }
            return creature;
        }).forEach((creature) -> {
            for (int x = creature.getMinTileXIdx(); x <= creature.getMaxTileXIdx(); x++) {
                for (int y = creature.getMinTileYIdx(); y <= creature.getMaxTileYIdx(); y++) {
                    if (isNewTileAt(x, y)) {
                        ground[x][y].addObjectToTile(creature);
                    }
                }
            }
        });
    }

    public List<GameObject> getObjectsNear(GameObject reference) {
        return getObjectsNear(reference.getMinTileXIdx(), reference.getMaxTileXIdx(), reference.getMinTileYIdx(), reference.getMaxTileYIdx());
    }

    public List<GameObject> getObjectsNear(double x, double y, double radius) {
        int minXIdx = (int) (x - radius);
        int maxXIdx = (int) (x + radius);
        int minYIdx = (int) (y - radius);
        int maxYIdx = (int) (y + radius);

        return getObjectsNear(minXIdx, maxXIdx, minYIdx, maxYIdx);
    }

    public List<GameObject> getObjectsNear(int minXIdx, int maxXIdx, int minYIdx, int maxYIdx) {
        List<GameObject> result = new ArrayList<>();
        for (int x = minXIdx; x <= maxXIdx; x++) {
            for (int y = minYIdx; y <= maxYIdx; y++) {
                result.addAll(ground[x][y].getContents());
            }
        }

        return result;
    }

    public Tile getRandomCoveredTile(double x, double y, double radius) {
        double choiceX = 0;
        double choiceY = 0;
        float fX = (float) x;
        float fY = (float) y;
        while (PApplet.dist(fX, fY, (float) choiceX, (float) choiceY) > radius) {
            choiceX = (Math.random() * 2 * radius - radius) + x;
            choiceY = (Math.random() * 2 * radius - radius) + y;
        }
        int tileX = capXToBorder((int) choiceX);
        int tileY = capYToBorder((int) choiceY);
        return ground[tileX][tileY];
    }

    private int capXToBorder(int x) {
        return Math.min(Math.max(x, 0), Configuration.BOARD_WIDTH - 1);
    }

    private int capYToBorder(int y) {
        return Math.min(Math.max(y, 0), Configuration.BOARD_HEIGHT - 1);
    }

    private void updateCollisions() {
        for (Tile[] tiles : ground) {
            for (Tile tile : tiles) {
                if (tile.isEmpty()) {
                    continue;
                }
                List<GameObject> contents = tile.getContents();
                int size = contents.size();
                for (int i = 0; i < size; i++) {
                    for (int j = i; j < size; j++) {
                        GameObject oI = contents.get(i);
                        GameObject oJ = contents.get(j);
                        if (oI.collides(oJ)) {
                            oI.applyColisionForces(oJ);
                            oJ.applyColisionForces(oI);
                        }
                    }
                }
            }
        }
    }

    public void dropEnergy(Creature creature, double energyLost) {
        double energyValue = creature.getEnergyValue();
        if (energyLost > 0) {
            energyLost = Math.min(energyLost, energyValue);
            creature.getEnergy().setValue(energyValue - energyLost);
            Tile tile = getRandomCoveredTile(creature.getxLocation(), creature.getyVelocity(), creature.getBodySize());
            tile.addFood(energyLost, creature.getBodyHue());
        }
    }

    public double toWorldXCoordinate(float x, float y) {
        double angle = Math.atan2(y - width, x - width);
        double dist = distance(width, width, x, y);
        return cam.getxLocation() + cam.grossify(Math.cos(angle - cam.getRotation()) * dist + width, Configuration.BOARD_WIDTH) / cam.getZoom();
    }

    public double toWorldYCoordinate(float x, float y) {
        double angle = Math.atan2(y - height, x - height);
        double dist = distance(height, height, x, y);
        return cam.getyLocation() + cam.grossify(Math.sin(angle - cam.getRotation()) * dist + height, Configuration.BOARD_HEIGHT) / cam.getZoom();
    }
}
