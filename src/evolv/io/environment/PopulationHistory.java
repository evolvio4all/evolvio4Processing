/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.environment;

import evolv.io.Configuration;
import evolv.io.engine.UIElement;
import processing.core.PApplet;

/**
 *
 * @author quentin
 */
public class PopulationHistory extends UIElement {

    private final int[] history = new int[Configuration.POPULATION_HISTORY_LENGTH];

    public int[] getHistory() {
        return history;
    }

    public void update(int population, double year, double prevYear) {
        if (Math.floor(year / Configuration.RECORD_POPULATION_EVERY) != Math
                .floor(prevYear / Configuration.RECORD_POPULATION_EVERY)) {
            for (int i = Configuration.POPULATION_HISTORY_LENGTH - 1; i >= 1; i--) {
                history[i] = history[i - 1];
            }
            history[0] = population;
        }
    }

    public int getMaxPopulation() {
        int maxPopulation = 0;
        for (int population : history) {
            maxPopulation = Math.max(population, maxPopulation);
        }
        return maxPopulation;
    }

    @Override
    public boolean intersectsWithPoint(double pointX, double pointY) {
        return false;
    }

    @Override
    public void render(PApplet g, float scale, float camZoom) {
        float barWidth = (float) (width / Configuration.POPULATION_HISTORY_LENGTH);
        float y2 = (float) (yLocation + height);
        g.noStroke();
        g.fill(0.33333f, 1, 0.6f);
        int maxPopulation = getMaxPopulation();
        for (int i = 0; i < Configuration.POPULATION_HISTORY_LENGTH; i++) {
            float h = (((float) history[i]) / maxPopulation) * (y2 - 770);
            g.rect((Configuration.POPULATION_HISTORY_LENGTH - 1 - i) * barWidth, y2 - h, barWidth, h);
        }
    }
}
