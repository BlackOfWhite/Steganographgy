package model;

import java.awt.*;

/**
 * Created by niewinskip on 2017-01-12.
 */
public class Pixel {

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    /**
     * alpha	=(byte)(color>>24)
     * red		=(byte)(color>>16);
     * green	=(byte)(color>>8);
     * blue		=(byte)(color);
     **/

    private int r, g, b;

    public Pixel(Color color) {
        this.r = color.getRed();
        this.g = color.getGreen();
        this.b = color.getBlue();
    }



}
