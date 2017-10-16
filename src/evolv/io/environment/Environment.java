/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.environment;

import processing.core.PApplet;

/**
 *
 * @author Quentin
 */
public class Environment {

    private Terrain ground;
    private Population population;
    private Thermometer thermometer;
    private double year;
    private double prevYear;

    public Environment(PApplet applet) {
        ground = new Terrain();
        ground.generate(applet);
        population = new Population();
        thermometer = new Thermometer();
        year = 0;
        prevYear = 0;
    }

    public Terrain getGround() {
        return ground;
    }

    public double getGrowthOverTimeRange(double startTime, double endTime) {
        double temperatureRange = thermometer.maxValue() - thermometer.minValue();
        double m = thermometer.minValue() + temperatureRange * 0.5f;
        return (endTime - startTime) * m + (temperatureRange / Math.PI / 4.0f)
                * (Math.sin(2 * Math.PI * startTime) - Math.sin(2 * Math.PI * endTime));
    }

    public Population getPopulation() {
        return population;
    }

    public Thermometer getThermometer() {
        return thermometer;
    }

    public double getYear() {
        return year;
    }

    public void update(double timestep) {
        year += timestep;
        ground.update(this, timestep);
        population.update(this, timestep);
        thermometer.update(this, timestep);
    }

    public void render(PApplet g, float scale, float camZoom) {
        ground.render(g, scale, camZoom);
        population.render(g, scale, camZoom);
    }

    public Camera getCam() {
        return ground.getCam();
    }

}
