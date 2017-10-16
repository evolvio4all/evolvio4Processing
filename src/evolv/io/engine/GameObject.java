/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.engine;

import evolv.io.environment.Environment;
import evolv.io.nn.structure.Input;
import evolv.io.nn.structure.Output;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author quentin
 */
public abstract class GameObject extends Container {

    private final float BASE_ROTATION_FORCE = 0.04f;
    private float friction = 0.004f;

    private final List<Input> inputs = new ArrayList();
    private final List< Output> outputs = new ArrayList();

    protected float xVelocity;
    protected float yVelocity;
    protected float rVelocity;

    public float getrVelocity() {
        return rVelocity;
    }

    public float getxVelocity() {
        return xVelocity;
    }

    public float getyVelocity() {
        return yVelocity;
    }

    public abstract void initObject(Environment env);

    public void addInput(Input input) {
        inputs.add(input);
    }

    public void addInputs(List<? extends Input> input) {
        inputs.addAll(input);
    }

    public void addOutput(Output output) {
        outputs.add(output);
    }

    public void addOutput(List<? extends Output> output) {
        outputs.addAll(output);
    }

    public void applyMotions(double timeStep) {
        xLocation += xVelocity * timeStep;
        yLocation += yVelocity * timeStep;
        rotation += rVelocity * timeStep;
        double frictionMultiplier = Math.max(0, 1 - friction / getMass());
        xVelocity *= frictionMultiplier;
        yVelocity *= frictionMultiplier;
        rVelocity *= frictionMultiplier;
        updateTileIdx();
    }

    public void applyRotationForce(double force) {
        rVelocity += BASE_ROTATION_FORCE * force / getMass();
    }

    public float getFriction() {
        return friction;
    }

    public abstract double getMass();

    public abstract boolean isAlive();

    public abstract double getBodySize();

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public void applyAcceleration(double amount) {
        double multiplied = amount / getMass();
        xVelocity += (Math.cos(rotation) * multiplied);
        yVelocity += (Math.sin(rotation) * multiplied);
    }

    public boolean collides(GameObject other) {
        return distance(other) < calculateMinDistance(other);
    }

    public abstract void applyColisionForces(GameObject other);

    public abstract double calculateMinDistance(GameObject other);

    public List<Stat> getStats() {
        List<Stat> stats = new ArrayList();
        for (Input input : inputs) {
            if (input instanceof Stat) {
                stats.add((Stat) input);
            }
        }
        return stats;
    }
}
