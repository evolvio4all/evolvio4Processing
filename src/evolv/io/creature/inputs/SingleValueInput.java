/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.creature.inputs;

import evolv.io.environment.Environment;
import evolv.io.nn.structure.Input;

/**
 *
 * @author quentin
 */
public class SingleValueInput implements Input {

    private String name;
    private double value;

    public SingleValueInput(String name) {
        this.name = name;
    }

    public SingleValueInput(String name, double value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public double[] gatherInputs() {
        return new double[]{value};
    }

    @Override
    public String[] getLabels() {
        return new String[]{name};
    }

    public String getName() {
        return name;
    }

    @Override
    public int getNumberOfInputs() {
        return 1;
    }

    public double getValue() {
        return value;
    }

    @Override
    public void scan(Environment environment) {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(double value) {
        this.value = value;
    }

}
