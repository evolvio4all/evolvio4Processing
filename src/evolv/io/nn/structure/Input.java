/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.nn.structure;

import evolv.io.environment.Environment;

/**
 *
 * @author quentin
 */
public interface Input {

    public double[] gatherInputs();

    public int getNumberOfInputs();

    public String[] getLabels();

    public void scan(Environment environment);
}
