package Drawers;

import Drawers.Interfaces.PixelDrawer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BufferedImagePixelDrawer implements PixelDrawer {

    private BufferedImage bi;

    public BufferedImagePixelDrawer(BufferedImage bi) {
        this.bi = bi;
    }

    @Override
    public void setPixel(int x, int y, Color c) {
        if (x > 0 && x < bi.getWidth() && y > 0 && y < bi.getHeight()) {
            bi.setRGB(x, y, c.getRGB());
        }
    }
}
