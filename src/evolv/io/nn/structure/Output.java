/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.nn.structure;

/**
 *
 * @author quentin
 */
public interface Output {

    public int getOutputLength();

    public void applyValues(double[] values);

    public String[] getLabels();
}
