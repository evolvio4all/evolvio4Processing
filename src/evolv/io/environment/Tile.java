/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.environment;

import evolv.io.Configuration;
import evolv.io.engine.GameObject;
import evolv.io.engine.UIElement;
import evolv.io.util.MathUtil;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import processing.core.PApplet;

/**
 *
 * @author Quentin
 */
public class Tile extends UIElement {

    private final int barrenColor = Color.HSBtoRGB(0, 0, 1);
    private final int fertileColor = Color.HSBtoRGB(0, 0, 0.2f);
    private final int blackColor = Color.HSBtoRGB(0, 1, 0);
    private final int waterColor = Color.HSBtoRGB(0, 0, 0);
    private final double climateType;
    private final double foodType;

    private double fertility;
    private double foodLevel;
    private double lastUpdateTime;
    private final List<GameObject> contents = new ArrayList<>();

    public Tile(double fertility,double climateType, double foodType) {
        this.climateType = climateType;
        this.foodType = foodType;
        this.fertility = fertility;
    }

    public Tile(double fertility,double climateType, double foodType, double x, double y) {
        super(x, y);
        this.climateType = climateType;
        this.foodType = foodType;
        this.fertility = fertility;
    }

    public Tile(double fertility, double climateType, double foodType, double x, double y, double width, double height) {
        super(x, y, width, height);
        this.climateType = climateType;
        this.foodType = foodType;
        this.fertility = fertility;
    }

    public void addObjectToTile(GameObject object) {
        contents.add(object);
    }

    public List<GameObject> getContents() {
        return contents;
    }

    public double getFertility() {
        return fertility;
    }

    public double getFoodLevel() {
        return foodLevel;
    }

    public double getFoodType() {
        return foodType;
    }

    public void removeObjectFromTile(GameObject object) {
        contents.remove(object);
    }

    public double[] getColor() {
        return new double[]{foodType, fertility, foodLevel};
    }

    @Override
    public void update(Environment environment, double timestep) {
        double updateTime = environment.getYear();
        if (Math.abs(lastUpdateTime - updateTime) >= 0.00001f) {
            double growthChange = environment.getGrowthOverTimeRange(lastUpdateTime, updateTime);
            if (isWater()) {
                foodLevel = 0;
            } else if (growthChange > 0) {
                if (foodLevel < Configuration.MAX_GROWTH_LEVEL) {
                    double newDistToMax = (Configuration.MAX_GROWTH_LEVEL - foodLevel)
                            * MathUtil.fastExp(-growthChange * fertility * Configuration.FOOD_GROWTH_RATE);
                    double foodGrowthAmount = (Configuration.MAX_GROWTH_LEVEL - newDistToMax) - foodLevel;
                    addFood(foodGrowthAmount, climateType);
                }
            } else {
                removeFood(foodLevel - foodLevel * MathUtil.fastExp(growthChange * Configuration.FOOD_GROWTH_RATE));
            }
            foodLevel = Math.max(foodLevel, 0);
            lastUpdateTime = updateTime;
        }
    }

    public void addFood(double amount, double addedFoodType) {
        foodLevel += amount;
    }

    public void removeFood(double amount) {
        foodLevel -= amount;
    }

    @Override
    public void render(PApplet g, float scale, float camZoom) {
        double[] color = getColor();
        int landFill = Color.HSBtoRGB((float) color[0], (float) color[1], (float) color[2]);
        g.stroke(0, 0, 0, 1);
        g.strokeWeight(2);
        g.fill(landFill);
        g.rect((float) xLocation * scale, (float) yLocation * scale, scale, scale);
    }

    public void renderTileValues(PApplet g, float scale, float camZoom) {
        if (camZoom > Configuration.MAX_DETAILED_ZOOM) {
            double[] color = getColor();
            if (color[2] >= 0.7f) {
                g.fill(0, 0, 0, 1);
            } else {
                g.fill(0, 0, 1, 1);
            }
            g.textAlign(PApplet.CENTER);
            g.textSize(21);
            g.text(PApplet.nf((float) (100 * foodLevel), 0, 2) + " yums", (float) (xLocation + 0.5f) * scale, (float) (yLocation + 0.3f) * scale);
            g.text("Clim: " + PApplet.nf((float) (climateType), 0, 2), (float) (xLocation + 0.5f) * scale, (float) (yLocation + 0.6f) * scale);
            g.text("Food: " + PApplet.nf((float) (foodType), 0, 2), (float) (xLocation + 0.5f) * scale, (float) (yLocation + 0.9f) * scale);
        }
    }

    public boolean isWater() {
        return fertility > 1;
    }

    public boolean isEmpty() {
        return contents.isEmpty();
    }

    @Override
    public String toString() {
        return "Tile{" + "barrenColor=" + barrenColor + ", fertileColor=" + fertileColor + ", blackColor=" + blackColor + ", waterColor=" + waterColor + ", climateType=" + climateType + ", foodType=" + foodType + ", fertility=" + fertility + ", foodLevel=" + foodLevel + ", lastUpdateTime=" + lastUpdateTime + '}';
    }

}
