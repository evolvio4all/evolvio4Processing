/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.creature.outputs;

import evolv.io.Configuration;
import evolv.io.creature.Creature;
import evolv.io.environment.Environment;
import evolv.io.environment.Terrain;
import evolv.io.environment.Tile;

/**
 *
 * @author quentin
 */
public class EatOutput extends InteractionOutput {

    public EatOutput(Environment environment, Creature body) {
        super(environment, body);
    }

    @Override
    protected void interact(double value) {
        final float timeStep = Configuration.TIME_STEP;
        Creature me = (Creature) getBody();
        Terrain ground = getEnvironment().getGround();
        double amount = value
                / (1.0f + me.distance(0, 0, me.getxVelocity(), me.getyVelocity()) * Configuration.EAT_WHILE_MOVING_INEFFICIENCY_MULTIPLIER);
        double changeValue = 0;
        if (amount < 0) {
            changeValue = -amount * timeStep;
            ground.dropEnergy(me, changeValue);
            me.loseEnergy(-value * Configuration.EAT_ENERGY * timeStep);
        } else {
            Tile tile = ground.getRandomCoveredTile(me.getxLocation(), me.getyVelocity(), me.getBodySize());
            double foodToEat = tile.getFoodLevel()
                    * (1 - Math.pow((1 - Configuration.EAT_SPEED), amount * timeStep));
            if (foodToEat > tile.getFoodLevel()) {
                foodToEat = tile.getFoodLevel();
            }
            tile.removeFood(foodToEat);
            double normalizedFoodType = Math.abs(tile.getFoodType() - 0.5f);
            double normalizedMouthHue = Math.abs(me.getMouth().getHue() - 0.5f);
            double foodDistance = Math.abs(normalizedFoodType - normalizedMouthHue);
            double multiplier = 1.0f - (foodDistance / Configuration.FOOD_SENSITIVITY);

            changeValue = foodToEat * multiplier;
            if (multiplier >= 0) {
                me.addEnergy(changeValue);
            } else {
                me.loseEnergy(changeValue);
            }
            me.loseEnergy(value * Configuration.EAT_ENERGY * timeStep);
        }
    }

}
