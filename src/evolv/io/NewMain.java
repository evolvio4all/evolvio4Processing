/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io;

import java.awt.Color;
import java.util.Arrays;

/**
 *
 * @author quentin
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int c = Color.RED.getRGB();
        int red = (c >> 16) & 0xFF;
        int green = (c >> 8) & 0xFF;
        int blue = c & 0xFF;
        float[] hsb = Color.RGBtoHSB(red, green, blue, null);
        System.out.println(Arrays.toString(hsb));
    }

}
